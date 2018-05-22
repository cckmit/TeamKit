package org.team4u.kit.core.lang;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ClassUtil;
import org.team4u.kit.core.action.Function;
import org.team4u.kit.core.util.CollectionExUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 环形数组
 *
 * @author Jay Wu
 */
public class CircleQueue<T> {
    /**
     * 容量
     */
    private int capacity;
    /**
     * 元素数组
     */
    private Object[] elementData;
    /**
     * 队头
     */
    private int head = 0;
    /**
     * 队尾
     */
    private int tail = 0;
    /**
     * 元素类型
     */
    private Class<T> elementClass;

    public CircleQueue(int capacity) {
        this.capacity = capacity;
        elementData = new Object[this.capacity];

        //noinspection unchecked
        elementClass = (Class<T>) ClassUtil.getTypeArgument(this.getClass());
    }

    /**
     * 获取元素数量
     */
    public int size() {
        if (isEmpty()) {
            return 0;
        } else if (isFull()) {
            return capacity;
        } else {
            return tail + 1;
        }
    }

    /**
     * 插入队尾一个元素
     */
    public void add(final T element) {
        if (isEmpty()) {
            elementData[0] = element;
        } else if (isFull()) {
            elementData[head] = element;
            head = head + 1 == capacity ? 0 : head + 1;
            tail = tail + 1 == capacity ? 0 : tail + 1;
        } else {
            elementData[tail + 1] = element;
            tail++;
        }
    }

    public boolean isEmpty() {
        return tail == head && tail == 0 && elementData[tail] == null;
    }

    public boolean isFull() {
        return head != 0 && head - tail == 1 || head == 0 && tail == capacity - 1;
    }

    /**
     * 清除数组
     */
    public void clear() {
        Arrays.fill(elementData, null);
        head = 0;
        tail = 0;
    }

    /**
     * 队列头部元素
     */
    @SuppressWarnings("unchecked")
    public T first() {
        return (T) elementData[head];
    }

    /**
     * 队列尾部元素
     */
    @SuppressWarnings("unchecked")
    public T last() {
        return (T) elementData[tail];
    }

    /**
     * @return 取 循环队列里的值（先进的index=0）
     */
    @SuppressWarnings("unchecked")
    public List<T> toList() {
        List<T> elementDataSort = new ArrayList<T>(capacity);
        Object[] elementDataCopy = elementData.clone();

        if (isFull()) {
            int indexMax = capacity;
            int indexSort = 0;
            for (int i = head; i < indexMax; ) {
                elementDataSort.add(indexSort, (T) elementDataCopy[i]);
                indexSort++;
                i++;
                if (i == capacity) {
                    i = 0;
                    indexMax = head;
                }
            }
        } else if (!isEmpty()) {
            elementDataSort = (List<T>) Convert.toList(
                    elementClass,
                    CollectionExUtil.findAll(elementDataCopy, new Function<Object, Boolean>() {
                        @Override
                        public Boolean invoke(Object obj) {
                            return obj != null;
                        }
                    }));
        }

        return elementDataSort;
    }
}