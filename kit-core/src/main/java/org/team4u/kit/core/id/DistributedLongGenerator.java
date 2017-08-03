package org.team4u.kit.core.id;


import org.team4u.kit.core.util.ValueUtil;

/**
 * 分布式ID生成器
 * <p>
 * 可以保证多台机器并发生成的id是唯一的,且总体趋势是增长的
 *
 * @author Jay.Wu
 */
public class DistributedLongGenerator implements INumberGenerator {

    /**
     * 默认最大机器标识
     */
    public static final int DEFAULT_MAX_WORKER_ID = 1;
    /**
     * 默认序列号最大位数
     */
    public static final int DEFAULT_MAX_SEQUENCE = 1023;
    /**
     * 默认时间起始标记点(2016-03-14)
     */
    public static final long DEFAULT_EPOCH = 1457884800000L;

    /**
     * 时间起始标记点，作为基准，一般取系统的最近时间
     */
    private final long epoch;

    /**
     * 当前机器标识
     */
    private final int workerId;
    /**
     * 最大序号
     */
    private final int maxSequence;
    /**
     * 毫秒内自增位
     */
    private final int sequenceBits;
    private final int workerIdBits;
    private final long timestampLeftShift;
    /**
     * 当前序号,并发控制
     */
    private int sequence = 0;
    private long lastTimestamp = -1;


    public DistributedLongGenerator(int workerId, Integer maxWorkerId, Integer maxSequence, Long epoch) {
        maxWorkerId = ValueUtil.defaultIfNull(maxWorkerId, DEFAULT_MAX_WORKER_ID);

        if (workerId > maxWorkerId || workerId < 0) {
            throw new IllegalArgumentException(
                    String.format("worker Id can't be greater than %d or less than 0", maxWorkerId));
        }
        this.workerId = workerId;

        this.maxSequence = ValueUtil.defaultIfNull(maxSequence, DEFAULT_MAX_SEQUENCE);
        if (this.maxSequence < 1) {
            throw new IllegalArgumentException("max sequence can't be less than 1");
        }
        sequenceBits = Integer.toBinaryString(this.maxSequence).length();

        this.epoch = ValueUtil.defaultIfNull(epoch, DEFAULT_EPOCH);
        if (this.epoch < DEFAULT_EPOCH) {
            throw new IllegalArgumentException("epoch can't be less than " + DEFAULT_EPOCH);
        }

        // 机器标识位数
        workerIdBits = Integer.toBinaryString(maxWorkerId).length();
        timestampLeftShift = sequenceBits + workerIdBits;
    }

    public DistributedLongGenerator(int workerId) {
        this(workerId, null, null, null);
    }

    /**
     * 获得系统当前毫秒数
     */
    private static long now() {
        return System.currentTimeMillis();
    }

    @Override
    public synchronized Long next() {
        long timestamp = now();
        // 如果上一个timestamp与新产生的相等，则sequence加一;
        if (this.lastTimestamp == timestamp) {
            // 超出最大序列号,重新生成timestamp
            if (++this.sequence > maxSequence) {
                timestamp = this.getAndWaitNextTimestamp(this.lastTimestamp);
                this.sequence = 0;
            }
        } else {
            // 对新的timestamp，sequence从0开始
            this.sequence = 0;
        }

        if (timestamp < this.lastTimestamp) {
            String errorInfo = String.format("clock moved backwards.Refusing to generate id for %d milliseconds",
                    lastTimestamp - timestamp);
            throw new RuntimeException(errorInfo);
        }

        this.lastTimestamp = timestamp;
        return timestamp - this.epoch << this.timestampLeftShift | this.workerId << this.sequenceBits | this.sequence;
    }

    @Override
    public void reset() {
        sequence = 0;
        lastTimestamp = -1;
    }

    /**
     * 获取同一组机器内生成的序列号所在的workId
     */
    public Integer getWorkIdOfSeq(Long seq) {
        String binaryString = Long.toBinaryString(seq);
        int start = binaryString.length() - workerIdBits - sequenceBits;
        int end = binaryString.length() - sequenceBits;
        String workIdBinaryString = binaryString.substring(start, end);
        return Integer.valueOf(workIdBinaryString, 2);
    }

    /**
     * 等待下一个毫秒到来, 保证返回的毫秒数在参数lastTimestamp之后
     */
    private long getAndWaitNextTimestamp(long lastTimestamp) {
        long timestamp = now();

        while (timestamp <= lastTimestamp) {
            timestamp = now();
        }

        return timestamp;
    }

    public static class Builder {

        private int workerId;
        private Integer maxWorkerId;
        private Integer maxSequence;
        private Long epoch;

        public Builder(int workerId) {
            this.workerId = workerId;
        }

        public Builder setMaxWorkerId(Integer maxWorkerId) {
            this.maxWorkerId = maxWorkerId;
            return this;
        }

        public Builder setMaxSequence(Integer maxSequence) {
            this.maxSequence = maxSequence;
            return this;
        }

        public Builder setEpoch(Long epoch) {
            this.epoch = epoch;
            return this;
        }

        public DistributedLongGenerator build() {
            return new DistributedLongGenerator(workerId, maxWorkerId, maxSequence, epoch);
        }
    }
}