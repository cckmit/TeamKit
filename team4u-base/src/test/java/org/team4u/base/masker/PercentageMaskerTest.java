package org.team4u.base.masker;

import cn.hutool.core.util.StrUtil;
import org.junit.Assert;
import org.junit.Test;

public class PercentageMaskerTest {

    @Test
    public void mask() {
        Masker masker = new PercentageMasker(0.8);
        String x = masker.mask("fjay");
        Assert.assertEquals("f***", x);
    }

    @Test
    public void geLimit() {
        checkLimit(4);
    }

    @Test
    public void leLimit() {
        checkLimit(5);
    }

    @Test
    public void eqLimit() {
        checkLimit(4);
    }

    private void checkLimit(int limit) {
        Masker masker = new PercentageMasker(0.5, limit);
        String x = masker.mask(StrUtil.repeat("1", 4));
        Assert.assertEquals("11**", x);
    }
}