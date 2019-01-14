package safayat.orm.jdbcUtility;

/**
 * Created by safayat on 10/26/18.
 */
public class SingleTableRow {

    private Object row;
    private Class type;
    private String table;
    private String rowAsString;

    public SingleTableRow(Object row, Class type, String table, String rowAsString) {
        this.row = row;
        this.type = type;
        this.table = table;
        this.rowAsString = rowAsString;
    }

    public Object getRow() {
        return row;
    }

    public void setRow(Object row) {
        this.row = row;
    }

    public Class getType() {
        return type;
    }

    public void setType(Class type) {
        this.type = type;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getRowAsString() {
        return rowAsString;
    }

    public void setRowAsString(String rowAsString) {
        this.rowAsString = rowAsString;
    }
    public boolean tableNameMaches(String tableName) {
        return table.equalsIgnoreCase(tableName);
    }
}
