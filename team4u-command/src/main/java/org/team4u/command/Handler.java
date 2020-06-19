package org.team4u.command;

import org.team4u.command.config.CommandConfig;
import org.team4u.core.filter.Filter;
import org.team4u.core.lang.EasyMap;
import org.team4u.core.lang.IdObject;

/**
 * 命令处理器
 *
 * @author Jay Wu
 */
public interface Handler extends Filter<Handler.Context>, IdObject<String> {

    class Context {
        /**
         * 运行时属性
         */
        private final EasyMap attributes;
        /**
         * 当前完整的命令配置
         */
        private final CommandConfig config;
        /**
         * 是否当前为最后一个处理器
         */
        private boolean isLastHandler;

        public Context(EasyMap attributes, CommandConfig config) {
            this.attributes = attributes;
            this.config = config;
        }

        public CommandConfig config() {
            return config;
        }

        public EasyMap attributes() {
            return attributes;
        }

        public boolean isLastHandler() {
            return isLastHandler;
        }

        public Context setLastHandler(boolean lastHandler) {
            isLastHandler = lastHandler;
            return this;
        }
    }
}