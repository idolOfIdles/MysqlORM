package safayat.orm.query;

import safayat.orm.dao.GeneralRepositoryManager;

import java.util.List;

/**
 * Created by safayat on 10/16/18.
 */

public abstract class QueryDataConverter {

    MysqlQuery mysqlQuery;

    protected QueryDataConverter(MysqlQuery mysqlQuery) {
        this.mysqlQuery = mysqlQuery;
    }

    public <I> List<I> toList(Class<I> clazz){
        return GeneralRepositoryManager
                .getInstance()
                .getGeneralRepository()
                .getAll(clazz, mysqlQuery.toString());
    }

    public <I> I first(Class<I> clazz){
         List<I> data = GeneralRepositoryManager
                .getInstance()
                .getGeneralRepository()
                .getAll(clazz, mysqlQuery.toString());

        if(data.size() == 0) return null;
        return data.get(0);
    }

}


