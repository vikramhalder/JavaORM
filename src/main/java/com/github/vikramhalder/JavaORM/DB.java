package  com.github.vikramhalder.JavaORM;

import com.github.vikramhalder.JavaORM.Annotations.Coloum;
import com.github.vikramhalder.JavaORM.Annotations.PK;
import com.github.vikramhalder.JavaORM.Error.NotNullException;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;

public class DB<T> implements IDB<T> {
    private static final int DATABASE_VERSION = 1;
    private String DB_CONTACTS ;
    private String TABLE_CONTACTS ;
    private ArrayList<DbField> KEYS;
    private Class<T> entityclass;
    private T entity,entity_temp;
    private String pk_id=null;
    private DBConfig dbConfig;

    public DB(Class<T> entityclass, String DB_CONTACTS, String TABLE_CONTACTS) {
        this.DB_CONTACTS= DB_CONTACTS;
        this.TABLE_CONTACTS= TABLE_CONTACTS;
        this.KEYS= DbCore.getFieldsName(entityclass);
        this.entityclass=entityclass;
        try {
            this.entity = entityclass.newInstance();
            this.entity_temp=entity;
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public DB Config(DBConfig dbConfig){
        this.dbConfig=dbConfig;
        return this;
    }
    @Override
    public int onCreateDB(String db_name) {
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
    @Override
    public boolean onCreateTable(){
        return tableCreate("","");
    }
    private boolean tableCreate(String db_name,String table_name){
        ArrayList<String> unique=new ArrayList<>();
        ArrayList<String> coloums=new ArrayList<>();
        String temp0="";
        for (DbField dbField:KEYS){
            if(dbField.fieldName!=null) {
                if(dbField.pk && dbField.autoincrement) {
                    pk_id=dbField.fieldName.mapname;
                    coloums.add(dbField.fieldName.mapname + " int NOT NULL AUTO_INCREMENT");
                }else if(dbField.pk) {
                    pk_id=dbField.fieldName.mapname;
                    coloums.add(dbField.fieldName.mapname + " varchar(255)");
                }else if(dbField.autoincrement) {
                    coloums.add(dbField.fieldName.mapname + " int NOT NULL AUTO_INCREMENT");
                }else if(dbField.qunique) {
                    unique.add(dbField.fieldName.mapname);
                    if(dbField.type.equals("int")) {
                        if (dbField.notNull) {
                            coloums.add(dbField.fieldName.mapname + " int NOT NULL");
                        } else {
                            coloums.add(dbField.fieldName.mapname + " int");
                        }
                    }
                    else {
                        if (dbField.notNull) {
                            coloums.add(dbField.fieldName.mapname + " varchar(255) NOT NULL");
                        } else {
                            coloums.add(dbField.fieldName.mapname + " varchar(255)");
                        }
                    }
                }else {
                    if(dbField.type.equals("int")) {
                        if (dbField.notNull) {
                            coloums.add(dbField.fieldName.mapname + " int NOT NULL");
                        } else {
                            coloums.add(dbField.fieldName.mapname + " int");
                        }
                    }
                    else {
                        if (dbField.notNull) {
                            coloums.add(dbField.fieldName.mapname + " varchar(255) NOT NULL");
                        } else {
                            coloums.add(dbField.fieldName.mapname + " varchar(255)");
                        }
                    }
                }
            }
        }
        String CREATE_CONTACTS_TABLE = "";
        if(pk_id!=null){
            if(unique.size()>0){
                CREATE_CONTACTS_TABLE=
                        "CREATE TABLE IF NOT EXISTS " + TABLE_CONTACTS + "("+
                                "   "+String.join(",",coloums)+","+
                                "   PRIMARY KEY ("+pk_id+"),"+
                                "   UNIQUE KEY ("+String.join(",",unique)+")" +
                                ");";
            }else {
                CREATE_CONTACTS_TABLE=
                        "CREATE TABLE IF NOT EXISTS " + TABLE_CONTACTS + "("+
                                "   "+String.join(",",coloums)+","+
                                "   PRIMARY KEY ("+pk_id+")"+
                                ");";
            }
        }else {
            if(unique.size()>0){
                CREATE_CONTACTS_TABLE=
                        "CREATE TABLE IF NOT EXISTS " + TABLE_CONTACTS + "("+
                                "   "+String.join(",",coloums)+","+
                                "   UNIQUE KEY ("+String.join(",",unique)+")" +
                                ");";
            }else {
                CREATE_CONTACTS_TABLE=
                        "CREATE TABLE IF NOT EXISTS " + TABLE_CONTACTS + "("+
                                "   "+String.join(",",coloums)+","+
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
            con=DriverManager.getConnection("jdbc:mysql://"+dbConfig.getHost()+":"+dbConfig.getPort()+"/"+DB_CONTACTS,dbConfig.getUsername(),dbConfig.getPassword());
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
    public boolean insert(T getEntity) {
        boolean notnull=false;
        ArrayList<String> coloums=new ArrayList<>();
        ArrayList<String> values=new ArrayList<>();

        for(Field field:getEntity.getClass().getDeclaredFields())
        {
            field.setAccessible(true);
            for(DbField dbField:KEYS){
                if(dbField.fieldName!=null) {
                    if (dbField.fieldName.realname.equals(field.getName())) {
                        try {
                            if(!dbField.autoincrement) {
                                if(field.get(getEntity)!=null) {
                                    coloums.add(dbField.fieldName.mapname);
                                    values.add("'" + (String.valueOf(field.get(getEntity)) != null ? field.get(getEntity) + "" : "NULL").replace("'", "''") + "'");
                                }else{
                                    if(dbField.notNull){
                                        notnull=true;
                                        throw new NotNullException(dbField.fieldName.realname+"  null value not support");
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
            String sql = "insert into " + TABLE_CONTACTS + "(" + String.join(",", coloums) + ") values " + "(" + String.join(",", values) + ")";
            sql = sql.replace("'null'", "NULL");
            if (dbConfig.getViewQuery())
                System.out.println(sql);

            PreparedStatement preparedStatement = null;
            Connection con = null;
            try {
                Class.forName("com.mysql.jdbc.Driver");
                con = DriverManager.getConnection("jdbc:mysql://" + dbConfig.getHost() + ":" + dbConfig.getPort() + "/" + DB_CONTACTS, dbConfig.getUsername(), dbConfig.getPassword());
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
                for (DbField dbField : KEYS) {
                    if (dbField.fieldName != null) {
                        if (dbField.fieldName.realname.equals(field.getName())) {
                            try {
                                if (!dbField.autoincrement) {
                                    if(field.get(getEntity)!=null) {
                                        if (count == 0) {
                                            coloums.add(dbField.fieldName.mapname);
                                        }
                                        values.add("'" + (String.valueOf(field.get(getEntity)) != null ? field.get(getEntity) + "" : "NULL").replace("'", "''") + "'");
                                        insert++;
                                    }else{
                                        if(dbField.notNull){
                                            notnull=true;
                                            Error(dbField.fieldName.realname+" null value not support");
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
            String sql = "insert into " + TABLE_CONTACTS + "(" + String.join(",", coloums) + ") values " + String.join(",", values_list);
            sql = sql.replace("'null'", "NULL");
            if (dbConfig.getViewQuery())
                System.out.println(sql);

            PreparedStatement preparedStatement = null;
            Connection con = null;
            try {
                Class.forName("com.mysql.jdbc.Driver");
                con = DriverManager.getConnection("jdbc:mysql://" + dbConfig.getHost() + ":" + dbConfig.getPort() + "/" + DB_CONTACTS, dbConfig.getUsername(), dbConfig.getPassword());
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
    public ArrayList<T> getAll() {
        ArrayList<T> tArrayList=new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS;
        Statement  statement=null;
        Connection con=null;
        ResultSet rs=null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con=DriverManager.getConnection("jdbc:mysql://"+dbConfig.getHost()+":"+dbConfig.getPort()+"/"+DB_CONTACTS,dbConfig.getUsername(),dbConfig.getPassword());
            statement = con.createStatement();
            rs = statement.executeQuery(selectQuery);
            while (rs.next()) {
                entity = entity_temp;
                T tentity = Entity.createInstance(entityclass);
                Field[] declaredFields = tentity.getClass().getDeclaredFields();
                for (Field field : declaredFields) {
                    field.setAccessible(true);
                    for (DbField dbField : KEYS) {
                        if (dbField.fieldName != null) {
                            if (dbField.fieldName.realname.equals(field.getName())) {
                                try {
                                    Object o=DbCore.CastType(field.getType().getSimpleName(), rs.getString(dbField.fieldName.mapname));
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
            con=DriverManager.getConnection("jdbc:mysql://"+dbConfig.getHost()+":"+dbConfig.getPort()+"/"+DB_CONTACTS,dbConfig.getUsername(),dbConfig.getPassword());
            statement = con.createStatement();
            rs = statement.executeQuery(selectQuery);
            while (rs.next()) {
                entity = entity_temp;
                T tentity = Entity.createInstance(entityclass);
                Field[] declaredFields = tentity.getClass().getDeclaredFields();
                for (Field field : declaredFields) {
                    field.setAccessible(true);
                    for (DbField dbField : KEYS) {
                        if (dbField.fieldName != null) {
                            if (dbField.fieldName.realname.equals(field.getName())) {
                                try {
                                    Object o=DbCore.CastType(field.getType().getSimpleName(), rs.getString(dbField.fieldName.mapname));
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
        for (DbField dbField:KEYS) {
            if (dbField.fieldName != null) {
                if (dbField.pk && dbField.autoincrement) {
                    pk_id = dbField.fieldName.mapname;
                }
            }
        }
        T tentity = Entity.createInstance(entityclass);
        Statement  statement=null;
        Connection con=null;
        ResultSet rs=null;
        try {
            String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS+" where "+pk_id+"='"+pk+"'";
            if(dbConfig.getViewQuery())
                System.out.println(selectQuery);
            Class.forName("com.mysql.jdbc.Driver");
            con=DriverManager.getConnection("jdbc:mysql://"+dbConfig.getHost()+":"+dbConfig.getPort()+"/"+DB_CONTACTS,dbConfig.getUsername(),dbConfig.getPassword());
            statement = con.createStatement();
            rs = statement.executeQuery(selectQuery);
            while (rs.next()) {
                Field[] declaredFields = tentity.getClass().getDeclaredFields();
                for(Field field:declaredFields)
                {
                    field.setAccessible(true);
                    for(DbField dbField:KEYS) {
                        if(dbField.fieldName!=null) {
                            if (dbField.fieldName.realname.equals(field.getName())) {
                                for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
                                    if (dbField.fieldName.realname.equals(field.getName())) {
                                        try {
                                            field.set(tentity, DbCore.CastType(field.getType().getSimpleName(), rs.getString(dbField.fieldName.mapname)));
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
            String selectQuery = "SELECT  "+select+" FROM " + TABLE_CONTACTS+" where "+where;
            if(dbConfig.getViewQuery())
                System.out.println(selectQuery);
            Class.forName("com.mysql.jdbc.Driver");
            con=DriverManager.getConnection("jdbc:mysql://"+dbConfig.getHost()+":"+dbConfig.getPort()+"/"+DB_CONTACTS,dbConfig.getUsername(),dbConfig.getPassword());
            statement = con.createStatement();
            rs = statement.executeQuery(selectQuery);
            while (rs.next()) {
                Field[] declaredFields = tentity.getClass().getDeclaredFields();
                for(Field field:declaredFields)
                {
                    field.setAccessible(true);
                    for(DbField dbField:KEYS) {
                        if(dbField.fieldName!=null) {
                            if (dbField.fieldName.realname.equals(field.getName())) {
                                for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
                                    if (dbField.fieldName.realname.equals(field.getName())) {
                                        try {
                                            field.set(tentity, DbCore.CastType(field.getType().getSimpleName(), rs.getString(dbField.fieldName.mapname)));
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
            for(DbField dbField:KEYS){
                if(dbField.fieldName!=null) {
                    if (dbField.fieldName.realname.equals(field.getName())) {
                        try {
                            if(dbField.pk){
                                pk = dbField.fieldName.mapname+" = '"+((String.valueOf(field.get(getEntity)) != null ? field.get(getEntity) + "" : "NULL").replace("'", "''"))+"'";
                            }
                            if(!dbField.autoincrement) {
                                if(field.get(getEntity)!=null) {
                                    set_value.add(dbField.fieldName.mapname + " = '" + ((String.valueOf(field.get(getEntity)) != null ? field.get(getEntity) + "" : "").replace("'", "''")) + "'");
                                }else{
                                    if(dbField.notNull){
                                        notnull=true;
                                        Error(dbField.fieldName.realname+" null value not support");
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
            String sql = "UPDATE " + TABLE_CONTACTS + " SET " + String.join(",", set_value) + " WHERE " + pk;
            ;

            PreparedStatement preparedStatement = null;
            Connection con = null;
            try {
                Class.forName("com.mysql.jdbc.Driver");
                con = DriverManager.getConnection("jdbc:mysql://" + dbConfig.getHost() + ":" + dbConfig.getPort() + "/" + DB_CONTACTS, dbConfig.getUsername(), dbConfig.getPassword());
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
            con=DriverManager.getConnection("jdbc:mysql://"+dbConfig.getHost()+":"+dbConfig.getPort()+"/"+DB_CONTACTS,dbConfig.getUsername(),dbConfig.getPassword());
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
            con=DriverManager.getConnection("jdbc:mysql://"+dbConfig.getHost()+":"+dbConfig.getPort()+"/"+DB_CONTACTS,dbConfig.getUsername(),dbConfig.getPassword());
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
    }
}
