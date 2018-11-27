package safayat.orm.crud;

public class UpdateQuery {
    StringBuilder updateBuilder;

    public UpdateQuery(String table) {
        this.updateBuilder = new StringBuilder();
        updateBuilder.append("update ").append(table);
    }

    public SetUpdate set(String field, Object value) {
        updateBuilder.append(" set ");
        return new SetUpdate(updateBuilder).set(field, value);
    }



}
