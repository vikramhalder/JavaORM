package com.github.vikramhalder.Example;

import com.github.vikramhalder.JavaORM.DB;
import com.github.vikramhalder.JavaORM.DBConfig;
import com.github.vikramhalder.JavaORM.DBInsert;
import com.github.vikramhalder.JavaORM.DBUser;
import com.github.vikramhalder.JavaORM.Interface.IDB;
import com.github.vikramhalder.JavaORM.Interface.IDBUser;

public class Hello{
    public static void main(String[] args){
        IDBUser idbUser=new DBUser();
        IDB<Ent> entDB=new DB<Ent>(Ent.class,"java_orm").Config(DBConn.dbConfig());

//        System.out.println(idbUser.onCreateDB(DBConn.dbConfig(),"java_orm"));
        System.out.println(entDB.onCreateTable());

//        Ent e=new Ent();
//        e.setName("dip");
//        e.setEmail("di/p@g.c");
//
//        Person p=new Person();
//        p.setName("Vik");
//        DBInsert dbInsert= entDB.insert(e);
//
//        System.out.println(dbInsert.isOk());
//        System.out.println(dbInsert.getId());
//        System.out.println(dbInsert.getMessage());

//        entDB.onCreateDB("java_orm");

//        boolean create=entDB.onCreateTable();
//        System.out.println(create);

//        Ent e=new Ent();
//        e.setName("2018-02-02 18:03:03");
//        e.setEmail("vikramgs@gmaild.com");
//        Ent e1=new Ent();
//        e1.setEmail("vikram1s@gmaild.com");
//        ArrayList<Ent> entArrayList=new ArrayList<>();
//        entArrayList.add(e);
//        entArrayList.add(e1);
//        boolean insert=entDB.insert(e);
        //System.out.println(entArrayList);

//        ArrayList<Ent> entArrayList=entDB.sqlSelect("select id,name from ent where id=1");
//        for (Ent ent:entArrayList){
//            System.out.println(ent.getId()+"/"+ent.getName()+"/"+ent.getEmail());
//        }

//        Ent ent=entDB.getFirstOrDefault("id=26","*");
//        System.out.println(ent.getId()+"/"+ent.getName()+"/"+ent.getEmail());
//        ent.setName("NULL");
//        ((DB<Ent>) entDB).update(ent);

    }
}