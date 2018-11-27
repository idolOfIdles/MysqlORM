package safayat.orm.query;

/**
 * Created by safayat on 10/16/18.
 */

public class MysqlGroupBy {

    private MysqlQuery mysqlQuery;
    private MysqlOrder mysqlOrder;

    public MysqlGroupBy(MysqlQuery mysqlQuery) {
        this.mysqlQuery = mysqlQuery;
    }

    public MysqlGroupBy groupBy(String groupByKey){
        mysqlQuery.append(" group by ").append(groupByKey);
        return this;
    }

    public String limit(int limit){
        return limit(limit, 0);
    }

    public String limit(int limit, int offset){
        mysqlQuery.append(" limit ").append(limit).append(" offset ").append(offset);
        return mysqlQuery.toString();
    }


    public  MysqlOrder order(String orderKey, String sort){
        if(mysqlOrder == null) mysqlOrder = new MysqlOrder(mysqlQuery);
        return mysqlOrder.order(orderKey, sort);
    }



}


