package safayat.orm.jdbcUtility;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by safayat on 10/26/18.
 */
public class MultipleTableRow {

    private Map<String,SingleTableRow> singleTableRowByTableName;

    public MultipleTableRow() {
         singleTableRowByTableName = new HashMap<>();
    }

    public SingleTableRow getSingleTableRowByTableName(String tableName){
        return singleTableRowByTableName.get(tableName);
    }

    public void addSingleRow(String tableName, SingleTableRow singleTableRow){
        singleTableRowByTableName.put(tableName, singleTableRow);
    }
}
