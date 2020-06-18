package org.team4u.core.masker;

import org.junit.Assert;
import org.junit.Test;

public class PercentageMaskerTest {

    @Test
    public void mask() {
        Masker masker = new PercentageMasker(0.8);
        String x = masker.mask("fjay");
        Assert.assertEquals("f***", x);
    }
}