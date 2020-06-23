package org.team4u.selector;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.json.JSONUtil;
import org.team4u.selector.application.SelectorAppService;
import org.team4u.selector.domain.config.entity.SelectorConfig;
import org.team4u.selector.domain.config.repository.InMemorySelectorConfigRepository;
import org.team4u.selector.domain.selector.entity.Selector;
import org.team4u.selector.domain.selector.entity.binding.ListBinding;
import org.team4u.selector.domain.selector.entity.binding.SimpleMapBinding;
import org.team4u.selector.domain.selector.entity.binding.SingleValueBinding;
import org.team4u.selector.domain.selector.entity.expression.ExpressionSelectorFactory;
import org.team4u.selector.domain.selector.entity.probability.ProbabilitySelector;
import org.junit.Assert;
import org.junit.Test;
import org.team4u.template.TemplateFunctionService;
import org.team4u.template.infrastructure.BeetlTemplateEngine;

/**
 * 选择器应用服务测试
 *
 * @author jay.wu
 */
public class SelectorAppServiceTest {

    @Test
    public void weightSelect() {
        SelectorAppService s = createService("config/weightConfig.json");

        int a = 0;
        int b = 0;
        int c = 0;

        for (int i = 0; i < 100; i++) {
            switch (s.select("test", new ListBinding().addValue("a").addValue("b"))) {
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
    public void zeroWeightSelect() {
        SelectorAppService s = createService("config/weightConfig.json");

        int none = 0;

        for (int i = 0; i < 100; i++) {
            if (Selector.NONE.equals(s.select("test", new ListBinding().addValue("c")))) {
                none++;
            }
        }
        Assert.assertEquals(100, none);
    }

    @Test
    public void probabilitySelect() {
        SelectorAppService s = createService("config/probabilityConfig.json");
        checkMatchAndNoneCount(s, 1, 100, 100, 0, 0);
        checkMatchAndNoneCount(s, 2, 100, 100, 0, 0);
        checkMatchAndNoneCount(s, 3, 100, 0, 100, 0);
        checkMatchAndNoneCount(s, 4, 100, 1, 99, 5);
    }

    @Test
    public void modProbabilitySelect() {
        SelectorAppService s = createService("config/modProbabilityConfig.json");
        checkMatchAndNoneCount(s, 1, 100, 100, 0, 0);
        checkMatchAndNoneCount(s, 2, 100, 0, 100, 0);
    }

    private void checkMatchAndNoneCount(SelectorAppService s,
                                        int cnd,
                                        int repeatTimes,
                                        int expectedMatchCount,
                                        int expectedNoneCount,
                                        int errorRange) {
        int a = 0;
        int b = 0;

        for (int i = 0; i < repeatTimes; i++) {
            switch (s.select("test", new SingleValueBinding(cnd))) {
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
    public void expressionSelect() {
        SelectorAppService s = createService("config/expressionConfig.json")
                .registerSelectorFactory(
                        new ExpressionSelectorFactory(
                                new BeetlTemplateEngine(new TemplateFunctionService())
                        )
                );

        SimpleMapBinding binding = new SimpleMapBinding();
        binding.set("a", 1);
        String result = s.select("test", binding);
        Assert.assertEquals("x", result);

        binding.set("a", 2);
        result = s.select("test", binding);
        Assert.assertEquals("y", result);
    }

    private SelectorAppService createService(String configPath) {
        SelectorConfig item = JSONUtil.toBean(FileUtil.readUtf8String(configPath), SelectorConfig.class);
        InMemorySelectorConfigRepository repository = new InMemorySelectorConfigRepository(CollUtil.newArrayList(item));
        return new SelectorAppService(repository);
    }
}