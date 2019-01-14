package safayat.orm.jdbcUtility;

import safayat.orm.config.ConfigManager;
import safayat.orm.reflect.ReflectUtility;
import safayat.orm.reflect.RelationInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by safayat on 10/26/18.
 */
public class TableMetadata {

    private Map<String,Class> primaryKeyDbTypeByName;
    private Map<String,Class> primaryKeyClassTypeByName;
    private String  tableName;
    private String databaseName;
    private boolean  isAutoIncrement;
    private Class  tableClass;
    private List<RelationInfo> relationInfoListFoundInClass;

    public Class getTableClass() {
        return tableClass;
    }

    public TableMetadata(String tableName
            , String databaseName
            , Class tableClass
            , List<RelationInfo> relationInfos
    ) throws Exception{
        this.tableName = tableName;
        this.databaseName = databaseName;
        primaryKeyDbTypeByName = new HashMap<>();
        primaryKeyClassTypeByName = new HashMap<>();
        this.tableClass = tableClass;
        relationInfoListFoundInClass = relationInfos;

    }

    public Map<String, Class> getPrimaryKeyDbTypeByName() {
        return primaryKeyDbTypeByName;
    }

    public void setPrimaryKeyDbTypeByName(Map<String, Class> primaryKeyDbTypeByName) {
        this.primaryKeyDbTypeByName = primaryKeyDbTypeByName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public boolean isAutoIncrement() {
        return isAutoIncrement;
    }

    public void setIsAutoIncrement(boolean isAutoIncrement) {
        this.isAutoIncrement = isAutoIncrement;
    }

    public Map<String, Class> getPrimaryKeyClassTypeByName() {
        return primaryKeyClassTypeByName;
    }

    public void setPrimaryKeyClassTypeByName(Map<String, Class> primaryKeyClassTypeByName) {
        this.primaryKeyClassTypeByName = primaryKeyClassTypeByName;
    }

    public String[] getPrimaryKeys() {
        return primaryKeyDbTypeByName.keySet().toArray(new String[0]);
    }

    public List<String> getPrimaryKeysAsList() {
        String[] primaryKeys =  getPrimaryKeys();
        List<String> primaryKeyList = new ArrayList<>();
        for (String key : primaryKeys)primaryKeyList.add(key);
        return primaryKeyList;
    }

    public String getSinglePrimaryKey() {
        return (String) primaryKeyDbTypeByName.keySet().toArray()[0];
    }

    public void addPrimaryKey(String key, Class type) {
         primaryKeyDbTypeByName.put(key, type);
    }

    public Class getKeyType(String key) {
         return primaryKeyDbTypeByName.get(key);
    }

    public void addClassPrimaryKey(String key, Class type) {
         primaryKeyClassTypeByName.put(key, type);
    }

    public Class getClassKeyType(String key) {
         return primaryKeyClassTypeByName.get(key);
    }

    public static String getTableName(Class tableClass){
        return ConfigManager.getInstance().getTableName(tableClass);
    }

    public List<RelationInfo> getRelationInfos() {
        return relationInfoListFoundInClass;
    }
}
