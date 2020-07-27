package org.team4u.base.sm;

import org.squirrelframework.foundation.fsm.StateMachineBuilderFactory;
import org.squirrelframework.foundation.fsm.UntypedStateMachine;
import org.squirrelframework.foundation.fsm.UntypedStateMachineBuilder;
import org.squirrelframework.foundation.fsm.impl.AbstractUntypedStateMachine;

/**
 * 简单状态机
 *
 * @param <Context> 上下文类型
 * @param <Action>  动作类型
 * @param <Status>  状态类型
 * @author jay.wu
 */
public abstract class AbstractSimpleStateMachine<Context, Action, Status> extends AbstractUntypedStateMachine {

    private final UntypedStateMachineBuilder builder = StateMachineBuilderFactory.create(this.getClass());

    /**
     * 获取指定起始状态的状态机实例
     */
    public UntypedStateMachine stateMachineOfInitStatus(Status initStatus) {
        return builder.newStateMachine(initStatus);
    }

    /**
     * 触发事件
     *
     * @param action 事件
     * @return 是否成功触发事件
     */
    public synchronized boolean fire(Context context, Action action, Status current) {
        UntypedStateMachine sm = stateMachineOfInitStatus(current);

        if (!sm.canAccept(action)) {
            return false;
        }

        sm.fire(action, context);
        return true;
    }

    /**
     * 触发事件并校验是否成功，失败则抛出异常
     */
    public void fireAndCheck(Context context, Action action, Status current)
            throws IllegalStateException {
        if (!fire(context, action, current)) {
            throw new IllegalStateException(current.toString());
        }
    }

    /**
     * 注册状态转移路径
     *
     * @param action 出发动作
     * @param from   来源状态
     * @param to     目标状态
     */
    public void registerTransition(Action action, Status from, Status to) {
        builder.externalTransition()
                .from(from)
                .to(to)
                .on(action)
                .callMethod("transform");
    }

    /**
     * 状态转换时触发此方法
     *
     * @param from    来源状态
     * @param to      目标状态
     * @param action  触发动作
     * @param context 上下文
     */
    protected abstract void transform(Status from,
                                      Status to,
                                      Action action,
                                      Context context);
}
