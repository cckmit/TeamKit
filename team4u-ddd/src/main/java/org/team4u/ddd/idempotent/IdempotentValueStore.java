package org.team4u.ddd.idempotent;


import org.team4u.base.error.IdempotentException;

/**
 * 幂等值资源库
 *
 * @author jay.wu
 */
public interface IdempotentValueStore {

    void append(IdempotentValue value) throws IdempotentException;

}