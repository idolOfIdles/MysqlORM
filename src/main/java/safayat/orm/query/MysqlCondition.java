package safayat.orm.query;

import safayat.orm.interfaces.ConditionInterface;
import safayat.orm.interfaces.GroupByInterface;
import safayat.orm.interfaces.LimitInterface;
import safayat.orm.interfaces.OrderInterface;
import safayat.orm.query.util.Util;

import java.util.List;

/**
 * Created by safayat on 10/16/18.
 */


public class MysqlCondition extends QueryDataConverter implements ConditionInterface, OrderInterface, GroupByInterface, LimitInterface{

    public MysqlCondition(QueryInfo mysqlQuery) {
        super(mysqlQuery);
        if(!mysqlQuery.isWhereBegan()){
            mysqlQuery.append(" WHERE 1=1");
            mysqlQuery.setWhereBegan(true);
        }
    }



/*
    public  MysqlOrder order(String orderKey, String sort){
        return getOrder().order(orderKey,sort);
    }

    public QueryDataConverter limit(int limit){
        return new Limit(query).limit(limit);
    }

    public QueryDataConverter limit(int limit, int offset){
        return new Limit(query).limit(limit, offset);
    }

    public MysqlGroupBy groupBy(String groupByKey){
        return getGroupBy().groupBy(groupByKey);
    }
*/

}


