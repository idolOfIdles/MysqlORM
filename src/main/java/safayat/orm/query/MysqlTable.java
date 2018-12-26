package safayat.orm.query;

import safayat.orm.config.ConfigManager;

import java.util.List;

/**
 * Created by safayat on 10/16/18.
 */


public class MysqlTable extends QueryDataConverter{
    private MysqlJoin mysqlJoin;
    private boolean tableSelectedOnce = false;
    private MysqlCondition mysqlCondition;
    private MysqlOrder mysqlOrder;
    private safayat.orm.query.MysqlGroupBy mysqlGroupBy;

    public static String JOIN = "join";
    public static String LEFT_JOIN = "left join";
    public static String RIGHT_JOIN = "right join";

    public MysqlTable(StringBuilder  query){
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
        createANdAppendJoinSqlString(tableName,"");
        return this;

    }

    private void createANdAppendJoinSqlString(String tableName, String joinName){
        String[] splitted = tableName.split(" ");
        tableName = splitted[0];
        String code = splitted.length > 1 ? splitted[1] : "";
        query
                .append(" ").append(joinName).append(" ")
                .append(tableName)
                .append(" ")
                .append(code);
    }

    private void createANdAppendJoinSqlString(String tableName,String code,  String joinName){
        query
                .append(" ").append(joinName).append(" ")
                .append(tableName)
                .append(" ")
                .append(code);

    }

    public MysqlJoin join(String tableName, String code){
        createANdAppendJoinSqlString(tableName, code, MysqlTable.JOIN);
        return mysqlJoin;
    }
    public MysqlJoin leftJoin(String tableName, String code){
        createANdAppendJoinSqlString(tableName, code, MysqlTable.LEFT_JOIN);
        return mysqlJoin;
    }

    public MysqlJoin rightJoin(String tableName, String code){
        createANdAppendJoinSqlString(tableName, code, MysqlTable.RIGHT_JOIN);
        return mysqlJoin;
    }

    public MysqlJoin rightJoin(String tableName){
        createANdAppendJoinSqlString(tableName, MysqlTable.RIGHT_JOIN);
        return mysqlJoin;
    }

    public MysqlJoin join(String tableName){
        createANdAppendJoinSqlString(tableName, MysqlTable.JOIN);
        return mysqlJoin;
    }
    public MysqlJoin leftJoin(String tableName){
        createANdAppendJoinSqlString(tableName, MysqlTable.LEFT_JOIN);
        return mysqlJoin;
    }

    public MysqlJoin join(Class tableName, String code){
        createANdAppendJoinSqlString(ConfigManager.getInstance().getTableName(tableName), code, MysqlTable.JOIN);
        return mysqlJoin;
    }
    public MysqlJoin leftJoin(Class tableName, String code){
        createANdAppendJoinSqlString(ConfigManager.getInstance().getTableName(tableName), code, MysqlTable.LEFT_JOIN);
        return mysqlJoin;
    }

    public MysqlJoin rightJoin(Class tableName, String code){
        createANdAppendJoinSqlString(ConfigManager.getInstance().getTableName(tableName), code, MysqlTable.RIGHT_JOIN);
        return mysqlJoin;
    }

    public MysqlJoin rightJoin(Class tableName){
        createANdAppendJoinSqlString(ConfigManager.getInstance().getTableName(tableName), MysqlTable.RIGHT_JOIN);
        return mysqlJoin;
    }

    public MysqlJoin join(Class tableName){
        createANdAppendJoinSqlString(ConfigManager.getInstance().getTableName(tableName), MysqlTable.JOIN);
        return mysqlJoin;
    }
    public MysqlJoin leftJoin(Class tableName){
        createANdAppendJoinSqlString(ConfigManager.getInstance().getTableName(tableName), MysqlTable.LEFT_JOIN);
        return mysqlJoin;
    }


    public MysqlCondition filter(String expression, Object value){
        if(mysqlCondition == null) mysqlCondition = new MysqlCondition(query);
        return mysqlCondition.filter(expression, value);
    }

    public MysqlCondition orFilter(String expression, Object value){
        if(mysqlCondition == null) mysqlCondition = new MysqlCondition(query);
        return mysqlCondition.orFilter(expression, value);

    }

    public MysqlCondition filter(String expression){
        if(mysqlCondition == null) mysqlCondition = new MysqlCondition(query);
        return mysqlCondition.filter(expression);
    }

    public MysqlCondition orFilter(String expression){
        if(mysqlCondition == null) mysqlCondition = new MysqlCondition(query);
        return mysqlCondition.orFilter(expression);

    }
    public MysqlCondition orIn(String field, List<Object> values){
        if(mysqlCondition == null) mysqlCondition = new MysqlCondition(query);
        return mysqlCondition.orIn(field, values);

    }
    public MysqlCondition in(String field, List<Object> values){
        if(mysqlCondition == null) mysqlCondition = new MysqlCondition(query);
        return mysqlCondition.in(field, values);

    }

    public  MysqlOrder order(String orderKey, String sort){
        if(mysqlOrder == null) mysqlOrder = new MysqlOrder(query);
        mysqlOrder.order(orderKey,sort);
        return mysqlOrder;
    }

    public QueryDataConverter limit(int limit){
        return new Limit(query).limit(limit);
    }

    public QueryDataConverter limit(int limit, int offset){
        return new Limit(query).limit(limit, offset);
    }

    public MysqlGroupBy groupBy(String groupByKey){
        if(mysqlGroupBy == null) mysqlGroupBy = new MysqlGroupBy(query);
        return mysqlGroupBy.groupBy(groupByKey);
    }



}


