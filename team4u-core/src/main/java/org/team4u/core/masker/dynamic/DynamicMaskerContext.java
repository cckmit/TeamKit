package org.team4u.core.masker.dynamic;

/**
 * 动态掩码器上下文
 *
 * @author jay.wu
 */
public class DynamicMaskerContext {

    /**
     * 配置标识
     */
    private String configId;
    /**
     * 当前值所在路径
     */
    private String valuePath;

    public String getConfigId() {
        return configId;
    }

    public DynamicMaskerContext setConfigId(String configId) {
        this.configId = configId;
        return this;
    }

    public String getValuePath() {
        return valuePath;
    }

    public DynamicMaskerContext setValuePath(String valuePath) {
        this.valuePath = valuePath;
        return this;
    }
}
