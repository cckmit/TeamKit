package org.team4u.exporter.domain;

import cn.hutool.core.collection.CollUtil;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class LazyListTest {

    @Test
    public void hasNext() {
        List<Integer> list = new LazyList<>(new LazyList.PageLoader<Integer>() {
            @Override
            protected List<Integer> load(long pageNumber) {
                if (pageNumber > 2) {
                    return null;
                }

                return CollUtil.newArrayList((int) pageNumber);
            }
        });

        List<Integer> list2 = new ArrayList<>();
        for (Integer integer : list) {
            list2.add(integer);
        }

        Assert.assertEquals(1, list2.get(0).intValue());
        Assert.assertEquals(2, list2.get(1).intValue());
        Assert.assertNull(CollUtil.get(list2, 2));
    }
}