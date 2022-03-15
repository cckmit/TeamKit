package org.team4u.selector;

import org.junit.Assert;
import org.junit.Test;
import org.team4u.base.config.LocalJsonConfigService;
import org.team4u.selector.application.SelectorAppService;
import org.team4u.selector.domain.selector.Selector;
import org.team4u.selector.domain.selector.SelectorValueHandler;
import org.team4u.selector.domain.selector.SelectorValueService;
import org.team4u.selector.domain.selector.binding.ListBinding;
import org.team4u.selector.domain.selector.binding.SimpleMapBinding;
import org.team4u.selector.domain.selector.binding.SingleValueBinding;
import org.team4u.selector.domain.selector.map.DynamicMapSelector;
import org.team4u.selector.domain.selector.probability.ProbabilitySelector;
import org.team4u.selector.infrastructure.persistence.JsonSelectorConfigRepository;

/**
 * 选择器应用服务测试
 *
 * @author jay.wu
 */
public class SelectorAppServiceTest {

    private final SelectorAppService s = createService();

    @Test
    public void dynamicMap() {
        SelectorValueService valueService = new SelectorValueService();
        valueService.registerPolicy(new SelectorValueHandler() {
            @Override
            public String handle(Context context) {
                return context.getParams().getStr("name");
            }

            @Override
            public String id() {
                return "test";
            }
        });


        checkDynamicMap(s, valueService, "a", "x");
        checkDynamicMap(s, valueService, "b", "y");
        checkDynamicMap(s, valueService, "c", "z");
    }

    private void checkDynamicMap(SelectorAppService s,
                                 SelectorValueService valueService,
                                 String expected, String key) {
        Assert.assertEquals(
                expected,
                s.select("dynamicMap", new DynamicMapSelector.Binding(key, valueService))
        );
    }

    @Test
    public void map() {
        Assert.assertEquals("1", s.select("map", new SingleValueBinding("x")));
        Assert.assertEquals("2", s.select("map", new SingleValueBinding("y")));
        Assert.assertEquals("3", s.select("map", new SingleValueBinding("z")));
        Assert.assertEquals("3", s.select("map", new SingleValueBinding("z1")));
        Assert.assertEquals("4", s.select("map", new SingleValueBinding("1")));
    }

    @Test
    public void whitelist() {
        Assert.assertTrue(s.match("whitelist1", new SimpleMapBinding().set("a", 1)));
        Assert.assertFalse(s.match("whitelist2", new SimpleMapBinding().set("a", 2)));
        Assert.assertTrue(s.match("whitelist1", new SimpleMapBinding().set("b", 3)));
        Assert.assertTrue(s.match("whitelist2", new SimpleMapBinding().set("d", 1)));
        Assert.assertFalse(s.match("whitelist1", new SimpleMapBinding().set("d", 2)));
    }

    @Test
    public void weight() {
        int a = 0;
        int b = 0;
        int c = 0;

        for (int i = 0; i < 100; i++) {
            switch (s.select("weight2", new ListBinding().addValue("a").addValue("b"))) {
                case "a": {
                    a++;
                    break;
                }

                case "b": {
                    b++;
                    break;
                }

                case "c": {
                    c++;
                    break;
                }
            }
        }
        Assert.assertEquals(100, a + b + c);
        Assert.assertTrue(Math.abs(a - b) < 30);
        Assert.assertEquals(0, c);
    }

    @Test
    public void zeroWeight() {
        int none = 0;

        for (int i = 0; i < 100; i++) {
            if (Selector.NONE.equals(s.select("weight1", new ListBinding().addValue("c")))) {
                none++;
            }
        }
        Assert.assertEquals(100, none);
    }

    @Test
    public void probability() {
        checkMatchAndNoneCount("probability1", 1, 100, 100, 0, 0);
        checkMatchAndNoneCount("probability1", 2, 100, 100, 0, 0);
        checkMatchAndNoneCount("probability2", 3, 100, 0, 100, 0);
        checkMatchAndNoneCount("probability2", 4, 100, 1, 99, 5);
    }

    @Test
    public void modProbability() {
        checkMatchAndNoneCount("modProbabilityConfig", 0, 100, 0, 100, 0);
        checkMatchAndNoneCount("modProbabilityConfig", 1, 100, 100, 0, 0);
        checkMatchAndNoneCount("modProbabilityConfig", 2, 100, 0, 100, 0);
    }

    private void checkMatchAndNoneCount(String configId,
                                        int binding,
                                        int repeatTimes,
                                        int expectedMatchCount,
                                        int expectedNoneCount,
                                        int errorRange) {
        int a = 0;
        int b = 0;

        for (int i = 0; i < repeatTimes; i++) {
            switch (s.select(configId, new SingleValueBinding(binding))) {
                case ProbabilitySelector.MATCH: {
                    a++;
                    break;
                }

                case ProbabilitySelector.NONE: {
                    b++;
                    break;
                }
            }
        }

        try {
            Assert.assertTrue(a - expectedMatchCount <= errorRange);
            Assert.assertTrue(b - expectedNoneCount <= errorRange);
        } catch (AssertionError e) {
            System.out.println("a:" + a + ", b:" + b);
            throw e;
        }
    }

    @Test
    public void expression() {
        expression("expressionBeetl");
        expression("expressionJs");
    }

    private void expression(String configId) {
        SimpleMapBinding binding = new SimpleMapBinding();
        binding.set("a", 1);
        String result = s.select(configId, binding);
        Assert.assertEquals("x", result);

        binding.set("a", 2);
        result = s.select(configId, binding);
        Assert.assertEquals("y", result);
    }

    private SelectorAppService createService() {
        return new SelectorAppService(new JsonSelectorConfigRepository(new LocalJsonConfigService("config")));
    }
}