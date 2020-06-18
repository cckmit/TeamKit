package org.team4u.core.masker;

import org.junit.Assert;
import org.junit.Test;

public class SimpleMaskerTest {

    @Test
    public void mask() {
        SimpleMasker masker = new SimpleMasker(0, 1);
        String value = masker.mask("fjay");
        Assert.assertEquals("*jay", value);
    }

    @Test
    public void maskWithReverse() {
        SimpleMasker masker = new SimpleMasker(1, -1);
        String value = masker.mask("fjay");
        Assert.assertEquals("f***", value);

        masker = new SimpleMasker(1, -3);
        value = masker.mask("fjayblue");
        Assert.assertEquals("f*****ue", value);
    }

    @Test
    public void unmask() {
        SimpleMasker masker = new SimpleMasker(1, 1);
        String value = masker.mask("f");
        Assert.assertEquals("f", value);

        masker = new SimpleMasker(1, -3);
        value = masker.mask("f");
        Assert.assertEquals("f", value);
    }

    @Test
    public void name() {
        Assert.assertEquals("fja*", Maskers.Type.NAME.mask("fjay"));
    }

    @Test
    public void mobile() {
        Assert.assertEquals("138****2222", Maskers.Type.MOBILE.mask("13822222222"));
    }

    @Test
    public void bankCardNo() {
        Assert.assertEquals("622908********5317", Maskers.Type.BANK_CARD_NO.mask("622908311111735317"));
    }
}