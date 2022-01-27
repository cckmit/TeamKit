package org.team4u.test;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.log.Log;

import java.io.File;
import java.util.List;

public class FileCopier {

    private static final Log log = Log.get();

    public static void copy(String sourcePath, String targetPath, String expectedSuffix) {
        doAction(sourcePath, targetPath, expectedSuffix, new Action() {
            @Override
            public void execute(File file, String targetPath) {
                FileUtil.copy(
                        file.getAbsolutePath(),
                        targetPath + File.separator + file.getName(),
                        true
                );
            }

            @Override
            public String id() {
                return "copy";
            }
        });
    }

    public static void move(String sourcePath, String targetPath, String expectedSuffix) {
        doAction(sourcePath, targetPath, expectedSuffix, new Action() {
            @Override
            public void execute(File file, String targetPath) {
                FileUtil.move(
                        file,
                        new File(targetPath + File.separator + file.getName()),
                        true
                );
            }

            @Override
            public String id() {
                return "move";
            }
        });
    }

    private static void doAction(String sourcePath, String targetPath, String expectedSuffix, Action action) {
        List<File> files = FileUtil.loopFiles(
                sourcePath,
                pathname -> {
                    if (pathname.getAbsolutePath().startsWith(targetPath)) {
                        return false;
                    }

                    return StrUtil.equalsIgnoreCase(FileUtil.getSuffix(pathname), expectedSuffix);
                }
        );

        files.forEach(it -> {
            log.info(FileCopier.class.getSimpleName() + "|" + action.id() + "|file=" + it.getName());
            action.execute(it, targetPath);
        });

        log.info(FileCopier.class.getSimpleName() + "|" + action.id() + "|size=" + files.size());
    }

    public interface Action {

        void execute(File file, String targetPath);

        String id();
    }

    public static void main(String[] args) {
        String path = "";
        move(path, path + File.separator + "html", "html");
    }
}