package org.team4u.core.lang;

/**
 * 对象资源库
 *
 * @author jay.wu
 */
public interface ObjectRepository<O> {

    /**
     * 获取对象
     *
     * @param objectId 对象标识
     * @return 对象
     */
    O objectOf(String objectId);
}