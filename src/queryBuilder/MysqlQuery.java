package queryBuilder;

import java.util.List;

/**
 * Created by safayat on 10/16/18.
 */
class MysqlJoin {

    MysqlTable mysqlTable;
    public MysqlJoin(MysqlTable mysqlTable) {
        this.mysqlTable = mysqlTable;
    }
    public MysqlTable on(String col1,String col2){
        mysqlTable.mysqlQuery.getQuery().append(" on ").append(col1).append("=").append(col2);
        return mysqlTable;
    }


}

class MysqlOrder implements MysqlQueryInterface{

    MysqlQueryInterface mysqlQuery;
    boolean firstOrder = true;
    public MysqlOrder(MysqlQueryInterface mysqlQuery) {
        this.mysqlQuery = mysqlQuery;
    }

    public  MysqlOrder order(String orderKey, String sort){
        if(!firstOrder) mysqlQuery.getQuery().append(",");
        firstOrder=false;
        mysqlQuery.getQuery().append(" order by ").append(orderKey);
        if(sort!= null && !sort.isEmpty()) mysqlQuery.getQuery().append(" ").append(sort);
        return this;
    }


    @Override
    public StringBuilder getQuery() {
        return mysqlQuery.getQuery();
    }
}
class MysqlLimit implements MysqlQueryInterface{

    MysqlQueryInterface mysqlQuery;
    public MysqlLimit(MysqlQueryInterface mysqlQuery) {
        this.mysqlQuery = mysqlQuery;
    }

    public MysqlQueryInterface limit(int limit){
        mysqlQuery.getQuery().append("limit ").append(limit);
        return mysqlQuery;
    }

    @Override
    public StringBuilder getQuery() {
        return mysqlQuery.getQuery();
    }
}
class MysqlCondition implements MysqlQueryInterface{

    MysqlQueryInterface mysqlQuery;
    MysqlOrder mysqlOrder;
    MysqlLimit mysqlLimit;
    public MysqlCondition(MysqlQueryInterface mysqlQuery) {
        this.mysqlQuery = mysqlQuery;
        mysqlQuery.getQuery().append(" WHERE 1=1");
    }


    public MysqlCondition filter(String expression, String value){
        mysqlQuery.getQuery().append(" AND ");
        mysqlQuery.getQuery().append(expression).append(value);
        return this;
    }

    public MysqlCondition orFilter(String expression, String value){
        mysqlQuery.getQuery().append(" OR ");
        mysqlQuery.getQuery().append(expression).append(value);
        return this;
    }
    public MysqlCondition orIn(String field, List<Object> values){
        mysqlQuery.getQuery().append(" OR ");
        mysqlQuery.getQuery().append(field).append(" in ").append("(");
        boolean firstElement = true;
        for(Object o : values){
            if(!firstElement) mysqlQuery.getQuery().append(",");
            firstElement = false;
            mysqlQuery.getQuery().append(o.hashCode());
        }
        mysqlQuery.getQuery().append(")");
        return this;
    }
    public MysqlCondition in(String field, List<Object> values){
        mysqlQuery.getQuery().append(" AND ");
        mysqlQuery.getQuery().append(field).append(" in ").append("(");
        boolean firstElement = true;
        for(Object o : values){
            if(!firstElement) mysqlQuery.getQuery().append(",");
            firstElement = false;
            mysqlQuery.getQuery().append(o.hashCode());
        }
        mysqlQuery.getQuery().append(")");
        return this;
    }

    public  MysqlQueryInterface order(String orderKey, String sort){
        if(mysqlOrder == null) mysqlOrder = new MysqlOrder(this);
        mysqlOrder.order(orderKey,sort);
        return this;
    }

    public  MysqlQueryInterface limit(int limit){
        if(mysqlLimit == null) mysqlLimit = new MysqlLimit(this);
        mysqlLimit.limit(limit);
        return this;
    }



    @Override
    public StringBuilder getQuery() {
        return mysqlQuery.getQuery();
    }
}
class MysqlTable implements MysqlQueryInterface{
    MysqlJoin mysqlJoin;
    boolean tableSelectedOnce = false;
    MysqlQueryInterface mysqlQuery;
    MysqlCondition mysqlCondition;
    MysqlOrder mysqlOrder;
    MysqlLimit mysqlLimit;

    public MysqlTable(MysqlQueryInterface mysqlQuery){
        this.mysqlQuery = mysqlQuery;
        mysqlJoin = new MysqlJoin(this);
//        mysqlCondition = new MysqlCondition(this);
    }
    public MysqlTable table(String tableName, String code){
        if(tableSelectedOnce) mysqlQuery.getQuery().append(",");
        tableSelectedOnce = true;
        mysqlQuery.getQuery().append(tableName).append(" ").append(code);
        return this;
    }
    public MysqlJoin join(String tableName, String code){
        mysqlQuery.getQuery().append(" join ").append(tableName).append(" ").append(code);
        return mysqlJoin;
    }
    public MysqlJoin leftJoin(String tableName, String code){
        mysqlQuery.getQuery().append(" left join ").append(tableName).append(" ").append(code);
        return mysqlJoin;
    }

    public MysqlJoin rightJoin(String tableName, String code){
        mysqlQuery.getQuery().append(" right join ").append(tableName).append(" ").append(code);
        return mysqlJoin;
    }

    public MysqlCondition filter(String expression, String value){
        if(mysqlCondition == null) mysqlCondition = new MysqlCondition(this);
        return mysqlCondition.filter(expression, value);
    }

    public MysqlCondition orFilter(String expression, String value){
        if(mysqlCondition == null) mysqlCondition = new MysqlCondition(this);
        return mysqlCondition.orFilter(expression, value);

    }
    public MysqlCondition orIn(String field, List<Object> values){
        if(mysqlCondition == null) mysqlCondition = new MysqlCondition(this);
        return mysqlCondition.orIn(field, values);

    }
    public MysqlCondition in(String field, List<Object> values){
        if(mysqlCondition == null) mysqlCondition = new MysqlCondition(this);
        return mysqlCondition.in(field, values);

    }

    public  MysqlQueryInterface order(String orderKey, String sort){
        if(mysqlOrder == null) mysqlOrder = new MysqlOrder(this);
        mysqlOrder.order(orderKey,sort);
        return this;
    }

    public  MysqlQueryInterface limit(int limit){
        if(mysqlLimit == null) mysqlLimit = new MysqlLimit(this);
        mysqlLimit.limit(limit);
        return this;
    }

    @Override
    public StringBuilder getQuery() {
        return mysqlQuery.getQuery();
    }
}

interface  MysqlQueryInterface {
    StringBuilder getQuery();
}
public class MysqlQuery implements MysqlQueryInterface{

    private StringBuilder query;

    public MysqlQuery() {
        this.query = new StringBuilder(" select * from ");
    }
    public static MysqlQuery get(){
        return new MysqlQuery();
    }

    public StringBuilder getQuery() {
        return query;
    }

    public MysqlTable table(String tableName, String code){
        return new MysqlTable(this).table(tableName, code);
    }

    public static void main(String[] args){
        String sql = MysqlQuery.get().table("user","us")
                .leftJoin("blog", "bl").on("us.id","bl.id")
                .filter("us.us_id =", "5")
                .order("bl.bl_name", "asc").getQuery().toString();
        System.out.println(sql);
    }

}


