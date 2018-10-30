package dao;

import jdbcUtility.ResultSetUtility;
import model.Category;
import model.SubCategory;
import queryBuilder.MysqlQuery;

import java.sql.*;
import java.util.*;

/**
 * Created by safayat on 10/20/18.
 */
public class CommonDAO {

    private String dbUserName = "root";

//    private String dbPassword = "root";
    private String dbPassword = "";

    private String dbName  = "alhelal";

        private String dbUrl = "jdbc:mysql://localhost:3306/alhelal?useSSL=false";
//    private String dbUrl = "jdbc:mysql://localhost:3306/alhelal";



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

    private List<String> getPrimaryKeys(String table, Connection connection) {


        List<String> primaryKeys = new ArrayList<String>();
        try {
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet rs = metaData.getPrimaryKeys(dbName, null, table);
            while (rs.next()){
                primaryKeys.add(rs.getString("column_name"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return primaryKeys;

    }


    public List<SubCategory> getSubcategorys() throws Exception{

        Connection dbConnection = null;
        PreparedStatement statement = null;
        List<SubCategory> SubCategories = new ArrayList<SubCategory>();
        try {
            MysqlQuery sqlQuery = MysqlQuery.get()
                    .table("subCategory", "sc")
                    .join("category", "ct").on("ct.id", "sc.categoryId")
                    .join("product", "pd").on("sc.id", "pd.subCategoryId")
                    .getQuery();

            String sql = sqlQuery.toString();
            System.out.println(sql);
            dbConnection = getConnection();
            statement = dbConnection.prepareStatement(sql);
            ResultSet rs = statement.executeQuery();
            ResultSetUtility resultSetUtility = new ResultSetUtility(rs);
            return resultSetUtility.mapResultsetToClass(SubCategory.class);


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
        return SubCategories;
    }

    public List<Category> getCategorys() throws Exception{

        Connection dbConnection = null;
        PreparedStatement statement = null;
        List<Category> subCategories = new ArrayList<Category>();
        try {
            MysqlQuery sqlQuery = MysqlQuery.get()
                    .table("subCategory", "sc")
                    .join("category", "ct").on("ct.id", "sc.categoryId")
                    .join("product", "pd").on("sc.id", "pd.subCategoryId")
                    .filter("ct.id =", "9")
                    .getQuery();

            String sql = sqlQuery.toString();
            System.out.println(sql);
            dbConnection = getConnection();
            statement = dbConnection.prepareStatement(sql);
            ResultSet rs = statement.executeQuery();
            ResultSetUtility resultSetUtility = new ResultSetUtility(rs);
            return resultSetUtility.mapResultsetToClass(Category.class);


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
        return subCategories;
    }

}
