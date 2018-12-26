package safayat.orm.query;

import safayat.orm.query.util.Util;

import java.util.List;

/**
 * Created by safayat on 10/16/18.
 */


public class MysqlCondition extends QueryDataConverter{

    private MysqlOrder mysqlOrder;
    private MysqlGroupBy mysqlGroupBy;
    public MysqlCondition(StringBuilder mysqlQuery) {
        super(mysqlQuery);
        this.query = mysqlQuery;
        mysqlQuery.append(" WHERE 1=1");
    }


    public MysqlCondition filter(String expression, Object value){
        query.append(" AND ");
        query.append(expression).append(Util.toMysqlString(value));
        return this;
    }

    public MysqlCondition orFilter(String expression, Object value){
        query.append(" OR ");
        query.append(expression).append(Util.toMysqlString(value));
        return this;
    }

    public MysqlCondition filter(String expression){
        query.append(" AND ");
        query.append(expression);
        return this;
    }

    public MysqlCondition orFilter(String expression){
        query.append(" OR ");
        query.append(expression);
        return this;
    }

    private void basicInOperation(String field, List<Object> values){
        query.append(field).append(" in ").append("(");
        boolean firstElement = true;
        for(Object o : values){
            if(!firstElement) query.append(",");
            firstElement = false;
            query.append(Util.toMysqlString(o));
        }
        query.append(")");
    }
    public MysqlCondition orIn(String field, List<Object> values){
        query.append(" OR ");
        basicInOperation(field, values);
        return this;
    }
    public MysqlCondition in(String field, List<Object> values){
        query.append(" AND ");
        basicInOperation(field, values);
        return this;
    }


    public  MysqlOrder order(String orderKey, String sort){
        if(mysqlOrder == null) mysqlOrder = new MysqlOrder(query);
        return mysqlOrder.order(orderKey,sort);
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


