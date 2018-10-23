package dao;

import model.student;
import queryBuilder.MysqlQuery;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by safayat on 10/20/18.
 */
public class CommonDAO {

    private String dbUserName = "root";

    private String dbPassword = "root";

    //    private String dbName  = "rssdesk";
    private String dbName  = "schoolmanagement";

    //    private String dbUrl = "jdbc:mysql://localhost:3306/rssdesk?useSSL=false";
    private String dbUrl = "jdbc:mysql://localhost:3306/schoolmanagement";



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


    public List<student> getAllStudents(Class cls)  {

        Connection dbConnection = null;
        PreparedStatement statement = null;
        List<student> studentList = new ArrayList<student>();
        try {
            String sql = MysqlQuery.get().table("student", "st").getQuery().toString();

            dbConnection = getConnection();
            statement = dbConnection.prepareStatement(sql);
            ResultSet rs = statement.executeQuery();

            if(rs.next()){
                ResultSetMetaData resultSetMetaData = rs.getMetaData();
                int columnCount = resultSetMetaData.getColumnCount();
                for(int i=1;i<=columnCount;i++){
                    String columnName = resultSetMetaData.getColumnName(i);
                    String tableName  = resultSetMetaData.getTableName(i);
                }

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
        return studentList;
    }


}
