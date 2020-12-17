package org.team4u.workflow.domain.instance.node.handler;

import org.team4u.selector.application.SelectorAppService;
import org.team4u.selector.domain.selector.entity.binding.SimpleMapBinding;
import org.team4u.workflow.domain.definition.node.DynamicChoiceNode;

/**
 * 动态选择器节点处理器
 *
 * @author jay.wu
 */
public class DynamicChoiceNodeHandler implements ProcessNodeHandler {

    private final SelectorAppService selectorAppService;

    public DynamicChoiceNodeHandler(SelectorAppService selectorAppService) {
        this.selectorAppService = selectorAppService;
    }

    @Override
    public String handle(Context context) {
        DynamicChoiceNode node = context.getNode();

        SimpleMapBinding binding = new SimpleMapBinding();
        binding.put("c", context);

        return selectorAppService.select(
                node.getRule(),
                binding
        );
    }

    @Override
    public String id() {
        return DynamicChoiceNode.class.getSimpleName();
    }
}
