package org.team4u.id.domain.seq.value;

import lombok.Data;
import org.team4u.base.registrar.StringIdPolicy;
import org.team4u.base.registrar.factory.StringConfigPolicyFactory;
import org.team4u.id.domain.seq.SequenceConfig;

import java.util.HashMap;
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

    /**
     * 是否序号值已耗尽
     *
     * @return true：已无可用序号，false：存在可用序号
     */
    boolean isEmpty(Context context);

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
        private Map<String, Object> ext;

        public Context(SequenceConfig sequenceConfig, String groupKey, Map<String, Object> ext) {
            this.sequenceConfig = sequenceConfig;
            this.groupKey = groupKey;
            this.ext = ext;
        }

        @SuppressWarnings("unchecked")
        public <T> T ext(String key) {
            if (ext == null) {
                return null;
            }

            return (T) ext.get(key);
        }

        public void ext(String key, Object value) {
            if (ext == null) {
                ext = new HashMap<>();
            }

            ext.put(key, value);
        }

        public String id() {
            return sequenceConfig.getConfigId() + ":" + groupKey;
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