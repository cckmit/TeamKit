package org.team4u.sql.domain;

/**
 * sql资源库
 *
 * @author jay.wu
 */
public interface SqlResourceRepository {

    /**
     * 根据标识加载sql资源
     *
     * @param id 资源标识
     * @return sql资源
     */
    SqlResource resourceOf(String id);
}