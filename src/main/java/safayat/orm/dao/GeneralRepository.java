package safayat.orm.dao;

import safayat.orm.annotation.ManyToOne;
import safayat.orm.annotation.OneToMany;
import safayat.orm.annotation.Table;
import safayat.orm.config.ConfigManager;
import safayat.orm.jdbcUtility.ResultSetUtility;
import safayat.orm.reflect.Util;
import safayat.orm.reflect.ReflectUtility;
import safayat.queryBuilder.MysqlQuery;

import java.lang.annotation.Annotation;
import java.sql.*;
import java.util.*;

/**
 * Created by safayat on 10/20/18.
 */
public class GeneralRepository {


    private Connection getConnection() throws SQLException {
        return ConfigManager.getInstance().getConnection();
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
    private int[] executeBatch(String[] sqls) throws SQLException {
        return executeBatch(sqls, getConnection());
    }

    private int[] executeBatch(String[] sqls, Connection dbConnection) throws SQLException{
        Statement statement = null;
        try {
                statement = dbConnection.createStatement();
                for(String sql : sqls){
                    statement.addBatch(sql);
                }
                return statement.executeBatch();

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
                    .append(" = ").append(Util.toMysqlString(id));

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

    public <T> T mapSingleObject(Class<T> tClass, ResultSet resultSet) throws Exception{
       return new ResultSetUtility(resultSet).mapRow(tClass);
    }

    public <T> void insert(T t)throws Exception{
        insert(t, getConnection());
    }

    public <T> void insert(T t, Connection connection) throws Exception {
        Map<String, Boolean> objectMap = new HashMap<>();
        createInsertSqlMapByTraversingRelationTree(t, connection, objectMap);
        if(objectMap.size() == 1){
            for(Object insertSql : objectMap.keySet()){
                execute(insertSql.toString(), connection);
            }
        }else {
            executeBatch(objectMap.keySet().toArray(new String[0]), connection);
        }
    }

    public <T> int[] insert(List<T> objects)throws Exception{
        return insert(objects, getConnection());
    }

    public <T> int[] insert(List<T> objects, Connection connection) throws Exception {
       List<String> sqls = new ArrayList<>();
        for(Object o : objects){
            Map<String, Boolean> objectMap = new HashMap<>();
            createInsertSqlMapByTraversingRelationTree(o, connection, objectMap);
            for(String sql : objectMap.keySet()) sqls.add(sql);
        }
        return executeBatch(sqls.toArray(new String[0]), connection);
    }

    private void createInsertSqlMapByTraversingRelationTree(Object t
            , Connection connection
            , Map<String, Boolean> nodes) throws Exception {
        String sql = ReflectUtility.createInsertSqlString(t);
        if(nodes.containsKey(t)) return;
        nodes.put(sql, true);
        Map<String,Annotation> annotationByTable = ReflectUtility.getAnnotationByTable(t.getClass());
        for(Annotation annotation : annotationByTable.values()){
            if(annotation instanceof ManyToOne){
                ManyToOne oneToMany = (ManyToOne) annotation;
                Object childObject = ReflectUtility.getValueFromObject(t, oneToMany.name());
                createInsertSqlMapByTraversingRelationTree(childObject, connection, nodes);
            }else if( annotation instanceof OneToMany){
                OneToMany oneToMany = (OneToMany) annotation;
                List list = (List)ReflectUtility.getValueFromObject(t, oneToMany.name());
                if(list != null){
                    for(Object o : list){
                        createInsertSqlMapByTraversingRelationTree(o, connection, nodes);
                    }
                }
            }
        }
    }

    private void createUpdateSqlMapByTraversingRelationTree(Object t
            , Connection connection
            , Map<Class, List<String>> primaryKeyByTable
            , Map<String,Boolean> nodes) throws Exception {
        List<String> primaryKeys = primaryKeyByTable.get(t.getClass());
        if(primaryKeys == null){
            primaryKeys = findPrimaryKeys(t, connection);
            primaryKeyByTable.put(t.getClass(), primaryKeys);
        }
        String sql =  ReflectUtility.createSingleRowUpdateSqlString(t, primaryKeys);
        if(nodes.containsKey(t)) return;
        nodes.put(sql, true);
        Map<String,Annotation> annotationByTable = ReflectUtility.getAnnotationByTable(t.getClass());
        for(Annotation annotation : annotationByTable.values()){
            if(annotation instanceof ManyToOne){
                ManyToOne oneToMany = (ManyToOne) annotation;
                Object childObject = ReflectUtility.getValueFromObject(t, oneToMany.name());
                createUpdateSqlMapByTraversingRelationTree(childObject, connection, primaryKeyByTable,  nodes);
            }else if( annotation instanceof OneToMany){
                OneToMany oneToMany = (OneToMany) annotation;
                List list = (List)ReflectUtility.getValueFromObject(t, oneToMany.name());
                if(list != null){
                    for(Object o : list){
                        createUpdateSqlMapByTraversingRelationTree(o, connection, primaryKeyByTable, nodes);
                    }
                }
            }
        }
    }

    public Object insertAndRetrieveId(Object row) throws SQLException {
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

    public void update(Object t) throws Exception{
        update(t,getConnection());
    }

    public void update(Object t, Connection connection) throws Exception{
        Map<String, Boolean> objectMap = new HashMap<>();
        createUpdateSqlMapByTraversingRelationTree(t, connection, new HashMap<Class, List<String>>(), objectMap);
        if(objectMap.size() == 1){
            for(Object insertSql : objectMap.keySet()){
                execute(insertSql.toString(), connection);
            }
        }else {
            executeBatch(objectMap.keySet().toArray(new String[0]), connection);
        }

    }

    public <T> int[] update(List<T> objects)throws Exception{
        return update(objects, getConnection());
    }

    public <T> int[] update(List<T> objects, Connection connection) throws Exception {
        List<String> sqls = new ArrayList<>();
        Map<Class, List<String>> primaryKeyMap = new HashMap<>();
        for(Object o : objects){
            Map<String, Boolean> objectMap = new HashMap<>();
            createUpdateSqlMapByTraversingRelationTree(o, connection, primaryKeyMap, objectMap);
            for(String sql : objectMap.keySet()) sqls.add(sql);
        }
        return executeBatch(sqls.toArray(new String[0]), connection);
    }

    public <T> List<T> getAll(Class<T> tClass, String sql) {

        ResultSetUtility resultSetUtility = executeQuery(sql);
        try {
            if(resultSetUtility!=null){
                List<T> result=  resultSetUtility.mapResultsetToObjects(tClass);
                resultSetUtility.close();
                return result;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }
    public <T> List<T> getAll(Class<T> tClass) {
        return getAll(tClass, "select * from " + ConfigManager.getInstance().getTableName(tClass));
    }

    public <T> List<T> getAll(Class<T> tClass, int limit) {
        return getAll(tClass, "select * from " + ConfigManager.getInstance().getTableName(tClass) + " limit " + limit);    }

    public <T> List<T> getAll(Class<T> tClass, int limit, int offset) {
        return getAll(tClass, "select * from " + ConfigManager.getInstance().getTableName(tClass) + " limit " + limit + " offset " + offset);
    }

    public <T> List<T> mapResultSetToObjects(Class<T> tClass, ResultSet resultSet) {
        try {
            return new ResultSetUtility(resultSet).mapResultsetToObjects(tClass);
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


    private void closeResourcesSafely(Connection dbConnection, Statement statement){

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
