package org.team4u.workflow.domain.definition;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import org.team4u.ddd.domain.model.ValueObject;
import org.team4u.workflow.domain.definition.exception.ProcessDefinitionIdIllegalException;

/**
 * 流程定义标识
 *
 * @author jay.wu
 */
public class ProcessDefinitionId implements ValueObject {
    /**
     * 标识
     */
    private final String id;
    /**
     * 版本
     */
    private final int version;

    public ProcessDefinitionId(String id) {
        String[] idAndVersion = StrUtil.splitToArray(id, "|");

        switch (idAndVersion.length) {
            case 0:
                this.id = id;
                this.version = 0;
                break;

            case 1:
                this.id = idAndVersion[0];
                this.version = 0;
                break;
            default:
                this.id = idAndVersion[0];
                this.version = Convert.toInt(idAndVersion[1]);
                break;
        }
    }

    public ProcessDefinitionId(String id, int version) {
        this.id = id;
        this.version = version;

        if (StrUtil.isBlank(id)) {
            throw new ProcessDefinitionIdIllegalException(id);
        }
    }

    public static ProcessDefinitionId of(String id) {
        return new ProcessDefinitionId(id);
    }

    public boolean hasVersion() {
        return getVersion() > 0;
    }

    public String getId() {
        return id;
    }

    public int getVersion() {
        return version;
    }

    @Override
    public String toString() {
        if (hasVersion()) {
            return id + "|" + version;
        }

        return id;
    }
}