package org.team4u.sc;

/**
 * 错误码决策器
 *
 * @author jay.wu
 */
public interface SystemErrorDecider {

    /**
     * 判断当前结果是否属于系统异常情况
     *
     * @param result           返回结果对象
     * @param resultCodeKey    结果返回码字段名称
     * @param systemErrorCodes 系统错误码集合
     * @return 是否系统错误
     */
    boolean isSystemError(Object result, String resultCodeKey, String[] systemErrorCodes);
}