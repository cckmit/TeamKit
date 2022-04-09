package org.team4u.command.domain.executor.handler.requester;


import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.team4u.base.log.LogMessage;
import org.team4u.command.domain.executor.handler.CommandHandler;
import org.team4u.selector.application.SelectorAppService;
import org.team4u.selector.domain.selector.SelectorConfig;
import org.team4u.selector.domain.selector.SelectorResult;
import org.team4u.selector.domain.selector.binding.SingleValueBinding;
import org.team4u.selector.infrastructure.persistence.InMemorySelectorConfigRepository;

import java.util.Collections;

/**
 * 请求者响应码映射器
 *
 * @author jay.wu
 */
public abstract class RequesterResponseCodeMapper implements CommandHandler {

    private final Log log = LogFactory.get();

    private final SelectorAppService selectorAppService;

    public RequesterResponseCodeMapper() {
        this(new SelectorAppService(new InMemorySelectorConfigRepository(Collections.emptyList())));
    }

    public RequesterResponseCodeMapper(SelectorAppService selectorAppService) {
        this.selectorAppService = selectorAppService;
    }

    @Override
    public void handle(CommandHandler.Context context) {
        SelectorConfig codeMapperConfig = context.getConfig().itemOf("codeMapper", SelectorConfig.class);
        String channelCode = channelCodeOf(context);

        SelectorResult standardCode = selectorAppService.select(
                codeMapperConfig,
                new SingleValueBinding(channelCode)
        );

        if (standardCode.isNotMatch()) {
            log.error(LogMessage.create(this.getClass().getSimpleName(), "handle")
                    .fail("Cannot find standard code")
                    .append("channelCode", channelCode)
                    .toString());
        }

        setStandardCode(context, standardCode);
    }

    /**
     * 获取渠道响应码
     *
     * @param context 上下文
     * @return 渠道响应码
     */
    protected abstract String channelCodeOf(CommandHandler.Context context);

    /**
     * 设置标准响应码
     *
     * @param context      上下文
     * @param standardCode 标准响应码命中结果
     */
    protected abstract void setStandardCode(CommandHandler.Context context, SelectorResult standardCode);
}