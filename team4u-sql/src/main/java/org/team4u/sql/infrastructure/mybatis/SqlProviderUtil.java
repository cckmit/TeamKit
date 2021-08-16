package org.team4u.sql.infrastructure.mybatis;

import org.team4u.sql.application.SqlResourceAppService;
import org.team4u.sql.domain.ConfigSqlResourceRepository;
import org.team4u.sql.infrastructure.config.SpringResourceSqlConfigService;
import org.team4u.template.TemplateEngine;
import org.team4u.template.TemplateFunctionService;
import org.team4u.template.infrastructure.JetbrickTemplateEngine;

/**
 * sql提供者工具类
 *
 * @author jay.wu
 */
public class SqlProviderUtil {

    public static final TemplateFunctionService TEMPLATE_FUNCTION_SERVICE = new TemplateFunctionService();

    public static TemplateEngine jetbrickTemplateEngine() {
        return new JetbrickTemplateEngine(TEMPLATE_FUNCTION_SERVICE);
    }

    public static SqlResourceAppService fileSqlResourceAppService() {
        return new SqlResourceAppService(
                new ConfigSqlResourceRepository(
                        new SpringResourceSqlConfigService(
                                new SpringResourceSqlConfigService.Config()
                        )
                )
        );
    }
}