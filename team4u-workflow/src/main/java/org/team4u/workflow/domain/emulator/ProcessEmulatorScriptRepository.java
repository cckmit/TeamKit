package org.team4u.workflow.domain.emulator;

/**
 * 流程模拟器脚本资源库
 *
 * @author jay.wu
 */
public interface ProcessEmulatorScriptRepository {

    /**
     * 获取模拟器脚本
     *
     * @param scriptId 脚本标识
     * @return 模拟器脚本
     */
    ProcessEmulatorScript configOfId(String scriptId);
}