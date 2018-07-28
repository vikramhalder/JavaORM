package com.github.vikramhalder.JavaORM;

import com.github.vikramhalder.JavaORM.Annotations.*;
import com.github.vikramhalder.JavaORM.Core.DateTime.CurrentDT;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.Date;
import java.util.ArrayList;

abstract class DBCore {

    protected static <T>TableValue getTableValue(T entity,Class<?> clazz) {
        TableValue tablevalue=new TableValue();

        String storage_engine="INNODB";
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
            StorageEngine se = clazz.getAnnotation(StorageEngine.class);
            storage_engine=se.value();
        }

        while (clazz != null) {
            for (Field field : clazz.getDeclaredFields()) {
                field.setAccessible(true);
                if(Modifier.toString(field.getModifiers()).equals("private") | Modifier.toString(field.getModifiers()).equals("protected") | Modifier.toString(field.getModifiers()).equals("public")) {
                    if (!field.getName().contains("$")) {
                        Item item=new Item();

                        item._classobject=field.getType();
                        item._modifier = Modifier.toString(field.getModifiers());
                        item._type = field.getType().getSimpleName();
                        try {
                            item._value = field.get(entity);
                        }catch (Exception ex){
                            //ex.printStackTrace();
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
                                item._value=DefaultValue(item._type,def.value());
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
                            Field f=field;
                            CustomClass customClass=new CustomClass();
                            customClass.table_value=getTableValue( item._value ,field.getType());
                            customClass.items=item;
                            foreign_key.add(customClass);
                        }else if(field.isAnnotationPresent(PK.class)){
                            primary_key=item;
                        }else if(field.isAnnotationPresent(Unique.class)){
                            CustomClass customClass=new CustomClass();
                            customClass.table_value=getTableValue( item._value ,field.getType());
                            customClass.items=item;
                            unique.add(customClass);
                        }else if(field.isAnnotationPresent(OneToOne.class)){
                            CustomClass customClass=new CustomClass();
                            customClass.table_value=getTableValue( item._value ,field.getType());
                            customClass.items=item;
                            one_to_one.add(customClass);
                        }else if(field.isAnnotationPresent(OneToOne.class)){
                            CustomClass customClass=new CustomClass();
                            customClass.table_value=getTableValue( item._value ,field.getType());
                            customClass.items=item;
                            one_to_many.add(customClass);
                        }else if(field.isAnnotationPresent(OneToOne.class)){
                            CustomClass customClass=new CustomClass();
                            customClass.table_value=getTableValue( item._value ,field.getType());
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
    protected static String DefaultType(String type,String value){
        if(type.toLowerCase().equals("date") && value.toUpperCase().equals("CURRENT_TIMESTAMP")){
            return value;
        }else {
            return "'"+value+"'";
        }
    }

    protected static String DefaultValue(String type,String value){
        if(type.toLowerCase().equals("date") && value.toUpperCase().equals("CURRENT_TIMESTAMP")){
            return CurrentDT.toStr("yyyy-MM-dd HH:mm:ss");
        }else if(type.toLowerCase().equals("date") && !value.toUpperCase().equals("CURRENT_TIMESTAMP")){
            return CurrentDT.toStr(value);
        }else {
            return "'"+value+"'";
        }
    }

}