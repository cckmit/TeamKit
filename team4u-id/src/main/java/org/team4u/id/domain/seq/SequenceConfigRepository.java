package org.team4u.id.domain.seq;

/**
 * 序号配置资源库
 *
 * @author jay.wu
 */
public interface SequenceConfigRepository {

    SequenceConfig2 configOfId(String id);
}