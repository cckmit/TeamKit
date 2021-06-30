package org.team4u.sql.infrastructure.mybatis;


import cn.hutool.log.Log;
import org.apache.ibatis.builder.annotation.ProviderContext;
import org.team4u.base.log.LogMessage;
import org.team4u.sql.application.SqlResourceAppService;
import org.team4u.template.TemplateEngine;

import java.util.Map;

/**
 * sql提供者
 *
 * @author jay.wu
 */
public class MybatisSqlProvider {

    private final Log log = Log.get();

    private final TemplateEngine engine;
    private final SqlResourceAppService sqlResourceAppService;

    private static final ThreadLocal<Map<String, Object>> EXTRA_PARAMS = new ThreadLocal<>();

    public MybatisSqlProvider(TemplateEngine engine,
                              SqlResourceAppService sqlResourceAppService) {
        this.engine = engine;
        this.sqlResourceAppService = sqlResourceAppService;
    }

    /**
     * 设置额外参数
     *
     * @param params 额外参数集合
     */
    public static void extraParams(Map<String, Object> params) {
        EXTRA_PARAMS.set(params);
    }

    /**
     * 提供执行的sql
     *
     * @param context 上下文
     * @param params  sql入参
     * @return sql
     */
    public String sql(ProviderContext context, Map<String, Object> params) {
        LogMessage lm = LogMessage.create(this.getClass().getSimpleName(), "sql");

        Map<String, Object> newParams = withExtraParams(params);

        String sqlId = context.getMapperType().getSimpleName() + "." + context.getMapperMethod().getName();
        String sqlContent = sqlResourceAppService.sql(sqlId);
        String result = engine.render(sqlContent, newParams);

        lm.append("id", sqlId).append("content", result);
        log.debug(lm.toString());

        return result;
    }

    private Map<String, Object> withExtraParams(Map<String, Object> params) {
        Map<String, Object> extra = EXTRA_PARAMS.get();

        if (extra != null) {
            params.putAll(extra);
        }

        EXTRA_PARAMS.remove();
        return params;
    }
}