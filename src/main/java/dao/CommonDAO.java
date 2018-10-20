package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by safayat on 10/20/18.
 */
public class CommonDAO {

    private String dbUserName;

    private String dbPassword;

    private String dbName;

    private String dbUrl;



    private Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        Connection connection = null;

        try {
            connection = DriverManager.getConnection(dbUrl, dbUserName, dbPassword);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return connection;

    }

    public void createTable() {
        Connection dbConnection = null;
        Statement statement = null;

        String createTableSQL = "CREATE TABLE IF NOT EXISTS `Bank`(" +
                "  `bank_id` VARCHAR(128) NOT NULL," +
                "  `bank_name` VARCHAR(128) NOT NULL," +
                "  PRIMARY KEY (`bank_id`));";

        try {
            dbConnection = getConnection();
            statement = dbConnection.createStatement();
            statement.execute(createTableSQL);

        }catch (Exception e){
            e.printStackTrace();
            if(statement!=null){
                try {
                    statement.close();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
            if(dbConnection!=null){
                try {
                    dbConnection.close();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }

        }

    }

}
