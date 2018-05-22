package org.team4u.kit.core.test.lang;

import org.junit.Assert;
import org.junit.Test;
import org.team4u.kit.core.lang.CircleQueue;

/**
 * @author Jay Wu
 */
public class CircleQueueTest {

    @Test
    public void add() {
        final CircleQueue<Integer> queue = new CircleQueue<Integer>(10);
        Assert.assertTrue(queue.toList().isEmpty());
        Assert.assertTrue(queue.isEmpty());
        Assert.assertFalse(queue.isFull());

        for (int i = 0; i < 9; i++) {
            queue.add(i);
        }

        Assert.assertFalse(queue.isEmpty());
        Assert.assertFalse(queue.isFull());
        Assert.assertEquals(9, queue.size());
        Assert.assertEquals(9, queue.toList().size());

        queue.add(9);
        Assert.assertTrue(queue.isFull());
        Assert.assertEquals(10, queue.size());
        Assert.assertEquals("[0, 1, 2, 3, 4, 5, 6, 7, 8, 9]", queue.toList().toString());

        queue.add(10);
        Assert.assertTrue(queue.isFull());
        Assert.assertEquals(10, queue.size());
        Assert.assertEquals("[1, 2, 3, 4, 5, 6, 7, 8, 9, 10]", queue.toList().toString());
    }
}