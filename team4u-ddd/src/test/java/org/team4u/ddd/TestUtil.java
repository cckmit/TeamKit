package org.team4u.ddd;

import cn.hutool.core.io.FileUtil;

public class TestUtil {

    public static String TEST_ID = "TEST";

    public static String readFile(String path) {
        return FileUtil.readUtf8String(path);
    }
}
