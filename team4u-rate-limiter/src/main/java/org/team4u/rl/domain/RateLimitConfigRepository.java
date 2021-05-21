package org.team4u.rl.domain;

public interface RateLimitConfigRepository {

    RateLimiterConfig configOf(String configId);
}