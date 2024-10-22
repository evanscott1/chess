package dataaccess;

import model.UserData;

import java.lang.reflect.Field;
import java.util.HashMap;

import static dataaccess.MemoryUtil.*;

public abstract class MemoryBaseDAO<T> {
    private int nextId = 1;
    final private HashMap<Integer, T> ts = new HashMap<>();

    public T addT(T t) throws DataAccessException {
        ts.put(nextId++, t);
        return t;
    }

    public T getT(String attributeValue, String value) throws DataAccessException {
        return findHashMapObjectByAttribute(ts, attributeValue, value);
    }

    public void deleteT(

    public void deleteAllTs() throws DataAccessException {
        ts.clear();
    }

    private T findHashMapObjectByKey(int key) {

    }

    private T findHashMapObjectByAttribute(HashMap<Integer, T> ts, String attributeValue, String value) {

        for (T t : ts.values()) {
            try{
                Field field = t.getClass().getDeclaredField(attributeValue);
                Object fieldValue = field.get(t);
                if(fieldValue != null && fieldValue.toString().equals(value)) {
                    return t;
                }
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}
