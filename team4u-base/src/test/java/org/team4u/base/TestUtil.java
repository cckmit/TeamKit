package org.team4u.base;


import cn.hutool.core.io.FileUtil;
import com.alibaba.fastjson.JSON;

public class TestUtil {

    public static String format(Object value) {
        return JSON.toJSONString(value);
    }

    public static void println(Object value) {
        System.out.println(format(value));
    }

    public static Object loadJsonObject(String path) {
        return JSON.parse(FileUtil.readUtf8String(path));
    }

    public static final String TEST = "test";
    public static final String TEST1 = "test1";
    public static final String TEST2 = "test2";
}