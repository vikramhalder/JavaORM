package com.github.vikramhalder.Example;

import com.github.vikramhalder.JavaORM.DB;
import com.github.vikramhalder.JavaORM.DBInsert;
import com.github.vikramhalder.JavaORM.DBUser;
import com.github.vikramhalder.JavaORM.Interface.IDB;
import com.github.vikramhalder.JavaORM.Interface.IDBUser;

public class Hello{
    public static void main(String[] args){
        IDBUser idbUser=new DBUser();
        IDB<User> userIDB=new DB<User>(new User(),"java_orm").Config(DBConn.dbConfig());
        IDB<Phone> phoneIDB=new DB<Phone>(new Phone(),"java_orm").Config(DBConn.dbConfig());
        IDB<Country> countryIDB=new DB<Country>(new Country(),"java_orm").Config(DBConn.dbConfig());

//        System.out.println(idbUser.onCreateDB(DBConn.dbConfig(),"java_orm"));
//        System.out.println(userIDB.onCreateTable());
//        System.out.println(phoneIDB.onCreateTable());
        System.out.println(countryIDB.onCreateTable());



        User user=new User();
        //user.setId(3);
        user.setName("dip");
        user.setEmail("di/p@g.c");

        Phone phone=new Phone();
        //phone.setId(5);
        phone.setNumber("0178965523");
        phone.setUser(user);
//
        Country country=new Country();
        country.setPhone(phone);
        country.setUser(user);
//
        DBInsert dbInsert= countryIDB.insert(country);
//        System.out.println(dbInsert.isOk());
//        System.out.println(dbInsert.getId());
//        System.out.println(dbInsert.getMessage());

//        entDB.onCreateDB("java_orm");

//        boolean create=entDB.onCreateTable();
//        System.out.println(create);

//        User e=new User();
//        e.setName("2018-02-02 18:03:03");
//        e.setEmail("vikramgs@gmaild.com");
//        User e1=new User();
//        e1.setEmail("vikram1s@gmaild.com");
//        ArrayList<User> entArrayList=new ArrayList<>();
//        entArrayList.add(e);
//        entArrayList.add(e1);
//        boolean insert=entDB.insert(e);
        //System.out.println(entArrayList);

//        ArrayList<User> entArrayList=entDB.sqlSelect("select id,name from ent where id=1");
//        for (User ent:entArrayList){
//            System.out.println(ent.getId()+"/"+ent.getName()+"/"+ent.getEmail());
//        }

//        User ent=entDB.getFirstOrDefault("id=26","*");
//        System.out.println(ent.getId()+"/"+ent.getName()+"/"+ent.getEmail());
//        ent.setName("NULL");
//        ((DB<User>) entDB).update(ent);

    }
}