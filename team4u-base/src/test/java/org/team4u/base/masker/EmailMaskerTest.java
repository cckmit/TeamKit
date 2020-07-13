package org.team4u.base.masker;

import org.junit.Assert;
import org.junit.Test;
import org.team4u.base.masker.EmailMasker;
import org.team4u.base.masker.SimpleMasker;

public class EmailMaskerTest {

    @Test
    public void mask() {
        SimpleMasker masker = new EmailMasker();
        String x = masker.mask("fjay@126.com");
        Assert.assertEquals("fj**@126.com", x);
    }
}