package queryBuilder;

import java.util.List;

/**
 * Created by safayat on 10/16/18.
 */


public class MysqlTable implements MysqlQueryInterface{
    private MysqlJoin mysqlJoin;
    private boolean tableSelectedOnce = false;
    private MysqlQueryInterface mysqlQuery;
    private MysqlCondition mysqlCondition;
    private MysqlOrder mysqlOrder;
    private MysqlGroupBy mysqlGroupBy;

    public MysqlTable(MysqlQueryInterface mysqlQuery){
        this.mysqlQuery = mysqlQuery;
        mysqlJoin = new MysqlJoin(this);
//        mysqlCondition = new MysqlCondition(this);
    }
    public MysqlTable table(String tableName, String code){
        if(tableSelectedOnce) mysqlQuery.getQuery().append(",");
        tableSelectedOnce = true;
        mysqlQuery.getQuery().append(tableName).append(" ").append(code);
        return this;
    }

    public MysqlTable table(String tableName){
        String[] splitted = tableName.trim().split(" ");
        String code = "";
        if(splitted.length > 1){
            tableName = splitted[0];
            code = splitted[1];
        }
        if(tableSelectedOnce) mysqlQuery.getQuery().append(",");
        tableSelectedOnce = true;
        mysqlQuery.getQuery().append(tableName).append(" ").append(code);
        return this;

    }

    public MysqlJoin join(String tableName, String code){
        mysqlQuery.getQuery().append(" join ").append(tableName).append(" ").append(code);
        return mysqlJoin;
    }
    public MysqlJoin leftJoin(String tableName, String code){
        mysqlQuery.getQuery().append(" left join ").append(tableName).append(" ").append(code);
        return mysqlJoin;
    }

    public MysqlJoin rightJoin(String tableName){
        String[] splitted = tableName.split(" ");
        tableName = splitted[0];
        String code = splitted.length > 1 ? splitted[1] : "";
        mysqlQuery.getQuery().append(" right join ").append(tableName).append(" ").append(code);
        return mysqlJoin;
    }

    public MysqlJoin join(String tableName){
        String[] splitted = tableName.split(" ");
        tableName = splitted[0];
        String code = splitted.length > 1 ? splitted[1] : "";
        mysqlQuery.getQuery().append(" join ").append(tableName).append(" ").append(code);
        return mysqlJoin;
    }
    public MysqlJoin leftJoin(String tableName){
        String[] splitted = tableName.split(" ");
        tableName = splitted[0];
        String code = splitted.length > 1 ? splitted[1] : "";
        mysqlQuery.getQuery().append(" left join ").append(tableName).append(" ").append(code);
        return mysqlJoin;
    }

    public MysqlJoin rightJoin(String tableName, String code){
        mysqlQuery.getQuery().append(" right join ").append(tableName).append(" ").append(code);
        return mysqlJoin;
    }

    public MysqlCondition filter(String expression, String value){
        if(mysqlCondition == null) mysqlCondition = new MysqlCondition(this);
        return mysqlCondition.filter(expression, value);
    }

    public MysqlCondition orFilter(String expression, String value){
        if(mysqlCondition == null) mysqlCondition = new MysqlCondition(this);
        return mysqlCondition.orFilter(expression, value);

    }
    public MysqlCondition orIn(String field, List<Object> values){
        if(mysqlCondition == null) mysqlCondition = new MysqlCondition(this);
        return mysqlCondition.orIn(field, values);

    }
    public MysqlCondition in(String field, List<Object> values){
        if(mysqlCondition == null) mysqlCondition = new MysqlCondition(this);
        return mysqlCondition.in(field, values);

    }

    public  MysqlQueryInterface order(String orderKey, String sort){
        if(mysqlOrder == null) mysqlOrder = new MysqlOrder(this);
        mysqlOrder.order(orderKey,sort);
        return this;
    }

    public String limit(int limit){
        mysqlQuery.getQuery().append(" limit ").append(limit);
        return mysqlQuery.getQuery().toString();
    }

    public MysqlGroupBy groupBy(String groupByKey){
        if(mysqlGroupBy == null) mysqlGroupBy = new MysqlGroupBy(this);
        return mysqlGroupBy.groupBy(groupByKey);
    }


    public StringBuilder getQuery() {
        return mysqlQuery.getQuery();
    }
}


