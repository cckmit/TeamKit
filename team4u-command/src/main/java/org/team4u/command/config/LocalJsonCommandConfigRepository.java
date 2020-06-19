package org.team4u.command.config;

import cn.hutool.core.io.FileUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;

/**
 * 本地json命令配置资源库
 *
 * @author jay.wu
 */
public class LocalJsonCommandConfigRepository implements CommandConfigRepository {

    @Override
    public CommandConfig objectOf(String objectId) {
        String jsonStr = FileUtil.readUtf8String(objectId);
        return toCommandConfig(jsonStr);
    }

    protected CommandConfig toCommandConfig(String jsonStr) {
        return JSON.parseObject(jsonStr, CommandConfig.class, Feature.OrderedField);
    }
}