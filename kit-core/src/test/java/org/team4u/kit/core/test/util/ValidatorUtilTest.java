package org.team4u.kit.core.test.util;

import org.junit.Assert;
import org.junit.Test;
import org.team4u.kit.core.util.ValidatorUtil;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.util.List;

public class ValidatorUtilTest {

    @Test
    public void validate() {
        ValidatorUtil.Result result = ValidatorUtil.validate(new A());
        Assert.assertTrue(result.hasError());
        Assert.assertEquals(3, result.messages().size());
    }

    public static class A {

        @NotEmpty
        public String name;

        @NotEmpty
        public List<String> names;

        @Email
        public String email;

        @Min(1)
        public int age;
    }
}
