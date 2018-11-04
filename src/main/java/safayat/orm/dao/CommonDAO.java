package safayat.orm.dao;

import safayat.orm.annotation.ManyToOne;
import safayat.orm.annotation.OneToMany;
import safayat.orm.annotation.Table;
import safayat.orm.config.ConfigManager;
import safayat.orm.jdbcUtility.ResultSetUtility;
import safayat.orm.reflect.Util;
import safayat.queryBuilder.MysqlQuery;
import safayat.orm.reflect.ReflectUtility;

import java.lang.annotation.Annotation;
import java.sql.*;
import java.util.*;
import java.util.concurrent.ExecutionException;

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


    private boolean execute(String sql) throws SQLException {
        return execute(sql, getConnection());
    }

    private boolean execute(String sql, Connection dbConnection) throws SQLException{
        PreparedStatement statement = null;
        try {

            statement = dbConnection.prepareStatement(sql);
            return statement.execute();

        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        }finally {
            closeResourcesSafely(null, statement);
        }


    }

    public <T> T get(Class<T> tClass, Object id) {
        Connection dbConnection = null;
        PreparedStatement statement = null;
        try {

            dbConnection = getConnection();
            Table table = tClass.getAnnotation(Table.class);
            List<String> primaryKeys = getPrimaryKeys(ConfigManager.getInstance().getTableName(tClass), dbConnection);

            if( primaryKeys.isEmpty()) throw new SQLException("Primary key not found");
            if( primaryKeys.size() > 1) throw new SQLException("Not applicable for composed primary keys");

            StringBuilder sqlBuilder = new StringBuilder("select * from ")
                    .append(ConfigManager.getInstance().getTableName(tClass))
                    .append(" where ").append(primaryKeys.get(0))
                    .append(" = ").append(Util.toString(id));

            statement = dbConnection.prepareStatement(sqlBuilder.toString());
            ResultSet rs = statement.executeQuery();
            if(rs!=null && rs.next()){
                ResultSetUtility resultSetUtility = new ResultSetUtility(rs);
                return resultSetUtility.mapRow(tClass);
            }

        } catch (Exception e) {
            e.printStackTrace();
            closeResourcesSafely(dbConnection, statement);
        }
        return null;

    }

    public boolean insert(Object t)throws SQLException{
        return insertTree(t, getConnection(), new HashMap<>());
    }

    public boolean insert(Object t, Connection connection) throws SQLException {
        return insertTree(t, connection, new HashMap<>());
    }

    private boolean insertTree(Object t, Connection connection, Map<String,Boolean> history) throws SQLException {
        String sql =  ReflectUtility.createInsertSqlString(t);
        if(history.containsKey(sql)) return true;
        boolean ans = execute(sql, connection);
        history.put(sql, true);
        try {
            Map<String,Annotation> annotationByTable = ReflectUtility.getAnnotationByTable(t.getClass());
            for(Annotation annotation : annotationByTable.values()){
                if(annotation instanceof ManyToOne){
                    ManyToOne oneToMany = (ManyToOne) annotation;
                    Object childObject = ReflectUtility.getValueFromObject(t, oneToMany.name());
                    insert(childObject, connection);
                }else if( annotation instanceof OneToMany){
                    OneToMany oneToMany = (OneToMany) annotation;
                    List list = (List)ReflectUtility.getValueFromObject(t, oneToMany.name());
                    for(Object o : list){
                        insert(o, connection);
                    }
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return ans;
    }

    public Object insertAndRetrieveId(Object row) {
        return insertAndRetrieveId(row, getConnection());
    }
    public Object insertAndRetrieveId(Object row, Connection dbConnection) {
        Object primaryKeyValue = null;
        PreparedStatement statement = null;
        try {

            List<String> primaryKeys = findPrimaryKeys(row,dbConnection);
            if(primaryKeys.isEmpty()) throw new SQLException("No primary key found");
            String sql =  ReflectUtility.createInsertSqlString(row);
            statement = dbConnection.prepareStatement(sql, primaryKeys.toArray(new String[0]));
            statement.execute();
            ResultSet rs = statement.getGeneratedKeys();
            while (rs.next()){
                primaryKeyValue = rs.getObject(1);
            }
            return primaryKeyValue;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            closeResourcesSafely(null, statement);
        }
        return null;

    }

    public boolean update(Object t) {
        return update(t,getConnection());
    }

    public boolean update(Object t, Connection connection) {
        try {
            String sql = ReflectUtility.createSingleRowUpdateSqlString(t);
            return execute(sql, connection);
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

    private  List<String> findPrimaryKeys(Object row, Connection connection) {

        Table tableAnnotation = row.getClass().getAnnotation(Table.class);
        List<String> primaryKeys = new ArrayList<>();
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
