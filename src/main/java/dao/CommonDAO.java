package dao;

import annotation.ManyToOne;
import annotation.OneToMany;
import model.user;
import queryBuilder.MysqlQuery;
import util.Util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.*;

/**
 * Created by safayat on 10/20/18.
 */
public class CommonDAO {

    private String dbUserName = "root";

    private String dbPassword = "";

        private String dbName  = "rssdesk";
//    private String dbName  = "schoolmanagement";

        private String dbUrl = "jdbc:mysql://localhost:3306/rssdesk?useSSL=false";
//    private String dbUrl = "jdbc:mysql://localhost:3306/schoolmanagement";



    private Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        Connection connection = null;

        try {
            connection = DriverManager.getConnection(dbUrl, dbUserName, dbPassword);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return connection;

    }

    private List<String> getPrimaryKeys(String table, Connection connection) {


        List<String> primaryKeys = new ArrayList<String>();
        try {
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet rs = metaData.getPrimaryKeys(dbName, null, table);
            while (rs.next()){
                primaryKeys.add(rs.getString("column_name"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return primaryKeys;

    }

    private Map<String,List<Integer>> getColumnIndexesByTable(ResultSetMetaData metaData) {

        Map<String, List<Integer>> columnsByTable = new HashMap<String, List<Integer>>();
        try {

            for(int column = 1;column <= metaData.getColumnCount(); column++){
                List<Integer> columns = columnsByTable.getOrDefault(metaData.getTableName(column), new ArrayList<Integer>());
                if(columns.size() == 0){
                    columnsByTable.put(metaData.getTableName(column), columns);
                }
                columns.add(column);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return columnsByTable;


    }

    private <T> T mapRow(ResultSet rs, List<Integer> columnIndexes, Class<T> clazz) throws Exception{

        T newClazz = clazz.newInstance();
        for(int index : columnIndexes){
            String columnName = rs.getMetaData().getColumnName(index);
            int columnType = rs.getMetaData().getColumnType(index);
            mapColumn(newClazz, columnName, Util.getClassByType(columnType) , rs.getObject(index));
        }
        return newClazz;

    }

    private <T> void mapColumn(T t, String columnName, Class type, Object value) throws Exception{

        String methodName = Util.toJavaMethodName(columnName, "set");
        Method method = t.getClass().getDeclaredMethod(methodName, type);
        if(method!=null){
            method.invoke(t,value);
        }

    }

    private <T> Object getValueFromObject(T t, String name) throws Exception{

        String methodName = Util.toJavaMethodName(name, "get");
        Method method = t.getClass().getDeclaredMethod(methodName);
        if(method!=null){
            return method.invoke(t);
        }
        return null;
    }

    private Map<String, Annotation> getAnnotationByTable(Class clazz) throws Exception{
        Map<String, Annotation> annotationByTable = new HashMap<String, Annotation>();
        List<Annotation> annotationList = Util.getMethodAnnotations(clazz);
        for(Annotation annotation : annotationList){
            if( annotation instanceof OneToMany){
                OneToMany oneToMany = (OneToMany) annotation;
                String type = oneToMany.type().getSimpleName();
                annotationByTable.put(type, annotation);
            }
            if( annotation instanceof ManyToOne){
                ManyToOne manyToOne = (ManyToOne)annotation;
                String type = manyToOne.type().getSimpleName();
                annotationByTable.put(type, annotation);
            }
        }
        return annotationByTable;

    }

    private String getUniqueLKeyFromAnnotation(Class clazz) throws Exception{
        List<Annotation> annotationList = Util.getMethodAnnotations(clazz);
        for(Annotation annotation : annotationList){
            if( annotation instanceof OneToMany){
                OneToMany oneToMany = (OneToMany) annotation;
                return oneToMany.outer();
            }
            if( annotation instanceof ManyToOne){
                ManyToOne manyToOne = (ManyToOne)annotation;
                return manyToOne.outer();
            }
        }
        return "";

    }

    private int getColumnIndex(ResultSetMetaData resultSetMetaData, List<Integer> columnIndexes, String columnName) throws Exception{
        for(int index : columnIndexes){
            String clName = resultSetMetaData.getColumnName(index);
            if(columnName.equals(clName)) return index;
        }
        return -1;
    }


    public List<user> getAllstudents() throws Exception{

        Connection dbConnection = null;
        PreparedStatement statement = null;
        List<user> desks = new ArrayList<user>();
        try {
            MysqlQuery sqlQuery = MysqlQuery.get("dk.name, us.*")
                    .table("Desk", "dk")
                    .leftJoin("User", "us").on("us.id", "dk.user_id")
                    .getQuery();

            String sql = sqlQuery.toString();

            dbConnection = getConnection();
            statement = dbConnection.prepareStatement(sql);
            ResultSet rs = statement.executeQuery();

            return getData(user.class, rs);


        } catch (SQLException e) {
            e.printStackTrace();
            if(statement!=null){
                try {
                    statement.close();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
            if(dbConnection!=null){
                try {
                    dbConnection.close();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return desks;
    }


    public <T> List<T> getData(Class<T> clazz, ResultSet resultSet) throws Exception{

        ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
        Map<String, Annotation> annotationByTable = getAnnotationByTable(clazz);
        Map<String,List<Integer>> columnIndexesByTable = getColumnIndexesByTable(resultSetMetaData);
        String parentClassUniqueField = getUniqueLKeyFromAnnotation(clazz);
        if(parentClassUniqueField.isEmpty()) return null;
        int parentClassUniqueFieldIndex = getColumnIndex(resultSetMetaData, columnIndexesByTable.get(clazz.getSimpleName()), parentClassUniqueField);
        int columnCount = resultSetMetaData.getColumnCount();
        Set<String> tableSet = new HashSet<String>();
        for(int i=1;i<=columnCount;i++){
            if(resultSetMetaData.getTableName(i).equalsIgnoreCase(clazz.getSimpleName())) continue;
            tableSet.add(resultSetMetaData.getTableName(i));
        }

        if(parentClassUniqueFieldIndex == 0) return null;

        Map<Object, T> parentMap = new HashMap<Object, T>();
        while (resultSet.next()){

            Object uniqueFieldValue = resultSet.getObject(parentClassUniqueFieldIndex);
            T parent = parentMap.get(uniqueFieldValue);
            if(parent == null ){
                parent = mapRow(resultSet, columnIndexesByTable.get(clazz.getSimpleName().toLowerCase()), clazz);
                parentMap.put(uniqueFieldValue, parent);
            }

            for(String tableName : tableSet){
                Class childClass = Class.forName("model."+tableName);
                Object childObject = mapRow(resultSet, columnIndexesByTable.get(tableName), childClass);
                Annotation annotation = annotationByTable.get(tableName);
                if(annotation instanceof ManyToOne){
                    ManyToOne oneToMany = (ManyToOne) annotation;
                    Class type = oneToMany.type();
                    mapColumn(parent,oneToMany.name(),type, childObject);
                }else if( annotation instanceof  OneToMany){
                    OneToMany oneToMany = (OneToMany) annotation;
                    List list = (List)getValueFromObject(parent, oneToMany.name());
                    list.add(childObject);
                }
            }

        }

        List<T> data = new ArrayList<T>();
        for(T t : parentMap.values()){
            data.add(t);
        }
        return data;
    }


}
