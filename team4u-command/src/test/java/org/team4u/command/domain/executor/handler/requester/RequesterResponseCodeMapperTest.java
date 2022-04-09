package org.team4u.command.domain.executor.handler.requester;

import org.junit.Assert;
import org.junit.Test;
import org.team4u.command.TestUtil;
import org.team4u.command.domain.executor.handler.CommandHandler;
import org.team4u.selector.domain.selector.SelectorResult;

public class RequesterResponseCodeMapperTest {

    private final RequesterResponseCodeMapper codeMapper = new RequesterResponseCodeMapper() {
        @Override
        protected String channelCodeOf(Context context) {
            return context.getRequest();
        }

        @Override
        protected void setStandardCode(Context context, SelectorResult standardCode) {
            context.setResponse(standardCode.toString());
        }
    };

    @Test
    public void handle() {
        CommandHandler.Context context = new CommandHandler.Context(
                null,
                TestUtil.configOf("test_command.json"),
                "1"
        );
        codeMapper.handle(context);

        Assert.assertEquals("2", context.getResponse());
    }
}