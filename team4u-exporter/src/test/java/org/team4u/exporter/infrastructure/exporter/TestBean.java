package org.team4u.exporter.infrastructure.exporter;

import cn.hutool.core.annotation.Alias;

public class TestBean {
    @Alias("1")
    private String a;
    @Alias("2")
    private String b;

    public TestBean(String a, String b) {
        this.a = a;
        this.b = b;
    }

    public String getA() {
        return a;
    }

    public TestBean setA(String a) {
        this.a = a;
        return this;
    }

    public String getB() {
        return b;
    }

    public TestBean setB(String b) {
        this.b = b;
        return this;
    }

    @Override
    public String toString() {
        return a + b;
    }
}
