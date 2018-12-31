package safayat.orm.config;

import safayat.orm.annotation.Table;
import safayat.orm.dao.GeneralRepository;
import safayat.orm.interfaces.ConnectionPoolInterface;
import safayat.orm.jdbcUtility.PrimaryKeyInfo;
import safayat.orm.reflect.FileManager;
import safayat.orm.reflect.Util;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.sql.*;
import java.util.*;

/**
 * Created by safayat on 10/29/18.
 */
public class ConfigManager {
    private static ConfigManager ourInstance = new ConfigManager();
    private Map<String, Map<String, Class>> databaseClassTableMap;
    private Map<Class, Table> tableAnnotationByClass;
    private Map<String, Class> classMap;
    private Map<String, Map<String,PrimaryKeyInfo>> primaryInfoMapForDatabases;
    private String dbUserName;
    private String dbPassword;
    private String dbName;
    private String dbUrl;
    private String dbDriverName;
    private String modelPackageName;
    ConnectionPoolInterface connectionPool;

    public static ConfigManager getInstance() {
        return ourInstance;
    }

    public void setConnectionPool(ConnectionPoolInterface connectionPoolInterface) {
        if(connectionPool == null){
            this.connectionPool = connectionPoolInterface;
        }
    }

    private ConfigManager(){

        try {
            readProperties();
        } catch (IOException e) {
            e.printStackTrace();
        }
        populateTableMapping();
        try {
            primaryInfoMapForDatabases = new HashMap<>();
            readAndPopulateDatabaseMetadata(getDbName());
        } catch (Exception e) {

        }
    }

    private void readProperties() throws IOException {
        final Properties properties = new Properties();
        String propertyFileName = "database.properties";
        properties.load(getClass()
                .getClassLoader()
                .getResourceAsStream(propertyFileName));
        dbUserName = properties.getProperty("db.user");
        dbUrl = properties.getProperty("db.url");
        dbPassword = properties.getProperty("db.password");
        modelPackageName = properties.getProperty("db.model.package");
        dbDriverName = properties.getProperty("db.driver");
        dbName = properties.getProperty("db.name");
    }

    private void populateTableMapping() {
        databaseClassTableMap = new HashMap<String, Map<String, Class>>();
        tableAnnotationByClass = new HashMap<Class, Table>();
        classMap = new HashMap<>();
        Class[] tableClasses = null;
        try {
            tableClasses = FileManager.getClasses(modelPackageName);

            for(Class tableClazz : tableClasses){
                classMap.put(tableClazz.getSimpleName().toLowerCase(), tableClazz);
                Annotation annotation = tableClazz.getAnnotation(Table.class);
                if(annotation != null){
                    Table table = (Table)annotation;
                    String databaseName = table.databaseName();
                    if(databaseName.isEmpty()) databaseName = dbName;
                    Map<String, Class> classByTable =   databaseClassTableMap.get(databaseName);
                    if(classByTable == null){
                        classByTable = new HashMap<String, Class>();
                        databaseClassTableMap.put(databaseName, classByTable);
                    }
                    classByTable.put(table.name().toLowerCase(), tableClazz);
                    tableAnnotationByClass.put(tableClazz, table);
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public Class getClassByTableName(String table) {
        return getClassByTableName(table, dbName);
    }


    public String getTableName(Class tableClass) {
        Table table = tableAnnotationByClass.get(tableClass);
        if(table!=null){
            return table.name();
        }
        return Util.classNameToTable(tableClass.getSimpleName());
    }

    public Class getClassByTableName(String table, String databaseName) {
        Class tableClass = null;
        if(databaseClassTableMap.containsKey(databaseName))
            tableClass = databaseClassTableMap.get(databaseName).get(table.toLowerCase());

        if(tableClass == null){
            tableClass = classMap.get(table.toLowerCase());
        }
        return tableClass;
    }

    public String getDbUserName() {
        return dbUserName;
    }

    public String getDbPassword() {
        return dbPassword;
    }

    public String getDbName() {
        return dbName;
    }

    public String getDbUrl() {
        return dbUrl;
    }

    public String getDbDriverName() {
        return dbDriverName;
    }

    public String getModelPackageName() {
        return modelPackageName;
    }
    public Connection getConnection() throws SQLException {
        if(connectionPool != null) return connectionPool.getConnection();
        return DriverManager.getConnection(dbUrl, dbUserName, dbPassword);
    }


    private void readAndPopulateDatabaseMetadata(String databaseName) throws Exception {

        Map<String, PrimaryKeyInfo> primaryKeyInfoByTable = new HashMap<>();
        primaryInfoMapForDatabases.put(databaseName, primaryKeyInfoByTable);
        Connection connection = getConnection();
        String[] types = {"TABLE"};
        ResultSet resultSet = connection.getMetaData().getTables(databaseName, null,"", types);
        List<String> tableNames = new ArrayList<>();
        while (resultSet.next()){
            tableNames.add(resultSet.getString("TABLE_NAME"));
        }

        resultSet.close();

        for(String tableName : tableNames){
            resultSet = connection.getMetaData().getPrimaryKeys(getDbName(), null, tableName);
            PrimaryKeyInfo primaryKeyInfo = new PrimaryKeyInfo(tableName, getDbName());
            while (resultSet.next()){
                String columnName = resultSet.getString(4);
                primaryKeyInfo.addPrimaryKey(columnName, null);
                primaryKeyInfo.addClassPrimaryKey(columnName, null);
            }
            resultSet.close();
            updatePrimaryKeyInfo(connection, databaseName, tableName, primaryKeyInfo);
            primaryKeyInfoByTable.put(tableName.toLowerCase(), primaryKeyInfo);
        }



    }

    private  void updatePrimaryKeyInfo(Connection connection
            , String databaseName
            , String tableName
            , PrimaryKeyInfo primaryKeyInfo) throws SQLException {


        for(String primaryKey : primaryKeyInfo.getPrimaryKeys()){
            ResultSet resultSet1 = connection.getMetaData().getColumns(databaseName, null, tableName, primaryKey);
            while (resultSet1.next()){
                Class primaryKeyType = Util.getClassByMysqlType(resultSet1.getInt("DATA_TYPE"));
                boolean autoIncrement = resultSet1.getString("IS_AUTOINCREMENT").equalsIgnoreCase("YES");
                primaryKeyInfo.addPrimaryKey(primaryKey,primaryKeyType);
                primaryKeyInfo.setIsAutoIncrement(autoIncrement);
                Class tableAsClass = getClassByTableName(primaryKeyInfo.getTableName());
                try {
                    Class primaryKeyTypeInClass = tableAsClass.getDeclaredField(primaryKey).getType();
                    primaryKeyInfo.addClassPrimaryKey(primaryKey, primaryKeyTypeInClass);
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                }
            }

            resultSet1.close();
        }
    }



    public PrimaryKeyInfo getPrimaryKeyInfo(String tableName) throws Exception {
        return primaryInfoMapForDatabases.get(getDbName()).get(tableName.toLowerCase());
    }
    public PrimaryKeyInfo getPrimaryKeyInfo(Class table) throws Exception {
        return getPrimaryKeyInfo(getTableName(table));
    }

    public boolean havePrimaryKey(Class table) throws Exception {
        PrimaryKeyInfo primaryKeyInfo = getPrimaryKeyInfo(table);
        return primaryKeyInfo != null && primaryKeyInfo.getPrimaryKeyDbTypeByName().size() > 0;
    }

}
