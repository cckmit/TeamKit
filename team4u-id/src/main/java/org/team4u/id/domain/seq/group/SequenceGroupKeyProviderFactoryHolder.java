package org.team4u.id.domain.seq.group;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.team4u.base.registrar.PolicyRegistrar;

/**
 * 序号分组提供者服务
 *
 * @author jay.wu
 */
public class SequenceGroupKeyProviderFactoryHolder extends PolicyRegistrar<String, SequenceGroupKeyProvider.Factory<?>> {

    public SequenceGroupKeyProviderFactoryHolder() {
        registerPoliciesByBeanProvidersAndEvent();
    }

    public String provide(SequenceGroupKeyProvider.Context context) {
        JSONObject config = JSONUtil.parseObj(context.getSequenceConfig().getGroupKeyConfig());
        return availablePolicyOf(config.getStr("id"))
                .create(config)
                .provide(context);
    }
}