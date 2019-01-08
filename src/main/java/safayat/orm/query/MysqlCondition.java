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

    public MysqlCondition(QueryInfo mysqlQuery, boolean startWhereCondition) {
        super(mysqlQuery);
        if(startWhereCondition && !mysqlQuery.isWhereBegan()){
            mysqlQuery.append(" WHERE 1=1");
            mysqlQuery.setWhereBegan(true);
        }
    }
}


