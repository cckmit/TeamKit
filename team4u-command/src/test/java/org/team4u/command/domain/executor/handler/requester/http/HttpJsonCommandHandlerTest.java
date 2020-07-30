package org.team4u.command.domain.executor.handler.requester.http;

import cn.hutool.http.HttpStatus;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.team4u.command.TestUtil;
import org.team4u.command.domain.executor.CommandHandler;
import org.team4u.command.infrastructure.executor.MockCommandRequest;
import org.team4u.command.infrastructure.executor.MockCommandResponse;

/**
 * @author Jay Wu
 */
@RunWith(MockitoJUnitRunner.class)
public class HttpJsonCommandHandlerTest {

    @Mock
    private HttpRequester httpRequester;

    @Test
    public void toCommandResponse() {
        HttpRequester.HttpResponse httpResponse = new HttpRequester.HttpResponse(
                HttpStatus.HTTP_OK,
                "{\"x\":\"0\"}"
        );

        Mockito.when(httpRequester.execute(null)).thenReturn(httpResponse);

        HttpJsonCommandRequester handler = new HttpJsonCommandRequester(httpRequester) {

            @Override
            protected HttpRequester.HttpRequest toRequest(Context context) {
                return null;
            }
        };

        CommandHandler.Context context = new CommandHandler.Context(
                "test_command",
                TestUtil.configOf("test_command.json"),
                new MockCommandRequest(null)
        );

        handler.handle(context);

        MockCommandResponse mr = (MockCommandResponse) context.getResponse();
        Assert.assertEquals("0", mr.getChannelCode());
    }
}