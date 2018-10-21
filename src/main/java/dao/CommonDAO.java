package dao;

import java.sql.*;

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

    public String getAll(String id)  {

        Connection dbConnection = null;
        PreparedStatement statement = null;
        String bank_name = "";

        try {
            dbConnection = getConnection();
            statement = dbConnection.prepareStatement("SELECT cl.accountNumber, cl.address, ac.clientId, ac.accountNumber FROM accountInfo ac RIGHT JOIN client cl ON ac.clientId = cl.id");
            ResultSet rs = statement.executeQuery();

            if(rs.next()){
                System.out.println(rs.getMetaData());
            }


        } catch (SQLException e) {
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
        return bank_name;
    }


}
