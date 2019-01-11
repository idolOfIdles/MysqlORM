package safayat.orm.jdbcUtility;

import safayat.orm.config.ConfigManager;
import safayat.orm.reflect.ReflectUtility;
import safayat.orm.reflect.RelationAnnotationInfo;
import safayat.orm.reflect.RelationInfo;
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


    private List<MultipleTableRow> processResultSetData() throws Exception{

        SingleTableRowMap singleTableRowMap = new SingleTableRowMap();
        List<MultipleTableRow> multipleTableRows = new ArrayList<>();

        while (getResultSet().next()){

            MultipleTableRow multipleTableRow = new MultipleTableRow();

            for(int i=0;i<metadata.getTableCount();i++){

                String tableName =  metadata.getTable(i);
                String tableKey = createTableKeyForCurrentRow(tableName);
                Class tableClass = ConfigManager.getInstance().getClassByTableName(tableName);
                SingleTableRow singleTableRow = singleTableRowMap.getSingleTableRow(tableName, tableKey);

                if(singleTableRow == null){
                    Object childObject = mapRow(tableClass);
                    singleTableRow = new SingleTableRow(childObject, tableClass, tableName, tableKey);
                    singleTableRowMap.addNewRow(tableName, tableKey, singleTableRow);
                    multipleTableRow.addSingleRow(tableName, singleTableRow);
                }
            }

            multipleTableRows.add(multipleTableRow);
        }

        return multipleTableRows;
    }
    public <T> List<T> mapResultsetToObjects(Class<T> clazz) throws Exception{

        String rootTableName = ConfigManager.getInstance().getTableName(clazz);
        Map<String, Class> relatedClassByName = new HashMap<String, Class>();
        Map<Class, RelationAnnotationInfo> parentClassMap = new HashMap<Class, RelationAnnotationInfo>();
        ReflectUtility.populateRelationDataStructures(clazz, relatedClassByName, parentClassMap);

        List<T> data = new ArrayList<T>();
        List<MultipleTableRow> compressedRows = processResultSetData();

        for(MultipleTableRow multipleTableRow : compressedRows){

           for(String table : metadata.getTables()){

               SingleTableRow singleTableRow = multipleTableRow.getSingleTableRowByTableName(table);

               if(singleTableRow.tableNameMaches(rootTableName) || singleTableRow.getType() == null) continue;

               RelationAnnotationInfo relationAnnotationInfo = parentClassMap.get(singleTableRow.getType());

               if(relationAnnotationInfo == null) continue;

               String parentTable = ConfigManager.getInstance().getTableName(relationAnnotationInfo.getParent());
               SingleTableRow parentSingleTableRow = multipleTableRow.getSingleTableRowByTableName(parentTable);

               if(relationAnnotationInfo.isAlreadyMapped(parentSingleTableRow.getRowAsString()
                       , singleTableRow.getRowAsString())) continue;

               ReflectUtility.mapRelation(new RelationInfo(relationAnnotationInfo.getRelationAnnotation())
                       , parentSingleTableRow.getRow()
                       , singleTableRow.getRow());

               relationAnnotationInfo.addMap(parentSingleTableRow.getRowAsString(), singleTableRow.getRowAsString());
           }

        }

        for(MultipleTableRow multipleTableRow : compressedRows){
            data.add((T)multipleTableRow.getSingleTableRowByTableName(rootTableName).getRow());
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
