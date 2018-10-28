package dao;

import jdbcUtility.ResultSetMetadataUtility;
import jdbcUtility.ResultSetUtility;
import model.category;
import model.subCategory;
import model.user;
import queryBuilder.MysqlQuery;
import util.ReflectUtility;
import util.RelationAnnotationInfo;

import java.lang.annotation.Annotation;
import java.sql.*;
import java.util.*;

/**
 * Created by safayat on 10/20/18.
 */
public class CommonDAO {

    private String dbUserName = "root";

    private String dbPassword = "root";

//        private String dbName  = "rssdesk";
    private String dbName  = "alhelal";

//        private String dbUrl = "jdbc:mysql://localhost:3306/rssdesk?useSSL=false";
    private String dbUrl = "jdbc:mysql://localhost:3306/alhelal";



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


    public List<subCategory> getSubcategorys() throws Exception{

        Connection dbConnection = null;
        PreparedStatement statement = null;
        List<subCategory> subCategorys = new ArrayList<subCategory>();
        try {
            MysqlQuery sqlQuery = MysqlQuery.get()
                    .table("subCategory", "sc")
                    .join("category", "ct").on("ct.id", "sc.categoryId")
                    .join("product", "pd").on("sc.id", "pd.subCategoryId")
                    .getQuery();

            String sql = sqlQuery.toString();
            System.out.println(sql);
            dbConnection = getConnection();
            statement = dbConnection.prepareStatement(sql);
            ResultSet rs = statement.executeQuery();

            return getData2(subCategory.class, rs);


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
        return subCategorys;
    }

    public List<category> getCategorys() throws Exception{

        Connection dbConnection = null;
        PreparedStatement statement = null;
        List<category> subCategorys = new ArrayList<category>();
        try {
            MysqlQuery sqlQuery = MysqlQuery.get()
                    .table("subCategory", "sc")
                    .join("category", "ct").on("ct.id", "sc.categoryId")
                    .join("product", "pd").on("sc.id", "pd.subCategoryId")
                    .filter("ct.id =", "9")
                    .getQuery();

            String sql = sqlQuery.toString();
            System.out.println(sql);
            dbConnection = getConnection();
            statement = dbConnection.prepareStatement(sql);
            ResultSet rs = statement.executeQuery();

            return getData2(category.class, rs);


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
        return subCategorys;
    }

    public <T> List<T> getData2(Class<T> clazz, ResultSet resultSet) throws Exception{

        ResultSetUtility resultSetUtility = new ResultSetUtility(resultSet);
        ResultSetMetadataUtility resultSetMetadataUtility = resultSetUtility.getMetadata();
        String[] tables = resultSetMetadataUtility.getTables().toArray(new String[0]);
        Map<String, Integer> tableIndexByName = new HashMap<String, Integer>();
        for(int i=0;i<tables.length;i++)tableIndexByName.put(tables[i], i);
        Map<String, Object>[] subRowMaps = new Map[tables.length];
        for(int i=0;i<subRowMaps.length;i++) subRowMaps[i] = new HashMap<String, Object>();

        List<String[]> rowsAsKeys = new ArrayList<String[]>();
        while (resultSetUtility.getResultSet().next()){
            String[] rowAsTableKeys = new String[subRowMaps.length];

            for(int i=0;i<subRowMaps.length;i++){
                String tableName = tables[i];
                String tableKey = resultSetUtility.createTableKeyForCurrentRow(tableName);
                rowAsTableKeys[i] = tableKey;
                Map<String,Object> subRowMap  = subRowMaps[i];
                Object subRow = subRowMap.get(tableKey);
                if(subRow == null){
                    Class childClass = Class.forName("model."+tables[i]);
                    Object childObject = resultSetUtility.mapRow(childClass);
                    subRowMap.put(tableKey, childObject);
                }
            }
            rowsAsKeys.add(rowAsTableKeys);
        }

        Map<String, Class> relatedClassByName = new HashMap<String, Class>();
        Map<Class, RelationAnnotationInfo> parentClassMap = new HashMap<Class, RelationAnnotationInfo>();
        ReflectUtility.populateDescentAnnotations(clazz, relatedClassByName, parentClassMap);
        List<T> data = new ArrayList<T>();
        for(int rowIndex=0;rowIndex<rowsAsKeys.size();rowIndex++){
            String[] rowAsKeys = rowsAsKeys.get(rowIndex);
            for(int tableIndex=0;tableIndex<tables.length;tableIndex++){
                String tableName = tables[tableIndex];
                Class tableClass = relatedClassByName.get(tableName);
                String key = rowAsKeys[tableIndex];
                if(tableClass != null){
                    Object tableObject = subRowMaps[tableIndex].get(key);
                    if( !tableName.equalsIgnoreCase(clazz.getSimpleName())){
                        RelationAnnotationInfo relationAnnotationInfo = parentClassMap.get(tableClass);
                        if(relationAnnotationInfo != null){
                            Class parentClass = relationAnnotationInfo.getParent();
                            String parentKey = rowAsKeys[tableIndexByName.get(parentClass.getSimpleName())];
                            if(relationAnnotationInfo.isAlreadyMapped(parentKey, key)) continue;
                            Object parentObject = subRowMaps[tableIndexByName.get(parentClass.getSimpleName())].get(parentKey);
                            ReflectUtility.mapRelation(relationAnnotationInfo.getRelationAnnotation(), parentObject, tableObject);
                            relationAnnotationInfo.addMap(parentKey, key);
                        }
                    }
                }


            }
        }
        for(Object o : subRowMaps[tableIndexByName.get(clazz.getSimpleName())].values()){
            data.add((T)o);
        }
        return data;
    }


}
