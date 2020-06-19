package org.team4u.sc;

import java.util.Objects;

/**
 * 测试样例
 *
 * @author jay.wu
 */
public class TestDemo {
    private String code;

    public String getCode() {
        return code;
    }

    public TestDemo setCode(String code) {
        this.code = code;
        return this;
    }

    @Override
    public String toString() {
        return code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestDemo testDemo = (TestDemo) o;
        return Objects.equals(code, testDemo.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }
}
