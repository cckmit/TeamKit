package org.team4u.sql.application;

import org.team4u.base.error.SystemDataNotExistException;
import org.team4u.sql.domain.SqlResource;
import org.team4u.sql.domain.SqlResourceRepository;

/**
 * sql资源应用服务
 *
 * @author jay.wu
 */
public class SqlResourceAppService {

    private final SqlResourceRepository sqlResourceRepository;

    public SqlResourceAppService(SqlResourceRepository sqlResourceRepository) {
        this.sqlResourceRepository = sqlResourceRepository;
    }

    public String sql(String id) {
        SqlResource resource = sqlResourceRepository.resourceOf(id);

        if (resource == null) {
            throw new SystemDataNotExistException("sql|id=" + id);
        }

        return resource.getContent().replace(";", "");
    }
}