package org.team4u.base.lang;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import org.junit.Assert;
import org.junit.Test;

public class IdCardTest {

    @Test
    public void nation() {
        IdCard.Front front = new IdCard.Front(null, null, "x", null);
        Assert.assertNull(front.getNation());

        front = new IdCard.Front(null, null, "汉", null);
        Assert.assertEquals("汉", front.getNation());
    }

    @Test
    public void name() {
        IdCard.Front front = new IdCard.Front("", null, null, null);
        Assert.assertNull(front.getName());

        front = new IdCard.Front(" 中 ", null, null, null);
        Assert.assertEquals("中", front.getName());

        front = new IdCard.Front(" 中·大 ", null, null, null);
        Assert.assertEquals("中·大", front.getName());

        front = new IdCard.Front(" 中·大1 ", null, null, null);
        Assert.assertNull(front.getName());

        front = new IdCard.Front("jay ", null, null, null);
        Assert.assertNull(front.getName());

        front = new IdCard.Front(StrUtil.repeat("中", 21), null, null, null);
        Assert.assertNull(front.getName());

        front = new IdCard.Front(StrUtil.repeat("中", 20), null, null, null);
        Assert.assertEquals(StrUtil.repeat("中", 20), front.getName());
    }

    @Test
    public void longValidityPeriod() {
        IdCard.Back back = new IdCard.Back(null, "长期");
        Assert.assertEquals(IdCard.Back.ValidityPeriodType.LONG, back.getValidityPeriodType());
    }

    @Test
    public void shortValidityPeriod() {
        IdCard.Back back = new IdCard.Back(null, "2021.01.01 - 2021.02.01");
        Assert.assertEquals(IdCard.Back.ValidityPeriodType.SHORT, back.getValidityPeriodType());
        Assert.assertEquals(DateUtil.parseDate("2021.01.01"), back.getValidityStartTime());
        Assert.assertEquals(DateUtil.parseDate("2021.02.01"), back.getValidityEndTime());

        back = new IdCard.Back(null, "2021.01.01 - ");
        Assert.assertEquals(IdCard.Back.ValidityPeriodType.SHORT, back.getValidityPeriodType());
        Assert.assertEquals(DateUtil.parseDate("2021.01.01"), back.getValidityStartTime());
        Assert.assertNull(back.getValidityEndTime());
    }

    @Test
    public void invalidityPeriod() {
        IdCard.Back back = new IdCard.Back(null, null);
        Assert.assertNull(back.getValidityPeriodType());
        Assert.assertNull(back.getValidityStartTime());
        Assert.assertNull(back.getValidityEndTime());
    }
}