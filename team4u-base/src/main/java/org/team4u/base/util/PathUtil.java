package org.team4u.base.util;

import cn.hutool.core.io.FileUtil;

import java.io.File;

public class PathUtil {

    public static String getPath(String path) {
        File file = FileUtil.file(".");
        String result = file.getAbsolutePath();

        if (file.getAbsolutePath().contains(".jar!")) {
            result = FileUtil.file(".").getParentFile().getParentFile().getParent();
        }

        return result + "/" + path;
    }
}