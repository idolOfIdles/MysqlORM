package safayat.orm.config;

import safayat.orm.annotation.Table;
import safayat.orm.reflect.FileManager;
import safayat.orm.reflect.Util;

import java.io.IOException;
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

    private String dbUserName;
    private String dbPassword;
    private String dbName;
    private String dbUrl;
    private String dbDriverName;
    private String modelPackageName;


    public static ConfigManager getInstance() {
        return ourInstance;
    }

    private ConfigManager(){
        try {
            readProperties();
        } catch (IOException e) {
            e.printStackTrace();
        }
        populateTableMapping();

        System.out.println(databaseClassTableMap.size());
        databaseClassTableMap.forEach((k,v)->{
            System.out.println(k);
            v.forEach((t,c)->{
                System.out.println("t:" + t + " class:" + c);
            });
        });


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
        System.out.println("dbUserName:" + dbUserName);
        System.out.println("dbUrl:" + dbUrl);
        System.out.println("dbPassword:" +dbPassword);
        System.out.println("modelPackageName: "+modelPackageName);
        System.out.println("dbDriverName:" + dbDriverName);
        System.out.println("dbDriverName:" + dbName);
    }

    private void populateTableMapping() {
        databaseClassTableMap = new HashMap<String, Map<String, Class>>();
        tableAnnotationByClass = new HashMap<Class, Table>();
        Class[] tableClasses = null;
        try {
            tableClasses = FileManager.getClasses(modelPackageName);
            for(Class tableClazz : tableClasses){
                Annotation annotation = tableClazz.getAnnotation(Table.class);
                if(annotation != null){
                    Table table = (Table)annotation;
                    String databaseName = table.databaseName();
                    if(databaseName.isEmpty()) databaseName = dbName;
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
        return Util.classNameToTable(tableClass.getSimpleName());
    }

    public Class getClassByTableName(String table, String databaseName) {
        Class tableClass = null;
        if(databaseClassTableMap.containsKey(databaseName))
            tableClass = databaseClassTableMap.get(databaseName).get(table.toLowerCase());

        if(tableClass == null){
            try {

                System.out.println(ConfigManager.getInstance().getModelPackageName()
                        + "."
                        + Util.toTitle(table));
                tableClass = Class.forName(ConfigManager.getInstance().getModelPackageName()
                        + "."
                        + Util.toTitle(table));
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

    public String getDbDriverName() {
        return dbDriverName;
    }

    public String getModelPackageName() {
        return modelPackageName;
    }

}
