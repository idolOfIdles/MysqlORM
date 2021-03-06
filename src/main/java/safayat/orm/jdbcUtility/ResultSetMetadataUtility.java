package safayat.orm.jdbcUtility;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by safayat on 10/26/18.
 */
public class ResultSetMetadataUtility {

    private ResultSetMetaData resultSetMetaData;
    private Map<String, List<Integer>> columnsByTable;
    private Map<String, Integer> tableIndexByName;
    private String[] tables;

    public ResultSetMetadataUtility(ResultSetMetaData resultSetMetaData) {

        this.resultSetMetaData = resultSetMetaData;

        tables = getColumnIndexesByTable().keySet().toArray(new String[0]);

        tableIndexByName = new HashMap<String, Integer>();
        for(int i=0;i<tables.length;i++){
            tableIndexByName.put(tables[i], i);
        }

    }

    public ResultSetMetaData get() {
       return resultSetMetaData;
    }

    public Map<String,List<Integer>> getColumnIndexesByTable() {

        if(columnsByTable == null) {

            columnsByTable = new HashMap<String, List<Integer>>();
            try {

                for (int column = 1; column <= resultSetMetaData.getColumnCount(); column++) {
                    List<Integer> columns = columnsByTable.getOrDefault(resultSetMetaData.getTableName(column).toLowerCase(), new ArrayList<Integer>());
                    if (columns.size() == 0) {
                        columnsByTable.put(resultSetMetaData.getTableName(column).toLowerCase(), columns);
                    }
                    columns.add(column);
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return columnsByTable;


    }
    public List<Integer> getColumnIndexes(String table) {
        return getColumnIndexesByTable().get(table.toLowerCase());
    }

    public int getColumnIndex(String tableName, String columnName) throws Exception{
        List<Integer> columnIndexes = getColumnIndexesByTable().get(tableName.toLowerCase());
        for(int index : columnIndexes){
            String clName = resultSetMetaData.getColumnName(index);
            if(columnName.equals(clName)) return index;
        }
        return -1;
    }

    public int getCount() throws Exception{
        return resultSetMetaData.getColumnCount();
    }

    public String[] getTables(){
        return tables;
    }

    public Integer getTableIndex(String name){
        return tableIndexByName.get(name.toLowerCase());
    }

    public Integer getTableCount(){
        return tables.length;
    }

    public String getTable(int index){
        return tables[index];
    }





}
