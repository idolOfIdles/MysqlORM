package safayat.queryBuilder;

/**
 * Created by safayat on 10/16/18.
 */

public class MysqlOrder implements MysqlQueryInterface{

    private MysqlQueryInterface mysqlQuery;
    boolean firstOrder = true;
    public MysqlOrder(MysqlQueryInterface mysqlQuery) {
        this.mysqlQuery = mysqlQuery;
    }

    public  MysqlOrder order(String orderKey, String sort){
        if(!firstOrder) mysqlQuery.getQuery().append(", ");
        else mysqlQuery.getQuery().append(" order by ");
        mysqlQuery.getQuery().append(orderKey);
        firstOrder=false;
        if(sort!= null && !sort.isEmpty()) mysqlQuery.getQuery().append(" ").append(sort);
        return this;
    }

    public String limit(int limit){
        mysqlQuery.getQuery().append(" limit ").append(limit);
        return mysqlQuery.getQuery().toString();
    }


    @Override
    public MysqlQuery getQuery() {
        return mysqlQuery.getQuery();
    }
}


