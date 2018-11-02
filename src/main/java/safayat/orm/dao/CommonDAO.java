package safayat.orm.dao;

import safayat.orm.annotation.Table;
import safayat.orm.config.ConfigManager;
import safayat.orm.jdbcUtility.ResultSetUtility;
import safayat.queryBuilder.MysqlQuery;
import safayat.orm.reflect.ReflectUtility;

import java.sql.*;
import java.util.*;

/**
 * Created by safayat on 10/20/18.
 */
public class CommonDAO {


    private Connection getConnection() {
        try {
            Class.forName(ConfigManager.getInstance().getDbDriverName());
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
            closeResourcesSafely(dbConnection, statement);
        }
        return null;

    }

    private ResultSet executeInsert(String sql, Object row) {
        Connection dbConnection = null;
        PreparedStatement statement = null;
        try {

            dbConnection = getConnection();

            List<String> primaryKeys = findPrimaryKeys(row,dbConnection);

            if(primaryKeys.isEmpty()) throw new SQLException("No primary key found");

            statement = dbConnection.prepareStatement(sql, primaryKeys.toArray(new String[0]));
            statement.execute();
            return statement.getGeneratedKeys();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            closeResourcesSafely(dbConnection, statement);
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
            closeResourcesSafely(dbConnection, statement);
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

    public Object insertAndRetrieveId(Object t) {
        Object primaryKeyValue = null;
        String sql =  ReflectUtility.createInsertSqlString(t);
        try {
            ResultSet rs = executeInsert(sql, t.getClass());
            while (rs.next()){
                primaryKeyValue = rs.getObject(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return primaryKeyValue;
    }

    public boolean update(Object t) {
        try {
            String sql = ReflectUtility.createSingleRowUpdateSqlString(t);
            execute(sql);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public  <T> List<T> getAll(Class<T> tClass, MysqlQuery q) {
        return getAll(tClass, q.toString());
    }

    public <T> List<T> getAll(Class<T> tClass, String sql) {
        ResultSetUtility resultSetUtility = executeQuery(sql);
        try {
            if(resultSetUtility!=null){
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

    private List<String> findPrimaryKeys(Object row, Connection connection) {

        Table tableAnnotation = row.getClass().getAnnotation(Table.class);
        List<String> primaryKeys = null;
        if(tableAnnotation != null && !tableAnnotation.primaryKeyColumn().isEmpty() ){
            primaryKeys = new ArrayList<>();
            String[] primaryKeysInAnnotation = tableAnnotation.primaryKey().split(",");
            for(String pKey : primaryKeysInAnnotation ){
                primaryKeys.add(pKey.trim());
            }
        }
        if(primaryKeys.isEmpty()){
            primaryKeys = getPrimaryKeys(ConfigManager.getInstance().getTableName(row.getClass()), connection);
        }
        return primaryKeys;

    }

    private void closeResourcesSafely(Connection dbConnection, PreparedStatement statement){

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
