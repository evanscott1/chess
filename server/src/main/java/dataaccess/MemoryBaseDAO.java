package dataaccess;


import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


public abstract class MemoryBaseDAO<T> {
    protected int nextId = 1;
    final protected HashMap<Integer, T> ts = new HashMap<>();


    public T addT(T t) throws DataAccessException {
        ts.put(nextId++, t);
        return t;
    }

    public T getT(String attributeValue, String value) throws DataAccessException {
        Integer key = findHashMapKeyByAttribute(ts, attributeValue, value);
        return ts.get(key);
    }

    public T updateT(T t, String attributeValue, String value) {
        Integer key = findHashMapKeyByAttribute(ts, attributeValue, value);
        ts.replace(key, t);
        return t;
    }

    public Collection<T> listTs() {
        return ts.values();
    }

    public void deleteT(String attributeValue, String value) {
        Integer key = findHashMapKeyByAttribute(ts, attributeValue, value);
        ts.remove(key);
    }

    public void deleteAllTs() throws DataAccessException {
        ts.clear();
    }


    //     throw access errors when null
    protected Integer findHashMapKeyByAttribute(HashMap<Integer, T> ts, String attributeValue, String value) {

        for (Map.Entry<Integer, T> entry : ts.entrySet()) {
            T t = entry.getValue();
            try {
                Field field = t.getClass().getDeclaredField(attributeValue);
                field.setAccessible(true);
                Object fieldValue = field.get(t);
                if (fieldValue != null && fieldValue.toString().equals(value)) {
                    return entry.getKey();
                }
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}
