package safayat.orm.query;

import safayat.orm.dao.GeneralRepositoryManager;

import java.util.List;

/**
 * Created by safayat on 10/16/18.
 */

public abstract class QueryDataConverter {

    StringBuilder query;

    protected QueryDataConverter(StringBuilder mysqlQuery) {
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

}


