package org.team4u.command.domain.executor;


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
    }
}