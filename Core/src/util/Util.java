package util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Map;

public final class Util {
    public static final class Reflections{
        public static <T> T instantiate(Class<T> clazz, Object... ctargs){
            Class<?>[] params = new Class<?>[ctargs.length];
            for (int i = 0; i < params.length; i++)
                params[i] = ctargs[i].getClass();
            
            try {
                return clazz.getConstructor(params).newInstance(ctargs);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
