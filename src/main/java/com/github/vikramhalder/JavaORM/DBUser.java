package com.github.vikramhalder.JavaORM;

import com.github.vikramhalder.JavaORM.Interface.IDBUser;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DBUser implements IDBUser {
    @Override
    public int onCreateDB(DBConfig dbConfig,String db_name) {
        if(dbConfig.getViewQuery())
            System.out.println("CREATE DATABASE "+db_name);
        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection con=DriverManager.getConnection("jdbc:mysql://"+dbConfig.getHost()+":"+dbConfig.getPort(),dbConfig.getUsername(),dbConfig.getPassword());
            Statement statement =con.createStatement();
            int Result=statement.executeUpdate("CREATE DATABASE "+db_name);
            con.close();
            return Result>0?1:0;
        }catch(Exception e){
            String err=e.toString()+"";
            if(err.contains("database exists"))
                return 11;
            else
                System.out.println(e);
        }
        return 0;
    }
}
