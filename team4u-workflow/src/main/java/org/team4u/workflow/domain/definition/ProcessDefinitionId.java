package org.team4u.workflow.domain.definition;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import org.team4u.base.error.DataNotExistException;

/**
 * 流程定义标识
 *
 * @author jay.wu
 */
public class ProcessDefinitionId {
    /**
     * 标识
     */
    private final String id;
    /**
     * 版本
     */
    private final long version;

    public ProcessDefinitionId(String id) {
        String[] idAndVersion = StrUtil.split(id, "|");

        switch (idAndVersion.length) {
            case 0:
                this.id = id;
                this.version = -1;
                break;

            case 1:
                this.id = idAndVersion[0];
                this.version = -1;
                break;
            default:
                this.id = idAndVersion[0];
                this.version = Convert.toLong(idAndVersion[1]);
                break;
        }
    }

    public ProcessDefinitionId(String id, long version) {
        this.id = id;
        this.version = version;

        if (StrUtil.isBlank(id)) {
            throw new DataNotExistException("ProcessDefinitionId=" + id);
        }
    }

    public static ProcessDefinitionId of(String id) {
        return new ProcessDefinitionId(id);
    }

    public String getId() {
        return id;
    }

    public long getVersion() {
        return version;
    }

    @Override
    public String toString() {
        return id + "|" + version;
    }
}