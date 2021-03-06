package org.team4u.id.domain.seq.value;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.team4u.base.config.IdentifiedConfig;

/**
 * 趋势增长序号提供者
 *
 * @author jay.wu
 */
public interface StepSequenceProvider extends SequenceProvider {

    Config config();

    /**
     * 当前序号值
     *
     * @param context 上下文
     */
    Number currentSequence(Context context);

    @Override
    default boolean isEmpty(Context context) {
        // 循环使用，永不为空
        if (config().isRecycleAfterMaxValue()) {
            return false;
        }

        // 判断是否已达到或者超过最大值
        return currentSequence(context).longValue() >= config().getMaxValue();
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    class Config extends IdentifiedConfig {
        /**
         * 初始值
         */
        private Long start = 1L;
        /**
         * 步进
         */
        private Integer step = 1;
        /**
         * 最大值
         */
        private Long maxValue = Long.MAX_VALUE;
        /**
         * 达到最大值后，是否从初始值重新循环
         */
        private boolean isRecycleAfterMaxValue;

        /**
         * 计算循环后正确的序号值
         * <p>
         * 当当前值＞最大值后，使用计算方式实现重置初始值的效果，以避免并发竞争，提高性能
         *
         * @param value 当前值，可能超出最大值
         * @return 正确的序号值
         */
        public Number valueWithRecycle(Number value) {
            // 获取当前值在等差数量中的正确位置
            long position = positionOfValue(value.longValue()) % sizeOfValues();
            // 根据位置计算正确的序号值
            return getStart() + position * getStep();
        }

        /**
         * 值在等差数列中的位置
         *
         * @return value 等差数列中的位置，从0开始
         */
        private long positionOfValue(long value) {
            return (value - getStart()) / getStep();
        }

        /**
         * 获取可用序号数量
         */
        private long sizeOfValues() {
            return positionOfValue(getMaxValue()) + 1;
        }
    }
}