package safayat.orm.interfaces;

import safayat.orm.query.MysqlCondition;
import safayat.orm.query.QueryDataConverter;
import safayat.orm.query.QueryInfo;
import safayat.orm.reflect.Util;

import java.util.List;

/**
 * Created by safayat on 1/4/19.
 */
public interface ConditionInterface {

    default MysqlCondition filter(String expression, Object value){
        QueryDataConverter queryDataConverter = ((QueryDataConverter)this);
        return filter(queryDataConverter, expression, value, true);
    }

    default MysqlCondition filter(String expression, Object value, boolean accept){
        QueryDataConverter queryDataConverter = ((QueryDataConverter)this);
        return filter(queryDataConverter, expression, value, accept);
    }

    static MysqlCondition filter(QueryDataConverter queryDataConverter, String expression, Object value, boolean accept){
        QueryInfo query = queryDataConverter.getQuery();
        MysqlCondition mysqlCondition = new MysqlCondition(query);
        if(!accept) return mysqlCondition;
        query.append(" AND ");
        query.append(expression);
        if(value!=null){
            query.append(" ").append(Util.toMysqlString(value));
        }
        return mysqlCondition;
    }

    default MysqlCondition orFilter(String expression, Object value){
        QueryDataConverter queryDataConverter = ((QueryDataConverter)this);
        return orFilter(queryDataConverter, expression, value, true);
    }
    default MysqlCondition orFilter(String expression, Object value, boolean accept){
        QueryDataConverter queryDataConverter = ((QueryDataConverter)this);
        return orFilter(queryDataConverter, expression, value, accept);
    }
    static MysqlCondition orFilter(QueryDataConverter queryDataConverter, String expression, Object value, boolean accept){
        QueryInfo query = queryDataConverter.getQuery();
        MysqlCondition mysqlCondition = new MysqlCondition(query);
        if(!accept) return mysqlCondition;
        query.append(" OR ");
        query.append(expression);
        if(value == null){
            query.append(Util.toMysqlString(value));
        }
        return mysqlCondition;
    }

    default QueryDataConverter in(String field, List<Object> values){
        QueryDataConverter queryDataConverter = ((QueryDataConverter)this);
        return in(queryDataConverter, field, values, true);
    }

    default QueryDataConverter in(String field, List<Object> values, boolean accept){
        QueryDataConverter queryDataConverter = ((QueryDataConverter)this);
        return in(queryDataConverter, field, values, accept);
    }

    static QueryDataConverter in(QueryDataConverter queryDataConverter, String field, List<Object> values, boolean accept){
        QueryInfo query = queryDataConverter.getQuery();
        MysqlCondition mysqlCondition = new MysqlCondition(query);
        if(!accept) return mysqlCondition;
        query.append(" AND ");
        basicInOperation(query, field, values);
        return mysqlCondition;
    }

    default QueryDataConverter orIn(String field, List<Object> values){
        QueryDataConverter queryDataConverter = ((QueryDataConverter)this);
        return orIn(queryDataConverter, field, values, true);
    }

    default QueryDataConverter orIn(String field, List<Object> values, boolean accept){
        QueryDataConverter queryDataConverter = ((QueryDataConverter)this);
        return orIn(queryDataConverter, field, values, accept);
    }

    static QueryDataConverter orIn(QueryDataConverter queryDataConverter, String field, List<Object> values, boolean accept){
        QueryInfo query = queryDataConverter.getQuery();
        MysqlCondition mysqlCondition = new MysqlCondition(query);
        if(!accept) return mysqlCondition;
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
