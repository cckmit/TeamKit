package org.team4u.command.domain.executor;


import org.team4u.command.domain.config.CommandConfig;

public interface CommandHandler {

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