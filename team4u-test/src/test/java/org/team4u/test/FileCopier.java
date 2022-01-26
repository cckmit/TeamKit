package org.team4u.test;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;

import java.io.File;

public class FileCopier {

    public static void copy(String sourcePath, String targetPath, String expectedSuffix) {
        FileUtil.loopFiles(
                        sourcePath,
                        pathname -> StrUtil.equalsIgnoreCase(FileUtil.getSuffix(pathname), expectedSuffix)
                )
                .forEach(it -> FileUtil.copy(
                        it.getAbsolutePath(),
                        targetPath + File.separator + it.getName(),
                        true
                ));
    }

    public static void main(String[] args) {
        String path = "";
        copy(
                path,
                path + File.separator + "html",
                "html"
        );
    }
}