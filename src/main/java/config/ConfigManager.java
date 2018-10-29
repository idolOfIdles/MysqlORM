package config;

import annotation.Table;
import util.FileManager;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by safayat on 10/29/18.
 */
public class ConfigManager {
    private static ConfigManager ourInstance = new ConfigManager();
    private Map<String, Map<String, Class>> databaseClassTableMap;

    private String dbUserName = "root";

    private String dbPassword = "root";

    //        private String dbName  = "rssdesk";
    private String dbName  = "alhelal";

    //        private String dbUrl = "jdbc:mysql://localhost:3306/rssdesk?useSSL=false";
    private String dbUrl = "jdbc:mysql://localhost:3306/alhelal";


    public static ConfigManager getInstance() {
        return ourInstance;
    }

    private ConfigManager() {
        populateTableMapping();

    }

    private void populateTableMapping() {
        databaseClassTableMap = new HashMap<String, Map<String, Class>>();
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

    public Class getClassByTableName(String table, String databaseName) {
        Class tableClass = null;
        if(databaseClassTableMap.containsKey(databaseName))
            tableClass = databaseClassTableMap.get(databaseName).get(tableClass);

        if(tableClass == null){
            try {
                tableClass = Class.forName(table);
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
