package org.team4u.id.infrastructure.seq;

import org.team4u.base.config.AbstractJsonConfigRepository;
import org.team4u.base.config.ConfigService;
import org.team4u.id.domain.seq.SequenceConfig;
import org.team4u.id.domain.seq.SequenceConfigRepository;

/**
 * 基于json的序号配置资源库
 *
 * @author jay.wu
 */
public class JsonSequenceConfigRepository extends AbstractJsonConfigRepository<SequenceConfig>
        implements SequenceConfigRepository {

    public JsonSequenceConfigRepository(ConfigService configService) {
        super(configService);
    }
}