package org.team4u.id.domain.seq.value;

import lombok.Data;

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
    }
}