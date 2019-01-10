package safayat.orm.jdbcUtility;

import safayat.orm.config.ConfigManager;
import safayat.orm.reflect.ReflectUtility;
import safayat.orm.reflect.RelationAnnotationInfo;
import safayat.orm.reflect.Util;

import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
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

    public boolean next() throws SQLException {
        return resultSet.next();
    }

    public String createTableKeyForCurrentRow(String table){
        List<Integer> columnIndexes = metadata.getColumnIndexes(table);
        StringBuilder keyBuilder = new StringBuilder();
        for(int columnIndex : columnIndexes){
            Object columnValue = null;
            try {
                columnValue = getColumnValue(columnIndex);
                keyBuilder.append(columnValue);
            } catch (Exception e) {
                e.printStackTrace();
            }
            keyBuilder.append(";");
        }
        return keyBuilder.toString();
    }

    public  <T> T mapRow(Class<T> clazz) throws Exception{

        List<Integer> columnIndexes =  metadata.getColumnIndexes(
                ConfigManager
                .getInstance()
                .getTableName(clazz)
                .toLowerCase());
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
            Object value = getColumnValue(index);
            String methodName = Util.toJavaMethodName(columnName, "set");
            Method method = row.getClass().getDeclaredMethod(methodName, Util.getClassByMysqlType(columnType));
            if(method!=null){
                method.invoke(row,value);
            }

        }catch (Exception e){
            e.printStackTrace();
        }


    }

    public Object  getColumnValue(int index) throws Exception{

        int columnType = metadata.get().getColumnType(index);
        Object value = null;

        if(Types.BIGINT == columnType){
            value = getResultSet().getLong(index);
        }else if(Types.BINARY == columnType){
            value = getResultSet().getBoolean(index);
        }
        else if(Types.INTEGER == columnType){
            value = getResultSet().getInt(index);
        }
        else if(Types.VARCHAR == columnType) {
            value = getResultSet().getString(index);
        }
        else if(Types.DATE == columnType){
            value = getResultSet().getDate(index);
        }
        else if(Types.TIMESTAMP == columnType){
            value = getResultSet().getTimestamp(index);
        }
        else if(Types.TIME == columnType){
            value = getResultSet().getTime(index);
        }
        else if(Types.BLOB == columnType){
            value = getResultSet().getBlob(index);
        }
        else if(Types.FLOAT == columnType){
            value = getResultSet().getFloat(index);
        }
        else if(Types.DOUBLE == columnType){
            value = getResultSet().getDouble(index);
        }
        else if(Types.ARRAY == columnType){
            value = getResultSet().getArray(index);
        }
        else if(Types.DECIMAL == columnType){
            value = getResultSet().getBigDecimal(index);
        }
        else {
            value = getResultSet().getObject(index);
        }

        return value;


    }


    private List<String[]> processResultSetData(Map<String, Object>[] subRowMaps) throws Exception{

        for(int i=0;i<subRowMaps.length;i++){
            subRowMaps[i] = new HashMap<String, Object>();
        }

        List<String[]> rowsMappedAsKeys = new ArrayList<String[]>();
        while (getResultSet().next()){
            String[] rowAsTableKeys = new String[subRowMaps.length];
            for(int i=0;i<subRowMaps.length;i++){
                String tableName =  metadata.getTable(i);
                String tableKey = this.createTableKeyForCurrentRow(tableName);
                rowAsTableKeys[i] = tableKey;
                Map<String,Object> subRowMap  = subRowMaps[i];
                Object subRow = subRowMap.get(tableKey);
                if(subRow == null){
                    Class childClass = ConfigManager.getInstance().getClassByTableName(tableName);
                    if(childClass != null){
                        Object childObject = this.mapRow(childClass);
                        subRowMap.put(tableKey, childObject);
                    }
                }
            }
            rowsMappedAsKeys.add(rowAsTableKeys);
        }

        return rowsMappedAsKeys;
    }
    public <T> List<T> mapResultsetToObjects(Class<T> clazz) throws Exception{

        Map<String, Object>[] subRowMaps = new Map[metadata.getTableCount()];

        List<String[]> rowsMappedAsKeys = processResultSetData(subRowMaps);

        Map<String, Class> relatedClassByName = new HashMap<String, Class>();
        Map<Class, RelationAnnotationInfo> parentClassMap = new HashMap<Class, RelationAnnotationInfo>();
        ReflectUtility.populateDescentAnnotations(clazz, relatedClassByName, parentClassMap);
        List<T> data = new ArrayList<T>();
        for(int rowIndex=0;rowIndex<rowsMappedAsKeys.size();rowIndex++){
            String[] rowAsKeys = rowsMappedAsKeys.get(rowIndex);
            for(int tableIndex=0;tableIndex<metadata.getTableCount();tableIndex++){
                String tableName = metadata.getTable(tableIndex);
                Class tableClass = relatedClassByName.get(tableName);
                String key = rowAsKeys[tableIndex];
                if(tableClass != null){
                    Object tableObject = subRowMaps[tableIndex].get(key);
                    if(tableObject == null) continue;
                    if( tableName.equalsIgnoreCase(clazz.getSimpleName()) == false){
                        RelationAnnotationInfo relationAnnotationInfo = parentClassMap.get(tableClass);
                        if(relationAnnotationInfo != null){
                            Class parentClass = relationAnnotationInfo.getParent();
                            String parentTable = parentClass.getSimpleName();
                            String parentKey = rowAsKeys[metadata.getTableIndex(parentTable)];
                            if(relationAnnotationInfo.isAlreadyMapped(parentKey, key)){
                                continue;
                            }
                            Object parentObject = subRowMaps[metadata.getTableIndex(parentTable)].get(parentKey);
                            ReflectUtility.mapRelation(relationAnnotationInfo.getRelationAnnotation(), parentObject, tableObject);
                            relationAnnotationInfo.addMap(parentKey, key);
                        }
                    }
                }
            }
        }
        for(Object o : subRowMaps[metadata.getTableIndex(clazz.getSimpleName())].values()){
            data.add((T)o);
        }
        return data;
    }

    public void close(){
        if(resultSet != null){
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
