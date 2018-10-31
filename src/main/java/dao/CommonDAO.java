package dao;

import annotation.Table;
import config.ConfigManager;
import jdbcUtility.ResultSetUtility;
import model.Category;
import model.SubCategory;
import queryBuilder.MysqlQuery;
import util.ReflectUtility;

import java.sql.*;
import java.util.*;

/**
 * Created by safayat on 10/20/18.
 */
public class CommonDAO {


    private Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        Connection connection = null;

        try {
            connection = DriverManager.getConnection(
                    ConfigManager.getInstance().getDbUrl()
                    , ConfigManager.getInstance().getDbUserName()
                    , ConfigManager.getInstance().getDbPassword());

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return connection;

    }

    private ResultSetUtility executeQuery(String sql) {
        Connection dbConnection = null;
        PreparedStatement statement = null;
        try {

            dbConnection = getConnection();
            statement = dbConnection.prepareStatement(sql);
            ResultSet rs = statement.executeQuery();
            try {
                return new ResultSetUtility(rs);
            } catch (Exception e) {
                e.printStackTrace();
            }

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
        return null;

    }
    private void execute(String sql) {
        Connection dbConnection = null;
        PreparedStatement statement = null;
        try {

            dbConnection = getConnection();
            statement = dbConnection.prepareStatement(sql);
            statement.execute();

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

    }

    public <T> T get(Class<T> tClass, Object id) {
        Table table = tClass.getAnnotation(Table.class);
        String sql = MysqlQuery.get()
                .table(table.name())
                .filter(table.primaryKeyColumn() + "=", id.toString())
                .getQuery()
                .toString();
        System.out.println(sql);
        ResultSetUtility resultSetUtility = executeQuery(sql);
        try {
            if(resultSetUtility!=null && resultSetUtility.getResultSet().next()){
               return resultSetUtility.mapRow(tClass);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    public void insert(Object t) {
        String sql =  ReflectUtility.createInsertSqlString(t);
        execute(sql);
    }

    public void update(Object t) {
        try {
            String sql = ReflectUtility.createSingleRowUpdateSqlString(t);
            System.out.println(sql);
            execute(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public  <T> List<T> getAll(Class<T> tClass, MysqlQuery q) {
        return getAll(tClass, q.toString());
    }

    public <T> List<T> getAll(Class<T> tClass, String sql) {
        ResultSetUtility resultSetUtility = executeQuery(sql);
        try {
            if(resultSetUtility!=null && resultSetUtility.getResultSet().next()){
               return resultSetUtility.mapResultsetToClass(tClass);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    private List<String> getPrimaryKeys(String table, Connection connection) {


        List<String> primaryKeys = new ArrayList<String>();
        try {
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet rs = metaData.getPrimaryKeys(ConfigManager.getInstance().getDbName(), null, table);
            while (rs.next()){
                primaryKeys.add(rs.getString("column_name"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return primaryKeys;

    }


}
