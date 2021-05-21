package org.team4u.rl.domain;

import java.util.List;
import java.util.Objects;

public class RateLimiterConfig {

    private List<Rule> rules;

    public List<Rule> getRules() {
        return rules;
    }

    public RateLimiterConfig setRules(List<Rule> rules) {
        this.rules = rules;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RateLimiterConfig that = (RateLimiterConfig) o;
        return Objects.equals(rules, that.rules);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rules);
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

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Rule rule = (Rule) o;
            return expirationMillis == rule.expirationMillis &&
                    threshold == rule.threshold &&
                    type.equals(rule.type);
        }

        @Override
        public int hashCode() {
            return Objects.hash(type, expirationMillis, threshold);
        }
    }
}