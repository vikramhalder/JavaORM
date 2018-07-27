package com.github.vikramhalder.Example;

import com.github.vikramhalder.JavaORM.Annotations.AutoIncrement;
import com.github.vikramhalder.JavaORM.Annotations.Coloum;
import com.github.vikramhalder.JavaORM.Annotations.ForeignKey;
import com.github.vikramhalder.JavaORM.Annotations.PK;


public class Person {
    @AutoIncrement
    @PK
    private int id;

    private String name;
    @ForeignKey
    @Coloum("dbconn")
    private DBConn area;







    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DBConn getArea() {
        return area;
    }

    public void setArea(DBConn area) {
        this.area = area;
    }
}

