package org.team4u.base.util;

import cn.hutool.core.util.StrUtil;

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

        public List<String> messages() {
            return messages;
        }

        public boolean hasError() {
            return !messages.isEmpty();
        }
    }
}