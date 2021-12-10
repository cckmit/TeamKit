package org.team4u.id.domain.seq;

/**
 * 序号配置资源库
 *
 * @author jay.wu
 */
public interface SequenceConfigRepository {

    /**
     * 获取序号配置
     *
     * @param id 配置标识
     * @return 配置
     */
    SequenceConfig configOfId(String id);
}