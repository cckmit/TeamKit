package org.team4u.id.domain.seq.value;

import lombok.Data;
import org.team4u.base.registrar.StringIdPolicy;
import org.team4u.base.registrar.factory.StringConfigPolicyFactory;
import org.team4u.id.domain.seq.SequenceConfig;

import java.util.Map;

/**
 * 序号值提供者
 *
 * @author jay.wu
 */
public interface SequenceProvider {
    /**
     * 生成序号
     *
     * @param context 上下文
     * @return 序号
     */
    Number provide(Context context);

    @Data
    class Context {
        /**
         * 序号配置
         */
        private final SequenceConfig sequenceConfig;
        /**
         * 分组标识
         */
        private final String groupKey;
        /**
         * 扩展属性
         */
        private final Map<String, Object> ext;

        public String id() {
            return sequenceConfig.getTypeId() + ":" + groupKey;
        }

        @Override
        public String toString() {
            return id();
        }
    }

    /**
     * 序号值提供者工厂
     */
    interface Factory extends StringConfigPolicyFactory<SequenceProvider>, StringIdPolicy {
    }
}