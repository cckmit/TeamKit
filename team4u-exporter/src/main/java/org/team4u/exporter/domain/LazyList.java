package org.team4u.exporter.domain;

import cn.hutool.core.collection.CollUtil;

import java.util.*;
import java.util.concurrent.LinkedTransferQueue;

/**
 * 延迟加载集合
 *
 * @author jay.wu
 */
public class LazyList<E> implements Iterator<E>, List<E> {

    private final Queue<E> queue = new LinkedTransferQueue<>();
    protected Loader<E> loader;

    public LazyList(Loader<E> loader) {
        this.loader = loader;
    }

    @Override
    public boolean isEmpty() {
        return queue.isEmpty();
    }

    @Override
    public Iterator<E> iterator() {
        return this;
    }

    @Override
    public boolean hasNext() {
        if (!isEmpty()) {
            return true;
        }

        // 队列为空，开始构建元素
        List<E> e = loader.load();
        if (CollUtil.isEmpty(e)) {
            return false;
        }

        queue.addAll(e);
        return true;
    }

    @Override
    public E next() {
        return queue.poll();
    }

    @Override
    public void remove() {
        queue.remove();
    }

    public boolean add(E e) {
        return queue.add(e);
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        return queue.addAll(c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int size() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        queue.clear();
    }

    @Override
    public E get(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public E set(int index, E element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void add(int index, E element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public E remove(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int indexOf(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int lastIndexOf(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ListIterator<E> listIterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean contains(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object[] toArray() {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException();
    }

    public interface Loader<E> {

        List<E> load();
    }

    public static abstract class PageLoader<E> implements Loader<E> {

        private long pageNumber = 1;

        @Override
        public List<E> load() {
            return load(pageNumber++);
        }

        protected abstract List<E> load(long pageNumber);
    }
}