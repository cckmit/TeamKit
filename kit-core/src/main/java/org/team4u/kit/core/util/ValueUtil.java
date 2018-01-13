package org.team4u.kit.core.util;

import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import org.team4u.kit.core.action.Each;
import org.team4u.kit.core.action.Loop;
import org.team4u.kit.core.error.ExceptionUtil;
import org.team4u.kit.core.lang.EmptyValue;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.Callable;

public class ValueUtil {

    public static <T> T defaultIfNull(T value, T defaultValue) {
        return value != null ? value : defaultValue;
    }

    public static <T> T defaultIfNull(T value, Callable<T> defaultValueFactory) {
        try {
            return value != null ? value : defaultValueFactory.call();
        } catch (Exception e) {
            throw ExceptionUtil.toRuntimeException(e);
        }
    }

    public static <T> T defaultIfEmpty(T value, T defaultValue) {
        return isEmpty(value) ? defaultValue : value;
    }

    public static <T> T defaultIfEmpty(T value, Callable<T> defaultValueFactory) {
        try {
            return isEmpty(value) ? value : defaultValueFactory.call();
        } catch (Exception e) {
            throw ExceptionUtil.toRuntimeException(e);
        }
    }

    public static boolean isEmpty(Object obj) {
        if (obj == null) {
            return true;
        }

        if (obj instanceof Iterator<?>) {
            return !((Iterator) obj).hasNext();
        }

        if (obj.getClass().isArray()) {
            return Array.getLength(obj) == 0;
        }

        if (obj instanceof Collection<?>) {
            return ((Collection<?>) obj).isEmpty();
        }

        if (obj instanceof Map<?, ?>) {
            return ((Map<?, ?>) obj).isEmpty();
        }

        if (obj instanceof String) {
            return StrUtil.isEmpty((String) obj);
        }

        return false;
    }

    /**
     * 获得一个对象的长度。它可以接受:
     * <ul>
     * <li>null : 0
     * <li>数组
     * <li>集合
     * <li>Map
     * <li>一般 Java 对象。 返回 1
     * </ul>
     * 如果你想让你的 Java 对象返回不是 1 ， 请在对象中声明 length() 函数
     *
     * @param obj
     * @return 对象长度
     */
    public static int length(Object obj) {
        if (null == obj)
            return 0;
        if (obj.getClass().isArray()) {
            return Array.getLength(obj);
        } else if (obj instanceof Collection<?>) {
            return ((Collection<?>) obj).size();
        } else if (obj instanceof Map<?, ?>) {
            return ((Map<?, ?>) obj).size();
        }

        return (Integer) ReflectUtil.invoke(obj, "length", EmptyValue.EMPTY_OBJECT_ARRAY);
    }

    /**
     * 用回调的方式，遍历一个对象，可以支持遍历
     * <ul>
     * <li>数组
     * <li>集合
     * <li>Map
     * <li>单一元素
     * </ul>
     *
     * @param obj      对象
     * @param callback 回调
     */
    public static <T> void each(Object obj, Each<T> callback) {
        each(obj, true, callback);
    }

    /**
     * 用回调的方式，遍历一个对象，可以支持遍历
     * <ul>
     * <li>数组
     * <li>集合
     * <li>Map
     * <li>单一元素
     * </ul>
     *
     * @param obj      对象
     * @param loopMap  是否循环 Map，如果循环 Map 则主要看 callback 的 T，如果是 Map.Entry 则循环 Entry
     *                 否循环 value。如果本值为 false， 则将 Map 当作一个完整的对象来看待
     * @param callback 回调
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static <T> void each(Object obj, boolean loopMap, Each<T> callback) {
        if (null == obj || null == callback)
            return;
        try {
            // 循环开始
            if (callback instanceof Loop)
                if (!((Loop) callback).begin())
                    return;

            // 进行循环
            if (obj.getClass().isArray()) {
                int len = Array.getLength(obj);
                for (int i = 0; i < len; i++)
                    try {
                        callback.invoke(i, (T) Array.get(obj, i), len);
                    } catch (Each.ContinueLoop e) {
                        // Ignore error
                    } catch (Each.ExitLoop e) {
                        break;
                    }
            } else if (obj instanceof Collection) {
                int len = ((Collection) obj).size();
                int i = 0;
                for (Iterator<T> it = ((Collection) obj).iterator(); it.hasNext(); )
                    try {
                        callback.invoke(i++, it.next(), len);
                    } catch (Each.ContinueLoop e) {
                        // Ignore error
                    } catch (Each.ExitLoop e) {
                        break;
                    }
            } else if (loopMap && obj instanceof Map) {
                Map map = (Map) obj;
                int len = map.size();
                int i = 0;
                Class<T> eType = (Class<T>) ClassUtil.getTypeArgument(callback.getClass(), 0);
                if (null != eType && eType != Object.class && eType.isAssignableFrom(Map.Entry.class)) {
                    for (Object v : map.entrySet())
                        try {
                            callback.invoke(i++, (T) v, len);
                        } catch (Each.ContinueLoop e) {
                            // Ignore error
                        } catch (Each.ExitLoop e) {
                            break;
                        }

                } else {
                    for (Object v : map.entrySet())
                        try {
                            callback.invoke(i++, (T) ((Map.Entry) v).getValue(), len);
                        } catch (Each.ContinueLoop e) {
                            // Ignore error
                        } catch (Each.ExitLoop e) {
                            break;
                        }
                }
            } else if (obj instanceof Iterator<?>) {
                Iterator<?> it = (Iterator<?>) obj;
                int i = 0;
                while (it.hasNext()) {
                    try {
                        callback.invoke(i++, (T) it.next(), -1);
                    } catch (Each.ContinueLoop e) {
                        // Ignore error
                    } catch (Each.ExitLoop e) {
                        break;
                    }
                }
            } else
                try {
                    callback.invoke(0, (T) obj, 1);
                } catch (Each.ContinueLoop e) {
                    // Ignore error
                } catch (Each.ExitLoop e) {
                    // Ignore error
                }

            // 循环结束
            if (callback instanceof Loop)
                ((Loop) callback).end();
        } catch (Each.LoopException e) {
            throw ExceptionUtil.toRuntimeException(e.getCause());
        }
    }

    public static void print(Object obj) {
        print(obj, false);
    }

    public static void println(Object obj) {
        print(obj, true);
    }

    public static void print(Object obj, final boolean newline) {
        each(obj, new Each<Object>() {
            @Override
            public void invoke(int index, Object ele, int length) throws ExitLoop, ContinueLoop, LoopException {
                System.out.print(ele + (newline ? "\n" : ""));
            }
        });
    }
}