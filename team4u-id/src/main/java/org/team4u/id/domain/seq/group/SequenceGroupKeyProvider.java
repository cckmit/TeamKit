package org.team4u.id.domain.seq.group;

import lombok.Data;
import org.team4u.base.registrar.StringIdPolicy;
import org.team4u.base.registrar.factory.StringConfigPolicyFactory;
import org.team4u.id.domain.seq.SequenceConfig;

import java.util.Map;

/**
 * 序号分组提供者
 *
 * @author jay.wu
 */
public interface SequenceGroupKeyProvider {
    /**
     * 生成分组标识
     *
     * @param context 上下文
     * @return 分组标识
     */
    String provide(Context context);

    @Data
    class Context {
        /**
         * 序号配置
         */
        private final SequenceConfig sequenceConfig;
        /**
         * 扩展属性
         */
        private final Map<String, Object> ext;
    }

    /**
     * 序号分组key提供者工厂
     *
     * @param <C> 配置类型
     */
    interface Factory<C> extends StringConfigPolicyFactory<SequenceGroupKeyProvider>, StringIdPolicy {
    }
}