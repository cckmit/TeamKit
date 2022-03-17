package org.team4u.id.domain.seq.group;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import lombok.Data;
import lombok.Getter;

import java.util.Date;

/**
 * 基于日期时间的分组提供者
 *
 * @author jay.wu
 */
public class DateTimeGroupKeyProvider implements SequenceGroupKeyProvider {

    @Getter
    private final Config config;

    public DateTimeGroupKeyProvider(Config config) {
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
            return "DT";
        }

        @Override
        protected SequenceGroupKeyProvider createWithConfig(Config config) {
            return new DateTimeGroupKeyProvider(ObjectUtil.defaultIfNull(config, new Config()));
        }
    }
}