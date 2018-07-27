package com.github.vikramhalder.Example;

import com.github.vikramhalder.JavaORM.Annotations.PK;
import com.github.vikramhalder.JavaORM.DBConfig;

import java.sql.*;

public class DBConn {
    @PK
    private int id;

    public static DBConfig dbConfig(){
        DBConfig dbConfig=new DBConfig();
        dbConfig.setHost("127.0.0.1");
        dbConfig.setPort("3306");
        dbConfig.setUsername("root");
        dbConfig.setViewQuery(true);
        dbConfig.setPassword("1");
        return dbConfig;
    }

    public static DBConfig dbConfig(Boolean view_sql){
        DBConfig dbConfig=new DBConfig();
        dbConfig.setHost("127.0.0.1");
        dbConfig.setPort("3306");
        dbConfig.setUsername("root");
        dbConfig.setPassword("1");
        if(view_sql)
            dbConfig.setViewQuery(true);
        else
            dbConfig.setViewQuery(false);
        return dbConfig;
    }
}
