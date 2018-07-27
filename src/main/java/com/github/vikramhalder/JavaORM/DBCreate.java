package com.github.vikramhalder.JavaORM;

import com.github.vikramhalder.JavaORM.Interface.IDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class DBCreate {
    protected static boolean tableCreate(String databasename,TableValue tableValue,DBConfig dbConfig){

        ArrayList<String> table_all_line=new ArrayList<>();
        if(tableValue.primary_key!=null){
            if(tableValue.primary_key._autoincrement)
                table_all_line.add(tableValue.primary_key._fieldname.mapname + " int NOT NULL AUTO_INCREMENT\n");
            else
                table_all_line.add(tableValue.primary_key._fieldname.mapname + " "+DBCore.ColoumType(tableValue.primary_key._type,tableValue.primary_key._type)+" NOT NULL\n");
        }
        if(tableValue.no_annotations.size()>0){
            for(Item item : tableValue.no_annotations){
                if(item._notnull)
                    if(item._default!=null)
                        table_all_line.add(item._fieldname.mapname + " "+DBCore.ColoumType(item._type,item._columetype)+" DEFAULT "+DBCore.defaultValue(item._type,item._default)+"\n");
                    else
                        table_all_line.add(item._fieldname.mapname + " "+DBCore.ColoumType(item._type,item._columetype)+"  NOT NULL\n");
                else
                if(item._default!=null)
                    table_all_line.add(item._fieldname.mapname + " "+DBCore.ColoumType(item._type,item._columetype)+" DEFAULT "+DBCore.defaultValue(item._type,item._default)+"\n");
                else
                    table_all_line.add(item._fieldname.mapname + " "+DBCore.ColoumType(item._type,item._columetype)+"\n");
            }
        }
        if(tableValue.unique.size()>0){
            for(CustomClass customClass : tableValue.unique){
                Item item=customClass.items;
                if(item._notnull)
                    if(item._default!=null)
                        table_all_line.add(item._fieldname.mapname + " "+DBCore.ColoumType(item._type,item._columetype)+" DEFAULT "+DBCore.defaultValue(item._type,item._default)+"\n");
                    else
                        table_all_line.add(item._fieldname.mapname + " "+DBCore.ColoumType(item._type,item._columetype)+" NOT NULL\n");
                else
                if(item._default!=null)
                    table_all_line.add(item._fieldname.mapname + " "+DBCore.ColoumType(item._type,item._columetype)+" DEFAULT "+DBCore.defaultValue(item._type,item._default)+"\n");
                else
                    table_all_line.add(item._fieldname.mapname + " "+DBCore.ColoumType(item._type,item._columetype)+"\n");
            }
        }
        if(tableValue.one_to_one.size()>0){
            for(CustomClass customClass : tableValue.one_to_one){
                Item item=customClass.items;
                if(item._notnull)
                    if(item._default!=null)
                        table_all_line.add(item._fieldname.mapname + " "+DBCore.ColoumType(item._type,item._columetype)+" DEFAULT "+DBCore.defaultValue(item._type,item._default)+"\n");
                    else
                        table_all_line.add(item._fieldname.mapname + " "+DBCore.ColoumType(item._type,item._columetype)+" NOT NULL\n");
                else
                if(item._default!=null)
                    table_all_line.add(item._fieldname.mapname + " "+DBCore.ColoumType(item._type,item._columetype)+" DEFAULT "+DBCore.defaultValue(item._type,item._default)+"\n");
                else
                    table_all_line.add(item._fieldname.mapname + " "+DBCore.ColoumType(item._type,item._columetype)+"\n");
            }
        }
        if(tableValue.one_to_many.size()>0){
            for(CustomClass customClass : tableValue.one_to_many){
                Item item=customClass.items;
                if(item._notnull)
                    if(item._default!=null)
                        table_all_line.add(item._fieldname.mapname + " "+DBCore.ColoumType(item._type,item._columetype)+" DEFAULT "+DBCore.defaultValue(item._type,item._default)+"\n");
                    else
                        table_all_line.add(item._fieldname.mapname + " "+DBCore.ColoumType(item._type,item._columetype)+" NOT NULL\n");
                else
                if(item._default!=null)
                    table_all_line.add(item._fieldname.mapname + " "+DBCore.ColoumType(item._type,item._columetype)+" DEFAULT "+DBCore.defaultValue(item._type,item._default)+"\n");
                else
                    table_all_line.add(item._fieldname.mapname + " "+DBCore.ColoumType(item._type,item._columetype)+"\n");
            }
        }
        if(tableValue.many_to_many.size()>0){
            for(CustomClass customClass : tableValue.many_to_many){
                Item item=customClass.items;
                if(item._notnull)
                    if(item._default!=null)
                        table_all_line.add(item._fieldname.mapname + " "+DBCore.ColoumType(item._type,item._columetype)+" DEFAULT "+DBCore.defaultValue(item._type,item._default)+"\n");
                    else
                        table_all_line.add(item._fieldname.mapname + " "+DBCore.ColoumType(item._type,item._columetype)+" NOT NULL\n");
                else
                if(item._default!=null)
                    table_all_line.add(item._fieldname.mapname + " "+DBCore.ColoumType(item._type,item._columetype)+" DEFAULT "+DBCore.defaultValue(item._type,item._default)+"\n");
                else
                    table_all_line.add(item._fieldname.mapname + " "+DBCore.ColoumType(item._type,item._columetype)+"\n");
            }
        }
        if(tableValue.foreign_key.size()>0){
            for(CustomClass customClass : tableValue.foreign_key) {
                TableValue tv = customClass.table_value;
                Item item = customClass.items;
                if (item._notnull)
                    if (item._default != null)
                        table_all_line.add( item._fieldname.mapname + " " + DBCore.ColoumType(tv.primary_key._type,tv.primary_key._columetype) + " DEFAULT '" + item._default + "'\n");
                    else
                        table_all_line.add( item._fieldname.mapname + " " + DBCore.ColoumType(tv.primary_key._type,tv.primary_key._columetype) + " NOT NULL\n");
                else
                if(item._default!=null)
                    table_all_line.add(item._fieldname.mapname + " "+DBCore.ColoumType(tv.primary_key._type,tv.primary_key._columetype)+" DEFAULT "+DBCore.defaultValue(item._type,item._default)+"\n");
                else
                    table_all_line.add(item._fieldname.mapname + " "+DBCore.ColoumType(tv.primary_key._type,tv.primary_key._columetype)+"\n");
            }
        }
        if(tableValue.foreign_key.size()>0){
            for(CustomClass customClass : tableValue.foreign_key){
                TableValue tv=customClass.table_value;
                Item item=customClass.items;
                IDB<String> d=new DB<String>(item._classobject,databasename).Config(dbConfig);;
                d.onCreateTable();
                table_all_line.add( "FOREIGN KEY ("+item._fieldname.mapname+") REFERENCES "+tv.table_name.mapname+"("+tv.primary_key._fieldname.mapname+")\n");
            }
        }
        if(tableValue.primary_key!=null){
            table_all_line.add("PRIMARY KEY ("+tableValue.primary_key._fieldname.mapname+")\n");
        }
        if(tableValue.unique.size()>0){
            ArrayList<String> unique=new ArrayList<>();
            for(CustomClass customClass : tableValue.unique){
                TableValue tv=customClass.table_value;
                Item item=customClass.items;
                unique.add(item._fieldname.mapname);

            }
            table_all_line.add( "UNIQUE KEY ("+String.join(",",unique)+")\n");
        }


        String CREATE_CONTACTS_TABLE=
                "CREATE TABLE IF NOT EXISTS " + tableValue.table_name.mapname + "(\n"+ String.join(",",table_all_line) +");\n\n\n\n\n";

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
        return true;
    }

    public static String replaceLastChar(String old_string,String char_value) {
        String input="wanting to replace last comma to dot,and get the output,";
        String replaceWith = "";
        Pattern p = Pattern.compile(char_value+"$");
        Matcher m = p.matcher(input);
        String output = m.replaceAll(replaceWith);
        return old_string;
    }
}
