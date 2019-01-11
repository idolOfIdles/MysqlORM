package safayat.orm.query;

import safayat.orm.annotation.ManyToMany;
import safayat.orm.annotation.ManyToOne;
import safayat.orm.annotation.OneToMany;
import safayat.orm.config.ConfigManager;
import safayat.orm.dao.GeneralRepositoryManager;
import safayat.orm.query.util.Util;
import safayat.orm.reflect.ReflectUtility;
import safayat.orm.reflect.RelationInfo;

import java.sql.ResultSet;
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

    public static ResultSet execute(String sql){
        return GeneralRepositoryManager
                .getInstance()
                .getGeneralRepository()
                .executeQuery(sql)
                .getResultSet();
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
        return new MysqlTable(query).table(tableName, "");
    }

    public MysqlTable table(Class tableClass, String alias){
        return table(ConfigManager.getInstance().getTableName(tableClass), alias);
    }

    public MysqlCondition oneToMany(Class parent, Class child) throws Exception{
        RelationInfo oneToMany = ReflectUtility.getRelationAnnotation(parent, OneToMany.class, child);
        if(oneToMany == null) throw new Exception("One to many Relation not found!");
        query.setTableBegan(true);
        query.append("select " + query.getQueryFields().toString() + " from ")
                .append(oneToMany.createJoinSql(parent));
        return new MysqlCondition(query, false);
    }

    public MysqlCondition manyToOne(Class parent, Class child) throws Exception{
        RelationInfo manyToOne = ReflectUtility.getRelationAnnotation(parent, ManyToOne.class, child);
        if(manyToOne == null) throw new Exception("Many to many Relation not found!");

        query.setTableBegan(true);
        query.append("select " + query.getQueryFields().toString() + " from ")
                .append(manyToOne.createJoinSql(parent));
        return new MysqlCondition(query, false);
    }

    public MysqlCondition manyToMany(Class parent, Class child) throws Exception{

        RelationInfo manyToMany = ReflectUtility.getRelationAnnotation(parent, ManyToMany.class, child);
        if(manyToMany == null) throw new Exception("Many to many Relation not found!");
        query.setTableBegan(true);
        query.append("select " + query.getQueryFields().toString() + " from ")
                .append(manyToMany.getTableName(parent)).append(" ").append(manyToMany.getTableName(parent).toLowerCase())
                .append(manyToMany.createManyToManyJoinSql(parent));
        return new MysqlCondition(query, false);
    }

    public MysqlTable table(Class tableClass){
        return table(ConfigManager.getInstance().getTableName(tableClass));
    }


}


