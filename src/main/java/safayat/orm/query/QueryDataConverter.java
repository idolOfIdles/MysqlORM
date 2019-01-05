package safayat.orm.query;

import safayat.orm.dao.GeneralRepositoryManager;
import safayat.orm.query.util.Util;

import java.util.List;

/**
 * Created by safayat on 10/16/18.
 */

public abstract class QueryDataConverter {

    QueryInfo query;

    public QueryInfo getQuery(){
        return query;
    }

    protected QueryDataConverter(QueryInfo mysqlQuery) {
        this.query = mysqlQuery;
    }

    public <I> List<I> toList(Class<I> clazz){
        System.out.println(query.toString());
        return GeneralRepositoryManager
                .getInstance()
                .getGeneralRepository()
                .getAll(clazz, query.toString());
    }

    public <I> I first(Class<I> clazz){
         List<I> data = GeneralRepositoryManager
                .getInstance()
                .getGeneralRepository()
                .getAll(clazz, query.toString());

        if(data.size() == 0) return null;
        return data.get(0);
    }

    void parentOrder(boolean firstOrder, String orderKey, String sort){
        if(!firstOrder) query.append(", ");
        else query.append(" order by ");
        query.append(orderKey);
        if(sort!= null && !sort.isEmpty()) query.append(" ").append(sort);
    }






}


