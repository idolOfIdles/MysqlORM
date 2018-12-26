package safayat.orm.query;

/**
 * Created by safayat on 10/16/18.
 */

public class MysqlGroupBy {

    private StringBuilder query;
    private MysqlOrder mysqlOrder;

    public MysqlGroupBy(StringBuilder mysqlQuery) {
        this.query = mysqlQuery;
    }

    public MysqlGroupBy groupBy(String groupByKey){
        query.append(" group by ").append(groupByKey);
        return this;
    }

    public QueryDataConverter limit(int limit){
        return new Limit(query).limit(limit);
    }

    public QueryDataConverter limit(int limit, int offset){
        return new Limit(query).limit(limit, offset);
    }

    public  MysqlOrder order(String orderKey, String sort){
        if(mysqlOrder == null) mysqlOrder = new MysqlOrder(query);
        return mysqlOrder.order(orderKey, sort);
    }



}


