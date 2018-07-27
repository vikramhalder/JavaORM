package  com.github.vikramhalder.JavaORM;

import com.github.vikramhalder.Example.DBConn;
import com.github.vikramhalder.Example.Person;
import com.github.vikramhalder.JavaORM.Annotations.*;
import com.github.vikramhalder.JavaORM.Interface.IDB;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
                            DBProperty._default_value=def.value();
                            DBProperty._default=true;
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

    protected static String getFKey(ArrayList<DBTable> foreignkey,DBConfig dbConfig,String databasename){
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