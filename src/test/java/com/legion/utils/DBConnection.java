package com.legion.utils;

import java.sql.*;

public class DBConnection {
    public static void updateDB(String sql) {
        Connection con;
        String driver = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://dev-eks-shared-rc-mysql8.cijomzi1o1vu.us-west-2.rds.amazonaws.com:3306";
        String user = "legion";
        String password = "legionwork";
        if(System.getProperty("env").contains("ephemeral-ops")){
            driver = "com.mysql.cj.jdbc.Driver";
            url = "jdbc:mysql://rds.ephemeral.legion.local:3306";
        }
        try {
            Class.forName(driver);
            con = DriverManager.getConnection(url, user, password);
            if (!con.isClosed())
                System.out.println("------Connect DB successfully!------");
            System.out.println(sql);
            Statement statement = con.createStatement();
            int result = statement.executeUpdate(sql);
            if (result > 0) {
                System.out.println("-------Update DB successfully----------" + "\t");
            } else {
                System.out.println("-------Update operation failed----------" + "\t");
            }

            statement.close();
            con.close();
            System.out.println("-------Disconnect DB successfully----------" + "\t");
        } catch (ClassNotFoundException e) {
            //DB driver exception
            System.out.println("Sorry,can`t find the Driver!");
            e.printStackTrace();
        } catch (SQLException e) {
            //connection exception
            e.printStackTrace();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    public static String queryDB(String table, String columnLabel, String condition) {
        Connection con;
        String driver = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://dev-eks-shared-rc-mysql8.cijomzi1o1vu.us-west-2.rds.amazonaws.com:3306";
        String user = "legion";
        String password = "legionwork";
        String value = null;
        if(System.getProperty("env").contains("ephemeral-ops")){
            driver = "com.mysql.cj.jdbc.Driver";
            url = "jdbc:mysql://rds.ephemeral.legion.local:3306";
        }
        try {
            Class.forName(driver);
            con = DriverManager.getConnection(url, user, password);
            if (!con.isClosed())
                System.out.println("------Connect DB successfully!------");
            Statement statement = con.createStatement();
            String sql = "Select " + columnLabel + " from " + table + " where " + condition;
            System.out.println(sql);
            ResultSet rs = statement.executeQuery(sql);
            if (rs.next()) {
                value = rs.getString(columnLabel);
                System.out.println(columnLabel + ": " + value + "\t");
                System.out.println("--------Query successfully---------");
            } else {
                value = "No item returned!";
            }
            rs.close();
            statement.close();
            con.close();
            System.out.println("-------Disconnect DB successfully----------" + "\t");
        } catch (ClassNotFoundException e) {
            //DB driver exception
            System.out.println("Sorry,can`t find the Driver!");
            e.printStackTrace();
        } catch (SQLException e) {
            //connection exception
            e.printStackTrace();
            System.out.println("Sorry,failed to execute the sql!");
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return value;
    }
    public static int queryMultipleTableAndGetNumber(String sqlStatement) {
        Connection con;
        String driver = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://dev-eks-shared-rds-mysql8.cijomzi1o1vu.us-west-2.rds.amazonaws.com:3306";
        String user = "legion";
        String password = "legionwork";
        int number = 0;
        try {
            Class.forName(driver);
            con = DriverManager.getConnection(url, user, password);
            if (!con.isClosed())
                System.out.println("------Connect DB successfully!------");
            Statement statement = con.createStatement();
            System.out.println(sqlStatement);
            ResultSet rs = statement.executeQuery(sqlStatement);
            while (rs.next()){
                number++;
            }
            rs.close();
            statement.close();
            con.close();
            System.out.println("-------Disconnect DB successfully----------" + "\t");
        } catch (ClassNotFoundException e) {
            //DB driver exception
            System.out.println("Sorry,can`t find the Driver!");
            e.printStackTrace();
        } catch (SQLException e) {
            //connection exception
            e.printStackTrace();
            System.out.println("Sorry,failed to execute the sql!");
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return number;
    }

    public static int getQueryResultNumber(String table, String columnLabel, String condition) {
        Connection con;
        String driver = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://dev-eks-shared-rds-mysql8.cijomzi1o1vu.us-west-2.rds.amazonaws.com:3306";
        String user = "legion";
        String password = "legionwork";
        int number = 0;
        try {
            Class.forName(driver);
            con = DriverManager.getConnection(url, user, password);
            if (!con.isClosed())
                System.out.println("------Connect DB successfully!------");
            Statement statement = con.createStatement();
            String sql = "Select " + columnLabel + " from " + table + " where " + condition;
            System.out.println(sql);
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()){
                number++;
            }
            rs.close();
            statement.close();
            con.close();
            System.out.println("-------Disconnect DB successfully----------" + "\t");
        } catch (ClassNotFoundException e) {
            //DB driver exception
            System.out.println("Sorry,can`t find the Driver!");
            e.printStackTrace();
        } catch (SQLException e) {
            //connection exception
            e.printStackTrace();
            System.out.println("Sorry,failed to execute the sql!");
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return number;
    }
}
