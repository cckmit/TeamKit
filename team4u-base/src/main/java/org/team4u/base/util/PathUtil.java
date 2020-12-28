package org.team4u.base.util;

import cn.hutool.core.io.FileUtil;

import java.io.File;

/**
 * 路径工具类
 *
 * @author jay.wu
 */
public class PathUtil {

    public static final String JAR_PATH_SUFFIX = ".jar!";

    /**
     * 获取基于运行时路径的绝对路径
     *
     * @param path 相对路径
     * @return 绝对路径
     */
    public static String runningPath(String path) {
        File file = FileUtil.file(".");
        String result = file.getAbsolutePath();

        if (file.getAbsolutePath().contains(JAR_PATH_SUFFIX)) {
            result = file.getParentFile().getParentFile().getParent();
        }

        return result + File.pathSeparatorChar + path;
    }
}