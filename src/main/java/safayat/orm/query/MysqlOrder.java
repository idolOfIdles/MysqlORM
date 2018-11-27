package safayat.orm.query;

/**
 * Created by safayat on 10/16/18.
 */

public class MysqlOrder extends QueryDataConverter{

    boolean firstOrder = true;
    public MysqlOrder(MysqlQuery mysqlQuery) {
        super(mysqlQuery);
        this.mysqlQuery = mysqlQuery;
    }

    public  MysqlOrder order(String orderKey, String sort){
        if(!firstOrder) mysqlQuery.append(", ");
        else mysqlQuery.append(" order by ");
        mysqlQuery.append(orderKey);
        firstOrder=false;
        if(sort!= null && !sort.isEmpty()) mysqlQuery.append(" ").append(sort);
        return this;
    }

    public String limit(int limit){
        return limit(limit, 0);
    }

    public String limit(int limit, int offset){
        mysqlQuery.append(" limit ").append(limit).append(" offset ").append(offset);
        return mysqlQuery.toString();
    }




}


