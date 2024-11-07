package com.erikat.crudjavageneral.Util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class MySQLUtils {
    public static Connection mysqlConnection;
    static {
        try{
            Properties dbConfig = new Properties();
            dbConfig.load(R.getDBConfig("MySQL.properties"));
            String host = dbConfig.getProperty("host");
            String port = dbConfig.getProperty("port");
            String dbname = dbConfig.getProperty("dbname");
            String user = dbConfig.getProperty("uname");
            String passwd = dbConfig.getProperty("passwd");

            Class.forName("com.mysql.cj.jdbc.Driver");
            mysqlConnection = DriverManager.getConnection("jdbc:mysql://"+host+":"+port+"/"+dbname+"?serverTimezone=UTC", user, passwd);

        }catch (Exception e){
            System.out.println("Error: " +e.getMessage());;
        }
    }
    public static void closeConnection(){
        try {
            mysqlConnection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public static Connection getConnection(){
        return mysqlConnection;
    }
}
