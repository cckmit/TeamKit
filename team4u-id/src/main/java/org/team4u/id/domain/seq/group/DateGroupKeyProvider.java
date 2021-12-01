package org.team4u.id.domain.seq.group;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONObject;
import lombok.Data;

import java.util.Date;

/**
 * 基于日期的分组提供者
 *
 * @author jay.wu
 */
public class DateGroupKeyProvider extends AbstractGroupKeyProviderFactory implements SequenceGroupKeyProvider {

    private final Config config;

    public DateGroupKeyProvider(Config config) {
        this.config = config;
    }

    @Override
    public String id() {
        return "DATE";
    }

    @Override
    public String provide(Context context) {
        return DateUtil.format(now(), config.getFormat());
    }

    protected Date now() {
        return new Date();
    }

    @Override
    protected SequenceGroupKeyProvider internalCreate(JSONObject config) {
        return new DateGroupKeyProvider(config.toBean(Config.class));
    }

    @Data
    public static class Config {
        /**
         * 时间格式
         */
        private String format = "yyyyMMdd";
    }
}