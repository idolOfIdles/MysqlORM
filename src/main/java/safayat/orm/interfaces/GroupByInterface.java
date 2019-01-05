package safayat.orm.interfaces;

import safayat.orm.query.MysqlGroupBy;
import safayat.orm.query.MysqlOrder;
import safayat.orm.query.QueryDataConverter;
import safayat.orm.query.QueryInfo;

/**
 * Created by safayat on 1/4/19.
 */
public interface GroupByInterface {



    default MysqlGroupBy groupBy(String groupByKey){
        QueryDataConverter queryDataConverter = ((QueryDataConverter)this);
        QueryInfo query = queryDataConverter.getQuery();
        query.append(" group by ").append(groupByKey);
        return new MysqlGroupBy(query);
    }



}
