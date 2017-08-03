package org.team4u.kit.core.test.util;

import com.xiaoleilu.hutool.util.CollectionUtil;
import org.junit.Assert;
import org.junit.Test;
import org.team4u.kit.core.lang.Pair;
import org.team4u.kit.core.util.MapUtil;

import java.util.ArrayList;
import java.util.Map;

import static org.team4u.kit.core.util.MapUtil.hashMapOf;
import static org.team4u.kit.core.util.MapUtil.singleHashMap;

public class MapUtilTest {

    @Test
    @SuppressWarnings("unchecked")
    public void toPathMap() {
        Map<String, Map<String, Map<String, ArrayList<Integer>>>> e = singleHashMap("a", singleHashMap("b", singleHashMap("c1", CollectionUtil.newArrayList(1))));
        e.get("a").get("b").put("c2", CollectionUtil.newArrayList(2));
        Assert.assertEquals(e, MapUtil.toPathMap(hashMapOf(Pair.create("a.b.c1[0]", 1), Pair.create("a.b.c2[0]", 2))));
    }
}
