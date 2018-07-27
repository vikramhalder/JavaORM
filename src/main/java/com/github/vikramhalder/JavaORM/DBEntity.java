package com.github.vikramhalder.JavaORM;

import java.util.ArrayList;

class Entity {
    protected static <T> T createInstance(Class clazz){
        T t = null;
        try {
            t = (T) clazz.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return t;
    }
}

class DBProperty {
    public String _type;
    public FieldName _fieldName;
    public Object _value;
    public String _modifier;
    public boolean _pk=false;
    public String _default;
    public boolean _isDefault=false;
    public boolean _autoincrement=false;
    public boolean _qunique=false;
    public boolean _notNull=false;
    public boolean _isForeignkey=false;
    public DBTable _foreignkey;
}

class FieldName{
    public String realname;
    public String mapname;
}

class DBTable{
    public Class clazz;
    public FieldName classname;
    public ArrayList<DBProperty> properties;
    public String _modifier;
}

class TableValue{
    public String storage_engine;
    public FieldName table_name;
    public Class class_object;
    public Item primary_key;
    public ArrayList<CustomClass> unique;
    public ArrayList<CustomClass> foreign_key;
    public ArrayList<Item> no_annotations;
    public ArrayList<CustomClass> one_to_one;
    public ArrayList<CustomClass> one_to_many;
    public ArrayList<CustomClass> many_to_many;
}

class Item{
    public Class _classobject;
    public FieldName _fieldname;
    public String _modifier;
    public String _default;
    public Object _value;
    public String _type;
    public String _columetype;
    public boolean _notnull=false;
    public boolean _autoincrement=false;
}


class CustomClass{
    public Item items;
    public TableValue table_value;
}
