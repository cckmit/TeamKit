package org.team4u.rl.domain;

import org.team4u.base.config.IdentifiedConfig;

import java.util.Objects;

public class RateLimiterConfig extends IdentifiedConfig {

    private long expirationMillis;

    private long threshold;

    public long getExpirationMillis() {
        return expirationMillis;
    }

    public RateLimiterConfig setExpirationMillis(long expirationMillis) {
        this.expirationMillis = expirationMillis;
        return this;
    }

    public long getThreshold() {
        return threshold;
    }

    public RateLimiterConfig setThreshold(long threshold) {
        this.threshold = threshold;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RateLimiterConfig config = (RateLimiterConfig) o;
        return expirationMillis == config.expirationMillis &&
                threshold == config.threshold &&
                getConfigId().equals(config.getConfigId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getConfigId(), expirationMillis, threshold);
    }
}
