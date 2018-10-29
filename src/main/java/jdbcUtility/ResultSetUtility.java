package jdbcUtility;

import config.ConfigManager;
import util.ReflectUtility;
import util.RelationAnnotationInfo;
import util.Util;

import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.util.*;

/**
 * Created by safayat on 10/26/18.
 */
public class ResultSetUtility {

    private ResultSet resultSet;
    private ResultSetMetadataUtility metadata;

    public ResultSetUtility(ResultSet resultSet) throws Exception{
        this.resultSet = resultSet;
        metadata = new ResultSetMetadataUtility(resultSet.getMetaData());
    }

    public ResultSet getResultSet() {
        return resultSet;
    }

    public ResultSetMetadataUtility getMetadata() {
        return metadata;
    }

    public String createTableKeyForCurrentRow(String table){
        List<Integer> columnIndexes = metadata.getColumnIndexes(table);
        StringBuilder keyBuilder = new StringBuilder();
        for(int columnIndex : columnIndexes){
            String columnValue = "";
            try {
                columnValue = resultSet.getObject(columnIndex).toString();
                keyBuilder.append(columnValue);
            } catch (Exception e) {
                e.printStackTrace();
            }
            keyBuilder.append(";");
        }
        return keyBuilder.toString();
    }

    public  <T> T mapRow(Class<T> clazz) throws Exception{

        List<Integer> columnIndexes =  metadata.getColumnIndexes(clazz.getSimpleName().toLowerCase());
        T newClazz = clazz.newInstance();
        for(int index : columnIndexes){
            mapColumn(newClazz, index);
        }
        return newClazz;

    }

    public  <T> void mapColumn(T row, int index) throws Exception{

        String columnName = metadata.get().getColumnName(index);
        int columnType = metadata.get().getColumnType(index);
        try {
            Object value = getResultSet().getObject(index);
            String methodName = Util.toJavaMethodName(columnName, "set");
            Method method = row.getClass().getDeclaredMethod(methodName, Util.getClassByType(columnType));
            if(method!=null){
                method.invoke(row,value);
            }

        }catch (Exception e){}


    }


    public <T> List<T> mapResultsetToClass(Class<T> clazz) throws Exception{

        String[] tables = metadata.getTables();
        Map<String, Integer> tableIndexByName = new HashMap<String, Integer>();
        for(int i=0;i<tables.length;i++)tableIndexByName.put(tables[i], i);
        Map<String, Object>[] subRowMaps = new Map[tables.length];
        for(int i=0;i<subRowMaps.length;i++) subRowMaps[i] = new HashMap<String, Object>();

        List<String[]> rowsAsKeys = new ArrayList<String[]>();
        while (this.getResultSet().next()){
            String[] rowAsTableKeys = new String[subRowMaps.length];
            for(int i=0;i<subRowMaps.length;i++){
                String tableName = tables[i];
                String tableKey = this.createTableKeyForCurrentRow(tableName);
                rowAsTableKeys[i] = tableKey;
                Map<String,Object> subRowMap  = subRowMaps[i];
                Object subRow = subRowMap.get(tableKey);
                if(subRow == null){
                    Class childClass = ConfigManager.getInstance().getClassByTableName(tables[i]);
                    Object childObject = this.mapRow(childClass);
                    subRowMap.put(tableKey, childObject);
                }
            }
            rowsAsKeys.add(rowAsTableKeys);
        }

        Map<String, Class> relatedClassByName = new HashMap<String, Class>();
        Map<Class, RelationAnnotationInfo> parentClassMap = new HashMap<Class, RelationAnnotationInfo>();
        ReflectUtility.populateDescentAnnotations(clazz, relatedClassByName, parentClassMap);
        List<T> data = new ArrayList<T>();
        for(int rowIndex=0;rowIndex<rowsAsKeys.size();rowIndex++){
            String[] rowAsKeys = rowsAsKeys.get(rowIndex);
            for(int tableIndex=0;tableIndex<tables.length;tableIndex++){
                String tableName = tables[tableIndex];
                Class tableClass = relatedClassByName.get(tableName);
                String key = rowAsKeys[tableIndex];
                if(tableClass != null){
                    Object tableObject = subRowMaps[tableIndex].get(key);
                    if( !tableName.equalsIgnoreCase(clazz.getSimpleName())){
                        RelationAnnotationInfo relationAnnotationInfo = parentClassMap.get(tableClass);
                        if(relationAnnotationInfo != null){
                            Class parentClass = relationAnnotationInfo.getParent();
                            String parentKey = rowAsKeys[tableIndexByName.get(parentClass.getSimpleName().toLowerCase())];
                            if(relationAnnotationInfo.isAlreadyMapped(parentKey, key)) continue;
                            Object parentObject = subRowMaps[tableIndexByName.get(parentClass.getSimpleName().toLowerCase())].get(parentKey);
                            ReflectUtility.mapRelation(relationAnnotationInfo.getRelationAnnotation(), parentObject, tableObject);
                            relationAnnotationInfo.addMap(parentKey, key);
                        }
                    }
                }
            }
        }
        for(Object o : subRowMaps[tableIndexByName.get(clazz.getSimpleName().toLowerCase())].values()){
            data.add((T)o);
        }
        return data;
    }




}
