package com.github.vikramhalder.Example;

import com.github.vikramhalder.JavaORM.DB;
import com.github.vikramhalder.JavaORM.IDB;

import java.util.ArrayList;
import java.util.Date;

public class Hello{
    public static void main(String[] args){
        IDB<Ent> entDB=new DB<Ent>(Ent.class,"java_orm","Ent").Config(DBConn.dbConfig());

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