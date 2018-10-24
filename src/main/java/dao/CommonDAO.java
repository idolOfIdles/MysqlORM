package dao;

import model.Desk;
import queryBuilder.MysqlQuery;
import util.Util;

import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by safayat on 10/20/18.
 */
public class CommonDAO {

    private String dbUserName = "root";

    private String dbPassword = "";

        private String dbName  = "rssdesk";
//    private String dbName  = "schoolmanagement";

        private String dbUrl = "jdbc:mysql://localhost:3306/rssdesk?useSSL=false";
//    private String dbUrl = "jdbc:mysql://localhost:3306/schoolmanagement";



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


    public List<Desk> getAllDesks() throws Exception{

        Connection dbConnection = null;
        PreparedStatement statement = null;
        List<Desk> desks = new ArrayList<Desk>();
        try {
            String sql = MysqlQuery.get()
                    .table("Desk", "dk")
                    .leftJoin("User","us").on("us.id","dk.user_id")
                    .getQuery().toString();

            dbConnection = getConnection();
            statement = dbConnection.prepareStatement(sql);
            ResultSet rs = statement.executeQuery();


            Map<String, Boolean> methodMap = new HashMap<String, Boolean>();
            Map<String, Method> methodByName = new HashMap<String, Method>();
            Method[] methods = Desk.class.getMethods();
            for(Method m : methods){
                methodMap.put(m.getName(), true);
                methodByName.put(m.getName(), m);
            }

            ResultSetMetaData resultSetMetaData = rs.getMetaData();
            int columnCount = resultSetMetaData.getColumnCount();

            while(rs.next()){
                Desk desk = new Desk();

                for(int i=1;i<=columnCount;i++){
                    String columnName = resultSetMetaData.getColumnName(i);
                    String tableName  = resultSetMetaData.getTableName(i);
                    if(tableName.equalsIgnoreCase(desk.getClass().getSimpleName())){
                        String getMethodName = Util.toJavaMethodName(columnName, "set");
                        if(methodMap.containsKey(getMethodName)){
                            Method columnMethod = methodByName.get(getMethodName);
                            Object ob = null;

                            try{
                                ob = rs.getObject(i);
                            }catch (Exception e){

                            }
                            columnMethod.invoke(desk, ob);
                        }
                    }
                }

                desks.add(desk);

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
        return desks;
    }


}
