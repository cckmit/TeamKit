package org.team4u.kit.core.util;

import com.xiaoleilu.hutool.util.StrUtil;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.groups.Default;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ValidatorUtil {

    private static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    public static <T> Result validate(T obj) {
        Result result = new Result();

        if (obj == null) {
            result.error = true;
            result.addMessage("obj is null");
            return result;
        }

        Set<ConstraintViolation<T>> violations = validator.validate(obj, Default.class);
        if (violations != null && !violations.isEmpty()) {
            for (ConstraintViolation violation : violations) {
                result.addMessage(violation.getPropertyPath() + violation.getMessage());
            }
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