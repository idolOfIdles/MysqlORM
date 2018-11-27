package safayat.orm.query;

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

    public MysqlTable(MysqlQuery mysqlQuery){
        super(mysqlQuery);
        mysqlJoin = new MysqlJoin(this);
//        mysqlCondition = new MysqlCondition(mysqlQuery);
    }
    public MysqlTable table(String tableName, String code){
        if(tableSelectedOnce) mysqlQuery.append(",");
        tableSelectedOnce = true;
        mysqlQuery.append(tableName).append(" ").append(code);
        return this;
    }

    public MysqlTable table(String tableName){
        String[] splitted = tableName.trim().split(" ");
        String code = "";
        if(splitted.length > 1){
            tableName = splitted[0];
            code = splitted[1];
        }
        if(tableSelectedOnce) mysqlQuery.append(",");
        tableSelectedOnce = true;
        mysqlQuery.append(tableName).append(" ").append(code);

        return this;

    }

    public MysqlJoin join(String tableName, String code){
        mysqlQuery.append(" join ").append(tableName).append(" ").append(code);

        return mysqlJoin;
    }
    public MysqlJoin leftJoin(String tableName, String code){
        mysqlQuery.append(" left join ").append(tableName).append(" ").append(code);

        return mysqlJoin;
    }

    public MysqlJoin rightJoin(String tableName){
        String[] splitted = tableName.split(" ");
        tableName = splitted[0];
        String code = splitted.length > 1 ? splitted[1] : "";

        mysqlQuery.append(" right join ").append(tableName).append(" ").append(code);
        return mysqlJoin;
    }

    public MysqlJoin join(String tableName){
        String[] splitted = tableName.split(" ");
        tableName = splitted[0];
        String code = splitted.length > 1 ? splitted[1] : "";
        mysqlQuery.append(" join ").append(tableName).append(" ").append(code);
        return mysqlJoin;
    }
    public MysqlJoin leftJoin(String tableName){
        String[] splitted = tableName.split(" ");
        tableName = splitted[0];
        String code = splitted.length > 1 ? splitted[1] : "";
        mysqlQuery.append(" left join ").append(tableName).append(" ").append(code);
        return mysqlJoin;
    }

    public MysqlJoin rightJoin(String tableName, String code){
        mysqlQuery.append(" right join ").append(tableName).append(" ").append(code);

        return mysqlJoin;
    }

    public MysqlCondition filter(String expression, Object value){
        if(mysqlCondition == null) mysqlCondition = new MysqlCondition(mysqlQuery);
        return mysqlCondition.filter(expression, value);
    }

    public MysqlCondition orFilter(String expression, Object value){
        if(mysqlCondition == null) mysqlCondition = new MysqlCondition(mysqlQuery);
        return mysqlCondition.orFilter(expression, value);

    }

    public MysqlCondition filter(String expression){
        if(mysqlCondition == null) mysqlCondition = new MysqlCondition(mysqlQuery);
        return mysqlCondition.filter(expression);
    }

    public MysqlCondition orFilter(String expression){
        if(mysqlCondition == null) mysqlCondition = new MysqlCondition(mysqlQuery);
        return mysqlCondition.orFilter(expression);

    }
    public MysqlCondition orIn(String field, List<Object> values){
        if(mysqlCondition == null) mysqlCondition = new MysqlCondition(mysqlQuery);
        return mysqlCondition.orIn(field, values);

    }
    public MysqlCondition in(String field, List<Object> values){
        if(mysqlCondition == null) mysqlCondition = new MysqlCondition(mysqlQuery);
        return mysqlCondition.in(field, values);

    }

    public  MysqlOrder order(String orderKey, String sort){
        if(mysqlOrder == null) mysqlOrder = new MysqlOrder(mysqlQuery);
        mysqlOrder.order(orderKey,sort);
        return mysqlOrder;
    }

    public String limit(int limit){
        return limit(limit, 0);
    }

    public String limit(int limit, int offset){
        mysqlQuery.append(" limit ").append(limit).append(" offset ").append(offset);
        return mysqlQuery.toString();
    }

    public MysqlGroupBy groupBy(String groupByKey){
        if(mysqlGroupBy == null) mysqlGroupBy = new MysqlGroupBy(mysqlQuery);
        return mysqlGroupBy.groupBy(groupByKey);
    }



}


