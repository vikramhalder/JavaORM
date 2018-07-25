package  com.github.vikramhalder.JavaORM;

import java.util.ArrayList;

public interface IDB<T> {

     int onCreateDB(String db_name);
     boolean onCreateTable();
     boolean insert(T t);
     boolean insert(ArrayList<T> tArrayList);
     ArrayList<T> getAll();
     T getByPK(Object pk);
     T getFirstOrDefault(String where, String select);
     ArrayList<T> sqlSelect(String sql);
     boolean update(T getEntity);
     boolean preparedStatement(String sql);
     boolean statement(String sql);
}
