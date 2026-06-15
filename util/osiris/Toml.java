package dev.levia.jimi.util.osiris;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This is simple Osiris Toml Convertor
 * Simply put bind Binds Java Records to Toml File input
 * design is that Toml contains List of HeshMap using record that is value from it
 * [Something]
 * name = "Else"
 * pos = [0,0,0]
 * -> HashMap<String, new Something(String, Vec3)
 * -> the code is not smartest, it can still accept classes but is suggested to use records
 * because you will not change the values(if you would change it, it would be probably in the file itself)
 * -> enables still using classes inside the records
 */
public class Toml {
    public static List<String> smartSplit(String value) {
        var out = new ArrayList<String>();
        var depth = 0;
        var last = 0;
        for (int i = 0; i < value.length(); i++) {
            var ch = value.charAt(i);
            if (ch == '[') depth ++; else if (ch == ']') depth --;
            if (ch == ',' && depth == 0) {
                out.add(value.substring(last, i));
                last = i+1;
            }
        }
        out.add(value.substring(last+1, value.length()-1));
        return out;
    }
    public static <T extends Object>T convert(String content, Class<T> clazz) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        if (content.startsWith("\"") && content.endsWith("\"")) return clazz.cast(content.substring(1, content.length()-1));
        // this means its array or object
        if (content.contains(",")) {
            var values = smartSplit(content.substring(1, content.length()-1));
            if (clazz.isArray()) {
                var type = clazz.getComponentType();
                var array = new ArrayList<T>();
                for (var value : values) array.add((T) convert(value, type));
                return (T) array.toArray();
            }
            var constructors = clazz.getDeclaredConstructors();
            Constructor<T> constructor = null;
            var id = 0;
            T entity = null;
            while (entity == null) {
                for (int i = id; i < constructors.length; i++) {
                    if (constructors[i].getParameterCount() == values.size()) {
                        constructor = (Constructor<T>) constructors[i]; break;
                    }
                }
                var arguments = new Object[values.size()];
                if (constructor == null) break;
                var params = constructor.getGenericParameterTypes();
                try {
                    for (int i = 0; i < params.length; i++) {
                        arguments[i] = convert(values.get(i), params[i].getClass());
                    }
                    entity = constructor.newInstance(arguments);
                    assert entity != null;
                    return (T) entity;
                }catch (Exception e) {
                    entity = null;
                    if (constructors.length == id) break;
                }
                id += 1;
            }
        }
        if (content.contains(".")) {
            if (content.endsWith("f")) return (T) Float.valueOf(content.substring(0, content.length()-2));
            return (T) Double.valueOf(content.trim());
        }
        return (T) Integer.valueOf(content.trim());
    }
    public static <T extends Record> Map<String, T> parse(String path, Class<T> typ) {
        var values = new HashMap<String, T>();
        var pointer = 0;
        try {
            var types = typ.getRecordComponents();
            var params = new Class<?>[types.length];
            var names = new String[types.length];

            for (int i = 0; i < types.length; i++) { names[i] = types[i].getName(); params[i] = types[i].getType(); }


            var arguments = new Object[types.length];
            // this could be done using reader
            var content = Files.readString(Path.of(path)).split("\n");
            var constructor = typ.getDeclaredConstructor(params);
            constructor.setAccessible(true);
            var next = "";

            for (var line : content){
                pointer ++;
                var trimmed = line.trim();
                if (trimmed.contains("#")) trimmed = trimmed.substring(0, trimmed.indexOf('#'));
                if (trimmed.isBlank()) continue;
                if (trimmed.startsWith("[") && trimmed.endsWith("]")) {
                    if (!next.isBlank()) {
                        for (int i = 0; i < arguments.length; i++) {
                            if (params[i].isArray() && params[i] != String.class) {
                                var array = (Object[]) arguments[i];
                                var newarguments = Array.newInstance(params[i].getComponentType(), arguments.length);
                                for (int j = 0; j < array.length; j++) {
                                    var parsed = params[i].getComponentType().cast(array[j]);
                                    Array.set(newarguments, j, parsed);
                                }
                                arguments[i] = newarguments;
                            }
                            System.out.println(arguments[i]+" "+params[i].cast(arguments[i]));
                        }
                        var instance = constructor.newInstance(arguments);
                        values.put(next, instance);
                    }
                    next = trimmed.substring(1, trimmed.length()-1);
                    arguments = new Object[types.length];
                    continue;
                }
                if (trimmed.contains("=")) {
                    var trix = trimmed.split("=");
                    var named = trix[0].trim();
                    var val = trix[1].trim();
                    var id = -1;
                    for (int i = 0; i < names.length; i++) { if (names[i].equals(named)) { id = i; break; } }
                    if (id == -1) continue;
                    arguments[id] = convert(val, params[id]);
                }
            }
        } catch (Exception e) {
            System.out.println(Path.of(path).toAbsolutePath() +" at line:"+pointer);
            throw new RuntimeException(e);
        }
        return values;
    }

}
