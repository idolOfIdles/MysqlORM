package safayat.orm.query;

import safayat.orm.dao.GeneralRepositoryManager;

import java.util.List;

/**
 * Created by safayat on 10/16/18.
 */




public class MysqlQuery{

    private StringBuilder query;
    private StringBuilder queryFields;

    public MysqlQuery(String fields) {
        this.query = new StringBuilder();
        queryFields = new StringBuilder();
        queryFields.append(fields);
    }

    public static MysqlQuery fields(String fields){
        return new MysqlQuery(fields);
    }

    public static <I> I load(Class<I> clazz, Object primaryOrUniqueKey){
        return GeneralRepositoryManager.getInstance().getGeneralRepository().get(clazz, primaryOrUniqueKey);
    }

    public static <T> List<T> getAll(Class<T> clazz, int limit){
        return GeneralRepositoryManager.getInstance().getGeneralRepository().getAll(clazz, limit);
    }

    public static <T> List<T> getAll(Class<T> clazz, int limit, int offset){
        return GeneralRepositoryManager.getInstance().getGeneralRepository().getAll(clazz, limit, offset);
    }

    public MysqlQuery sum(String field){
        appendAggregateFunction(field, "sum");
        return this;
    }

    public MysqlQuery avg(String field){
        appendAggregateFunction(field, "avg");
        return this;
    }

    public MysqlQuery max(String field){
        appendAggregateFunction(field, "max");
        return this;
    }

    public MysqlQuery min(String field){
        appendAggregateFunction(field, "min");
        return this;
    }

    private void appendAggregateFunction(String field, String op){
        if(queryFields.length()>0) queryFields.append(",");
        queryFields.append(op).append("(").append(field).append(")");
    }




    public String toString() {
        return query.toString();
    }

    public MysqlQuery append(Object str) {
        query.append(str);
        return this;
    }

    public MysqlTable table(String tableName, String alias){
        query.append("select " + queryFields.toString() + " from ");
        return new MysqlTable(this).table(tableName, alias);
    }

    public MysqlTable table(String tableName){
        query.append("select " + queryFields.toString() + " from ");
        String[] splitted = tableName.trim().split(" ");
        String code = "";
        if(splitted.length > 1){
            code = splitted[1];
            return new MysqlTable(this).table(splitted[0], code);
        }
        return new MysqlTable(this).table(tableName,"");
    }


}


