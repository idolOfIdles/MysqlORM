package safayat.orm.config;

import safayat.orm.annotation.Table;
import safayat.orm.interfaces.ConnectionPoolInterface;
import safayat.orm.jdbcUtility.TableMetadata;
import safayat.orm.reflect.FileManager;
import safayat.orm.reflect.ReflectUtility;
import safayat.orm.reflect.Util;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.sql.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by safayat on 10/29/18.
 */
public class ConfigManager {

    Logger logger = Logger.getLogger(ConfigManager.class.getName());
    private static ConfigManager ourInstance = new ConfigManager();
    private Map<String, Map<String,Class>> classByDatabaseAndTable;
    private Map<String,TableMetadata> tableMetadataMap;
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

        try{
            readProperties();
            classByDatabaseAndTable = parseModelPackageAndGenerateTableClassMap();
            tableMetadataMap = new HashMap<>();
            readAndCacheDatabaseMetadata(getDbName());
            logger.log(Level.INFO ,"succecssfully read configuration data");
        }catch (Exception e){
            e.printStackTrace();
            logger.log(Level.SEVERE ,"FAILED to initialize config manager. error is:"+e.getMessage());
        }
    }

    private void readProperties() throws IOException {
/*
        final Properties properties = new Properties();
        String propertyFileName = "database.properties";
        properties.load(getClass()
                .getClassLoader()
                .getResourceAsStream(propertyFileName));
*/
//        dbUserName = properties.getProperty("db.user");
        dbUserName = "safayat";
//        dbUrl = properties.getProperty("db.url");
        dbUrl = "jdbc:mysql://localhost:3307/online_exam?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
//        dbPassword = properties.getProperty("db.password");
        dbPassword = "cefalo";
//        modelPackageName = properties.getProperty("db.model.package");
        modelPackageName = "safayat.orm.model";
//        dbName = properties.getProperty("db.name");
        dbName = "online_exam";
//        dbDriverName = properties.getProperty("db.driver");
        dbDriverName = "com.mysql.cj.jdbc.Driver";
        logger.log(Level.INFO, "read properties: dbName : " + dbName +
                "\n, dbUserName:"+ dbUserName +
                "\n, dbUrl:"+ dbUrl +
                "\n, modelPackageName:"+ modelPackageName +
                "\n, dbDriverName:"+ dbDriverName +
                ".");
    }

    private Map<String, Map<String, Class>> parseModelPackageAndGenerateTableClassMap() {
        Map<String, Map<String, Class>> classByTableAndDatabase = new HashMap<String, Map<String, Class>>();
        Class[] tableClasses = null;
        try {
            tableClasses = FileManager.getClasses(modelPackageName);
            logger.log(Level.INFO, "total number of classes read by file manger: " + tableClasses.length);
            for(Class t : tableClasses){
                logger.log(Level.INFO, "tablename: " + t);
            }
            for(Class tableClazz : tableClasses){
                Annotation annotation = tableClazz.getAnnotation(Table.class);
                String tableName = tableClazz.getSimpleName();
                String databaseName = dbName;
                if(annotation != null){
                    Table table = (Table)annotation;
                    databaseName = table.databaseName().isEmpty() ? dbName : table.databaseName();
                    tableName = table.name();
                }

                Map<String, Class> classByTable = classByTableAndDatabase.get(databaseName);
                if(classByTable == null){
                    classByTable = new HashMap<String, Class>();
                    classByTableAndDatabase.put(databaseName, classByTable);
                }
                classByTable.put(tableName.toLowerCase(), tableClazz);
                logger.log(Level.INFO, "tablename: " + tableName + " class: " + tableClazz.getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return classByTableAndDatabase;

    }

    public Class getClassByTableName(String table) {
        return getClassByTableName(table, dbName);
    }


    public String getTableName(Class tableClass){
        return getTableMetadata(tableClass).getTableName();

    }

    public Class getClassByTableName(String table, String databaseName) {
        Class tableClass = null;
        if(classByDatabaseAndTable.containsKey(databaseName))
            tableClass = classByDatabaseAndTable.get(databaseName).get(table.toLowerCase());
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


    private void readAndCacheDatabaseMetadata(String databaseName) throws Exception {

        Connection connection = getConnection();
        String[] types = {"TABLE"};
        ResultSet resultSet = connection.getMetaData().getTables(databaseName, null,"", types);
        List<String> tableNames = new ArrayList<>();
        while (resultSet.next()){
            tableNames.add(resultSet.getString("TABLE_NAME"));
        }

        resultSet.close();
        tableNames.forEach(tn-> logger.log(Level.INFO, "table found in database: " + tn));

        for(String tableName : tableNames){
            Class tableClass = getClassByTableName(tableName ,databaseName);
            if( tableClass == null) continue;
            resultSet = connection.getMetaData().getPrimaryKeys(getDbName(), null, tableName);
            TableMetadata tableMetadata = new TableMetadata(tableName
                    , getDbName()
                    , tableClass
                    , ReflectUtility.getRelationAnnotations(tableClass));

            while (resultSet.next()){
                String columnName = resultSet.getString(4);
                tableMetadata.addPrimaryKey(columnName, null);
                tableMetadata.addClassPrimaryKey(columnName, null);
            }
            resultSet.close();
            readPrimaryKeyAndUpdateTableInfo(connection, databaseName, tableName, tableMetadata);
            tableMetadataMap.put(tableClass.getName(), tableMetadata);
            logger.log(Level.INFO, tableClass.getName());
        }



    }

    private  void readPrimaryKeyAndUpdateTableInfo(Connection connection
            , String databaseName
            , String tableName
            , TableMetadata tableMetadata) throws SQLException {


        for(String primaryKey : tableMetadata.getPrimaryKeys()){
            ResultSet resultSet1 = connection.getMetaData().getColumns(databaseName, null, tableName, primaryKey);
            while (resultSet1.next()){
                Class primaryKeyType = Util.getClassByMysqlType(resultSet1.getInt("DATA_TYPE"));
                boolean autoIncrement = resultSet1.getString("IS_AUTOINCREMENT").equalsIgnoreCase("YES");
                tableMetadata.addPrimaryKey(primaryKey,primaryKeyType);
                tableMetadata.setIsAutoIncrement(autoIncrement);
                Class tableAsClass = getClassByTableName(tableMetadata.getTableName());
                try {
                    Class primaryKeyTypeInClass = tableAsClass.getDeclaredField(primaryKey).getType();
                    tableMetadata.addClassPrimaryKey(primaryKey, primaryKeyTypeInClass);
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                }
            }

            resultSet1.close();
        }
    }

    public TableMetadata getTableMetadata(String tableName) {
        return tableMetadataMap.get(getClassByTableName(tableName).getName());
    }
    public TableMetadata getTableMetadata(Class table) {
        logger.log(Level.INFO, "getTableMetadata: " + table.getName());
        return tableMetadataMap.get(table.getName());
    }

    public boolean havePrimaryKey(Class table) throws Exception {
        TableMetadata tableMetadata = getTableMetadata(table);
        return tableMetadata != null && tableMetadata.getPrimaryKeyDbTypeByName().size() > 0;
    }

}
