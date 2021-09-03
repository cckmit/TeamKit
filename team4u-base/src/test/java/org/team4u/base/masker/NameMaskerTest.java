package org.team4u.base.masker;

import org.junit.Assert;
import org.junit.Test;

public class NameMaskerTest {

    private final NameMasker masker = new NameMasker();

    @Test
    public void chineseNameSizeLessThan4() {
        Assert.assertEquals("**伦", masker.mask("周杰伦"));
        Assert.assertEquals("*伦", masker.mask("杰伦"));
        Assert.assertEquals("伦", masker.mask("伦"));
    }

    @Test
    public void chineseNameSizeGreatThan3() {
        Assert.assertEquals("**杰伦", masker.mask("是周杰伦"));
    }

    @Test
    public void otherName() {
        Assert.assertEquals("j****u", masker.mask("jay.wu"));
    }
}