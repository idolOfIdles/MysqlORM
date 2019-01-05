package safayat.orm.interfaces;

import safayat.orm.query.MysqlCondition;
import safayat.orm.query.MysqlOrder;
import safayat.orm.query.QueryDataConverter;
import safayat.orm.query.QueryInfo;
import safayat.orm.query.util.Util;

import java.util.List;

/**
 * Created by safayat on 1/4/19.
 */
public interface OrderInterface {


    default MysqlOrder order(String orderKey, String sort){
        QueryDataConverter queryDataConverter = ((QueryDataConverter)this);
        QueryInfo query = queryDataConverter.getQuery();
        if(query.isOrderBegan()) query.append(", ");
        else query.append(" order by ");
        query.append(orderKey);
        if(sort!= null && !sort.isEmpty()) query.append(" ").append(sort);
        query.setOrderBegan(true);
        return new MysqlOrder(query);
    }


}
