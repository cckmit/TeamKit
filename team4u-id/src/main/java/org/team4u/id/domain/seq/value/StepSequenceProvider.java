package org.team4u.id.domain.seq.value;

import lombok.Data;
import org.team4u.base.lang.lazy.LazySupplier;

/**
 * 趋势增长序号提供者
 *
 * @author jay.wu
 */
public interface StepSequenceProvider extends SequenceProvider {

    Config config();

    @Data
    class Config {
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
         * 循环因子
         */
        private final transient LazySupplier<Integer> recycleFactor = LazySupplier.of(
                () -> getMaxValue() % getStep() > 0 ? 1 : 0
        );

        /**
         * 计算循环后正确的序号值
         *
         * @param value 当前值，可能超出最大值
         * @return 正确的序号值
         */
        public Number valueWithRecycle(Number value) {
            return getStart() + (value.longValue() % maxSizeOfSequence()) * getStep() - 1;
        }

        /**
         * 最大可用序号数量
         */
        public long maxSizeOfSequence() {
            return getMaxValue() / getStep() + getRecycleFactor().get();
        }
    }
}