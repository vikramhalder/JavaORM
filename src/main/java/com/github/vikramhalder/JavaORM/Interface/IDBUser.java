package com.github.vikramhalder.JavaORM.Interface;

import com.github.vikramhalder.JavaORM.DBConfig;

public interface IDBUser {
    int onCreateDB(DBConfig dbConfig, String db_name);
}
