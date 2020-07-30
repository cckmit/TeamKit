package org.team4u.command.domain.executor;


import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Dict;
import org.team4u.base.filter.Filter;
import org.team4u.base.filter.FilterInvoker;
import org.team4u.command.domain.config.CommandConfig;

/**
 * 命令处理器
 *
 * @author jay.wu
 */
public interface CommandHandler extends Filter<CommandHandler.Context> {

    @Override
    default void doFilter(Context context, FilterInvoker<Context> nextFilter) {
        handle(context);
        nextFilter.invoke(context);
    }

    /**
     * 处理命令
     *
     * @param context 处理器上下文
     */
    void handle(Context context);

    class Context {

        private final CommandConfig config;

        private final CommandRequest request;

        private CommandResponse response;

        private final Dict extraAttributes = new Dict();

        public Context(CommandConfig config, CommandRequest request) {
            this.config = config;
            this.request = request;
        }

        public CommandConfig getConfig() {
            return config;
        }

        public CommandRequest getRequest() {
            return request;
        }

        public CommandResponse getResponse() {
            return response;
        }

        public Context setResponse(CommandResponse response) {
            this.response = response;
            return this;
        }

        public void setExtraAttribute(String key, Object value) {
            extraAttributes.set(key, value);
        }

        public <V> V extraAttribute(String key, Class<V> valueType) {
            return Convert.convert(valueType, extraAttributes.get(key));
        }
    }
}