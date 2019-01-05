package safayat.orm.query;

import safayat.orm.interfaces.LimitInterface;

/**
 * Created by safayat on 10/16/18.
 */
public class Limit extends QueryDataConverter implements LimitInterface{

    public Limit(QueryInfo mysqlQuery) {
        super(mysqlQuery);
    }


}


