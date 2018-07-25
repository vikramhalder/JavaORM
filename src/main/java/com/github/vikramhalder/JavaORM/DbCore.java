package  com.github.vikramhalder.JavaORM;

import com.github.vikramhalder.JavaORM.Annotations.*;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

abstract class DbCore {
    protected static Map<String,Boolean> getFieldsNames(Class<?> clazz) {
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

    protected static ArrayList<DbField> getFieldsName(Class<?> clazz) {
        ArrayList<DbField> fields = new ArrayList<>();
        while (clazz != null) {
            for (Field field : clazz.getDeclaredFields()) {
                if(Modifier.toString(field.getModifiers()).equals("private") | Modifier.toString(field.getModifiers()).equals("protected") | Modifier.toString(field.getModifiers()).equals("public")) {
                    if (!field.getName().contains("$")) {
                        DbField dbField=new DbField();
                        dbField.type = field.getType().getSimpleName();
                        dbField.modifier = Modifier.toString(field.getModifiers());

                        FieldName fieldName=new FieldName();
                        if(field.isAnnotationPresent(UnMap.class)){
                        }else if(field.isAnnotationPresent(Coloum.class)){
                            Coloum column = field.getAnnotation(Coloum.class);
                            fieldName.realname=field.getName();
                            fieldName.mapname=column.name();
                            dbField.fieldName=fieldName;
                        }else{
                            fieldName.realname=field.getName();
                            fieldName.mapname=field.getName();
                            dbField.fieldName=fieldName;
                        }

                        if(field.isAnnotationPresent(NotNull.class)){
                            dbField.notNull=true;
                        }if(field.isAnnotationPresent(PK.class)){
                            dbField.pk=true;
                        }if(field.isAnnotationPresent(Unique.class)){
                            dbField.qunique=true;
                        }if(field.isAnnotationPresent(AutoIncrement.class)){
                            if(dbField.type.equals("int")) {
                                dbField.autoincrement = true;
                            }else{
                            }
                        }

                        fields.add(dbField);
                    }
                }
            }
            clazz = clazz.getSuperclass();
        }

        return fields;
    }
    protected static ArrayList<DbField> getFieldsValue(Class<?> clazz) {
        ArrayList<DbField> fields = new ArrayList<>();
        while (clazz != null) {
            for (Field field : clazz.getDeclaredFields()) {
                if(Modifier.toString(field.getModifiers()).equals("private") | Modifier.toString(field.getModifiers()).equals("protected") | Modifier.toString(field.getModifiers()).equals("public")) {
                    if (!field.getName().contains("$")) {
                        DbField dbField=new DbField();
                        dbField.type = field.getType().getSimpleName();
                        try {
                            Object objectValue = field.getType().newInstance();
                            dbField.value = String.valueOf(field.get(objectValue));
                        }catch (Exception ex){dbField.value =null;}

                        dbField.modifier = Modifier.toString(field.getModifiers());

                        FieldName fieldName=new FieldName();
                        if(field.isAnnotationPresent(UnMap.class)){
                        }else if(field.isAnnotationPresent(Coloum.class)){
                            Coloum column = field.getAnnotation(Coloum.class);
                            fieldName.realname=field.getName();
                            fieldName.mapname=column.name();
                            dbField.fieldName=fieldName;
                        }else{
                            fieldName.realname=field.getName();
                            fieldName.mapname=field.getName();
                            dbField.fieldName=fieldName;
                        }

                        if(field.isAnnotationPresent(NotNull.class)){
                            dbField.notNull=true;
                        }if(field.isAnnotationPresent(PK.class)){
                            dbField.pk=true;
                        }if(field.isAnnotationPresent(Unique.class)){
                            dbField.qunique=true;
                        }if(field.isAnnotationPresent(AutoIncrement.class)){
                            if(dbField.type.equals("int")) {
                                dbField.autoincrement = true;
                            }else{
                            }
                        }
                        fields.add(dbField);
                    }
                }
            }
            clazz = clazz.getSuperclass();
        }

        return fields;
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
}