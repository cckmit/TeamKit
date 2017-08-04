package org.team4u.kit.core.util;

import com.xiaoleilu.hutool.convert.Convert;
import com.xiaoleilu.hutool.util.ClassUtil;
import com.xiaoleilu.hutool.util.CollectionUtil;
import com.xiaoleilu.hutool.util.ReflectUtil;
import com.xiaoleilu.hutool.util.StrUtil;
import org.team4u.kit.core.action.Each;
import org.team4u.kit.core.error.ExceptionUtil;
import org.team4u.kit.core.lang.Pair;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.Callable;

public abstract class MapUtil {

    /**
     * 创建一个一个键的 Map 对象
     *
     * @param key   键
     * @param value 值
     * @return Map 对象
     */
    public static <K, V> Map<K, V> singleHashMap(K key, V value) {
        HashMap<K, V> map = new HashMap<K, V>();
        map.put(key, value);
        return map;
    }

    public static <K, V> Map<K, V> hashMapOf(Pair<K, V>... pairs) {
        HashMap<K, V> map = new HashMap<K, V>();
        for (Pair<K, V> pair : pairs) {
            map.put(pair.getKey(), pair.getValue());
        }
        return map;
    }

    @SuppressWarnings("unchecked")
    public static <K, V> Map<K, V> newInstance(Class<?> toType) {
        if (toType == Map.class) {
            return newHashMap();
        } else {
            return (Map<K, V>) ClassUtil.newInstance(toType);
        }
    }

    /**
     * map sort
     */
    public static <K, V> Map<K, V> sortMap(Map<K, V> map, Comparator<Map.Entry<K, V>> compator) {
        Map<K, V> result = new LinkedHashMap<K, V>();
        List<Map.Entry<K, V>> entries = new ArrayList<Map.Entry<K, V>>(map.entrySet());
        Collections.sort(entries, compator);
        for (Map.Entry<K, V> entry : entries) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }

    public static <K, V> boolean isEmpty(Map<K, V> map) {
        return (map == null || map.isEmpty());
    }

    public static boolean isNotEmpty(Map<?, ?> map) {
        return !isEmpty(map);
    }

    public static synchronized <K, V> V putIfAbsent(Map<K, V> map, K key, Callable<V> valueFactory) {
        V v = map.get(key);

        if (v == null) {
            try {
                v = valueFactory.call();
                map.put(key, v);
            } catch (Exception e) {
                throw ExceptionUtil.toRuntimeException(e);
            }
        }

        return v;
    }

    /**
     * 获得表中的第一个名值对
     *
     * @param map 表
     * @return 第一个名值对
     */
    public static <K, V> Map.Entry<K, V> first(Map<K, V> map) {
        if (isEmpty(map)) {
            return null;
        }

        return map.entrySet().iterator().next();
    }

    public static <V> Map<String, V> toCamelCaseKeyMap(Map<String, V> map) {
        Map<String, V> result = new HashMap<String, V>();
        for (Map.Entry<String, V> entry : map.entrySet()) {
            String key = StrUtil.toCamelCase(entry.getKey());
            result.put(key, entry.getValue());
        }

        return result;
    }

    public static <K, V> Map<K, V> newHashMap() {
        return new HashMap<K, V>();
    }

    public static Map<String, ?> toPathMap(Map<?, ?> map) {
        return toPathMap(map, "node.");
    }

    @SuppressWarnings("unchecked")
    public static Map<String, ?> toPathMap(Map<?, ?> map, String prefixToRemove) {
        PathMapBuilder builder = new PathMapBuilder();
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            builder.put(prefixToRemove + entry.getKey().toString(), entry.getValue());
        }
        return (Map<String, ?>) builder.build();
    }

    /**
     * 根据一个 Map，和给定的对象类型，创建一个新的 JAVA 对象
     *
     * @param src    Map 对象
     * @param toType JAVA 对象类型
     * @return JAVA 对象
     */
    public static <T> T toObject(Map<?, ?> src, Class<T> toType) {
        return MapToObjectBuilder.build(src, toType);
    }

    /**
     * 将对象转换成 Map
     *
     * @param obj     POJO 对象
     * @param mapType Map 的类型
     * @return Map 对象
     */
    public static <T extends Map<String, Object>> T toMap(Object obj, Class<T> mapType) {
        return ObjectToMapBuilder.build(obj, mapType);
    }

    /**
     * 将对象转换成 HashMap
     *
     * @param obj POJO 对象
     * @return Map 对象
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> toMap(Object obj) {
        return ObjectToMapBuilder.build(obj, HashMap.class);
    }

    @SuppressWarnings("unchecked")
    private static class ObjectToMapBuilder {
        /**
         * 将对象转换成 Map
         *
         * @param obj     POJO 对象
         * @param mapType Map 的类型
         * @return Map 对象
         */
        public static <T extends Map<String, Object>> T build(Object obj, Class<T> mapType) {
            if (ClassUtil.isSimpleValueType(obj.getClass())
                    || obj.getClass().isArray()
                    || Collection.class.isAssignableFrom(obj.getClass())) {
                throw new IllegalArgumentException("obj为基本类型,无法转为map");
            }

            T map = ReflectUtil.newInstance(mapType);
            Map<Object, Object> cache = newHashMap();

            if (obj instanceof Map) {
                return (T) parseValue(obj, map.getClass(), cache);
            } else {
                return parseObject(obj, map, cache);
            }
        }

        private static <T extends Map<String, Object>> T parseObject(Object obj, T map, Map<Object, Object> cache) {
            if (obj == null) {
                return map;
            }

            for (Field field : ReflectUtil.getFields(obj.getClass())) {
                if (!FieldUtil.isNormalField(field)) {
                    continue;
                }

                Object v = ReflectUtil.getFieldValue(obj, field);
                if (v == null) {
                    map.put(field.getName(), null);
                    continue;
                }

                map.put(field.getName(), parseValue(v, map.getClass(), cache));
            }

            return map;
        }

        private static <T extends Map<String, Object>> Object parseValue(Object obj,
                                                                         final Class<T> mapType,
                                                                         final Map<Object, Object> cache) {
            if (cache.containsKey(obj)) {
                return cache.get(obj);
            }

            Object result;

            if (ClassUtil.isSimpleValueType(obj.getClass())) {
                return obj;
            } else if (obj.getClass().isArray() || Collection.class.isAssignableFrom(obj.getClass())) {
                Class<?> componentType = obj.getClass().getComponentType();
                if (componentType != null && ClassUtil.isSimpleValueType(componentType)) {
                    return obj;
                }

                final List list = new ArrayList();

                ValueUtil.each(obj, new Each<Object>() {
                    @Override
                    public void invoke(int index, Object ele, int length) throws ExitLoop, ContinueLoop, LoopException {
                        list.add(parseValue(ele, mapType, cache));
                    }
                });

                result = list;
            } else if (Map.class.isAssignableFrom(obj.getClass())) {
                Map fieldMap = ReflectUtil.newInstance(mapType);

                for (Map.Entry<?, ?> entry : ((Map<?, ?>) obj).entrySet()) {
                    fieldMap.put(
                            parseValue(entry.getKey(), fieldMap.getClass(), cache),
                            parseValue(entry.getValue(), fieldMap.getClass(), cache)
                    );
                }

                result = fieldMap;
            } else {
                result = parseObject(obj, (Map) ReflectUtil.newInstance(mapType), cache);
            }

            cache.put(obj, result);
            return result;
        }
    }

    @SuppressWarnings("unchecked")
    private static class MapToObjectBuilder {

        public static <T> T build(Map<?, ?> src, Class<T> toType) {
            if (null == toType) {
                throw new IllegalArgumentException("target type is Null");
            }

            if (toType == Map.class) {
                return (T) src;
            }

            if (Map.class.isAssignableFrom(toType)) {
                Map map = newInstance(toType);
                map.putAll(src);
                return (T) map;
            }

            // 数组
            if (toType.isArray()) {
                return (T) Convert.convert(toType.getComponentType(), src.values());
            }

            // List
            if (List.class == toType) {
                return (T) new ArrayList<T>((Collection<? extends T>) src.values());
            }

            // Object
            return parseObject(src, toType);
        }

        private static <T> T parseObject(Map<?, ?> src, Class<T> toType) {
            T obj = ReflectUtil.newInstance(toType);

            for (Field field : ReflectUtil.getFields(toType)) {
                if (!FieldUtil.isNormalField(field)) {
                    continue;
                }

                if (src.containsKey(field.getName())) {
                    Object mapValue = src.get(field.getName());
                    if (null == mapValue) {
                        continue;
                    }

                    Class<?> fieldType = field.getType();
                    Object fieldValue;
                    // 集合
                    if (mapValue instanceof Collection) {
                        Collection actualMapValue = (Collection) mapValue;
                        if (Collection.class.isAssignableFrom(field.getType())) {
                            fieldValue = fromCollection(field, actualMapValue);
                        } else {
                            fieldValue = Convert.convert(fieldType, CollectionUtil.getFirst(actualMapValue));
                        }
                    }
                    // 处理mapValue为字符串形式的集合
                    else if (Collection.class.isAssignableFrom(fieldType) && mapValue instanceof String) {
                        fieldValue = fromString(field, (String) mapValue);
                    }
                    // Map
                    else if (mapValue instanceof Map && Map.class.isAssignableFrom(fieldType)) {
                        fieldValue = fromMap(field, (Map) mapValue);
                    } else if (mapValue instanceof Map) {
                        fieldValue = toObject((Map<?, ?>) mapValue, fieldType);
                    }
                    // 强制转换
                    else {
                        fieldValue = Convert.convert(fieldType, mapValue);
                    }

                    ReflectUtil.setFieldValue(obj, field, fieldValue);
                }
            }

            return obj;
        }

        private static Object fromCollection(Field field, Collection value) {
            Class<?> fieldType = field.getType();

            // 集合到数组
            if (fieldType.isArray()) {
                return Convert.convert(fieldType.getComponentType(), value);
            }
            // 集合到集合
            else {
                Class eleType = FieldUtil.getGenericTypes(field, 0);
                return Convert.toCollection(fieldType, eleType, value);
            }
        }

        private static Object fromString(Field field, String value) {
            Class<?> fieldType = field.getType();
            Class eleType = FieldUtil.getGenericTypes(field, 0);

            Collection collectionValue = (Collection) Convert.convert(fieldType, value);
            return Convert.toCollection(fieldType, eleType, collectionValue);
        }

        private static Object fromMap(Field field, Map value) {
            Class<?> fieldType = field.getType();
            final Map map = newInstance(fieldType);

            // 赋值
            final Class<?> valType = FieldUtil.getGenericTypes(field, 1);
            ValueUtil.each(value, new Each<Map.Entry>() {
                public void invoke(int i, Map.Entry en, int length) {
                    map.put(en.getKey(), Convert.convert(valType, en.getValue()));
                }
            });

            return map;
        }
    }

    /**
     * 对象路径节点转换.<br/>
     * 将URL中的字符串参数名转换成对结构<br/>
     * URL规则:
     * <ul>
     * <li>对象与属性之间使用"."做为连接符
     * <li>数组,Collection对象, 使用"[]"或":"做为索引引用符. <b style='color:red'>索引只是一个参考字段, 不会根据其值设置索引</b>
     * <li>Map使用"()"或"."分割key值
     * </ul>
     * 例:<br>
     * <code>
     * Object:  node.str = str<br>
     * list:    node.list[1].str = abc;<br>
     * node.list:2.str = 2<br>
     * set:     node.set[2].str = bbb<br>
     * node.set:jk.str = jk<br>
     * Map:     node.map(key).str = bb;<br>
     * node.map.key.name = map<br>
     * <p>
     * </code>
     */
    private static class PathMapBuilder {
        private static final char separator = '.';
        private static final char LIST_SEPARATOR = ':';
        private static final int TYPE_NONE = 0;
        private static final int TYPE_LIST = 1;
        //节点名
        private String name;
        //叶子节点的值
        private Object value;
        //是否是叶子节点
        private boolean leaf = true;
        //子节点
        private Map<String, PathMapBuilder> child = new LinkedHashMap<String, PathMapBuilder>();
        //类型
        private int type = TYPE_NONE;

        /**
         * 初始化当前结点
         */
        public void put(String path, Object value) {
            StringBuilder sb = new StringBuilder();
            char[] chars = path.toCharArray();
            OUT:
            for (int i = 0; i < chars.length; i++) {
                char c = chars[i];
                switch (c) {
                    case '[':
                    case '(':
                        i++;
                        StringBuilder sb2 = new StringBuilder();
                        boolean isNumber = true;
                        for (; i < chars.length; i++) {
                            char c2 = chars[i];
                            switch (c2) {
                                case ']':
                                case ')':
                                    if ((c == '[' && c2 == ']') || (c == '(' && c2 == ')')) {
                                        if (isNumber && !(c == '(')) {
                                            sb.append(':').append(sb2);
                                        } else {
                                            sb.append('.').append(sb2);
                                        }
                                        continue OUT;
                                    }
                            }
                            isNumber = isNumber && Character.isDigit(c2);
                            sb2.append(c2);
                        }
                        break;
                    default:
                        sb.append(c);
                        break;
                }
            }
            path = sb.toString();
            putPath(path, value);
        }

        private void putPath(String path, Object value) {
            init(path);
            String subPath = fetchSubPath(path);
            if ("".equals(subPath) || path.equals(subPath)) {
                this.value = value;
                return;
            }
            leaf = false;
            addChild(subPath, value);
        }

        /**
         * 添加子结点
         */
        private void addChild(String path, Object value) {
            String subname = fetchName(path);
            PathMapBuilder onn = child.get(subname);
            if (onn == null) {
                onn = new PathMapBuilder();
            }
            onn.putPath(path, value);
            child.put(subname, onn);
        }

        /**
         * 初始化name, type信息
         */
        private void init(String path) {
            String key = fetchNode(path);
            if (isList(key)) {
                type = TYPE_LIST;
                name = key.substring(0, key.indexOf(LIST_SEPARATOR));
                return;
            }
            name = key;
        }

        /**
         * 提取子路径
         */
        private String fetchSubPath(String path) {
            if (isList(fetchNode(path))) {
                return path.substring(path.indexOf(LIST_SEPARATOR) + 1);
            }
            return path.substring(path.indexOf(separator) + 1);
        }


        /**
         * 取得节点名
         */
        private String fetchNode(String path) {
            if (path.indexOf(separator) <= 0) {
                return path;
            }
            return path.substring(0, path.indexOf(separator));
        }


        /**
         * 取得节点的name信息
         */
        private String fetchName(String path) {
            String key = fetchNode(path);
            if (isList(key)) {
                return key.substring(0, key.indexOf(LIST_SEPARATOR));
            }
            return key;
        }

        @SuppressWarnings("unchecked")
        public Object build() {
            if (isLeaf()) {
                return value;
            }

            if (type == TYPE_LIST) {
                List list = new ArrayList();
                for (String o : child.keySet()) {
                    list.add(child.get(o).build());
                }
                return list;
            }

            Map map = new HashMap();
            for (String o : child.keySet()) {
                map.put(o, child.get(o).build());
            }
            return map;
        }

        /**
         * 是否是list节点
         */
        private boolean isList(String key) {
            return key.indexOf(LIST_SEPARATOR) > 0;
        }

        private String getName() {
            return name;
        }

        private Object getValue() {
            return value;
        }

        private boolean isLeaf() {
            return leaf;
        }
    }
}