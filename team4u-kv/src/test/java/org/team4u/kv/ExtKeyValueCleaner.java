package org.team4u.kv;

public class ExtKeyValueCleaner extends KeyValueCleaner {

    private int removeCount = 0;

    public ExtKeyValueCleaner(Config config, SimpleLockService lockService) {
        super(config, lockService);
    }

    @Override
    protected int removeExpirationValues() {
        removeCount = super.removeExpirationValues();
        return removeCount;
    }

    public int getRemoveCount() {
        return removeCount;
    }
}
