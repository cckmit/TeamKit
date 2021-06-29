package org.team4u.sql.infrastructure.mybatis;

/**
 * 基于jetbrick模板的文件sql提供者
 *
 * @author jay.wu
 */
public class JetFileSqlProvider extends MybatisSqlProvider {

    public JetFileSqlProvider() {
        super(
                SqlProviderUtil.jetbrickTemplateEngine(),
                SqlProviderUtil.fileSqlResourceAppService()
        );
    }
}