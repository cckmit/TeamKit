package org.team4u.sc;

/**
 * 执行命令
 *
 * @author jay.wu
 */
public class InvokeCommand {

    /**
     * 执行标识
     */
    private String id;

    /**
     * 执行返回结果类型
     */
    private Class<?> resultClass;

    /**
     * 结果缓存最大有效时长（毫秒）
     */
    private int resultTtlMillis;

    /**
     * 锁最大有效时长（毫秒）
     */
    private int lockTtlMillis;

    /**
     * 执行者
     */
    private Invoker invoker;

    /**
     * 结果返回码字段名称
     */
    private String resultCodeKey;

    /**
     * 系统错误码集合
     */
    private String[] systemErrorCodes;

    /**
     * 错误码决策器
     */
    private SystemErrorDecider systemErrorDecider;

    public String getId() {
        return id;
    }

    public InvokeCommand setId(String id) {
        this.id = id;
        return this;
    }

    public Class<?> getResultClass() {
        return resultClass;
    }

    public InvokeCommand setResultClass(Class<?> resultClass) {
        this.resultClass = resultClass;
        return this;
    }

    public int getResultTtlMillis() {
        return resultTtlMillis;
    }

    public InvokeCommand setResultTtlMillis(int resultTtlMillis) {
        this.resultTtlMillis = resultTtlMillis;
        return this;
    }

    public int getLockTtlMillis() {
        return lockTtlMillis;
    }

    public InvokeCommand setLockTtlMillis(int lockTtlMillis) {
        this.lockTtlMillis = lockTtlMillis;
        return this;
    }

    public Invoker getInvoker() {
        return invoker;
    }

    public InvokeCommand setInvoker(Invoker invoker) {
        this.invoker = invoker;
        return this;
    }

    public String getResultCodeKey() {
        return resultCodeKey;
    }

    public InvokeCommand setResultCodeKey(String resultCodeKey) {
        this.resultCodeKey = resultCodeKey;
        return this;
    }

    public String[] getSystemErrorCodes() {
        return systemErrorCodes;
    }

    public InvokeCommand setSystemErrorCodes(String[] systemErrorCodes) {
        this.systemErrorCodes = systemErrorCodes;
        return this;
    }

    public SystemErrorDecider getSystemErrorDecider() {
        return systemErrorDecider;
    }

    public InvokeCommand setSystemErrorDecider(SystemErrorDecider systemErrorDecider) {
        this.systemErrorDecider = systemErrorDecider;
        return this;
    }
}