package org.team4u.selector.domain.selector;

import cn.hutool.core.lang.Dict;
import org.team4u.base.registrar.StringIdPolicy;
import org.team4u.selector.domain.selector.binding.SelectorBinding;

/**
 * 选择器值处理器
 *
 * @author jay.wu
 */
public interface SelectorValueHandler extends StringIdPolicy {

    /**
     * 处理选择值
     *
     * @param context 上下文
     * @return 处理后的值
     */
    String handle(Context context);

    class Context {
        /**
         * 值处理器标识KEY
         */
        public static final String VALUE_HANDLER_ID_KEY = "id";
        /**
         * 处理器所需参数
         */
        private final Dict params;
        /**
         * 选择器上下文
         */
        private final SelectorBinding binding;

        public Context(SelectorBinding binding, Dict params) {
            this.binding = binding;
            this.params = params;
        }


        public SelectorBinding getBinding() {
            return binding;
        }

        public Dict getParams() {
            return params;
        }

        public String getId() {
            return params.getStr(VALUE_HANDLER_ID_KEY);
        }
    }
}