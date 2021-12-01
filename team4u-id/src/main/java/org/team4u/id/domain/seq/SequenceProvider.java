package org.team4u.id.domain.seq;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.Data;
import org.team4u.base.registrar.StringIdPolicy;

import java.util.Map;

/**
 * 序号值提供者
 *
 * @author jay.wu
 */
public interface SequenceProvider {

    Number provide(Context context);

    @Data
    class Context {
        private final SequenceConfig2 sequenceConfig;
        private final String groupKey;
        private final Map<String, Object> ext;

        @Override
        public String toString() {
            return sequenceConfig.getTypeId() + ":" + groupKey;
        }
    }

    interface Factory extends StringIdPolicy {

        default SequenceProvider create(String config) {
            return create(JSONUtil.parseObj(config));
        }

        SequenceProvider create(JSONObject config);
    }
}