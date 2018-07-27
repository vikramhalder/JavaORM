package com.github.vikramhalder.JavaORM;

import com.github.vikramhalder.JavaORM.*;
import com.github.vikramhalder.JavaORM.Annotations.*;
import com.github.vikramhalder.JavaORM.Interface.IDB;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.Date;
import java.util.ArrayList;

abstract class DBCore {
    /*protected static Map<String,Boolean> getFieldsNames(Class<?> clazz) {
        Map<String,Boolean> attributes=new HashMap<>();
        while (clazz != null) {
            for (Field field : clazz.getDeclaredFields()) {
                if(Modifier.toString(field.getModifiers()).equals("private") | Modifier.toString(field.getModifiers()).equals("protected") | Modifier.toString(field.getModifiers()).equals("public")) {
                    if (!field.getName().contains("$")) {
                        if(field.isAnnotationPresent(UnMap.class)){
                            attributes.put(field.getName(),false);
                        }else{
                            attributes.put(field.getName(),true);
                        }
                    }
                }
            }
            clazz = clazz.getSuperclass();
        }
        return attributes;
    }

    protected static ArrayList<DBProperty> getFieldsName(Class<?> clazz) {
        ArrayList<DBProperty> fields = new ArrayList<>();
        while (clazz != null) {
            for (Field field : clazz.getDeclaredFields()) {
                if(Modifier.toString(field.getModifiers()).equals("private") | Modifier.toString(field.getModifiers()).equals("protected") | Modifier.toString(field.getModifiers()).equals("public")) {
                    if (!field.getName().contains("$")) {
                        DBProperty DBProperty =new DBProperty();
                        DBProperty._type = field.getType().getSimpleName();
                        DBProperty._modifier = Modifier.toString(field.getModifiers());

                        FieldName fieldName=new FieldName();
                        if(field.isAnnotationPresent(UnMap.class)){
                        }else if(field.isAnnotationPresent(Coloum.class)){
                            Coloum column = field.getAnnotation(Coloum.class);
                            fieldName.realname=field.getName();
                            fieldName.mapname=column.value();
                            DBProperty._fieldName=fieldName;
                        }else{
                            fieldName.realname=field.getName();
                            fieldName.mapname=field.getName();
                            DBProperty._fieldName=fieldName;
                        }

                        if(field.isAnnotationPresent(NotNull.class)){
                            DBProperty._notNull=true;
                        }if(field.isAnnotationPresent(PK.class)){
                            DBProperty._pk=true;
                        }if(field.isAnnotationPresent(Unique.class)){
                            DBProperty._qunique=true;
                        }if(field.isAnnotationPresent(AutoIncrement.class)){
                            if(DBProperty._type.equals("int")) {
                                DBProperty._autoincrement = true;
                            }else{
                            }
                        }

                        fields.add(DBProperty);
                    }
                }
            }
            clazz = clazz.getSuperclass();
        }

        return fields;
    }

    protected static ArrayList<DBProperty> getFieldsValue(Class<?> clazz) {
        ArrayList<DBProperty> fields = new ArrayList<>();
        while (clazz != null) {
            for (Field field : clazz.getDeclaredFields()) {
                if(Modifier.toString(field.getModifiers()).equals("private") | Modifier.toString(field.getModifiers()).equals("protected") | Modifier.toString(field.getModifiers()).equals("public")) {
                    if (!field.getName().contains("$")) {
                        DBProperty DBProperty =new DBProperty();
                        DBProperty._type = field.getType().getSimpleName();
                        try {
                            Object objectValue = field.getType().newInstance();
                            DBProperty._value = String.valueOf(field.get(objectValue));
                        }catch (Exception ex){
                            DBProperty._value =null;}

                        DBProperty._modifier = Modifier.toString(field.getModifiers());

                        FieldName fieldName=new FieldName();
                        if(field.isAnnotationPresent(UnMap.class)){
                        }else if(field.isAnnotationPresent(Coloum.class)){
                            Coloum column = field.getAnnotation(Coloum.class);
                            fieldName.realname=field.getName();
                            fieldName.mapname=column.value();
                            DBProperty._fieldName=fieldName;
                        }else{
                            fieldName.realname=field.getName();
                            fieldName.mapname=field.getName();
                            DBProperty._fieldName=fieldName;
                        }

                        if(field.isAnnotationPresent(NotNull.class)){
                            DBProperty._notNull=true;
                        }if(field.isAnnotationPresent(PK.class)){
                            DBProperty._pk=true;
                        }if(field.isAnnotationPresent(Unique.class)){
                            DBProperty._qunique=true;
                        }if(field.isAnnotationPresent(AutoIncrement.class)){
                            if(DBProperty._type.equals("int")) {
                                DBProperty._autoincrement = true;
                            }else{
                            }
                        }
                        fields.add(DBProperty);
                    }
                }
            }
            clazz = clazz.getSuperclass();
        }

        return fields;
    }*/

    protected static DBTable getDBTable(Class<?> clazz) {
        DBTable dbTable=new DBTable();
        dbTable.clazz=clazz;
        FieldName field_name_for_class=new FieldName();

        if(clazz.isAnnotationPresent(Table.class)) {
            Table table = clazz.getAnnotation(Table.class);
            field_name_for_class.mapname = table.value();
            field_name_for_class.realname = clazz.getSimpleName();
            dbTable.classname=field_name_for_class;
        }else{
            field_name_for_class.mapname = clazz.getSimpleName();
            field_name_for_class.realname = clazz.getSimpleName();
            dbTable.classname=field_name_for_class;
        }

        ArrayList<DBProperty> get_class_property = new ArrayList<>();
        while (clazz != null) {
            for (Field field : clazz.getDeclaredFields()) {
                if(Modifier.toString(field.getModifiers()).equals("private") | Modifier.toString(field.getModifiers()).equals("protected") | Modifier.toString(field.getModifiers()).equals("public")) {
                    if (!field.getName().contains("$")) {
                        DBProperty DBProperty =new DBProperty();
                        DBProperty._type = field.getType().getSimpleName();
                        try {
                            Object objectValue = field.getType().newInstance();
                            DBProperty._value = field.get(objectValue);
                        }catch (Exception ex){
                            DBProperty._value =null;
                        }

                        DBProperty._modifier = Modifier.toString(field.getModifiers());

                        FieldName fieldName=new FieldName();
                        if(field.isAnnotationPresent(UnMap.class)){
                        }else if(field.isAnnotationPresent(Coloum.class)){
                            Coloum column = field.getAnnotation(Coloum.class);
                            fieldName.realname=field.getName();
                            fieldName.mapname=column.value();
                            DBProperty._fieldName=fieldName;
                        }else{
                            fieldName.realname=field.getName();
                            fieldName.mapname=field.getName();
                            DBProperty._fieldName=fieldName;
                        }

                        if(field.isAnnotationPresent(ForeignKey.class)){
                            DBProperty._foreignkey=getDBTable(field.getType());
                        }if(field.isAnnotationPresent(NotNull.class)){
                            DBProperty._notNull=true;
                        }if(field.isAnnotationPresent(PK.class)){
                            DBProperty._pk=true;
                        }if(field.isAnnotationPresent(Unique.class)){
                            DBProperty._qunique=true;
                        }if(field.isAnnotationPresent(Default.class)){
                            Default def = field.getAnnotation(Default.class);
                            DBProperty._default=def.value();
                            DBProperty._isDefault=true;
                            if(DBProperty._value==null){
                                DBProperty._value=def.value();
                            }
                        }if(field.isAnnotationPresent(AutoIncrement.class)){
                            if(DBProperty._type.equals("int")) {
                                DBProperty._autoincrement = true;
                            }else{
                            }
                        }
                        get_class_property.add(DBProperty);
                    }
                }
            }
            clazz = clazz.getSuperclass();
        }
        dbTable.properties=get_class_property;
        
        return dbTable;
    }
    protected static TableValue getTableValue(Class<?> clazz) {
        TableValue tablevalue=new TableValue();

        String storage_engine=null;
        FieldName table_name=null;
        Class class_object=clazz;
        Item primary_key=null;
        ArrayList<CustomClass> unique = new ArrayList<>();
        ArrayList<CustomClass> foreign_key = new ArrayList<>();
        ArrayList<Item> no_annotations = new ArrayList<>();
        ArrayList<CustomClass> one_to_one = new ArrayList<>();
        ArrayList<CustomClass> one_to_many = new ArrayList<>();
        ArrayList<CustomClass> many_to_many = new ArrayList<>();


        FieldName field_name_for_class=new FieldName();
        if(clazz.isAnnotationPresent(Table.class)) {
            Table table = clazz.getAnnotation(Table.class);
            field_name_for_class.mapname = table.value();
            field_name_for_class.realname = clazz.getSimpleName();
            table_name=field_name_for_class;
        }else{
            field_name_for_class.mapname = clazz.getSimpleName();
            field_name_for_class.realname = clazz.getSimpleName();
            table_name=field_name_for_class;
        }
        if(clazz.isAnnotationPresent(StorageEngine.class)){
            StorageEngine table = clazz.getAnnotation(StorageEngine.class);
            storage_engine=table.value();
        }

        while (clazz != null) {
            for (Field field : clazz.getDeclaredFields()) {
                if(Modifier.toString(field.getModifiers()).equals("private") | Modifier.toString(field.getModifiers()).equals("protected") | Modifier.toString(field.getModifiers()).equals("public")) {
                    if (!field.getName().contains("$")) {
                        Item item=new Item();

                        item._classobject=field.getType();
                        item._modifier = Modifier.toString(field.getModifiers());
                        item._type = field.getType().getSimpleName();
                        try {
                            Object objectValue = field.getType().newInstance();
                            item._value = field.get(objectValue);
                        }catch (Exception ex){
                            item._value =null;
                        }


                        FieldName fieldName=new FieldName();
                        if(field.isAnnotationPresent(UnMap.class)){
                        }else if(field.isAnnotationPresent(Coloum.class)){
                            Coloum column = field.getAnnotation(Coloum.class);
                            fieldName.realname=field.getName();
                            fieldName.mapname=column.value();
                            item._fieldname=fieldName;
                        }else{
                            fieldName.realname=field.getName();
                            fieldName.mapname=field.getName();
                            item._fieldname=fieldName;
                        }

                        if(field.isAnnotationPresent(Default.class)){
                            Default def = field.getAnnotation(Default.class);
                            item._default=def.value();
                            if(item._value==null){
                                item._value=def.value();
                            }
                        }if(field.isAnnotationPresent(ColumnType.class)){
                            ColumnType def = field.getAnnotation(ColumnType.class);
                            item._columetype=def.value();
                        }if(field.isAnnotationPresent(AutoIncrement.class)){
                            item._autoincrement = true;
                        }
                        if(field.isAnnotationPresent(NotNull.class)){
                            item._notnull=true;
                        }


                        if(field.isAnnotationPresent(ForeignKey.class)){
                            CustomClass customClass=new CustomClass();
                            customClass.table_value=getTableValue(field.getType());
                            customClass.items=item;
                            foreign_key.add(customClass);
                        }else if(field.isAnnotationPresent(PK.class)){
                            primary_key=item;
                        }else if(field.isAnnotationPresent(Unique.class)){
                            CustomClass customClass=new CustomClass();
                            customClass.table_value=getTableValue(field.getType());
                            customClass.items=item;
                            unique.add(customClass);
                        }else if(field.isAnnotationPresent(OneToOne.class)){
                            CustomClass customClass=new CustomClass();
                            customClass.table_value=getTableValue(field.getType());
                            customClass.items=item;
                            one_to_one.add(customClass);
                        }else if(field.isAnnotationPresent(OneToOne.class)){
                            CustomClass customClass=new CustomClass();
                            customClass.table_value=getTableValue(field.getType());
                            customClass.items=item;
                            one_to_many.add(customClass);
                        }else if(field.isAnnotationPresent(OneToOne.class)){
                            CustomClass customClass=new CustomClass();
                            customClass.table_value=getTableValue(field.getType());
                            customClass.items=item;
                            many_to_many.add(customClass);
                        }else {
                            no_annotations.add(item);
                        }

                    }
                }
            }
            clazz = clazz.getSuperclass();
        }

        tablevalue.storage_engine=storage_engine;
        tablevalue.table_name=table_name;
        tablevalue.class_object=class_object;
        tablevalue.primary_key=primary_key;
        tablevalue.unique=unique;
        tablevalue.foreign_key=foreign_key;
        tablevalue.no_annotations=no_annotations;
        tablevalue.one_to_one=one_to_one;
        tablevalue.one_to_many=one_to_many;
        tablevalue.many_to_many=many_to_many;

        return tablevalue;
    }
    protected static  <T> T CastType(String type, String value) {
        type=type.toLowerCase();
        if(type.equals("int"))
            return (T) Integer.valueOf(value);
        else if(type.equals("double"))
            return (T) Double.valueOf(value);
        else if(type.equals("float"))
            return (T) Float.valueOf(value);
        else if(type.equals("string"))
            return (T)value;
        else if(type.equals("boolean"))
            return (T)Boolean.valueOf(value);
        else if(type.equals("byte"))
            return (T)Byte.valueOf(value);
        else if(type.equals("char"))
            return (T)Character.valueOf(value.charAt(0));
        else if(type.equals("short"))
            return (T)Short.valueOf(value);
        else if(type.equals("long"))
            return (T)Long.valueOf(value);
        else if(type.toLowerCase().equals("date"))
            return (T)Date.valueOf(value);
        else
            return (T)value;
    }

    protected static String ColoumType(String type,String columeType){
        if(columeType==null)
            if(type.toLowerCase().equals("int"))
                return "int";
            else if(type.toLowerCase().equals("St"))
                return "int";
            else if(type.toLowerCase().equals("float"))
                return "float";
            else if(type.toLowerCase().equals("boolean"))
                return "boolean";
            else if(type.toLowerCase().equals("date"))
                return "date";
            else if(type.toLowerCase().equals("long"))
                return "bigint";
            else if(type.toLowerCase().equals("enum"))
                return "enum";
            else return "varchar(255)";
        else return columeType;
    }

    protected static String defaultValue(String type,String value){
        if(type.toLowerCase().equals("date") && value.toUpperCase().equals("CURRENT_TIMESTAMP")){
            return value;
        }else {
            return "'"+value+"'";
        }
    }
    protected static String getClassNamme(String classname){
        String[] str=classname.replace(".",",").split(",");
        if(str!=null)
            if(str.length!=0)
                return str[str.length-1];
        return null;
    }

    protected static String getPkAndVal(DBTable dbTable){
        String pk=null;
        for (DBProperty DBProperty : dbTable.properties) {
            if (DBProperty._fieldName != null) {
                if (DBProperty._pk && DBProperty._autoincrement) {
                    pk = DBProperty._fieldName.mapname+" = '"+DBProperty._value+"'";
                }
            }
        }
        return pk;
    }

    protected static String getFKey(ArrayList<DBTable> foreignkey, DBConfig dbConfig, String databasename){
        String fkey="";
        for(DBTable dbTable:foreignkey){
            for(DBProperty dbProperty:dbTable.properties) {
                if(dbProperty._pk) {

                    IDB<String> d=new DB<String>(dbTable.clazz,databasename).Config(dbConfig);;
                    d.onCreateTable();
                    fkey += "FOREIGN KEY ("+dbTable.classname.mapname+") REFERENCES "+dbTable.classname.mapname+"("+dbProperty._fieldName.mapname+"),";
                }
            }
        }
        return fkey.length()>5?fkey:null;
    }
}