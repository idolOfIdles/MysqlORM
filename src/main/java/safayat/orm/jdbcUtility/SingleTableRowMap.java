package safayat.orm.jdbcUtility;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by safayat on 10/26/18.
 */
public class SingleTableRowMap {

    private Map<String,Map<String, SingleTableRow>> singleTableRowByTableName;

    public SingleTableRowMap() {
        singleTableRowByTableName = new HashMap<>();
    }

    public Map<String, SingleTableRow> getSingleTableRowMapByTableName(String tableName){
        return singleTableRowByTableName.get(tableName);
    }

    public SingleTableRow getSingleTableRow(String tableName, String singleTableRowAsString){
        Map<String, SingleTableRow> singleTableRowMap = getSingleTableRowMapByTableName(tableName);
        if(singleTableRowMap != null) {
            return singleTableRowMap.get(singleTableRowAsString);
        }
        return null;
    }

    public void addNewRow(String tableName, String singleTableRowAsString, SingleTableRow singleTableRow){
        Map<String, SingleTableRow> singleTableRowMap = getSingleTableRowMapByTableName(tableName);
        if(singleTableRowMap == null){
            singleTableRowMap = new HashMap<>();
            singleTableRowByTableName.put(tableName, singleTableRowMap);
        }

        singleTableRowMap.put(singleTableRowAsString, singleTableRow);
    }
}
