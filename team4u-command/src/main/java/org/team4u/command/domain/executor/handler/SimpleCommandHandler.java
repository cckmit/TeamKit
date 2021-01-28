package org.team4u.command.domain.executor.handler;


/**
 * 简单命令处理器
 *
 * @author jay.wu
 */
public interface SimpleCommandHandler extends CommandHandler {

    @Override
    default boolean handle(Context context) {
        internalHandle(context);
        return true;
    }

    /**
     * 处理命令
     *
     * @param context 处理器上下文
     */
    void internalHandle(Context context);
}