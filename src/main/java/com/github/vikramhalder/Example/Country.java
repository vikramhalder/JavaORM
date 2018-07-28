package com.github.vikramhalder.Example;

import com.github.vikramhalder.JavaORM.Annotations.AutoIncrement;
import com.github.vikramhalder.JavaORM.Annotations.ForeignKey;
import com.github.vikramhalder.JavaORM.Annotations.PK;
import com.github.vikramhalder.JavaORM.Annotations.StorageEngine;

//@StorageEngine("MyISAM")
public class Country {
    @PK
    @AutoIncrement
    private int id;
    @ForeignKey
    private Phone phone;
    @ForeignKey
    private User user;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Phone getPhone() {
        return phone;
    }

    public void setPhone(Phone phone) {
        this.phone = phone;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
