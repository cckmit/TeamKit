package org.team4u.base.masker;

import org.junit.Assert;
import org.junit.Test;

public class EmailMaskerTest {

    @Test
    public void mask() {
        EmailMasker masker = new EmailMasker();
        Assert.assertEquals("fja***@126.com", masker.mask("fjay@126.com"));
        Assert.assertEquals("fj***@126.com", masker.mask("fj@126.com"));
    }
}