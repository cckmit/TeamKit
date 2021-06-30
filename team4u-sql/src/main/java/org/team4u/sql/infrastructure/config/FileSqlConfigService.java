package org.team4u.sql.infrastructure.config;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.io.watch.WatchMonitor;
import cn.hutool.core.io.watch.Watcher;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.log.Log;
import org.team4u.base.config.ConfigService;
import org.team4u.base.log.LogMessage;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 基于文件的sql配置
 *
 * @author jay.wu
 */
public class FileSqlConfigService implements ConfigService {

    private final Log log = Log.get();

    private final Config config;

    protected Map<String, String> sqlList = new ConcurrentHashMap<>();

    public FileSqlConfigService(Config config) {
        this.config = config;

        refresh();
        watch();
    }

    @Override
    public String get(String key) {
        return sqlList.get(key);
    }

    @Override
    public <T> T get(String key, T defaultValue) {
        String value = get(key);
        if (ObjectUtil.isEmpty(value)) {
            return defaultValue;
        }
        //noinspection unchecked
        return (T) Convert.convert(defaultValue.getClass(), value);
    }

    public void put(String key, String value) {
        sqlList.put(key, value);
    }

    private void watch() {
        if (!config.isWatch() || !FileUtil.exist(config.getPath())) {
            return;
        }

        WatchMonitor watchMonitor = WatchMonitor.create(ResourceUtil.getResource(config.getPath()), WatchMonitor.ENTRY_MODIFY);
        watchMonitor.setWatcher(new Watcher() {
            @Override
            public void onCreate(WatchEvent<?> watchEvent, Path path) {
            }

            @Override
            public void onModify(WatchEvent<?> watchEvent, Path path) {
                refresh();
            }

            @Override
            public void onDelete(WatchEvent<?> watchEvent, Path path) {
            }

            @Override
            public void onOverflow(WatchEvent<?> watchEvent, Path path) {

            }
        });
        watchMonitor.start();
    }

    public void refresh() {
        Map<String, String> sqlList = new HashMap<>();

        for (File file : FileUtil.loopFiles(config.getPath())) {
            refresh(sqlList, file);
        }

        this.sqlList = sqlList;
    }

    private void refresh(Map<String, String> sqlList, File file) {
        LogMessage lm = new LogMessage(this.getClass().getSimpleName(), "refresh")
                .append("fileName", file.getName());
        try {
            parseFile(sqlList, ResourceUtil.getReader(file.getPath(), CharsetUtil.CHARSET_UTF_8));
        } catch (IOException e) {
            log.debug(lm.fail().toString(), e);
        }
        log.debug(lm.success().toString());
    }

    public void parseFile(Map<String, String> sqlList, Reader r) throws IOException {
        try {
            BufferedReader br;
            if (r instanceof BufferedReader) {
                br = (BufferedReader) r;
            } else {
                br = new BufferedReader(r);
            }

            StringBuilder key = new StringBuilder();
            StringBuilder sb = new StringBuilder();
            OUT:
            while (br.ready()) {
                String line = nextLineTrim(br);
                if (line == null)
                    break;
                if (line.startsWith("/*")) {
                    if (key.length() > 0 && line.contains("*/") && !line.endsWith("*/")) {
                        sb.append(line);
                        continue;
                    }
                    if (key.length() > 0 && sb.length() > 0) {
                        saveSql(sqlList, key.toString(), sb.toString());
                    }
                    key.setLength(0);
                    sb.setLength(0);

                    if (line.endsWith("*/")) {
                        if (line.length() > 4)
                            key.append(line.substring(2, line.length() - 2).trim());
                        continue;
                    } else {
                        key.append(line.substring(2).trim());
                        while (br.ready()) {
                            line = nextLineTrim(br);
                            if (line == null)
                                break OUT;
                            if (line.endsWith("*/")) {
                                if (line.length() > 2)
                                    key.append(line.substring(0, line.length() - 2).trim());
                                continue OUT;
                            } else {
                                key.append(line);
                            }
                        }
                    }
                }
                if (key.length() == 0) {
                    log.info("skip not key sql line {}", line);
                    continue;
                }
                if (sb.length() > 0)
                    sb.append("\n");
                sb.append(line);
            }

            // 最后一个sql也许是存在的
            if (key.length() > 0 && sb.length() > 0) {
                saveSql(sqlList, key.toString(), sb.toString());
            }
        } finally {
            IoUtil.close(r);
        }
    }

    public void saveSql(Map<String, String> sqlList, String key, String value) {
        log.trace(LogMessage.create(this.getClass().getSimpleName(), "saveSql")
                .append("key", key)
                .append("value", value)
                .toString());
        Assert.isFalse(!config.isAllowDuplicate() && sqlList.containsKey(key), "Duplicate sql key=[" + key + "]");
        sqlList.put(key, value);
    }

    private String nextLineTrim(BufferedReader br) throws IOException {
        String line = null;
        while (br.ready()) {
            line = br.readLine();
            if (line == null)
                break;
            if (StrUtil.isBlank(line))
                continue;
            return line.trim();
        }
        return line;
    }


    public static class Config {
        /**
         * sql所在目录
         */
        private String path = "sqls";
        /**
         * 是否允许重复的sql标识，默认不允许
         */
        private boolean allowDuplicate = false;
        /**
         * 是否需要监听文件变化
         */
        private boolean watch = true;

        public String getPath() {
            return path;
        }

        public Config setPath(String path) {
            this.path = path;
            return this;
        }

        public boolean isAllowDuplicate() {
            return allowDuplicate;
        }

        public Config setAllowDuplicate(boolean allowDuplicate) {
            this.allowDuplicate = allowDuplicate;
            return this;
        }

        public boolean isWatch() {
            return watch;
        }

        public Config setWatch(boolean watch) {
            this.watch = watch;
            return this;
        }
    }
}