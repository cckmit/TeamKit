package org.team4u.ddd.domain.model;


import org.team4u.base.error.OptimisticLockingFailureException;

/**
 * 并发安全的实体类
 *
 * @author jay.wu
 */
public class ConcurrencySafeEntity extends Entity {

    private static final long serialVersionUID = 1L;

    private long concurrencyVersion = 0;

    protected ConcurrencySafeEntity() {
        super();
    }

    public long concurrencyVersion() {
        return this.concurrencyVersion;
    }

    public void setConcurrencyVersion(long concurrencyVersion) {
        this.concurrencyVersion = concurrencyVersion;
    }

    public void failWhenConcurrencyViolation() throws OptimisticLockingFailureException {
        throw new OptimisticLockingFailureException(getClass().getSimpleName() + "|domainId=" + getId());
    }
}