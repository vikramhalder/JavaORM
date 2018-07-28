package com.github.vikramhalder.JavaORM;

import com.github.vikramhalder.JavaORM.Annotations.Table;
import com.github.vikramhalder.JavaORM.Error.ErrorMessage;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.logging.ErrorManager;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DBInsert {
    private boolean ok=false;
    private int id;
    private String message;

    private ArrayList<String> mess=new ArrayList<>();

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getQuery() {
        return String.join(" ! \n",mess);
    }

    public void setQuery(String query) {
        mess.add(query);
    }

    protected static DBInsert insert(String databasename, TableValue tableValue, DBConfig dbConfig, Class<?> getEntity) {
        DBInsert dbInsert=new DBInsert();

        boolean notnull=false;
        ArrayList<String> coloums=new ArrayList<>();
        ArrayList<String> values=new ArrayList<>();

        if(tableValue.no_annotations.size()>0){
            for(Item item : tableValue.no_annotations){
                if(item._value!=null){
                    coloums.add(item._fieldname.mapname);
                    values.add("'"+String.valueOf(item._value).replace("'","''")+"'");
                }
            }
        }

        if(tableValue.unique.size()>0){
            for(CustomClass customClass : tableValue.unique){
                Item item=customClass.items;
                if(item._value!=null){
                    coloums.add(item._fieldname.mapname);
                    values.add("'"+String.valueOf(item._value).replace("'","''")+"'");
                }
            }
        }

        if(tableValue.one_to_one.size()>0){
            for(CustomClass customClass : tableValue.one_to_one){
                Item item=customClass.items;
                if(item._value!=null){
                    Item pk=customClass.table_value.primary_key;
                    if(pk!=null) {
                        coloums.add(item._fieldname.mapname);
                        values.add("'"+String.valueOf(pk._value).replace("'","''")+"'");
                    }
                }
            }
        }

        if(tableValue.one_to_many.size()>0){
            for(CustomClass customClass : tableValue.one_to_many){
                Item item=customClass.items;
                if(item._value!=null){
                    Item pk=customClass.table_value.primary_key;
                    if(pk!=null) {
                        coloums.add(item._fieldname.mapname);
                        values.add("'"+String.valueOf(pk._value).replace("'","''")+"'");
                    }
                }
            }
        }
        if(tableValue.many_to_many.size()>0){
            for(CustomClass customClass : tableValue.many_to_many){
                Item item=customClass.items;
                if(item._value!=null){
                    Item pk=customClass.table_value.primary_key;
                    if(pk!=null) {
                        coloums.add(item._fieldname.mapname);
                        values.add("'"+String.valueOf(pk._value).replace("'","''")+"'");
                    }
                }
            }
        }
        if(tableValue.foreign_key.size()>0){
            for(CustomClass customClass : tableValue.foreign_key){
                Item item=customClass.items;
                if(item._value!=null){
                    Item pk=customClass.table_value.primary_key;
                    if(pk!=null) {
                        if(pk._value instanceof Integer){
                            if((int)pk._value>0) {
                                coloums.add(item._fieldname.mapname);
                                values.add("'" + String.valueOf(pk._value).replace("'", "''") + "'");
                            }else{
                                DBInsert di=insert(databasename,customClass.table_value,dbConfig,item._classobject);
                                if(di.isOk()){
                                    customClass.table_value.primary_key._value=di.getId();
                                    coloums.add(item._fieldname.mapname);
                                    values.add("'"+String.valueOf(pk._value).replace("'","''")+"'");
                                    System.out.println(pk._value);
                                }
                            }
                        }else {
                            if (pk._value != null) {
                                coloums.add(item._fieldname.mapname);
                                values.add("'" + String.valueOf(pk._value).replace("'", "''") + "'");
                            } else {
                                DBInsert di = insert(databasename, customClass.table_value, dbConfig, item._classobject);
                                if (di.isOk()) {
                                    customClass.table_value.primary_key._value = di.getId();
                                    coloums.add(item._fieldname.mapname);
                                    values.add("'" + String.valueOf(pk._value).replace("'", "''") + "'");
                                    System.out.println(pk._value);
                                }
                            }
                        }
                    }else{
                        DBInsert di=insert(databasename,customClass.table_value,dbConfig,item._classobject);
                        if(di.isOk()){
                            customClass.table_value.primary_key._value=di.getId();
                            coloums.add(item._fieldname.mapname);
                            values.add("'"+String.valueOf(pk._value).replace("'","''")+"'");
                            System.out.println(pk._value);
                        }
                    }
                }
            }
        }

        boolean ret=false;
        if(!notnull) {
            String sql = "insert into " + tableValue.table_name.mapname + "(" + String.join(",", coloums) + ") values " + "(" + String.join(",", values) + ")";
            sql = sql.replace("'null'", "NULL");
            dbInsert.setQuery(sql);

            if (dbConfig.getViewQuery())
                System.out.println(sql);

            PreparedStatement preparedStatement = null;
            Connection con = null;
            try {
                try {
                    Class.forName("com.mysql.jdbc.Driver");
                }catch (Exception ex){}
                con = DriverManager.getConnection("jdbc:mysql://" + dbConfig.getHost() + ":" + dbConfig.getPort() + "/" + databasename, dbConfig.getUsername(), dbConfig.getPassword());
                preparedStatement = con.prepareStatement(sql);
                preparedStatement.execute();
            } catch (SQLException se){
                dbInsert.setOk(false);
                dbInsert.setId(-1);
                dbInsert.setMessage(ErrorMessage.SQLError(se));
            }finally {
                try {
                    if (con != null) {
                        if (preparedStatement != null) {
                            try {
                                if(tableValue.primary_key!=null) {
                                    if(tableValue.primary_key._autoincrement) {
                                        ResultSet tableKeys = preparedStatement.getGeneratedKeys();
                                        tableKeys.next();
                                        int autoGeneratedID = tableKeys.getInt(1);
                                        if (autoGeneratedID > 0) {
                                            dbInsert.setOk(true);
                                            dbInsert.setId(autoGeneratedID);
                                            dbInsert.setMessage("Successful");
                                        }else {
                                            dbInsert.setOk(true);
                                            dbInsert.setId(-1);
                                            dbInsert.setMessage("Successful. And Primary Key Not Found");
                                        }
                                    }
                                }else {
                                    dbInsert.setOk(true);
                                    dbInsert.setId(-1);
                                    dbInsert.setMessage("Successful. And Primary Key Not Found");
                                }
                                con.close();
                            }catch (SQLException se){
                                dbInsert.setOk(false);
                                dbInsert.setId(-1);
                                dbInsert.setMessage(ErrorMessage.SQLError(se));
                                con.close();
                            }
                        }
                    }
                } catch (SQLException se){
                    dbInsert.setOk(false);
                    dbInsert.setId(-1);
                    dbInsert.setMessage(ErrorMessage.SQLError(se));
                }
            }
        }

        return dbInsert;
    }
}
