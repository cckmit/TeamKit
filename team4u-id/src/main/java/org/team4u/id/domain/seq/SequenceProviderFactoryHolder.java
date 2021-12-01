package org.team4u.id.domain.seq;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.team4u.base.registrar.PolicyRegistrar;

/**
 * 序号值提供者服务
 *
 * @author jay.wu
 */
public class SequenceProviderFactoryHolder extends PolicyRegistrar<String, SequenceProvider.Factory> {

    public SequenceProviderFactoryHolder() {
        registerPoliciesByBeanProvidersAndEvent();
    }

    public Number provide(SequenceProvider.Context context) {
        JSONObject config = JSONUtil.parseObj(context.getSequenceConfig().getSequenceNoConfig());
        return availablePolicyOf(config.getStr("id"))
                .create(config)
                .provide(context);
    }
}