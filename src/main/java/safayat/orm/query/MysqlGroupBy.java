package safayat.orm.query;

import safayat.orm.interfaces.GroupByInterface;
import safayat.orm.interfaces.LimitInterface;
import safayat.orm.interfaces.OrderInterface;

/**
 * Created by safayat on 10/16/18.
 */

public class MysqlGroupBy extends QueryDataConverter implements GroupByInterface, OrderInterface, LimitInterface{

    public MysqlGroupBy(QueryInfo mysqlQuery) {
        super(mysqlQuery);
    }

}


