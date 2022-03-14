package org.team4u.id.domain.seq.value.cache.queue;

import lombok.Getter;
import org.team4u.id.domain.seq.value.StepSequenceProvider;
import org.team4u.id.domain.seq.value.cache.CacheStepSequenceConfig;

/**
 * 序号号段
 * <p>
 * - 根据当前号段计算最新序号
 * - 缓存未来号段，若当前号段耗尽，将自动切换
 */
@Getter
public class SequenceSegment {
    /**
     * 当前序列
     */
    private Long currentSeq;
    /**
     * 当前号段（不包含）
     */
    private Long currentSegment;
    /**
     * 下一号段（不包含）
     */
    private Long nextSegment;

    private final CacheStepSequenceConfig cacheConfig;

    private final StepSequenceProvider.Config delegateConfig;

    public SequenceSegment(CacheStepSequenceConfig cacheConfig, StepSequenceProvider.Config delegateConfig) {
        this.cacheConfig = cacheConfig;
        this.delegateConfig = delegateConfig;
    }

    public void refreshSegment(Number seq) {
        if (seq == null) {
            return;
        }

        // 更新下一个缓存最大值
        refreshNextSegment(seq);

        // 如果当前号段已耗尽，需重新初始化
        handleIfEmpty();
    }

    private void refreshNextSegment(Number value) {
        nextSegment = value.longValue() + delegateConfig.getStep();
    }

    /**
     * 当前号段是否已耗尽
     */
    public boolean isEmpty() {
        if (currentSeq == null) {
            return true;
        }

        if (currentSeq >= currentSegment) {
            return true;
        }

        return currentSeq > delegateConfig.getMaxValue();
    }

    public Long next() {
        if (isEmpty()) {
            return null;
        }

        return getAndAdd();
    }

    private Long getAndAdd() {
        Long result = currentSeq;
        currentSeq += cacheConfig.getCacheStep();
        return result;
    }

    private void handleIfEmpty() {
        // 当前缓存号段未用完，无需处理
        if (!isEmpty()) {
            return;
        }

        // 下一个号段不可用，重置seq
        if (!isNextBufferAvailable()) {
            resetCurrentSeq();
            return;
        }

        // 尝试用下一个号段
        refreshCurrentSegment();
        refreshCurrentSeqBySegment();

        // 若仍然超出，重置seq
        if (isEmpty()) {
            resetCurrentSeq();
        }
    }

    /**
     * 下一个号段是否可用
     */
    private boolean isNextBufferAvailable() {
        if (nextSegment == null) {
            return false;
        }

        if (currentSegment == null) {
            return true;
        }

        // 循环序号，永不耗尽
        if (delegateConfig.isRecycleAfterMaxValue()) {
            return true;
        }

        return !nextSegment.equals(currentSegment);
    }

    private void resetCurrentSeq() {
        currentSeq = null;
    }

    private void refreshCurrentSeqBySegment() {
        currentSeq = currentSegment - delegateConfig.getStep();
    }

    private void refreshCurrentSegment() {
        currentSegment = nextSegment;
    }

    @Override
    public String toString() {
        return String.format("currentSeq:%s,currentSegment:%s,nextSegment:%s", currentSeq, currentSegment, nextSegment);
    }
}