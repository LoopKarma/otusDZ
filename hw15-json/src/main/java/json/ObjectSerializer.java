package json;


import json.exception.NotSupportedPrimitive;
import json.utils.ReflectionUtils;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

public class ObjectSerializer {
    private static final List<String> primitives = new ArrayList<>(Arrays.asList(
            "java.lang.Boolean",
            "java.lang.Byte",
            "java.lang.Character",
            "java.lang.Double",
            "java.lang.Float",
            "java.lang.Integer",
            "java.lang.Long",
            "java.lang.Short",
//            "java.lang.Void",
            "java.lang.String"
    ));

//    private static final Map<String, Function<?, JsonValue>> primitivesCasting = new HashMap<>();

    private Config config;

    static final public class Config {
        private boolean excludeNull = true;

        public Config(boolean excludeNull) {
            this.excludeNull = excludeNull;
        }

        public boolean isExcludeNull() {
            return excludeNull;
        }
    }

    public ObjectSerializer(Config config) {
        this.config = config;
//        primitivesCasting.put("java.lang.Boolean", new Function<Boolean, JsonValue>() {
//            @Override
//            public JsonValue apply(Boolean aBoolean) {
//                return aBoolean ? JsonValue.TRUE : JsonValue.FALSE;
//            }
//        });
    }

    public static String serialize(Object o, Config config)
    {
        if (config == null) {
            config = new Config(true);
        }
        ObjectSerializer objectSerializer = new ObjectSerializer(config);
        return objectSerializer.doSerialize(o).toString().replace("\\\"", "");
    }

    private JsonValue doSerialize(Object obj) {
        if (obj == null) {
            return JsonValue.NULL;
        }
        Class clazz = obj.getClass();

        if (primitives.contains(clazz.getName())) {
            return serializePrimitive(obj);
        }
        if (clazz.isArray()) {
            return serializeArray(obj).build();
        }
        if (obj instanceof Collection) {
            return serializeArray(((Collection) obj).toArray()).build();
        }
        if (obj instanceof Map) {
            return serializeMap((Map) obj).build();
        }

        return serializeObject(obj).build();
    }

    private JsonObjectBuilder serializeObject(Object object) {
        JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
        Collection<Field> deepDeclaredFields = ReflectionUtils.getDeepDeclaredFields(object.getClass());

        for (Field field: deepDeclaredFields) {
            int modifiers = field.getModifiers();
            if (!Modifier.isTransient(modifiers) && !Modifier.isStatic(modifiers)) {
                Object value = ReflectionUtils.getFieldValue(object, field);
                if (value == null) {
                    if (!config.isExcludeNull()) {
                        jsonObjectBuilder.add(field.getName(), JsonValue.NULL);
                    }
                    //continue
                } else {
                    jsonObjectBuilder.add(field.getName(), doSerialize(value));
                }
            }
        }
        return jsonObjectBuilder;
    }

    private JsonObjectBuilder serializeMap(Map map) {
        JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
        map.forEach((k, v) -> jsonObjectBuilder.add(k.toString(), serializePrimitive(v)));

        return jsonObjectBuilder;
    }


    private JsonArrayBuilder serializeArray(Object array) {
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        for (int i = 0; i < Array.getLength(array); i++) {
            Object elem = Array.get(array, i);
            if (primitives.contains(elem.getClass().getName())) {
                arrayBuilder.add(serializePrimitive(elem));
            } else {
                arrayBuilder.add(doSerialize(elem));
            }
        }
        return arrayBuilder;
    }

    private JsonValue serializePrimitive(Object elem) {
        if (elem instanceof Byte) {
            return Json.createValue((Byte) elem);
        }
        if (elem instanceof Integer) {
            return Json.createValue((Integer) elem);
        }
        if (elem instanceof Long) {
            return Json.createValue((Long) elem);
        }
        if (elem instanceof Float || elem instanceof Double) {
            return Json.createValue(((Number) elem).doubleValue());
        }
        if (elem instanceof Short) {
            return Json.createValue((Short) elem);
        }
        if (elem instanceof Boolean) {
            return elem.equals(true) ? JsonValue.TRUE : JsonValue.FALSE;
        }
        if (elem instanceof Character || elem instanceof String) {
            return Json.createValue(String.valueOf(elem));
        }

        throw new NotSupportedPrimitive("Class : " + elem.getClass());
    }

}
