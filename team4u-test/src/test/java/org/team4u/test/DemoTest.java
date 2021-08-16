package org.team4u.test;

import com.alibaba.testable.core.annotation.MockMethod;
import org.junit.Test;

import static com.alibaba.testable.core.matcher.InvokeVerifier.verify;
import static org.junit.Assert.assertEquals;

/**
 * https://alibaba.github.io/testable-mock/#/zh-cn/doc/use-mock
 *
 * @author jay.wu
 */
public class DemoTest {

    private final Demo demo = new Demo();

    /**
     * Mock 任意方法
     */
    @MockMethod(targetClass = String.class)
    private String trim() {
        return "http://www";
    }

    @MockMethod(targetClass = String.class, targetMethod = "substring")
    private String substr(int i) {
        return "javastack.cn_";
    }

    @MockMethod(targetClass = String.class)
    private boolean startsWith(String website) {
        return false;
    }

    /**
     * Mock 成员方法
     */
    @MockMethod(targetClass = Demo.class)
    private String innerMethod(String text) {
        return "mock_" + text;
    }

    /**
     * Mock 静态方法
     */
    @MockMethod(targetClass = Demo.class)
    private String staticMethod() {
        return "_MOCK_JAVASTACK";
    }

    @Test
    public void commonMethodTest() {
        assertEquals("http://www.javastack.cn_false", demo.commonMethod());
        verify("trim").withTimes(1);
        verify("substr").withTimes(1);
        verify("startsWith").withTimes(1);
    }

    @Test
    public void memberMethodTest() {
        assertEquals("{ \"result\": \"mock_hello_MOCK_JAVASTACK\"}", demo.memberMethod("hello"));
        verify("innerMethod").withTimes(1);
        verify("staticMethod").withTimes(1);
        verify("innerMethod").with("hello");
        verify("staticMethod").with();
    }
}
