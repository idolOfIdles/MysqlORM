package safayat.orm.interfaces;

import safayat.orm.query.MysqlOrder;
import safayat.orm.query.QueryDataConverter;
import safayat.orm.query.QueryInfo;

/**
 * Created by safayat on 1/4/19.
 */
public interface LimitInterface {


    default QueryDataConverter limit(int limit, int offset){
        QueryDataConverter queryDataConverter = ((QueryDataConverter)this);
        return limit(queryDataConverter, limit, offset, true);
    }

    default QueryDataConverter limit(int limit, int offset, boolean accept){
        QueryDataConverter queryDataConverter = ((QueryDataConverter)this);
        return limit(queryDataConverter, limit, offset, accept);
    }

    default QueryDataConverter limit(int limit, boolean accept){
        QueryDataConverter queryDataConverter = ((QueryDataConverter)this);
        return limit(queryDataConverter, limit, 0, accept);
    }

    default QueryDataConverter limit(int limit){
        QueryDataConverter queryDataConverter = ((QueryDataConverter)this);
        return limit(queryDataConverter, limit, 0, true);
    }

    static QueryDataConverter limit(QueryDataConverter queryDataConverter, int limit, int offset, boolean accept){
        QueryInfo query = queryDataConverter.getQuery();
        if(!accept) return queryDataConverter;
        query.append(" limit ").append(String.valueOf(limit)).append(" offset ").append(String.valueOf(offset));
        return queryDataConverter;
    }

}
