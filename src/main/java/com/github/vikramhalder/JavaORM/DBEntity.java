package  com.github.vikramhalder.JavaORM;

import java.util.ArrayList;
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
class DBProperty {
    public String _type;
    public FieldName _fieldName;
    public Object _value;
    public String _modifier;
    public String _default_value;
    public boolean _pk=false;
    public boolean _default=false;
    public boolean _autoincrement=false;
    public boolean _qunique=false;
    public boolean _notNull=false;
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
}

class Foreignkey{
    private DBTable dbTable;
}
