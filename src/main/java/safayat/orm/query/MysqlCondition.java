package safayat.orm.query;

import safayat.orm.query.util.Util;

import java.util.List;

/**
 * Created by safayat on 10/16/18.
 */


public class MysqlCondition extends QueryDataConverter{

    private MysqlOrder mysqlOrder;
    private MysqlGroupBy mysqlGroupBy;
    public MysqlCondition(MysqlQuery mysqlQuery) {
        super(mysqlQuery);
        this.mysqlQuery = mysqlQuery;
        mysqlQuery.append(" WHERE 1=1");
    }


    public MysqlCondition filter(String expression, Object value){
        mysqlQuery.append(" AND ");
        mysqlQuery.append(expression).append(Util.toMysqlString(value));
        return this;
    }

    public MysqlCondition orFilter(String expression, Object value){
        mysqlQuery.append(" OR ");
        mysqlQuery.append(expression).append(Util.toMysqlString(value));
        return this;
    }

    public MysqlCondition filter(String expression){
        mysqlQuery.append(" AND ");
        mysqlQuery.append(expression);
        return this;
    }

    public MysqlCondition orFilter(String expression){
        mysqlQuery.append(" OR ");
        mysqlQuery.append(expression);
        return this;
    }

    private void basicInOperation(String field, List<Object> values){
        mysqlQuery.append(field).append(" in ").append("(");
        boolean firstElement = true;
        for(Object o : values){
            if(!firstElement) mysqlQuery.append(",");
            firstElement = false;
            mysqlQuery.append(Util.toMysqlString(o));
        }
        mysqlQuery.append(")");
    }
    public MysqlCondition orIn(String field, List<Object> values){
        mysqlQuery.append(" OR ");
        basicInOperation(field, values);
        return this;
    }
    public MysqlCondition in(String field, List<Object> values){
        mysqlQuery.append(" AND ");
        basicInOperation(field, values);
        return this;
    }


    public  MysqlOrder order(String orderKey, String sort){
        if(mysqlOrder == null) mysqlOrder = new MysqlOrder(mysqlQuery);
        return mysqlOrder.order(orderKey,sort);
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


