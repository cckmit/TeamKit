package org.team4u.test;

public class Demo {

    /**
     * 调用任意方法
     */
    public String commonMethod() {
        return " www ".trim() + "." + " javastack".substring(1) + "www.javastack.cn".startsWith(".com");
    }


    /**
     * 调用成员、静态方法
     */
    public String memberMethod(String s) {
        return "{ \"result\": \"" + innerMethod(s) + staticMethod() + "\"}";
    }

    private static String staticMethod() {
        return "WWW_JAVASTACK_CN";
    }

    private String innerMethod(String website) {
        return "our website is: " + website;
    }
}
