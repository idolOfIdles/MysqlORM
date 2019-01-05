package safayat.orm.query;

import safayat.orm.interfaces.LimitInterface;
import safayat.orm.interfaces.OrderInterface;

/**
 * Created by safayat on 10/16/18.
 */

public class MysqlOrder extends QueryDataConverter implements OrderInterface, LimitInterface{

    public MysqlOrder(QueryInfo mysqlQuery) {
        super(mysqlQuery);
    }

}


