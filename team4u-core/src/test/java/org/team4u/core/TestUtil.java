package org.team4u.core;


import cn.hutool.core.io.FileUtil;
import com.alibaba.fastjson.JSON;

import java.io.File;

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
}