package com.github.vikramhalder.JavaORM;

import com.github.vikramhalder.JavaORM.Error.NotNullException;
import com.github.vikramhalder.JavaORM.Interface.IDB;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;

public class DB1<T> {
    /*private static final int DATABASE_VERSION = 1;
    private String databasename; ;
    private String tablename ;
    private DBTable DB_TABLE;
    private Class<T> entityclass;
    private T entity,entity_temp;
    private String pk_id=null;
    private DBConfig dbConfig;

    public DB1(Class<T> entityclass, String databasename) {
        this.databasename= databasename;
        this.DB_TABLE= DBCore.getDBTable(entityclass);
        this.entityclass=entityclass;
        try {
            this.tablename=DB_TABLE.classname.mapname;
            this.entity = entityclass.newInstance();
            this.entity_temp=entity;
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public DB1 Config(DBConfig dbConfig){
        this.dbConfig=dbConfig;
        return this;
    }
    
    @Override
    public boolean onCreateTable(){
        return tableCreate("",tablename,dbConfig);
    }

    private boolean tableCreate(String db_name,String table_name,DBConfig dbConfig){

        ArrayList<DBTable> foreignkey=new ArrayList<>();
        ArrayList<String> unique=new ArrayList<>();
        ArrayList<String> coloums=new ArrayList<>();
        String temp0="";
        for (com.github.vikramhalder.JavaORM.DBProperty DBProperty : DB_TABLE.properties){
            if(DBProperty._fieldName!=null) {
                if(DBProperty._foreignkey!=null){
                    foreignkey.add(DBProperty._foreignkey);
                }

                if(DBProperty._pk && DBProperty._autoincrement) {
                    pk_id= DBProperty._fieldName.mapname;
                    coloums.add(DBProperty._fieldName.mapname + " int NOT NULL AUTO_INCREMENT");
                }else if(DBProperty._pk) {
                    pk_id= DBProperty._fieldName.mapname;
                    coloums.add(DBProperty._fieldName.mapname + " varchar(255)");
                }else if(DBProperty._autoincrement) {
                    coloums.add(DBProperty._fieldName.mapname + " int NOT NULL AUTO_INCREMENT");
                }else if(DBProperty._qunique) {
                    unique.add(DBProperty._fieldName.mapname);
                    if(DBProperty._type.equals("int")) {
                        if (DBProperty._notNull) {
                            coloums.add(DBProperty._fieldName.mapname + " int NOT NULL");
                        } else {
                            coloums.add(DBProperty._fieldName.mapname + " int");
                        }
                    }else if(DBProperty._type.equals("Date")) {
                        if(DBProperty._default.toUpperCase().equals("CURRENT_TIMESTAMP")) {
                            coloums.add(DBProperty._fieldName.mapname + " timestamp DEFAULT CURRENT_TIMESTAMP");
                        }else{
                            coloums.add(DBProperty._fieldName.mapname + " date");
                        }
                    }
                    else {
                        if (DBProperty._notNull) {
                            coloums.add(DBProperty._fieldName.mapname + " varchar(255) NOT NULL "+(DBProperty._default!=null?"default '"+DBProperty._default+"'":""));
                        } else {
                            coloums.add(DBProperty._fieldName.mapname + " varchar(255) "+(DBProperty._default!=null?"default '"+DBProperty._default+"'":""));
                        }
                    }
                }else {
                    if(DBProperty._type.equals("int")) {
                        if (DBProperty._notNull) {
                            coloums.add(DBProperty._fieldName.mapname + " int NOT NULL");
                        } else {
                            coloums.add(DBProperty._fieldName.mapname + " int ");
                        }
                    }else if(DBProperty._type.toLowerCase().equals("date")) {
                        if(DBProperty._default.toUpperCase().equals("CURRENT_TIMESTAMP")) {
                            coloums.add(DBProperty._fieldName.mapname + " timestamp DEFAULT CURRENT_TIMESTAMP");
                        }else{
                            coloums.add(DBProperty._fieldName.mapname + " date");
                        }
                    }else {
                        if (DBProperty._notNull) {
                            coloums.add(DBProperty._fieldName.mapname + " varchar(255) NOT NULL "+(DBProperty._default!=null?"default '"+DBProperty._default+"'":""));
                        } else {
                            coloums.add(DBProperty._fieldName.mapname + " varchar(255) "+(DBProperty._default!=null?"default ''"+DBProperty._default+"'"+"'":""));
                        }
                    }
                }
            }
        }
        System.out.println(foreignkey.size());
        String CREATE_CONTACTS_TABLE = "";
        if(pk_id!=null){
            if(unique.size()>0){
                CREATE_CONTACTS_TABLE=
                        "CREATE TABLE IF NOT EXISTS " + table_name + "(\n"+
                                "   "+String.join(",",coloums)+",\n"+
                                "   PRIMARY KEY ("+pk_id+"),\n"+
                                "   "+(DBCore.getFKey(foreignkey, dbConfig,databasename)!=null?DBCore.getFKey(foreignkey, dbConfig,databasename)+"\n":"")+
                                "   UNIQUE KEY ("+String.join(",",unique)+")\n" +
                                ");";
            }else {
                CREATE_CONTACTS_TABLE=
                        "CREATE TABLE IF NOT EXISTS " + table_name + "(\n"+
                                "   "+String.join(",",coloums)+",\n"+
                                "   "+(DBCore.getFKey(foreignkey, dbConfig,databasename)!=null?DBCore.getFKey(foreignkey, dbConfig,databasename)+"\n":"")+
                                "   PRIMARY KEY ("+pk_id+")\n"+
                                ");";
            }
        }else {
            if(unique.size()>0){
                CREATE_CONTACTS_TABLE=
                        "CREATE TABLE IF NOT EXISTS " + table_name + "(\n"+
                                "   "+String.join(",",coloums)+",\n"+
                                "   "+(DBCore.getFKey(foreignkey, dbConfig,databasename)!=null?DBCore.getFKey(foreignkey, dbConfig,databasename)+"\n":"")+
                                "   UNIQUE KEY ("+String.join(",",unique)+")\n" +
                                ");";
            }else {
                CREATE_CONTACTS_TABLE=
                        "CREATE TABLE IF NOT EXISTS " + table_name + "(\n"+
                                "   "+String.join(",",coloums)+",\n"+
                                "   "+(DBCore.getFKey(foreignkey,dbConfig,databasename)!=null?DBCore.getFKey(foreignkey,dbConfig,databasename)+"\n":"")+
                                ");";
            }
        }

        if(dbConfig.getViewQuery())
            System.out.println(CREATE_CONTACTS_TABLE);

        boolean ret=false;
        Statement stmt=null;
        Connection con=null;
        try{
            Class.forName("com.mysql.jdbc.Driver");
            con=DriverManager.getConnection("jdbc:mysql://"+dbConfig.getHost()+":"+dbConfig.getPort()+"/"+databasename,dbConfig.getUsername(),dbConfig.getPassword());
            stmt =con.createStatement();
            stmt.execute(CREATE_CONTACTS_TABLE);
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            try{
                if(con!=null) {
                    if(stmt!=null) {
                        ret=true;
                        con.close();
                    }
                }
            }catch(SQLException se){
                se.printStackTrace();
            }
        }
        return ret;
    }

    @Override
    public DBInsert insert(T getEntity) {
        DBInsert dbInsert=new DBInsert();
        boolean notnull=false;
        ArrayList<String> coloums=new ArrayList<>();
        ArrayList<String> values=new ArrayList<>();

        for(Field field:getEntity.getClass().getDeclaredFields())
        {
            ArrayList<DBTable> foreignkey=new ArrayList<>();
            field.setAccessible(true);
            for(DBProperty DBProperty :DB_TABLE.properties){
                if(DBProperty._fieldName!=null) {
                    if (DBProperty._fieldName.realname.equals(field.getName())) {
                        try {
                            if(!DBProperty._autoincrement) {
                                if(field.get(getEntity)!=null) {
                                    coloums.add(DBProperty._fieldName.mapname);
                                    if(DBProperty._foreignkey!=null){
                                        System.out.println("........."+DBProperty._foreignkey.clazz);
                                        foreignkey.add(DBProperty._foreignkey);
                                        for(DBProperty fdb_property:DBProperty._foreignkey.properties){
                                            DBInsert dbInsert1=insert((T) DBProperty._value.getClass());
                                            System.out.println(dbInsert1.isOk());
                                            System.out.println(dbInsert1.getId());
                                            System.out.println(dbInsert1.getMessage());
                                        }
                                    }else {
                                        values.add("'" + (String.valueOf(field.get(getEntity)) != null ? field.get(getEntity) + "" : "NULL").replace("'", "''") + "'");
                                    }
                                    }else{
                                    if(DBProperty._notNull){
                                        notnull=true;
                                        throw new NotNullException(DBProperty._fieldName.realname+"  null value not support");
                                    }
                                }
                            }
                        } catch (Exception esx) {
                            esx.printStackTrace();
                        }
                    }
                }
            }
        }

        boolean ret=false;
        if(!notnull) {
            String sql = "insert into " + tablename + "(" + String.join(",", coloums) + ") values " + "(" + String.join(",", values) + ")";
            sql = sql.replace("'null'", "NULL");
            if (dbConfig.getViewQuery())
                System.out.println(sql);

            PreparedStatement preparedStatement = null;
            Connection con = null;
            try {
                Class.forName("com.mysql.jdbc.Driver");
                con = DriverManager.getConnection("jdbc:mysql://" + dbConfig.getHost() + ":" + dbConfig.getPort() + "/" + databasename, dbConfig.getUsername(), dbConfig.getPassword());
                preparedStatement = con.prepareStatement(sql);
                preparedStatement.execute();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (con != null) {
                        if (preparedStatement != null) {
                            try {
                                ResultSet tableKeys = preparedStatement.getGeneratedKeys();
                                tableKeys.next();
                                int autoGeneratedID = tableKeys.getInt(1);
                                dbInsert.setOk(true);
                                dbInsert.setId(autoGeneratedID);
                                con.close();
                            }catch (Exception ex){
                                dbInsert.setOk(true);
                                dbInsert.setId(0);
                                dbInsert.setMessage("Insert successfull. But failed to get generated keys");
                                con.close();
                            }
                        }
                    }
                } catch (SQLException se) {
                    se.printStackTrace();
                }
            }
        }
        return dbInsert;
    }
    @Override
    public boolean insert(ArrayList<T> tArrayList) {
        boolean notnull=false;
        ArrayList<String> coloums=new ArrayList<>();
        ArrayList<String> values=new ArrayList<>();
        ArrayList<String> values_list=new ArrayList<>();

        int insert=0;
        int count=0;
        for(T getEntity:tArrayList ) {
            for (Field field : getEntity.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                for (DBProperty DBProperty : DB_TABLE.properties) {
                    if (DBProperty._fieldName != null) {
                        if (DBProperty._fieldName.realname.equals(field.getName())) {
                            try {
                                if (!DBProperty._autoincrement) {
                                    if(field.get(getEntity)!=null) {
                                        if (count == 0) {
                                            coloums.add(DBProperty._fieldName.mapname);
                                        }
                                        values.add("'" + (String.valueOf(field.get(getEntity)) != null ? field.get(getEntity) + "" : "NULL").replace("'", "''") + "'");
                                        insert++;
                                    }else{
                                        if(DBProperty._notNull){
                                            notnull=true;
                                            Error(DBProperty._fieldName.realname+" null value not support");
                                        }
                                    }
                                }
                            } catch (Exception esx) {
                                esx.printStackTrace();
                            }
                        }
                    }
                }
            }
            count++;
            values_list.add("("+String.join(",",values)+")");
            values=new ArrayList<>();
        }

        boolean ret = false;
        if(!notnull) {
            String sql = "insert into " + tablename + "(" + String.join(",", coloums) + ") values " + String.join(",", values_list);
            sql = sql.replace("'null'", "NULL");
            if (dbConfig.getViewQuery())
                System.out.println(sql);

            PreparedStatement preparedStatement = null;
            Connection con = null;
            try {
                Class.forName("com.mysql.jdbc.Driver");
                con = DriverManager.getConnection("jdbc:mysql://" + dbConfig.getHost() + ":" + dbConfig.getPort() + "/" + databasename, dbConfig.getUsername(), dbConfig.getPassword());
                preparedStatement = con.prepareStatement(sql);
                preparedStatement.execute();
                System.out.println("JavaORM: insert row " + insert);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (con != null) {
                        if (preparedStatement != null) {
                            ret = true;
                            con.close();
                        }
                    }
                } catch (SQLException se) {
                    se.printStackTrace();
                }
            }
        }
        return ret;
    }
    @Override
    public boolean delete(String coloum, String value) {
        String sql = "delete from "+tablename+" where "+coloum+" = "+"'"+value+"'";
        return preparedStatement(sql);
    }
    @Override
    public boolean delete(T getEntity) {
        try{
            String sql = "delete from "+tablename+" where "+DBCore.getPkAndVal(DBCore.getDBTable(entityclass));
            return preparedStatement(sql);
        }catch (Exception ex) {
            return false;
        }
    }
    @Override
    public ArrayList<T> getAll() {
        ArrayList<T> tArrayList=new ArrayList<>();
        String selectQuery = "SELECT  * FROM " +  DB_TABLE.properties;
        Statement  statement=null;
        Connection con=null;
        ResultSet rs=null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con=DriverManager.getConnection("jdbc:mysql://"+dbConfig.getHost()+":"+dbConfig.getPort()+"/"+databasename,dbConfig.getUsername(),dbConfig.getPassword());
            statement = con.createStatement();
            rs = statement.executeQuery(selectQuery);
            while (rs.next()) {
                entity = entity_temp;
                T tentity = Entity.createInstance(entityclass);
                Field[] declaredFields = tentity.getClass().getDeclaredFields();
                for (Field field : declaredFields) {
                    field.setAccessible(true);
                    for (DBProperty DBProperty :  DB_TABLE.properties) {
                        if (DBProperty._fieldName != null) {
                            if (DBProperty._fieldName.realname.equals(field.getName())) {
                                try {
                                    Object o=DBCore.CastType(field.getType().getSimpleName(), rs.getString(DBProperty._fieldName.mapname));
                                    field.set(tentity, o);
                                } catch (Exception esx) {
                                    try {
                                        field.set(tentity, null);
                                    } catch (Exception ex) {
                                    }
                                }
                            }
                        }
                    }
                }
                tArrayList.add(tentity);
                entity = null;
        }
        }catch (Exception e){
            e.printStackTrace();
        }
        return tArrayList;
    }

    @Override
    public ArrayList<T> sqlSelect(String sql) {
        if(dbConfig.getViewQuery())
            System.out.println(sql);

        ArrayList<T> tArrayList=new ArrayList<>();
        String selectQuery = sql;
        Statement  statement=null;
        Connection con=null;
        ResultSet rs=null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con=DriverManager.getConnection("jdbc:mysql://"+dbConfig.getHost()+":"+dbConfig.getPort()+"/"+databasename,dbConfig.getUsername(),dbConfig.getPassword());
            statement = con.createStatement();
            rs = statement.executeQuery(selectQuery);
            while (rs.next()) {
                entity = entity_temp;
                T tentity = Entity.createInstance(entityclass);
                Field[] declaredFields = tentity.getClass().getDeclaredFields();
                for (Field field : declaredFields) {
                    field.setAccessible(true);
                    for (DBProperty DBProperty :  DB_TABLE.properties) {
                        if (DBProperty._fieldName != null) {
                            if (DBProperty._fieldName.realname.equals(field.getName())) {
                                try {
                                    Object o=DBCore.CastType(field.getType().getSimpleName(), rs.getString(DBProperty._fieldName.mapname));
                                    field.set(tentity, o);
                                } catch (Exception esx) {
                                    try {
                                        field.set(tentity, null);
                                    } catch (Exception ex) {
                                    }
                                }
                            }
                        }
                    }
                }
                tArrayList.add(tentity);
                entity = null;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return tArrayList;
    }

    @Override
    public T getByPK(Object pk) {
        for (DBProperty DBProperty : DB_TABLE.properties) {
            if (DBProperty._fieldName != null) {
                if (DBProperty._pk && DBProperty._autoincrement) {
                    pk_id = DBProperty._fieldName.mapname;
                }
            }
        }
        T tentity = Entity.createInstance(entityclass);
        Statement  statement=null;
        Connection con=null;
        ResultSet rs=null;
        try {
            String selectQuery = "SELECT  * FROM " + tablename+" where "+pk_id+"='"+pk+"'";
            if(dbConfig.getViewQuery())
                System.out.println(selectQuery);
            Class.forName("com.mysql.jdbc.Driver");
            con=DriverManager.getConnection("jdbc:mysql://"+dbConfig.getHost()+":"+dbConfig.getPort()+"/"+databasename,dbConfig.getUsername(),dbConfig.getPassword());
            statement = con.createStatement();
            rs = statement.executeQuery(selectQuery);
            while (rs.next()) {
                Field[] declaredFields = tentity.getClass().getDeclaredFields();
                for(Field field:declaredFields)
                {
                    field.setAccessible(true);
                    for(DBProperty DBProperty : DB_TABLE.properties) {
                        if(DBProperty._fieldName!=null) {
                            if (DBProperty._fieldName.realname.equals(field.getName())) {
                                for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
                                    if (DBProperty._fieldName.realname.equals(field.getName())) {
                                        try {
                                            field.set(tentity, DBCore.CastType(field.getType().getSimpleName(), rs.getString(DBProperty._fieldName.mapname)));
                                        } catch (Exception esx) {
                                            try {
                                                field.set(tentity, null);
                                            } catch (Exception ex) {
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                return tentity;

            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return tentity;
    }

    @Override
    public T getFirstOrDefault(String where,String select) {
        T tentity = Entity.createInstance(entityclass);
        Statement  statement=null;
        Connection con=null;
        ResultSet rs=null;
        try {
            String selectQuery = "SELECT  "+select+" FROM " + tablename+" where "+where;
            if(dbConfig.getViewQuery())
                System.out.println(selectQuery);
            Class.forName("com.mysql.jdbc.Driver");
            con=DriverManager.getConnection("jdbc:mysql://"+dbConfig.getHost()+":"+dbConfig.getPort()+"/"+databasename,dbConfig.getUsername(),dbConfig.getPassword());
            statement = con.createStatement();
            rs = statement.executeQuery(selectQuery);
            while (rs.next()) {
                Field[] declaredFields = tentity.getClass().getDeclaredFields();
                for(Field field:declaredFields)
                {
                    field.setAccessible(true);
                    for(DBProperty DBProperty : DB_TABLE.properties) {
                        if(DBProperty._fieldName!=null) {
                            if (DBProperty._fieldName.realname.equals(field.getName())) {
                                for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
                                    if (DBProperty._fieldName.realname.equals(field.getName())) {
                                        try {
                                            field.set(tentity, DBCore.CastType(field.getType().getSimpleName(), rs.getString(DBProperty._fieldName.mapname)));
                                        } catch (Exception esx) {
                                            try {
                                                field.set(tentity, null);
                                            } catch (Exception ex) {
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                return tentity;

            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return tentity;
    }

    @Override
    public boolean update(T getEntity){
        boolean notnull=false;
        String pk="";
        ArrayList<String> set_value=new ArrayList<>() ;

        for(Field field:getEntity.getClass().getDeclaredFields())
        {
            field.setAccessible(true);
            for(DBProperty DBProperty : DB_TABLE.properties){
                if(DBProperty._fieldName!=null) {
                    if (DBProperty._fieldName.realname.equals(field.getName())) {
                        try {
                            if(DBProperty._pk){
                                pk = DBProperty._fieldName.mapname+" = '"+((String.valueOf(field.get(getEntity)) != null ? field.get(getEntity) + "" : "NULL").replace("'", "''"))+"'";
                            }
                            if(!DBProperty._autoincrement) {
                                if(field.get(getEntity)!=null) {
                                    set_value.add(DBProperty._fieldName.mapname + " = '" + ((String.valueOf(field.get(getEntity)) != null ? field.get(getEntity) + "" : "").replace("'", "''")) + "'");
                                }else{
                                    if(DBProperty._notNull){
                                        notnull=true;
                                        Error(DBProperty._fieldName.realname+" null value not support");
                                    }
                                }
                            }
                        } catch (Exception esx) {
                            esx.printStackTrace();
                        }
                    }
                }
            }
        }

        boolean ret=false;
        if(!notnull) {
            String sql = "UPDATE " + tablename + " SET " + String.join(",", set_value) + " WHERE " + pk;
            ;

            PreparedStatement preparedStatement = null;
            Connection con = null;
            try {
                Class.forName("com.mysql.jdbc.Driver");
                con = DriverManager.getConnection("jdbc:mysql://" + dbConfig.getHost() + ":" + dbConfig.getPort() + "/" + databasename, dbConfig.getUsername(), dbConfig.getPassword());
                preparedStatement = con.prepareStatement(sql);
                preparedStatement.execute();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (con != null) {
                        if (preparedStatement != null) {
                            ret = true;
                            con.close();
                        }
                    }
                } catch (SQLException se) {
                    se.printStackTrace();
                }
            }
        }
        return ret;
    }

    @Override
    public boolean preparedStatement(String sql){
        if(dbConfig.getViewQuery())
            System.out.println(sql);
        boolean ret=false;
        PreparedStatement preparedStatement=null;
        Connection con=null;
        try{
            Class.forName("com.mysql.jdbc.Driver");
            con=DriverManager.getConnection("jdbc:mysql://"+dbConfig.getHost()+":"+dbConfig.getPort()+"/"+databasename,dbConfig.getUsername(),dbConfig.getPassword());
            preparedStatement =con.prepareStatement(sql);
            preparedStatement.execute();
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            try{
                if(con!=null) {
                    if(preparedStatement!=null) {
                        ret=true;
                        con.close();
                    }
                }
            }catch(SQLException se){
                se.printStackTrace();
            }
        }
        return ret;
    }

    @Override
    public boolean statement(String sql){
        if(dbConfig.getViewQuery())
            System.out.println(sql);
        boolean ret=false;
        Statement stmt=null;
        Connection con=null;
        try{
            Class.forName("com.mysql.jdbc.Driver");
            con=DriverManager.getConnection("jdbc:mysql://"+dbConfig.getHost()+":"+dbConfig.getPort()+"/"+databasename,dbConfig.getUsername(),dbConfig.getPassword());
            stmt =con.createStatement();
            stmt.execute(sql);
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            try{
                if(con!=null) {
                    if(stmt!=null) {
                        ret=true;
                        con.close();
                    }
                }
            }catch(SQLException se){
                se.printStackTrace();
            }
        }
        return ret;
    }

    public void Error(String mess){
        try {
            throw new NotNullException(mess);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }*/
}
