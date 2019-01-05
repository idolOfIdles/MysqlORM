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
        QueryInfo query = queryDataConverter.getQuery();
        query.append(" limit ").append(String.valueOf(limit)).append(" offset ").append(String.valueOf(offset));
        return queryDataConverter;
    }

}
