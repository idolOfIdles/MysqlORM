package safayat.orm.config;

import safayat.orm.annotation.Table;
import safayat.orm.interfaces.ConnectionPoolInterface;
import safayat.orm.reflect.FileManager;
import safayat.orm.reflect.Util;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by safayat on 10/29/18.
 */
public class ConfigManager {
    private static ConfigManager ourInstance = new ConfigManager();
    private Map<String, Map<String, Class>> databaseClassTableMap;
    private Map<Class, Table> tableAnnotationByClass;
    private Map<String, Class> classMap;
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

}
