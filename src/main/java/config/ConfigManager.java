package config;

import annotation.Table;
import dao.CommonDAO;
import queryBuilder.MysqlQuery;
import util.FileManager;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
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

    private static String dbUserName;
    private static String dbPassword;
    private static String dbName;
    private static String dbUrl;


    public static ConfigManager getInstance() {
        return ourInstance;
    }

    private ConfigManager(){
        populateTableMapping();
        try {
            readProperties();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void readProperties() throws IOException {
        final Properties properties = new Properties();
        String propertyFileName = "database.properties";
        properties.load(getClass()
                .getClassLoader()
                .getResourceAsStream(propertyFileName));
        dbName = properties.getProperty("db.name");
        dbUserName = properties.getProperty("db.user");
        dbUrl = properties.getProperty("db.url");
        dbPassword = properties.getProperty("db.password");
    }

    private void populateTableMapping() {
        databaseClassTableMap = new HashMap<String, Map<String, Class>>();
        tableAnnotationByClass = new HashMap<Class, Table>();
        Class[] tableClasses = null;
        try {
            tableClasses = FileManager.getClasses("model");
            for(Class tableClazz : tableClasses){
                Annotation annotation = tableClazz.getAnnotation(Table.class);
                if(annotation != null){
                    Table table = (Table)annotation;
                    Map<String, Class> classByTable =   databaseClassTableMap.get(table.databaseName());
                    if(classByTable == null){
                        classByTable = new HashMap<String, Class>();
                        databaseClassTableMap.put(table.databaseName(), classByTable);
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
        return tableClass.getSimpleName();
    }

    public Class getClassByTableName(String table, String databaseName) {
        Class tableClass = null;
        if(databaseClassTableMap.containsKey(databaseName))
            tableClass = databaseClassTableMap.get(databaseName).get(table);

        if(tableClass == null){
            try {
                tableClass = Class.forName("model." + table);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
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
}
