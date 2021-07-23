package org.team4u.base.util;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;

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

        // 处理独立jar包
        if (file.getAbsolutePath().contains(JAR_PATH_SUFFIX)) {
            result = file.getParentFile().getParentFile().getParent();
        }

        return result + File.separatorChar + path;
    }

    public static String standardizedPrefix(String prefix, String joiner) {
        if (StrUtil.isBlank(prefix)) {
            return "";
        } else {
            return prefix.endsWith(joiner) ? prefix.substring(0, prefix.length() - 1) : prefix;
        }
    }
}