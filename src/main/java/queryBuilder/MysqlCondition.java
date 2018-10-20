package queryBuilder;

import java.util.List;

/**
 * Created by safayat on 10/16/18.
 */


class MysqlCondition implements MysqlQueryInterface{

    private MysqlQueryInterface mysqlQuery;
    private MysqlOrder mysqlOrder;
    private MysqlGroupBy mysqlGroupBy;
    public MysqlCondition(MysqlQueryInterface mysqlQuery) {
        this.mysqlQuery = mysqlQuery;
        mysqlQuery.getQuery().append(" WHERE 1=1");
    }


    public MysqlCondition filter(String expression, Object value){
        mysqlQuery.getQuery().append(" AND ");
        mysqlQuery.getQuery().append(expression).append(value);
        return this;
    }

    public MysqlCondition orFilter(String expression, Object value){
        mysqlQuery.getQuery().append(" OR ");
        mysqlQuery.getQuery().append(expression).append(value);
        return this;
    }

    private void basicInOperation(String field, List<Object> values){
        mysqlQuery.getQuery().append(field).append(" in ").append("(");
        boolean firstElement = true;
        for(Object o : values){
            if(!firstElement) mysqlQuery.getQuery().append(",");
            firstElement = false;
            mysqlQuery.getQuery().append(o.hashCode());
        }
        mysqlQuery.getQuery().append(")");
    }
    public MysqlCondition orIn(String field, List<Object> values){
        mysqlQuery.getQuery().append(" OR ");
        basicInOperation(field, values);
        return this;
    }
    public MysqlCondition in(String field, List<Object> values){
        mysqlQuery.getQuery().append(" AND ");
        basicInOperation(field, values);
        return this;
    }


    public  MysqlOrder order(String orderKey, String sort){
        if(mysqlOrder == null) mysqlOrder = new MysqlOrder(this);
        return mysqlOrder.order(orderKey,sort);
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


