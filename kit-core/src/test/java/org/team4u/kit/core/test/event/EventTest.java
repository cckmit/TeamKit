package org.team4u.kit.core.test.event;

import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.team4u.kit.core.event.api.AbstractEvent;

public class EventTest {
    @Test
    public void x() {
        System.out.println(JSON.toJSONString(new AbstractEvent() {
        }));
    }
}