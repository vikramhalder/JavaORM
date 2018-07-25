package  com.github.vikramhalder.JavaORM;

import java.util.Map;

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
class DbField {
    public String type;
    public FieldName fieldName;
    public String value;
    public String modifier;
    public boolean pk=false;
    public boolean autoincrement=false;
    public boolean qunique=false;
    public boolean notNull=false;
}

class FieldName{
    public String realname;
    public String mapname;
}