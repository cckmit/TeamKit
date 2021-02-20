package org.team4u.command.domain.executor.handler;


import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Dict;
import org.team4u.command.domain.config.CommandConfig;

/**
 * 命令处理器
 *
 * @author jay.wu
 */
public interface CommandHandler {

    /**
     * 处理命令
     *
     * @param context 处理器上下文
     */
    void handle(Context context);

    class Context {

        private final String commandId;
        private final CommandConfig config;
        private final Object request;
        private final Dict extraAttributes = new Dict();
        private String commandLogId;
        private Object response;

        public Context(String commandId, CommandConfig config, Object request) {
            this.commandId = commandId;
            this.config = config;
            this.request = request;
        }

        public String getCommandLogId() {
            return commandLogId;
        }

        public void setCommandLogId(String commandLogId) {
            this.commandLogId = commandLogId;
        }

        public String getCommandId() {
            return commandId;
        }

        public CommandConfig getConfig() {
            return config;
        }

        @SuppressWarnings("unchecked")
        public <Request> Request getRequest() {
            return (Request) request;
        }

        @SuppressWarnings("unchecked")
        public <Response> Response getResponse() {
            return (Response) response;
        }

        public Context setResponse(Object response) {
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