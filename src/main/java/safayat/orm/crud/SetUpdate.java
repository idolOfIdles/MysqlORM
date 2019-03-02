package safayat.orm.crud;


import safayat.orm.reflect.Util;
import safayat.orm.dao.GeneralRepositoryManager;

import java.sql.Connection;
import java.sql.SQLException;

public class SetUpdate{

    private StringBuilder updateBuilder;

    public SetUpdate(StringBuilder insertBuilder) {
        this.updateBuilder = insertBuilder;
    }

    public SetUpdate set(String field, Object value) {
        updateBuilder.append(field).append(" = ").append(Util.toMysqlString(value)).append(",");
        return this;
    }

    public UpdateFilter filter(String expr, Object value) {
        Util.rightStripIfExists(updateBuilder, ',');
        return new UpdateFilter(updateBuilder).filter(expr, value);
    }

    private String asString() {
        Util.rightStripIfExists(updateBuilder, ',');
        return updateBuilder.toString();
    }

    public void update() throws SQLException {
        GeneralRepositoryManager.getInstance().getGeneralRepository().execute(asString());
    }

    public void update(Connection connection) throws SQLException {
        GeneralRepositoryManager.getInstance().getGeneralRepository().execute(asString(), connection);
    }


}
