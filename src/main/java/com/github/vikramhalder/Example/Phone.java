package com.github.vikramhalder.Example;

import com.github.vikramhalder.JavaORM.Annotations.*;

//@StorageEngine("MyISAM")
public class Phone {
    @AutoIncrement
    @PK
    private int id;
    @NotNull
    private String number;
    @ForeignKey
    private User user;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

