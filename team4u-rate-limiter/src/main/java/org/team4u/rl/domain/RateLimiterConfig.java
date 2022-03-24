package org.team4u.rl.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.team4u.base.config.IdentifiedConfig;

@EqualsAndHashCode(callSuper = true)
@Data
public class RateLimiterConfig extends IdentifiedConfig {

    private long expirationMillis;

    private long threshold;
}
