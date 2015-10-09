package Db;

import java.lang.reflect.Field;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class ActiveRecord {

    private static Map<String, HashMap> cache;

    public <T extends ActiveRecord> List<T> getAll() {
        init();
        List<T> out = new ArrayList<>();
        try {
            Statement st = Db.Connector.getConnection().createStatement();

            HashMap hm = cache.get(this.getClass().getName());
            ResultSet rs = st.executeQuery("select * from " + hm.get("tabl"));
            Field[] fields = (Field[]) hm.get("flds");
            while (rs.next()) {
                Object obj = ((Class<T>) hm.get("clss")).newInstance();
                for (Field f : fields) {
                    if (f.isAnnotationPresent(Column.class)) {
                        String clm = f.getAnnotation(Column.class).column();
                        f.setAccessible(true);
                        if (f.getType() == byte.class) {
                            byte b = rs.getByte(clm.isEmpty() ? f.getName() : clm);
                            f.set(obj, b);
                        } else if (f.getType() == short.class) {
                            short s = rs.getShort(clm.isEmpty() ? f.getName() : clm);
                            f.set(obj, s);
                        } else {
                            f.set(obj, rs.getObject(
                                    clm.isEmpty() ? f.getName() : clm,
                                    f.getType())
                            );
                        }
                    }
                }
                out.add((T) obj);
            }

        } catch (InstantiationException | IllegalAccessException | SQLException ex) {
            Logger.getLogger(ActiveRecord.class.getName()).log(Level.SEVERE, null, ex);
        }
        return out;
    }

    public <T extends ActiveRecord> T getById(int id) {
        init();
        try {
            Statement st = Db.Connector.getConnection().createStatement();

            HashMap hm = cache.get(this.getClass().getName());
            ResultSet rs = st.executeQuery("select * from " + hm.get("tabl") + " where " + hm.get("id") + "= " + id);
            Field[] fields = (Field[]) hm.get("flds");
            while (rs.next()) {
                Object obj = ((Class) hm.get("clss")).newInstance();
                for (Field f : fields) {
                    if (f.isAnnotationPresent(Column.class)) {
                        String clm = f.getAnnotation(Column.class).column();
                        f.setAccessible(true);
                        if (f.getType() == byte.class) {
                            byte b = rs.getByte(clm.isEmpty() ? f.getName() : clm);
                            f.set(obj, b);
                        } else if (f.getType() == short.class) {
                            short s = rs.getShort(clm.isEmpty() ? f.getName() : clm);
                            f.set(obj, s);
                        } else {
                            f.set(obj, rs.getObject(
                                    clm.isEmpty() ? f.getName() : clm,
                                    f.getType())
                            );
                        }
                    }
                }
                return (T) obj;
            }

        } catch (InstantiationException | IllegalAccessException | SQLException ex) {
            Logger.getLogger(ActiveRecord.class.getName()).log(Level.SEVERE, "nije se instanciralo", ex);
        }
        return null;
    }

    /**
     * <p>
     * Call this method upon an instance of the extending class
     * <p>
     * Object field values will be used to insert the data into the database
     * <p>
     * id on the instance will be updated if the insert is successful
     *
     * @return returns true if insert successful;
     * <p>
     * false otherwise;
     */
    public boolean insert() {
        init();
        try {
            Statement st = Db.Connector.getConnection().createStatement();

            HashMap hm = cache.get(this.getClass().getName());
            Field[] fields = (Field[]) hm.get("flds");
            StringBuilder sql = new StringBuilder("insert into ");
            sql.append(hm.get("tabl"));
            sql.append(" values(");
            int idCount = 0;
            for (int i = 0; i < fields.length; i++) {
                fields[i].setAccessible(true);
                String temp = "";
                if (fields[i].isAnnotationPresent(Column.class)) {
                    if (!fields[i].isAnnotationPresent(Id.class)) {
                        if (fields[i].getType() == String.class) {
                            temp += "'";
                        }
                        try {
                            temp += fields[i].get(this).toString();
                            sql.append(temp);
                        } catch (NullPointerException e) {
                            sql.append("null");
                            sql.append(",");
                            continue;
                        }
                        if (fields[i].getType() == String.class) {
                            sql.append("'");
                        }
                    } else {
                        idCount = i;
                        sql.append("null");
                    }
                    sql.append(",");
                }
            }
            sql.deleteCharAt(sql.length() - 1);
            sql.append(" )");
            st.executeUpdate(sql.toString(), Statement.RETURN_GENERATED_KEYS);
            ResultSet key = st.getGeneratedKeys();
            key.next();
            fields[idCount].set(this, key.getInt(1));
        } catch (IllegalAccessException | SQLException ex) {
            Logger.getLogger(ActiveRecord.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }

    public boolean update() {
        init();
        try {
            Statement st = Db.Connector.getConnection().createStatement();

            HashMap hm = cache.get(this.getClass().getName());
            Field[] fields = (Field[]) hm.get("flds");
            
            StringBuilder sql = new StringBuilder("update ");
            sql.append(hm.get("tabl"));
            sql.append(" set ");
            int idCount = 0;
            for (int i = 0; i < fields.length; i++) {
                fields[i].setAccessible(true);
                String temp = "";
                if (fields[i].isAnnotationPresent(Column.class)) {
                    if (!fields[i].isAnnotationPresent(Id.class)) {
                        sql.append(getColumnName(fields[i]));
                        sql.append("=");
                        if (fields[i].getType() == String.class) {
                            temp += "'";
                        }
                        try {
                            temp += fields[i].get(this).toString();
                            sql.append(temp);
                        } catch (NullPointerException e) {
                            sql.append("null");
                            if (i < fields.length - 1) {
                                sql.append(", ");
                            }
                            continue;
                        }
                        if (fields[i].getType() == String.class) {
                            sql.append("'");
                        }
                        sql.append(",");
                    } else {
                        if((int)(fields[i].get(this))==0)
                            throw new RuntimeException("Object instance has no id set, or is set to 0");
                        idCount = i;
                    }
                }
            }
            sql.deleteCharAt(sql.length() - 1);
            sql.append(" where ");
            sql.append(hm.get("id").toString());
            sql.append("=");
            sql.append(fields[idCount].get(this).toString());
            st.executeUpdate(sql.toString());
        } catch (IllegalAccessException | SQLException ex) {
            Logger.getLogger(ActiveRecord.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }

    public boolean delete(int id) {
        init();
        try {
            Statement st = Db.Connector.getConnection().createStatement();
            HashMap hm = cache.get(this.getClass().getName());
            StringBuilder sql = new StringBuilder("delete from ");
            sql.append(hm.get("tabl"));
            sql.append(" where ");
            sql.append(hm.get("id").toString());
            sql.append("=");
            sql.append(id);
            System.out.println(sql.toString());
            st.execute(sql.toString());
        } catch (SQLException ex) {
            Logger.getLogger(ActiveRecord.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }
    
    public boolean delete(){
        init();
        try {
            Statement st = Db.Connector.getConnection().createStatement();
            HashMap hm = cache.get(this.getClass().getName());
            Field[] fields = (Field[]) hm.get("flds");
            StringBuilder sql = new StringBuilder("delete from ");
            sql.append(hm.get("tabl"));
            sql.append(" where ");
            sql.append(hm.get("id").toString());
            sql.append("=");
            for(Field f : fields){
                if(f.isAnnotationPresent(Id.class)&&f.isAnnotationPresent(Column.class)){
                    f.setAccessible(true);
                    sql.append(f.get(this).toString());
                }
            }
            System.out.println(sql.toString());
            st.executeUpdate(sql.toString());

        } catch (IllegalAccessException | SQLException ex) {
            Logger.getLogger(ActiveRecord.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }

    public boolean test() {
        init();
        try {
            Statement st = Db.Connector.getConnection().createStatement();
            HashMap hm = cache.get(this.getClass().getName());
            Field[] fields = (Field[]) hm.get("flds");
            StringBuilder sql = new StringBuilder("delete * from ");
            sql.append(hm.get("tabl"));
            sql.append(" where ");
            sql.append(hm.get("id").toString());
            sql.append("=");
            for(Field f : fields){
                if(f.isAnnotationPresent(Id.class)&&f.isAnnotationPresent(Column.class)){
                    f.setAccessible(true);
                    sql.append(f.get(this).toString());
                }
            }
            System.out.println(sql.toString());
            st.executeUpdate(sql.toString());

        } catch (IllegalAccessException | SQLException ex) {
            Logger.getLogger(ActiveRecord.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }

    private void init() {
        if (cache == null) {
            cache = new HashMap();
        }

        if (cache.containsKey(this.getClass().getName())) {
            return;
        }

        HashMap set = new HashMap();
        Class clss = this.getClass();
        AREntity annt = (AREntity) clss.getAnnotation(AREntity.class);
        Field[] flds = clss.getDeclaredFields();
        for (Field f : flds) {
            if (f.isAnnotationPresent(Id.class)) {
                set.put("id", getColumnName(f));
                break;
            }
        }
        set.put("clss", clss);
        set.put("tabl", annt.tableName());
        set.put("flds", flds);
        cache.put(clss.getName(), set);
    }

    private String getColumnName(Field f) {
        return f.getAnnotation(Column.class).column().isEmpty() ? f.getName() : f.getAnnotation(Column.class).column();
    }
//    private static final Map<String, Class> typeMap = new HashMap<String, Class>() {
//        {
//            put("int", Integer.class);
//            put("long", Long.class);
//            put("double", Double.class);
//            put("float", Float.class);
//            put("bool", Boolean.class);
//            put("char", Character.class);
//            put("byte", Byte.class);
//            put("void", Void.class);
//            put("short", Short.class);
//        }
//    };

}
