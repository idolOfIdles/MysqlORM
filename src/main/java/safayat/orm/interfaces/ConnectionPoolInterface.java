package safayat.orm.interfaces;

import java.sql.Connection;

/**
 * Created by safayat on 11/26/18.
 */
public interface ConnectionPoolInterface {
    Connection getConnection();
}
