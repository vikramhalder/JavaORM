package com.github.vikramhalder.Example;

import com.github.vikramhalder.JavaORM.Annotations.*;

import java.util.Date;

//@StorageEngine("MyISAM")
public class User {
    @AutoIncrement
    @PK
    @Coloum("id")
    private int ids;
    @NotNull
    private String name;
    @NotNull
    @Unique
    private String email;

    @ColumnType("timestamp")
    @Default("CURRENT_TIMESTAMP")
    private Date date;

    public int getId() {
        return ids;
    }

    public void setId(int id) {
        this.ids = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
