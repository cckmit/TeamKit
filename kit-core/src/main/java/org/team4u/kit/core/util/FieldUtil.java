package org.team4u.kit.core.util;

import com.xiaoleilu.hutool.util.ClassUtil;
import com.xiaoleilu.hutool.util.StrUtil;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Jay Wu
 */
public class FieldUtil {

    private static final Pattern PTN = Pattern.compile("(<)(.+)(>)");

    /**
     * 获取一个字段的泛型参数数组，如果这个字段没有泛型，返回空数组
     *
     * @param f 字段
     * @return 泛型参数数组
     */
    public static Class<?>[] getGenericTypes(Field f) {
        String gts = f.toGenericString();
        Matcher m = PTN.matcher(gts);
        if (m.find()) {
            String s = m.group(2);
            List<String> ss = StrUtil.split(s, ',', true, true);
            if (ss.size() > 0) {
                Class<?>[] re = new Class<?>[ss.size()];
                for (int i = 0; i < ss.size(); i++) {
                    String className = ss.get(i);
                    if (className.length() > 0 && className.charAt(0) == '?')
                        re[i] = Object.class;
                    else {
                        int pos = className.indexOf('<');
                        if (pos < 0)
                            re[i] = ClassUtil.loadClass(className);
                        else
                            re[i] = ClassUtil.loadClass(className.substring(0, pos));
                    }
                }
                return re;
            }
        }
        return new Class<?>[0];
    }


    public static void setValue(Object obj, String name, Object value)
            throws InvocationTargetException, NoSuchFieldException {
        if (obj == null || StrUtil.isEmpty(name)) {
            return;
        }

        Method method = ClassUtil.getPublicMethod(obj.getClass(), "set" + StrUtil.upperFirst(name));

        if (method == null) {
            setValue(obj, obj.getClass().getDeclaredField(name), value);
        } else {
            ClassUtil.invoke(obj, method, EmptyValue.EMPTY_OBJECT_ARRAY);
        }
    }

    public static void setValue(Object obj, Field f, Object value) {
        if (!f.isAccessible())
            f.setAccessible(true);
        try {
            f.set(obj, value);
        } catch (Exception e) {
            throw new RuntimeException(obj.getClass() + "." + f.getName() + " setValue fail", e);
        }
    }

    public static <T> T getValue(Object obj, String name)
            throws InvocationTargetException, NoSuchFieldException, IllegalAccessException {
        if (obj == null || StrUtil.isEmpty(name)) {
            return null;
        }

        Method method = ClassUtil.getPublicMethod(obj.getClass(), "get" + StrUtil.upperFirst(name));

        if (method == null) {
            return getValue(obj, obj.getClass().getDeclaredField(name));
        } else {
            return ClassUtil.invoke(obj, method, EmptyValue.EMPTY_OBJECT_ARRAY);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T getValue(Object obj, Field f) {
        if (!f.isAccessible()) {
            f.setAccessible(true);
        }

        try {
            return (T) f.get(obj);
        } catch (IllegalAccessException e) {
            return null;
        }
    }


    /**
     * 获取一个字段的某一个泛型参数，如果没有，返回 null
     *
     * @param f 字段
     * @return 泛型参数数
     */
    public static Class<?> getGenericTypes(Field f, int index) {
        Class<?>[] types = getGenericTypes(f);
        if (null == types || types.length <= index)
            return null;
        return types[index];
    }

    public static boolean isNormalField(Field field) {
        int m = field.getModifiers();
        if (Modifier.isStatic(m)) {
            return false;
        }
        if (Modifier.isFinal(m)) {
            return false;
        }
        if (field.getName().startsWith("this$")) {
            return false;
        }

        return true;
    }
}
