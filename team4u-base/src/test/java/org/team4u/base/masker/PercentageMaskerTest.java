package org.team4u.base.masker;

import org.junit.Assert;
import org.junit.Test;
import org.team4u.base.masker.Masker;
import org.team4u.base.masker.PercentageMasker;

public class PercentageMaskerTest {

    @Test
    public void mask() {
        Masker masker = new PercentageMasker(0.8);
        String x = masker.mask("fjay");
        Assert.assertEquals("f***", x);
    }
}