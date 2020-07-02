package org.team4u.ddd.domain.model;

/**
 * 领域资源库
 *
 * @author jay.wu
 */
public interface DomainRepository<E extends Entity> {

    /**
     * 获取领域对象
     *
     * @param domainId 领域标识
     * @return 领域对象
     */
    E domainOf(String domainId);

    /**
     * 保存领域对象
     *
     * @param domain 领域对象
     */
    void save(E domain);
}