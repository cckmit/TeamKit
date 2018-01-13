package org.team4u.kit.core.test;


import cn.hutool.core.io.FileUtil;

import java.io.File;

public class TestUtil {

    public static String getTempDir() {
        return FileUtil.file("target").getAbsolutePath() + File.separator + "temp";
    }
}