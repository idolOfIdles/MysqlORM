package dao;

import jdbcUtility.ResultSetMetadataUtility;
import model.desk;
import model.user;
import queryBuilder.MysqlQuery;
import util.ReflectUtility;

import java.lang.annotation.Annotation;
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

    public List<desk> getDesks() throws Exception{

        Connection dbConnection = null;
        PreparedStatement statement = null;
        List<desk> desks = new ArrayList<desk>();
        try {
            MysqlQuery sqlQuery = MysqlQuery.get("dk.*, us.*")
                    .table("Desk", "dk")
                    .leftJoin("User", "us").on("us.id", "dk.user_id")
                    .getQuery();

            String sql = sqlQuery.toString();

            dbConnection = getConnection();
            statement = dbConnection.prepareStatement(sql);
            ResultSet rs = statement.executeQuery();

            return getData(desk.class, rs);


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

        ResultSetMetadataUtility resultSetMetadataUtility = new ResultSetMetadataUtility(resultSet.getMetaData());
        Map<String, Annotation> annotationByTable = ReflectUtility.getAnnotationByTable(clazz);
        int parentClassUniqueFieldIndex = ReflectUtility.getParentUniqueColumnIndex(resultSetMetadataUtility, clazz);
        Map<Object, T> parentMap = new HashMap<Object, T>();
        List<T> data = new ArrayList<T>();
        while (resultSet.next()){

            T parent = null;
            if( parentClassUniqueFieldIndex  > 0){
                Object uniqueFieldValue = resultSet.getObject(parentClassUniqueFieldIndex);
                parent = parentMap.get(uniqueFieldValue);
                if(parent == null ){
                    parent = ReflectUtility.mapRow(resultSet, resultSetMetadataUtility.getColumnIndexes(clazz.getSimpleName()), clazz);
                    parentMap.put(uniqueFieldValue, parent);
                    data.add(parent);
                }
            }else {
                parent = ReflectUtility.mapRow(resultSet, resultSetMetadataUtility.getColumnIndexes(clazz.getSimpleName()), clazz);
                data.add(parent);
            }

            for(String tableName : resultSetMetadataUtility.getTables()){
                Class childClass = Class.forName("model."+tableName);
                Object childObject = ReflectUtility.mapRow(resultSet, resultSetMetadataUtility.getColumnIndexes(childClass.getSimpleName()), childClass);
                Annotation annotation = annotationByTable.get(tableName);
                ReflectUtility.mapRelation(annotation, parent, childObject);
            }

        }

        return data;
    }


}
