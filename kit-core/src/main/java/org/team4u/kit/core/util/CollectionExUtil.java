package org.team4u.kit.core.util;

import com.xiaoleilu.hutool.util.CollectionUtil;
import com.xiaoleilu.hutool.util.ReflectUtil;
import org.team4u.kit.core.action.Function;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
}
