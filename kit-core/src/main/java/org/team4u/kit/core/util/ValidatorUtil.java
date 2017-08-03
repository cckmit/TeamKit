package org.team4u.kit.core.util;

import com.xiaoleilu.hutool.util.StrUtil;
import net.sf.oval.ConstraintViolation;
import net.sf.oval.Validator;

import java.util.ArrayList;
import java.util.List;

public class ValidatorUtil {

    private static Validator validator = new Validator();

    public static Result validate(Object obj) {
        Result result = new Result();

        if (obj == null) {
            result.error = true;
            result.addMessage("obj is null");
            return result;
        }

        List<ConstraintViolation> constraintViolations = validator.validate(obj);
        result.error = !constraintViolations.isEmpty();

        for (ConstraintViolation c : constraintViolations) {
            result.addMessage(c.getMessage());
        }

        return result;
    }

    public static class Result {

        private boolean error;

        private List<String> messages = new ArrayList<String>();

        public String firstMessage() {
            if (messages.isEmpty()) {
                return "";
            }

            return messages.get(0);
        }

        public Result addMessage(String message) {
            messages.add(message);
            return this;
        }

        public String message() {
            return StrUtil.join(",", messages);
        }

        public boolean hasError() {
            return error;
        }
    }
}