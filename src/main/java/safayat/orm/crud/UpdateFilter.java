package safayat.orm.crud;


import safayat.orm.dao.GeneralRepositoryManager;
import safayat.orm.reflect.Util;

import java.sql.Connection;
import java.sql.SQLException;

public class UpdateFilter{

    private StringBuilder updateBuilder;

    public UpdateFilter(StringBuilder stringBuilder) {
        this.updateBuilder = stringBuilder;
        stringBuilder.append(" WHERE 1=1");
    }

    public UpdateFilter filter(String expr, Object value){
        updateBuilder.append(" AND ").append(expr).append(Util.toMysqlString(value));
        return this;
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
