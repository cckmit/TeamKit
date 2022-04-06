package org.team4u.command.infrastructure.util;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import lombok.Data;

import java.util.List;

/**
 * 响应码映射器
 * <p>
 * 1 originalCode和originalSubCode支持匹配为空字符串或者null，如：originalCode:"-"
 * <p>
 * 2 originalCode和originalSubCode支持正则表达式，如：originalCode:"1.1"，表示111,121等均匹配
 * <p>
 * 3 支持只配置originalCode，不配置originalSubCode，表示仅匹配originalCode
 * <p>
 * 4支持兜底规则，即不配置originalCode和originalSubCode，只配置standardCode
 *
 * @author jay.wu
 */
public class CodeMapper {

    public static final String NULL = "-";

    public String map(List<CodeMapping> codeMappings, String code, String subCode) {
        code = ObjectUtil.defaultIfEmpty(code, NULL);
        subCode = ObjectUtil.defaultIfEmpty(subCode, NULL);

        CodeMapping codeMapping = findSubCodeMapping(codeMappings, code, subCode);
        if (codeMapping != null) {
            return codeMapping.getStandardCode();
        }


        codeMapping = findCodeMapping(codeMappings, code);
        if (codeMapping != null) {
            return codeMapping.getStandardCode();
        }

        codeMapping = findDefaultCodeMapping(codeMappings);
        if (codeMapping != null) {
            return codeMapping.getStandardCode();
        }

        return null;
    }

    /**
     * 查找二级响应码映射对象
     *
     * @param codeMappings 配置
     * @param code         原始一级响应码
     * @param subCode      原始二级响应码
     * @return 映射对象
     */
    private CodeMapping findSubCodeMapping(List<CodeMapping> codeMappings, String code, String subCode) {
        return codeMappings.stream()
                .filter(it -> isMatch(code, it.getOriginalCode()))
                .filter(it -> isMatch(subCode, it.getOriginalSubCode()))
                .findFirst()
                .orElse(null);
    }

    /**
     * 查找一级响应码映射对象
     *
     * @param codeMappings 配置
     * @param code         原始响应码
     * @return 映射对象
     */
    private CodeMapping findCodeMapping(List<CodeMapping> codeMappings, String code) {
        return codeMappings.stream()
                .filter(it -> isMatch(code, it.getOriginalCode()))
                .filter(it -> StrUtil.isEmpty(it.getOriginalSubCode()))
                .findFirst()
                .orElse(null);
    }

    /**
     * 查找默认响应码映射对象
     *
     * @param codeMappings 配置
     * @return 映射对象
     */
    private CodeMapping findDefaultCodeMapping(List<CodeMapping> codeMappings) {
        return codeMappings.stream()
                .filter(it -> StrUtil.isEmpty(it.getOriginalCode()))
                .filter(it -> StrUtil.isEmpty(it.getOriginalSubCode()))
                .findFirst()
                .orElse(null);
    }

    private boolean isMatch(String actualCode, String originalCode) {
        if (originalCode == null) {
            return StrUtil.equals(actualCode, null);
        }

        if (StrUtil.equals(actualCode, originalCode)) {
            return true;
        }

        return ReUtil.isMatch(originalCode, actualCode);
    }

    /**
     * 错误码映射规则
     */
    @Data
    public static class CodeMapping {
        /**
         * 一级原始响应码
         * <p>
         * 支持匹配空字符串或者null：-
         * <p>
         * 支持正则表达式
         */
        private String originalCode;
        /**
         * 二级级原始响应码
         * <p>
         * 支持匹配空字符串或者null：-
         * <p>
         * 支持正则表达式
         */
        private String originalSubCode;
        /**
         * 映射后的标准响应码
         */
        private String standardCode;

        public CodeMapping() {
        }

        public CodeMapping(String originalCode, String originalSubCode, String standardCode) {
            this.originalCode = originalCode;
            this.originalSubCode = originalSubCode;
            this.standardCode = standardCode;
        }
    }
}