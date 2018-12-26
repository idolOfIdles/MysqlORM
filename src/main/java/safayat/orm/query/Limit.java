package safayat.orm.query;

/**
 * Created by safayat on 10/16/18.
 */
public class Limit extends QueryDataConverter{

    public Limit(StringBuilder mysqlQuery) {
        super(mysqlQuery);
    }

    public QueryDataConverter limit(int limit){
        return limit(limit, 0);
    }

    public QueryDataConverter limit(int limit, int offset){
        query.append(" limit ").append(limit).append(" offset ").append(offset);
        return this;
    }

}


