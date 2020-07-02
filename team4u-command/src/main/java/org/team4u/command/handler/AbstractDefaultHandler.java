package org.team4u.command.handler;

import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.team4u.command.HandlerInterceptorService;
import org.team4u.core.lang.EasyMap;
import org.team4u.core.log.LogMessage;
import org.team4u.core.log.LogMessages;
import org.team4u.template.TemplateEngine;

import static org.team4u.command.handler.HandlerAttributesKeyNames.SOURCE_KEY;
import static org.team4u.command.handler.HandlerAttributesKeyNames.TARGET_KEY;
import static org.team4u.command.handler.HandlerAttributesKeys.TARGET;


/**
 * 默认抽象处理器
 *
 * @param <Config> 配置类型
 * @param <Target> 结果类型
 * @author jay.wu
 */
public abstract class AbstractDefaultHandler<Config, Target> extends AbstractHandler {

    private final Log log = LogFactory.get();

    public AbstractDefaultHandler(TemplateEngine templateEngine,
                                  HandlerInterceptorService handlerInterceptorService) {
        super(templateEngine, handlerInterceptorService);
    }

    @Override
    protected void handle(EasyMap config, EasyMap attributes) {
        LogMessage lm = LogMessages.createWithMasker(this.getClass().getSimpleName(), "handle");
        lm.config().setMinSpendTimeMillsToDisplay(50);

        try {
            Config actualConfig = buildConfig(config, attributes);
            lm.append("config", actualConfig).append("isSaveTargetToAttributes", isSaveTargetToAttributes());

            Target target = internalHandle(actualConfig, attributes);

            log.info(lm.append(targetKey(config), target)
                    .success()
                    .toString());

            if (!isSaveTargetToAttributes()) {
                return;
            }

            saveTarget(config, attributes, target);
        } catch (Exception e) {
            log.error(lm.fail().toString());
            throw e;
        }
    }

    /**
     * 根据来源key获取来源
     *
     * @param config     处理器配置
     * @param attributes 上下文属性
     * @param sourceType 来源类型，null时为Object.class
     * @param <T>        来源类型
     * @return 来源
     */
    @SuppressWarnings("unchecked")
    protected <T> T sourceOf(Config config, EasyMap attributes, Class<T> sourceType) {
        String sourceKey = ObjectUtil.defaultIfEmpty(
                (String) ReflectUtil.getFieldValue(config, SOURCE_KEY),
                TARGET
        );

        if (sourceType == null) {
            return (T) attributes.getProperty(sourceKey);
        }

        return attributes.getProperty(sourceKey, sourceType);
    }

    /**
     * 构建处理器配置
     *
     * @param config     原始配置
     * @param attributes 上下文属性
     * @return 具体配置
     */
    protected Config buildConfig(EasyMap config, EasyMap attributes) {
        return config.toBean(configType());
    }

    /**
     * 获取配置类型
     *
     * @return 配置类型
     */
    @SuppressWarnings("unchecked")
    protected Class<Config> configType() {
        return (Class<Config>) ClassUtil.getTypeArgument(this.getClass());
    }

    /**
     * 内部处理器
     *
     * @param config     配置
     * @param attributes 上下文属性
     * @return 处理结果
     */
    protected abstract Target internalHandle(Config config, EasyMap attributes);

    /**
     * 是否保存结果到上下文属性
     *
     * @return true:保存（默认值）,false:不保持
     */
    protected boolean isSaveTargetToAttributes() {
        return true;
    }

    private void saveTarget(EasyMap config, EasyMap attributes, Target target) {
        attributes.set(targetKey(config), target);
    }

    protected String targetKey(EasyMap config) {
        return ObjectUtil.defaultIfEmpty(config.getStr(TARGET_KEY), TARGET);
    }
}