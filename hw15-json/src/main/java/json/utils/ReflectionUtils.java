package json.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ReflectionUtils {
    private static final Map<Class, Collection<Field>> reflectedFields = new ConcurrentHashMap<>();

    public static Collection<Field> getDeepDeclaredFields(Class c)
    {
        if (reflectedFields.containsKey(c))
        {
            return reflectedFields.get(c);
        }
        Collection<Field> fields = new ArrayList<>();
        Class curr = c;

        while (curr != null) {
            getDeclaredFields(curr, fields);
            curr = curr.getSuperclass();
        }
        reflectedFields.put(c, fields);
        return fields;
    }

    private static void getDeclaredFields(Class c, Collection<Field> fields) {
        try
        {
            Field[] local = c.getDeclaredFields();

            for (Field field : local)
            {
                try
                {
                    field.setAccessible(true);
                }
                catch (Exception ignored) { }

                int modifiers = field.getModifiers();
                if (!Modifier.isStatic(modifiers) && !Modifier.isTransient(modifiers)) {
                    fields.add(field);
                }
            }
        }
        catch (Throwable e)
        {
            //ignore
        }
    }

    public static Object getFieldValue(Object object, Field field) {
        boolean isAccessible = true;
        try {
            isAccessible = field.canAccess(object);
            field.setAccessible(true);
            return field.get(object);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } finally {
            if (field != null && !isAccessible) {
                field.setAccessible(false);
            }
        }
        return null;
    }

}
