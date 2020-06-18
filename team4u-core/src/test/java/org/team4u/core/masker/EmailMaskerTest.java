package org.team4u.core.masker;

import org.junit.Assert;
import org.junit.Test;

public class EmailMaskerTest {

    @Test
    public void mask() {
        SimpleMasker masker = new EmailMasker();
        String x = masker.mask("fjay@126.com");
        Assert.assertEquals("fj**@126.com", x);
    }
}