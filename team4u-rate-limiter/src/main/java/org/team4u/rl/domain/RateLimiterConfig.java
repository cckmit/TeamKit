package org.team4u.rl.domain;

import java.util.List;

public class RateLimiterConfig {

    private List<Rule> rules;

    public List<Rule> getRules() {
        return rules;
    }

    public RateLimiterConfig setRules(List<Rule> rules) {
        this.rules = rules;
        return this;
    }

    public static class Rule {

        private String type;

        private long expirationMillis;

        private long threshold;

        public String getType() {
            return type;
        }

        public Rule setType(String type) {
            this.type = type;
            return this;
        }

        public long getExpirationMillis() {
            return expirationMillis;
        }

        public Rule setExpirationMillis(long expirationMillis) {
            this.expirationMillis = expirationMillis;
            return this;
        }

        public long getThreshold() {
            return threshold;
        }

        public Rule setThreshold(long threshold) {
            this.threshold = threshold;
            return this;
        }
    }
}