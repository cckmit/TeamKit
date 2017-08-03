package org.team4u.kit.core.test;

import com.xiaoleilu.hutool.io.FileUtil;

import java.io.File;

public class TestUtil {

    public static String getTempDir() {
        return FileUtil.file("target").getAbsolutePath() + File.separator + "temp";
    }
}