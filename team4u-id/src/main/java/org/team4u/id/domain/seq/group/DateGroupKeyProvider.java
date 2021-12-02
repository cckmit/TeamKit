package org.team4u.id.domain.seq.group;

import cn.hutool.core.date.DateUtil;
import lombok.Data;
import lombok.Getter;

import java.util.Date;

/**
 * 基于日期的分组提供者
 *
 * @author jay.wu
 */
public class DateGroupKeyProvider implements SequenceGroupKeyProvider {

    @Getter
    private final Config config;

    public DateGroupKeyProvider(Config config) {
        this.config = config;
    }

    @Override
    public String provide(Context context) {
        return DateUtil.format(now(), config.getFormat());
    }

    protected Date now() {
        return new Date();
    }

    @Data
    public static class Config {
        /**
         * 时间格式
         */
        private String format = "yyyyMMdd";
    }

    public static class Factory extends AbstractGroupKeyProviderFactory<Config> {

        @Override
        public String id() {
            return "DATE";
        }

        @Override
        protected SequenceGroupKeyProvider internalCreate(Config config) {
            return new DateGroupKeyProvider(config);
        }
    }
}