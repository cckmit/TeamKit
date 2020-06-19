package org.team4u.kv.model;

/**
 * 键值
 *
 * @author jay.wu
 */
public class KeyValue {

    /**
     * 模型标识
     */
    private KeyValueId keyValueId;
    /**
     * 对象值
     */
    private String value;
    /**
     * 过期时间戳，0为永不过期
     */
    private long expirationTimestamp;


    public KeyValueId id() {
        return keyValueId;
    }

    public KeyValue setId(KeyValueId keyValueId) {
        this.keyValueId = keyValueId;
        return this;
    }

    public String getValue() {
        return value;
    }

    public KeyValue setValue(String value) {
        this.value = value;
        return this;
    }

    public long getExpirationTimestamp() {
        return expirationTimestamp;
    }

    public KeyValue setExpirationTimestamp(long expirationTimestamp) {
        this.expirationTimestamp = expirationTimestamp;
        return this;
    }

    /**
     * 判断该键值是否已过期
     */
    public boolean isExpired() {
        // 0为永不过期
        if (getExpirationTimestamp() == 0) {
            return false;
        }

        return System.currentTimeMillis() > getExpirationTimestamp();
    }

    @Override
    public String toString() {
        return "keyValueId=" + keyValueId +
                "|value='" + value + '\'' +
                "|expirationTimestamp=" + expirationTimestamp;
    }
}