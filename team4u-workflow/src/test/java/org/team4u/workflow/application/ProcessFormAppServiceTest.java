package org.team4u.workflow.application;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Dict;
import com.alibaba.fastjson.JSON;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.team4u.base.config.ConfigService;
import org.team4u.base.config.LocalJsonConfigService;
import org.team4u.ddd.event.EventStore;
import org.team4u.ddd.infrastructure.persistence.memory.InMemoryEventStore;
import org.team4u.workflow.TestUtil;
import org.team4u.workflow.application.command.CreateProcessFormCommand;
import org.team4u.workflow.application.command.StartProcessFormCommand;
import org.team4u.workflow.application.model.ProcessFormModel;
import org.team4u.workflow.domain.definition.ProcessAction;
import org.team4u.workflow.domain.definition.ProcessDefinition;
import org.team4u.workflow.domain.definition.ProcessDefinitionId;
import org.team4u.workflow.domain.form.DefaultProcessFormPermissionService;
import org.team4u.workflow.domain.form.ProcessFormItem;
import org.team4u.workflow.domain.instance.ProcessNodeHandlers;
import org.team4u.workflow.domain.instance.node.handler.DynamicChoiceNodeHandler;
import org.team4u.workflow.infrastructure.DbTest;
import org.team4u.workflow.infrastructure.persistence.definition.JsonProcessDefinitionRepository;
import org.team4u.workflow.infrastructure.persistence.form.ProcessFormItemMapper;
import org.team4u.workflow.infrastructure.persistence.form.TestForm;
import org.team4u.workflow.infrastructure.persistence.form.TestFormMapper;
import org.team4u.workflow.infrastructure.persistence.form.TestProcessFormRepository;
import org.team4u.workflow.infrastructure.persistence.instance.InMemoryProcessInstanceRepository;

import javax.annotation.PostConstruct;
import java.util.List;

import static org.team4u.workflow.TestUtil.*;

public class ProcessFormAppServiceTest extends DbTest {

    @Autowired
    private TestFormMapper testFormMapper;
    @Autowired
    private ProcessFormItemMapper formItemMapper;

    private ProcessFormAppService processFormAppService;

    @PostConstruct
    private void initProcessFormAppService() {
        EventStore eventStore = new InMemoryEventStore();
        ConfigService configService = new LocalJsonConfigService();
        ProcessNodeHandlers handlers = new ProcessNodeHandlers();

        handlers.saveIdObject(new DynamicChoiceNodeHandler(selectorAppService()));

        processFormAppService = new ProcessFormAppService(
                eventStore,
                new ProcessAppService(
                        handlers,
                        new InMemoryProcessInstanceRepository(eventStore),
                        new JsonProcessDefinitionRepository(configService)
                ),
                new TestProcessFormRepository(testFormMapper, formItemMapper),
                new DefaultProcessFormPermissionService()
        );
    }

    @Test
    public void submit() {
        String creator = TEST1;

        processFormAppService.create(
                CreateProcessFormCommand.Builder.newBuilder()
                        .withActionId("submit")
                        .withProcessDefinitionId("simple")
                        .withProcessInstanceName(TEST)
                        .withOperatorId(creator)
                        .withProcessForm(
                                TestForm.Builder.newBuilder()
                                        .withName(TEST)
                                        .withFormId(TEST)
                                        .withFormItem(new ProcessFormItem(new Dict().set("x", TEST)))
                                        .build()
                        )
                        .build()
        );

        // 审批人查看
        ProcessFormModel model = processFormAppService.formOf(TEST, TEST);
        System.out.println(JSON.toJSONString(model));

        Assert.assertEquals("[reject, approve]", model.getActions().toString());

        Assert.assertEquals("[nodeId=created,actionId=submit,nextNodeId=pending,remark='null',operator='test1']", model.getEvents().toString());

        Assert.assertNotNull(model.getForm().getProcessInstanceId());
        Assert.assertEquals("pending", model.getInstance().getCurrentNode().getNodeId());
        Assert.assertNotNull(model.getInstance().currentAssigneeOf(TEST));

        // 申请人查看
        model = processFormAppService.formOf(TEST, creator);
        Assert.assertEquals("[]", model.getActions().toString());
    }

    @Test
    public void reject() {
        submit();

        processFormAppService.start(
                StartProcessFormCommand.Builder
                        .newBuilder()
                        .withActionId("reject")
                        .withOperatorId(TEST)
                        .withRemark(TEST)
                        .withProcessForm(TestForm.Builder.newBuilder()
                                .withFormId(TEST)
                                .build())
                        .build()
        );

        // 申请人查看
        ProcessFormModel model = processFormAppService.formOf(TEST, TEST1);
        Assert.assertEquals("rejected", model.getInstance().getCurrentNode().getNodeId());
        Assert.assertEquals(2, model.getEvents().size());
        Assert.assertEquals(TEST, CollUtil.getLast(model.getEvents()).getRemark());
    }

    @Test
    public void hasAvailableActions() {
        checkAvailableActions(TestUtil.TEST, "[save, submit, test]");
    }

    @Test
    public void noAvailableActions() {
        checkAvailableActions(TestUtil.TEST1, "[]");
    }

    private void checkAvailableActions(String operator, String expectedActions) {
        ProcessDefinition definition = TestUtil.definitionOf("simple");

        List<ProcessAction> actions = processFormAppService.availableActionsOf(
                null,
                TestUtil.newInstance()
                        .setCurrentNode(definition.availableProcessNodeOf("created"))
                        .setProcessDefinitionId(ProcessDefinitionId.of("simple")),
                operator
        );
        Assert.assertEquals(expectedActions, actions.toString());
    }

    @Override
    protected String[] ddlResourcePaths() {
        return new String[]{"sql/process_form.sql", "sql/process_instance.sql"};
    }
}