package org.team4u.command.domain.executor.handler.requester.http;

/**
 * soap请求者
 *
 * @author jay.wu
 */
public interface SoapRequester extends HttpRequester {

    /**
     * 命名空间key
     */
    String NAMESPACE_KEY = "namespace";
}