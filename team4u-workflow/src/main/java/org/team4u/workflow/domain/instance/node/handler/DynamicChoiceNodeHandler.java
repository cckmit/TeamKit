package org.team4u.workflow.domain.instance.node.handler;

import cn.hutool.core.bean.BeanUtil;
import org.team4u.selector.application.SelectorAppService;
import org.team4u.selector.domain.selector.entity.binding.SimpleMapBinding;
import org.team4u.workflow.domain.definition.node.DynamicChoiceNode;

/**
 * 动态选择器节点处理器
 *
 * @author jay.wu
 */
public class DynamicChoiceNodeHandler extends AbstractProcessNodeHandler<DynamicChoiceNode> {
    private final SelectorAppService selectorAppService;

    public DynamicChoiceNodeHandler(SelectorAppService selectorAppService) {
        this.selectorAppService = selectorAppService;
    }

    @Override
    public String handle(ProcessNodeHandlerContext context) {
        DynamicChoiceNode node = node(context);

        SimpleMapBinding binding = new SimpleMapBinding();
        binding.putAll(BeanUtil.beanToMap(context));

        return selectorAppService.select(
                node.getRule(),
                binding
        );
    }
}
