package org.team4u.id.domain.seq.value;

import cn.hutool.log.Log;
import org.team4u.base.log.LogMessage;

/**
 * 自动递增的序号提供者
 * <p>
 * 适用场景：自身拥有原生并发递增能力的计数器，如AtomicLong、Redis的increment等
 *
 * @author jay.wu
 */
public abstract class AutoIncrementStepSequenceProvider implements StepSequenceProvider {

    protected final Log log = Log.get();

    @Override
    public Number provide(Context context) {
        LogMessage lm = LogMessage.create(this.getClass().getSimpleName(), "provide")
                .append("context", context);

        if (!canUpdateIfOverMaxValue(standardizedValue(currentSequence(context)))) {
            log.info(lm.fail().append("canUpdateIfOverMaxValue", false).toString());
            return null;
        }

        Number newValue = resetIfOverMaxValue(standardizedValue(addAndGet(context)));
        log.info(lm.append("currentValue", newValue).success().toString());
        return newValue;
    }

    /**
     * 获取递增后的值
     * <p>
     * 注意，递增前的初始值固定为0
     */
    protected abstract Number addAndGet(Context context);

    /**
     * 超过最大值后是否可以继续更新
     *
     * @param value 当前值
     * @return true:未超过最大值或者可以循环使用，false:当前值超过最大值，且不循环使用
     */
    private boolean canUpdateIfOverMaxValue(Number value) {
        if (value == null) {
            return true;
        }

        if (value.longValue() < config().getMaxValue()) {
            return true;
        }

        return config().isRecycleAfterMaxValue();
    }

    /**
     * 如果超过最大值，将重置序列
     *
     * @param value 当前值
     * @return 重置后的值
     */
    private Number resetIfOverMaxValue(Number value) {
        if (value.longValue() <= config().getMaxValue()) {
            return value;
        }

        // 循环使用
        if (config().isRecycleAfterMaxValue()) {
            return config().valueWithRecycle(value).longValue();
        }

        return config().getMaxValue();
    }

    /**
     * 修正当前值
     * <p>
     * 原因：addAndGet返回的是从0开始递增后的值，而我们期望返回累加前值，并且从start开始
     */
    private Long standardizedValue(Number value) {
        if (value == null) {
            return null;
        }

        return value.longValue() - config().getStep() + config().getStart();
    }
}