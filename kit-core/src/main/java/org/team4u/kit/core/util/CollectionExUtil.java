package org.team4u.kit.core.util;

import com.xiaoleilu.hutool.convert.Convert;
import com.xiaoleilu.hutool.util.CollectionUtil;
import com.xiaoleilu.hutool.util.ReflectUtil;
import org.team4u.kit.core.action.Function;

import java.lang.reflect.Array;
import java.util.*;

/**
 * @author Jay Wu
 */
public class CollectionExUtil {

    /**
     * 展开操作符，可以根据指定的字段名称，重组字段值数据
     * ["a", "b"] == collectWithKey([new A(name: "a"),new A(name: "b")], "name")
     */
    @SuppressWarnings("unchecked")
    public static <T, V> List<V> collectWithKey(T[] self, String key) {
        List<V> result = new ArrayList<V>();

        if (ValueUtil.isEmpty(self)) {
            return result;
        }

        for (T it : self) {
            Object value = ReflectUtil.getFieldValue(it, key);
            result.add((V) value);
        }

        return result;
    }

    /**
     * 展开操作符，可以根据指定的字段名称，重组字段值数据
     * ["a", "b"] == collectWithKey([new A(name: "a"),new A(name: "b")], "name")
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> collectWithKey(Iterable<?> self, String key) {
        List<T> result = new ArrayList<T>();

        if (self == null) {
            return result;
        }

        for (Object it : self) {
            Object value = ReflectUtil.getFieldValue(it, key);
            result.add((T) value);
        }

        return result;
    }

    public static <S, T> List<T> collect(Iterable<S> self, Function<S, T> transform) {
        List<T> result = new ArrayList<T>();

        if (self == null) {
            return result;
        }

        for (S it : self) {
            result.add(transform.invoke(it));
        }

        return result;
    }

    public static <T> T find(Iterable<T> self, Function<T, Boolean> filter) {
        for (T it : self) {
            if (filter.invoke(it)) {
                return it;
            }
        }

        return null;
    }

    public static <T> T find(T[] self, Function<T, Boolean> filter) {
        for (T it : self) {
            if (filter.invoke(it)) {
                return it;
            }
        }

        return null;
    }

    public static <T> List<T> findAll(Iterable<T> self, Function<T, Boolean> filter) {
        List<T> result = new ArrayList<T>();

        for (T it : self) {
            if (filter.invoke(it)) {
                result.add(it);
            }
        }

        return result;
    }

    public static <T> List<T> findAll(T[] self, Function<T, Boolean> filter) {
        List<T> result = new ArrayList<T>();

        for (T it : self) {
            if (filter.invoke(it)) {
                result.add(it);
            }
        }

        return result;
    }

    public static <T> boolean any(Iterable<T> self, Function<T, Boolean> predicate) {
        for (T it : self) {
            if (predicate.invoke(it)) {
                return true;
            }
        }

        return false;
    }

    public static <T> boolean all(Iterable<T> self, Function<T, Boolean> predicate) {
        for (T it : self) {
            if (!predicate.invoke(it)) {
                return false;
            }
        }

        return true;
    }

    public static <T, R, C extends Collection<R>> List<R> flatMap(Iterable<T> self,
                                                                  Function<T, Iterable<R>> transform) {
        List<R> result = CollectionUtil.newArrayList();
        return flatMapTo(self, result, transform);
    }

    @SuppressWarnings("unchecked")
    public static <T, R, C extends Collection<R>> C flatMapTo(Iterable<T> self,
                                                              C destination,
                                                              Function<T, Iterable<R>> transform) {
        if (destination == null) {
            return (C) new ArrayList<R>();
        }

        for (T element : self) {
            Iterable<R> iterable = transform.invoke(element);

            if (iterable instanceof Collection) {
                destination.addAll((Collection<? extends R>) iterable);
            } else {
                for (R item : iterable) {
                    destination.add(item);
                }
            }
        }
        return destination;
    }

    /**
     * 对Collection进行分页
     */
    @SuppressWarnings("unchecked")
    public static <E, T extends List<E>> T query(T list, int pageNumber, int pageSize) {
        if (list == null || list.isEmpty()) {
            return list;
        }

        // 无每页显示记录数或者大于总数，直接返回
        if (pageSize <= 0 || pageSize > list.size()) {
            return list;
        }

        int fromIndex = pageSize * (pageNumber - 1);
        int toIndex = fromIndex + pageSize;
        if (toIndex > list.size()) {
            toIndex = list.size();
        }

        return (T) list.subList(fromIndex, toIndex);
    }

    /**
     * 如果是数组或集合取得第一个对象。 否则返回自身
     *
     * @param obj 任意对象
     * @return 第一个代表对象
     */
    @SuppressWarnings("unchecked")
    public static <T> T getFirst(Object obj) {
        if (null == obj)
            return null;

        if (obj instanceof Iterable<?>) {
            return (T) CollectionUtil.getFirst((Iterable<?>) obj);
        }

        if (obj.getClass().isArray())
            return Array.getLength(obj) > 0 ? (T) Array.get(obj, 0) : null;

        return (T) obj;
    }

    /**
     * 获取集合中的最后一个元素，如果集合为空，返回 null
     *
     * @param self 集合
     * @return 第一个元素
     */
    public static <T> T getLast(List<T> self) {
        if (ValueUtil.isEmpty(self)) {
            return null;
        }

        return self.get(self.size() - 1);
    }

    public static <T> T getLast(T[] array) {
        if (ValueUtil.isEmpty(array)) {
            return null;
        }

        return array[array.length - 1];
    }

    /**
     * 将一个或者多个数组填入一个集合。
     *
     * @param <C>     集合类型
     * @param <T>     数组元素类型
     * @param self    集合
     * @param sources 数组 （数目可变）
     * @return 集合对象
     */
    public static <C extends Collection<T>, T> C addAll(C self, T[]... sources) {
        for (T[] objs : sources) {
            Collections.addAll(self, objs);
        }

        return self;
    }

    /**
     * 将集合变成数组，数组的类型为集合的第一个元素的类型。如果集合为空，则返回 null
     *
     * @param self 集合对象
     * @return 数组
     */
    @SuppressWarnings("unchecked")
    public static <E> E[] toArray(Collection<E> self) {
        if (null == self)
            return null;
        if (self.size() == 0)
            return (E[]) new Object[0];

        @SuppressWarnings("ConstantConditions")
        Class<E> eleType = (Class<E>) getFirst(self).getClass();
        return toArray(self, eleType);
    }

    /**
     * 将集合变成指定类型的数组
     *
     * @param col     集合对象
     * @param eleType 数组元素类型
     * @return 数组
     */
    @SuppressWarnings("unchecked")
    public static <E> E[] toArray(Collection<?> col, Class<E> eleType) {
        if (null == col)
            return null;
        Object re = Array.newInstance(eleType, col.size());
        int i = 0;
        for (Object obj : col) {
            if (null == obj)
                Array.set(re, i++, null);
            else
                Array.set(re, i++, Convert.convert(eleType, obj));
        }
        return (E[]) re;
    }

    public static <E> Collection<E> toCollection(E[] array, Class<?> collectionType) {
        Collection<E> result = newInstance(collectionType);
        Collections.addAll(result, array);
        return result;
    }

    public static <E> Collection<E> toCollection(Collection<E> self, Class<?> toCollectionType) {
        Collection<E> result = newInstance(toCollectionType);
        result.addAll(self);
        return result;
    }

    public static <F, T> Collection<T> toCollection(Collection<F> self, Class<?> collectionType, Class<T> eleType) {
        Collection<T> result = newInstance(collectionType);
        for (F it : self) {
            result.add(Convert.convert(eleType, it));
        }
        return result;
    }

    public static <E> List<E> toList(E[] array) {
        List<E> result = new ArrayList<E>();
        Collections.addAll(result, array);
        return result;
    }

    public static <F, T> List<T> toList(F[] array, Class<T> eleType) {
        List<T> result = new ArrayList<T>();
        for (F it : array) {
            result.add(Convert.convert(eleType, it));
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    public static <T> Collection<T> newInstance(Class<?> collectionType) {
        if (collectionType == List.class) {
            return new ArrayList<T>();
        } else if (collectionType == Set.class) {
            return new LinkedHashSet<T>();
        } else {
            return (Collection<T>) ReflectUtil.newInstance(collectionType);
        }
    }
}