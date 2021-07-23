package org.team4u.rl.domain;

import java.util.Objects;

public class RateLimiterConfig {

    private String type;

    private long expirationMillis;

    private long threshold;

    public String getType() {
        return type;
    }

    public RateLimiterConfig setType(String type) {
        this.type = type;
        return this;
    }

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
                type.equals(config.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, expirationMillis, threshold);
    }
}
