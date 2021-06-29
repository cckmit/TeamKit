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

    public MybatisSqlProvider(TemplateEngine engine,
                              SqlResourceAppService sqlResourceAppService) {
        this.engine = engine;
        this.sqlResourceAppService = sqlResourceAppService;
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

        String sqlId = context.getMapperType().getSimpleName() + "." + context.getMapperMethod().getName();
        String sqlContent = sqlResourceAppService.sql(sqlId);
        String result = engine.render(sqlContent, params);

        lm.append("id", sqlId).append("content", result);
        log.debug(lm.toString());

        return result;
    }
}