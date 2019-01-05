package safayat.orm.query;

import safayat.orm.config.ConfigManager;
import safayat.orm.interfaces.ConditionInterface;
import safayat.orm.interfaces.GroupByInterface;
import safayat.orm.interfaces.LimitInterface;
import safayat.orm.interfaces.OrderInterface;

/**
 * Created by safayat on 10/16/18.
 */


public class MysqlTable extends QueryDataConverter implements ConditionInterface, OrderInterface, GroupByInterface, LimitInterface{
    private MysqlJoin mysqlJoin;
    private boolean tableSelectedOnce = false;

    public static String JOIN = "join";
    public static String LEFT_JOIN = "left join";
    public static String RIGHT_JOIN = "right join";

    public MysqlTable(QueryInfo  query){
        super(query);
        mysqlJoin = new MysqlJoin(this);
//        mysqlCondition = new MysqlCondition(query);
    }

    public MysqlTable table(String tableName, String code){
        if(tableSelectedOnce) query.append(",");
        tableSelectedOnce = true;
        query.append(tableName).append(" ").append(code);
        return this;
    }

    public MysqlTable table(String tableName){
        if(tableSelectedOnce) query.append(",");
        tableSelectedOnce = true;
        createAndAppendJoinSqlString(tableName, "");
        return this;

    }

    private void createAndAppendJoinSqlString(String tableName, String joinName){
        String[] splitted = tableName.split(" ");
        tableName = splitted[0];
        String code = splitted.length > 1 ? splitted[1] : "";
        query
                .append(" ").append(joinName).append(" ")
                .append(tableName)
                .append(" ")
                .append(code);
    }

    private void createAndAppendJoinSqlString(String tableName, String code, String joinName){
        query
                .append(" ").append(joinName).append(" ")
                .append(tableName)
                .append(" ")
                .append(code);

    }


    public MysqlJoin join(String tableName, String code){
        createAndAppendJoinSqlString(tableName, code, MysqlTable.JOIN);
        return mysqlJoin;
    }
    public MysqlJoin leftJoin(String tableName, String code){
        createAndAppendJoinSqlString(tableName, code, MysqlTable.LEFT_JOIN);
        return mysqlJoin;
    }

    public MysqlJoin rightJoin(String tableName, String code){
        createAndAppendJoinSqlString(tableName, code, MysqlTable.RIGHT_JOIN);
        return mysqlJoin;
    }

    public MysqlJoin rightJoin(String tableName){
        createAndAppendJoinSqlString(tableName, MysqlTable.RIGHT_JOIN);
        return mysqlJoin;
    }

    public MysqlJoin join(String tableName){
        createAndAppendJoinSqlString(tableName, MysqlTable.JOIN);
        return mysqlJoin;
    }
    public MysqlJoin leftJoin(String tableName){
        createAndAppendJoinSqlString(tableName, MysqlTable.LEFT_JOIN);
        return mysqlJoin;
    }

    public MysqlJoin join(Class tableName, String code){
        createAndAppendJoinSqlString(ConfigManager.getInstance().getTableName(tableName), code, MysqlTable.JOIN);
        return mysqlJoin;
    }
    public MysqlJoin leftJoin(Class tableName, String code){
        createAndAppendJoinSqlString(ConfigManager.getInstance().getTableName(tableName), code, MysqlTable.LEFT_JOIN);
        return mysqlJoin;
    }

    public MysqlJoin rightJoin(Class tableName, String code){
        createAndAppendJoinSqlString(ConfigManager.getInstance().getTableName(tableName), code, MysqlTable.RIGHT_JOIN);
        return mysqlJoin;
    }

    public MysqlJoin rightJoin(Class tableName){
        createAndAppendJoinSqlString(ConfigManager.getInstance().getTableName(tableName), MysqlTable.RIGHT_JOIN);
        return mysqlJoin;
    }

    public MysqlJoin join(Class tableName){
        createAndAppendJoinSqlString(ConfigManager.getInstance().getTableName(tableName), MysqlTable.JOIN);
        return mysqlJoin;
    }
    public MysqlJoin leftJoin(Class tableName){
        createAndAppendJoinSqlString(ConfigManager.getInstance().getTableName(tableName), MysqlTable.LEFT_JOIN);
        return mysqlJoin;
    }

}


