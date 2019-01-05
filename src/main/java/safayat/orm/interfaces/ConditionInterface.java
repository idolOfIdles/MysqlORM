package safayat.orm.interfaces;

import safayat.orm.query.MysqlCondition;
import safayat.orm.query.QueryDataConverter;
import safayat.orm.query.QueryInfo;
import safayat.orm.query.util.Util;

import java.util.List;

/**
 * Created by safayat on 1/4/19.
 */
public interface ConditionInterface {

    default MysqlCondition filter(String expression, Object value){
        QueryDataConverter queryDataConverter = ((QueryDataConverter)this);
        QueryInfo query = queryDataConverter.getQuery();
        MysqlCondition mysqlCondition = new MysqlCondition(query);
        query.append(" AND ");
        query.append(expression);
        if(value!=null){
            query.append(Util.toMysqlString(value));
        }
        return mysqlCondition;
    }

    default MysqlCondition orFilter(String expression, Object value){
        QueryDataConverter queryDataConverter = ((QueryDataConverter)this);
        QueryInfo query = queryDataConverter.getQuery();
        query.append(" OR ");
        query.append(expression);
        if(value == null){
            query.append(Util.toMysqlString(value));
        }
        return new MysqlCondition(query);
    }

    default QueryDataConverter in(String field, List<Object> values){
        QueryDataConverter queryDataConverter = ((QueryDataConverter)this);
        QueryInfo query = queryDataConverter.getQuery();
        MysqlCondition mysqlCondition = new MysqlCondition(query);
        query.append(" AND ");
        basicInOperation(query, field, values);
        return mysqlCondition;
    }

    default QueryDataConverter orIn(String field, List<Object> values){
        QueryDataConverter queryDataConverter = ((QueryDataConverter)this);
        QueryInfo query = queryDataConverter.getQuery();
        MysqlCondition mysqlCondition = new MysqlCondition(query);
        query.append(" OR ");
        basicInOperation(query, field, values);
        return mysqlCondition;
    }

    static void basicInOperation(QueryInfo query, String field, List<Object> values){
        query.append(field).append(" in ").append("(");
        boolean firstElement = true;
        for(Object o : values){
            if(!firstElement) query.append(",");
            firstElement = false;
            query.append(Util.toMysqlString(o));
        }
        query.append(")");
    }


}
