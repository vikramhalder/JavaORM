package com.github.vikramhalder.JavaORM.Interface;

import com.github.vikramhalder.JavaORM.DBInsert;

import java.util.ArrayList;

public interface IDB<T> {
     boolean onCreateTable();
     DBInsert insert(T t);
     boolean insert(ArrayList<T> tArrayList);
     boolean delete(T t);
     boolean delete(String coloum, String value);
     ArrayList<T> getAll();
     T getByPK(Object pk);
     T getFirstOrDefault(String where, String select);
     ArrayList<T> sqlSelect(String sql);
     boolean update(T getEntity);
     boolean preparedStatement(String sql);
     boolean statement(String sql);
}
