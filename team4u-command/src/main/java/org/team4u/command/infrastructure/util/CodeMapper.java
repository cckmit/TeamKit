package org.team4u.command.infrastructure.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import lombok.Data;

import java.util.List;
import java.util.Objects;

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
        return CollUtil.newArrayList(
                        findSubCodeMapping(codeMappings, code, subCode),
                        findCodeMapping(codeMappings, code),
                        findDefaultCodeMapping(codeMappings)
                ).stream()
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(new CodeMapping())
                .getStandardCode();
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

    @Data
    public static class CodeMapping {
        private String originalCode;
        private String originalSubCode;
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