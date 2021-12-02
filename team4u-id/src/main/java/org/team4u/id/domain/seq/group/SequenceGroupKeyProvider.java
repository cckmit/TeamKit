package org.team4u.id.domain.seq.group;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.Data;
import org.team4u.base.registrar.StringIdPolicy;
import org.team4u.id.domain.seq.SequenceConfig;

import java.util.Map;

/**
 * 序号分组key提供者
 *
 * @author jay.wu
 */
public interface SequenceGroupKeyProvider {

    String provide(Context context);

    @Data
    class Context {
        private final SequenceConfig sequenceConfig;
        private final Map<String, Object> ext;
    }

    interface Factory<C> extends StringIdPolicy {

        default SequenceGroupKeyProvider create(String config) {
            return create(JSONUtil.parseObj(config));
        }

        default SequenceGroupKeyProvider create(JSONObject config) {
            return create(config.toBean(configType()));
        }

        SequenceGroupKeyProvider create(C config);

        Class<C> configType();
    }
}