package org.team4u.command.domain.executor.handler.requester.http;

/**
 * 模拟Soap的请求者
 *
 * @author jay.wu
 */
public class MockSoapRequester extends MockHttpRequester implements SoapRequester {

    public MockSoapRequester(String body) {
        super(body);
    }

    public MockSoapRequester(int status, String body) {
        super(status, body);
    }
}