package org.team4u.workflow.domain.instance.node.handler.bean;

import cn.hutool.log.Log;
import org.team4u.base.log.LogMessage;
import org.team4u.workflow.domain.instance.node.handler.ProcessNodeHandlerContext;

/**
 * 日志打印处理器
 *
 * @author jay.wu
 */
public class LogBeanHandler implements ProcessBeanHandler {

    private final Log log = Log.get();

    @Override
    public String id() {
        return "log";
    }

    @Override
    public void handle(ProcessNodeHandlerContext context) {
        log.info(LogMessage.create(this.getClass().getSimpleName(), "log")
                .append("context", context)
                .toString());
    }
}