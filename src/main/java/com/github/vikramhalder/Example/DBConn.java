package com.github.vikramhalder.Example;

import com.github.vikramhalder.JavaORM.DBConfig;

import java.sql.*;

class DBConn {

    public static DBConfig dbConfig(){
        DBConfig dbConfig=new DBConfig();
        dbConfig.setHost("127.0.0.1");
        dbConfig.setPort("3306");
        dbConfig.setUsername("root");
        dbConfig.setPassword("1");
        return dbConfig;
    }

    public static boolean insert(String inset_sql){
        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/","root","1");
            PreparedStatement preparedStmt =con.prepareStatement(inset_sql);
            boolean ret= preparedStmt.execute();
            con.close();
            return ret;
        }catch(Exception e){
            System.out.println(e);
        }
        return false;
    }

    public static int createTable(String db_name,String table_sql){
        System.out.println(table_sql);
        int ret=0;
        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/","root","1");
            Statement stmt =con.createStatement();
            ret= stmt.executeUpdate(table_sql);
            System.out.println("Table Created");
            con.close();
            return ret;
        }catch(Exception e){
            e.printStackTrace();
            System.out.println(e);
        }
        return ret;
    }

    public static void insert1(String inset_sql){
        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/","root","1");
            Statement stmt=con.createStatement();
            ResultSet rs=stmt.executeQuery("select * from test.ent");
            while(rs.next())
                System.out.println(rs.getInt(1)+"  "+rs.getString(2)+"  "+rs.getString(3));
            con.close();
        }catch(Exception e){
            System.out.println(e);
        }
    }
}
