package safayat.orm.query;

/**
 * Created by safayat on 10/16/18.
 */

public class QueryInfo{

    private StringBuilder query;
    private StringBuilder queryFields;
    private boolean orderBegan;
    private boolean tableBegan;
    private boolean whereBegan;


    public QueryInfo() {
        query = new StringBuilder();
        queryFields = new StringBuilder();
    }


    public StringBuilder getQuery() {
        return query;
    }

    public void setQuery(StringBuilder query) {
        this.query = query;
    }

    public StringBuilder getQueryFields() {
        return queryFields;
    }

    public void setQueryFields(StringBuilder queryFields) {
        this.queryFields = queryFields;
    }

    public boolean isOrderBegan() {
        return orderBegan;
    }

    public void setOrderBegan(boolean orderBegan) {
        this.orderBegan = orderBegan;
    }

    public boolean isTableBegan() {
        return tableBegan;
    }

    public void setTableBegan(boolean tableBegan) {
        this.tableBegan = tableBegan;
    }

    public QueryInfo append(String data) {
        query.append(data);
        return this;
    }
    public void appendFields(String fields) {
        queryFields.append(fields);
    }

    @Override
    public String toString() {
        return query.toString();
    }

    public boolean isWhereBegan() {
        return whereBegan;
    }

    public void setWhereBegan(boolean whereBegan) {
        this.whereBegan = whereBegan;
    }

}


