package queryBuilder;

import java.util.Date;
import java.util.List;

/**
 * Created by safayat on 10/16/18.
 */



interface  MysqlQueryInterface {
    StringBuilder getQuery();
}
public class MysqlQuery implements MysqlQueryInterface{

    private StringBuilder query;
    private StringBuilder queryFields;

    public MysqlQuery(String fields) {
        this.query = new StringBuilder();
        queryFields = new StringBuilder();
        queryFields.append(fields);
    }
    public static MysqlQuery get(){
        return new MysqlQuery("*");
    }

    public static MysqlQuery get(String fields){
        return new MysqlQuery(fields);
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
        appendAggregateFunction(field, ",min");
        return this;
    }

    public void appendAggregateFunction(String field, String op){
        if(queryFields.length()>0) queryFields.append(",");
        queryFields.append(op).append("(").append(field).append(")");
    }

    public StringBuilder getQuery() {
        return query;
    }

    public MysqlTable table(String tableName, String code){
        query.append("select " + queryFields.toString() + " from ");
        return new MysqlTable(this).table(tableName, code);
    }

    public MysqlTable table(String tableName){
        query.append("select " + queryFields.toString() + " from ");
        String[] splitted = tableName.trim().split(" ");
        if(splitted.length > 1){
            return new MysqlTable(this).table(splitted[0],splitted[1]);
        }
        return new MysqlTable(this).table(tableName,"");
    }

    public static void main(String[] args){
        String sql = MysqlQuery.get()
                .sum("*")
                .avg("id")
                .table("user us")
                .table("profile pf")
                .leftJoin("blog bl").on("us.id", "bl.id")
                .filter("us.us_id =", "5")
                .getQuery()
                .toString();
        System.out.println(sql);


        sql = MysqlQuery.get()
                .table("user us")
                .join("profile pf").on("pf.us_id", "us.id")
                .filter("pf.age >", "36")
                .groupBy("pf_id")
                .order("pf_id", "desc")
                .order("us_name", "asc")
                .limit(10);
        System.out.println(sql);
        Object o = 5;
        System.out.println(o.toString());
        System.out.println(new Date().toString());

    }

}


