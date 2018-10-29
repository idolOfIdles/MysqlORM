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
                    
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
