package safayat.orm.query;

/**
 * Created by safayat on 10/16/18.
 */

public class MysqlOrder extends QueryDataConverter{

    boolean firstOrder = true;
    public MysqlOrder(StringBuilder mysqlQuery) {
        super(mysqlQuery);
        this.query = mysqlQuery;
    }

    public  MysqlOrder order(String orderKey, String sort){
        if(!firstOrder) query.append(", ");
        else query.append(" order by ");
        query.append(orderKey);
        firstOrder=false;
        if(sort!= null && !sort.isEmpty()) query.append(" ").append(sort);
        return this;
    }

    public QueryDataConverter limit(int limit){
        return new Limit(query).limit(limit);
    }

    public QueryDataConverter limit(int limit, int offset){
        return new Limit(query).limit(limit, offset);
    }



}


