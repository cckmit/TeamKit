package org.team4u.command;

import org.team4u.command.handler.remote.RemoteHandler;

public class FakeTraceInterceptor implements HandlerInterceptor {

    private boolean hasInserted = false;
    private boolean hasUpdated = false;

    @Override
    public boolean preHandle(Handler.Context context, Handler handler) throws Exception {
        if (handler instanceof RemoteHandler) {
            hasInserted = true;
        }

        return true;
    }

    @Override
    public void postHandle(Handler.Context context, Handler handler) throws Exception {
    }

    @Override
    public void afterCompletion(Handler.Context context, Handler handler, Exception ex) throws Exception {
        if (!hasInserted) {
            return;
        }

        if (ex != null) {
            hasUpdated = true;
            return;
        }

        if (context.isLastHandler()) {
            hasUpdated = true;
        }
    }

    @Override
    public String id() {
        return "trace";
    }

    public boolean isHasInserted() {
        return hasInserted;
    }

    public boolean isHasUpdated() {
        return hasUpdated;
    }
}
