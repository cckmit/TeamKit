package org.team4u.command.handler.code;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import org.team4u.command.HandlerInterceptorService;
import org.team4u.command.handler.AbstractDefaultHandler;
import org.team4u.core.lang.EasyMap;
import org.team4u.template.TemplateEngine;

import java.util.List;
import java.util.Objects;

/**
 * 响应码映射处理器
 *
 * @author jay.wu
 */
public class CodeMappingHandler extends AbstractDefaultHandler<
        CodeMappingHandler.MappingConfig,
        CodeMappingHandler.CodeMapping
        > {

    public CodeMappingHandler(TemplateEngine templateEngine,
                              HandlerInterceptorService handlerInterceptorService) {
        super(templateEngine, handlerInterceptorService);
    }

    @Override
    protected CodeMapping internalHandle(MappingConfig mappingConfig, EasyMap attributes) {
        String code = attributes.getProperty(mappingConfig.getCodeKey(), "");
        String subCode = attributes.getProperty(mappingConfig.getSubCodeKey(), "");

        CodeMapping matchedCodeMapping = CollUtil.newArrayList(
                findSubCodeMapping(mappingConfig, code, subCode),
                findCodeMapping(mappingConfig, code),
                findDefaultCodeMapping(mappingConfig)
        ).stream()
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(new CodeMapping());

        setResult(mappingConfig.getStandardCodeKey(), attributes, matchedCodeMapping);

        return matchedCodeMapping;
    }

    /**
     * 查找二级响应码映射对象
     *
     * @param mappingConfig 配置
     * @param code          原始一级响应码
     * @param subCode       原始二级响应码
     * @return 映射对象
     */
    private CodeMapping findSubCodeMapping(MappingConfig mappingConfig, String code, String subCode) {
        return mappingConfig.codeMappings.stream()
                .filter(it -> StrUtil.equals(code, it.getOriginalCode()))
                .filter(it -> StrUtil.equals(subCode, it.getOriginalSubCode()))
                .findFirst()
                .orElse(null);
    }

    /**
     * 查找一级响应码映射对象
     *
     * @param mappingConfig 配置
     * @param code          原始响应码
     * @return 映射对象
     */
    private CodeMapping findCodeMapping(MappingConfig mappingConfig, String code) {
        return mappingConfig.codeMappings.stream()
                .filter(it -> StrUtil.equals(code, it.getOriginalCode()))
                .filter(it -> StrUtil.isEmpty(it.getOriginalSubCode()))
                .findFirst()
                .orElse(null);
    }

    /**
     * 查找默认响应码映射对象
     *
     * @param mappingConfig 配置
     * @return 映射对象
     */
    private CodeMapping findDefaultCodeMapping(MappingConfig mappingConfig) {
        return mappingConfig.codeMappings.stream()
                .filter(it -> StrUtil.isEmpty(it.getOriginalCode()))
                .findFirst()
                .orElse(null);
    }

    @Override
    protected boolean isSaveResultToAttributes() {
        return false;
    }

    private void setResult(String standardCodeKey,
                           EasyMap attributes,
                           CodeMapping codeMapping) {
        attributes.setProperty(
                standardCodeKey,
                codeMapping.getStandardCode()
        );
    }

    @Override
    public String id() {
        return "codeMapping";
    }

    public static class MappingConfig {
        private List<CodeMapping> codeMappings;
        /**
         * 原始一级响应值属性路径
         */
        private String codeKey;
        /**
         * 原始二级响应值属性路径
         */
        private String subCodeKey;
        /**
         * 标准值属性路径
         */
        private String standardCodeKey;

        public List<CodeMapping> getCodeMappings() {
            return codeMappings;
        }

        public MappingConfig setCodeMappings(List<CodeMapping> codeMappings) {
            this.codeMappings = codeMappings;
            return this;
        }

        public String getCodeKey() {
            return codeKey;
        }

        public MappingConfig setCodeKey(String codeKey) {
            this.codeKey = codeKey;
            return this;
        }

        public String getSubCodeKey() {
            return subCodeKey;
        }

        public MappingConfig setSubCodeKey(String subCodeKey) {
            this.subCodeKey = subCodeKey;
            return this;
        }

        public String getStandardCodeKey() {
            return standardCodeKey;
        }

        public MappingConfig setStandardCodeKey(String standardCodeKey) {
            this.standardCodeKey = standardCodeKey;
            return this;
        }
    }

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

        public String getOriginalCode() {
            return originalCode;
        }

        public void setOriginalCode(String originalCode) {
            this.originalCode = originalCode;
        }

        public String getOriginalSubCode() {
            return originalSubCode;
        }

        public void setOriginalSubCode(String originalSubCode) {
            this.originalSubCode = originalSubCode;
        }

        public String getStandardCode() {
            return standardCode;
        }

        public void setStandardCode(String standardCode) {
            this.standardCode = standardCode;
        }
    }
}