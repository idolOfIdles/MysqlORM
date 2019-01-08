package safayat.orm.query;

import safayat.orm.config.ConfigManager;
import safayat.orm.dao.GeneralRepositoryManager;
import safayat.orm.query.util.Util;
import safayat.orm.reflect.ReflectUtility;

import java.util.List;

/**
 * Created by safayat on 10/16/18.
 */




public class MysqlQuery{

    private QueryInfo query;
    public MysqlQuery(String fields) {
        query = new QueryInfo();
        query.appendFields(fields);
    }

    public static MysqlQuery fields(String fields){
        return new MysqlQuery(fields);
    }

    public static MysqlQuery All(){
        return new MysqlQuery("*");
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

    public String toString() {
        return query.toString();
    }

    public MysqlTable table(String tableName, String alias){
        query.append("select " + query.getQueryFields().toString() + " from ");
        return new MysqlTable(query).table(tableName, alias);
    }

    public MysqlTable table(String tableName){
        query.append("select " + query.getQueryFields().toString() + " from ");
        String[] splitted = tableName.trim().split(" ");
        String code = "";
        if(splitted.length > 1){
            code = splitted[1];
            return new MysqlTable(query).table(splitted[0], code);
        }
        return new MysqlTable(query).table(tableName,"");
    }

    public MysqlTable table(Class tableClass, String alias){
        return table(ConfigManager.getInstance().getTableName(tableClass), alias);
    }

    public MysqlTable oneToMany(Class tableClass) throws Exception{
        query.setTableBegan(true);
        query.append("select " + query.getQueryFields().toString() + " from ");
        return new MysqlTable(query.append(ReflectUtility.createOneToManyJoinSql(tableClass)));
    }

    public MysqlTable table(Class tableClass){
        return table(ConfigManager.getInstance().getTableName(tableClass));
    }


}

