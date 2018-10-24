package queryBuilder;

/**
 * Created by safayat on 10/16/18.
 */
public class MysqlJoin {

    private MysqlTable mysqlTable;
    public MysqlJoin(MysqlTable mysqlTable) {
        this.mysqlTable = mysqlTable;
    }
    public MysqlTable on(String col1,String col2){

        return mysqlTable;
    }
}


