package org.team4u.workflow.application;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Dict;
import com.alibaba.fastjson.JSON;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.team4u.base.config.ConfigService;
import org.team4u.base.config.LocalJsonConfigService;
import org.team4u.ddd.event.EventStore;
import org.team4u.ddd.infrastructure.persistence.memory.InMemoryEventStore;
import org.team4u.test.spring.SpringDbTest;
import org.team4u.workflow.TestUtil;
import org.team4u.workflow.application.command.CreateProcessFormCommand;
import org.team4u.workflow.application.command.StartProcessFormCommand;
import org.team4u.workflow.application.model.ProcessFormModel;
import org.team4u.workflow.domain.definition.ProcessAction;
import org.team4u.workflow.domain.definition.ProcessDefinition;
import org.team4u.workflow.domain.definition.ProcessDefinitionId;
import org.team4u.workflow.domain.form.DefaultFormPermissionService;
import org.team4u.workflow.domain.instance.ProcessInstance;
import org.team4u.workflow.domain.instance.node.handler.DynamicChoiceNodeHandler;
import org.team4u.workflow.infrastructure.BeanConfig;
import org.team4u.workflow.infrastructure.persistence.definition.JsonProcessDefinitionRepository;
import org.team4u.workflow.infrastructure.persistence.form.TestFormIndex;
import org.team4u.workflow.infrastructure.persistence.form.TestFormIndexMapper;
import org.team4u.workflow.infrastructure.persistence.form.TestFormIndexRepository;
import org.team4u.workflow.infrastructure.persistence.instance.InMemoryProcessInstanceRepository;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Set;

import static org.team4u.workflow.TestUtil.*;

@ContextConfiguration(classes = BeanConfig.class)
public class ProcessFormAppServiceTest extends SpringDbTest {

    @Autowired
    private TestFormIndexMapper testFormIndexMapper;

    private ProcessFormAppService processFormAppService;

    @PostConstruct
    private void initProcessFormAppService() {
        EventStore eventStore = new InMemoryEventStore();
        ConfigService configService = new LocalJsonConfigService();

        ProcessAppService appService = new ProcessAppService(
                new InMemoryProcessInstanceRepository(eventStore),
                new JsonProcessDefinitionRepository(configService)
        );

        appService.registerNodeHandler(new DynamicChoiceNodeHandler(selectorAppService()));

        processFormAppService = new ProcessFormAppService(
                eventStore,
                appService,
                new TestFormIndexRepository(testFormIndexMapper),
                new DefaultFormPermissionService()
        );
    }

    @Test
    public void submit() {
        String creator = TEST1;
        Dict detail = new Dict().set("x", TEST);

        processFormAppService.create(
                CreateProcessFormCommand.builder()
                        .processDefinitionId("simple")
                        .processInstanceId(TEST)
                        .processInstanceName(TEST)
                        .operatorId(creator)
                        .processInstanceDetail(detail)
                        .formIndex(
                                TestFormIndex.Builder.newBuilder()
                                        .withName(TEST)
                                        .build()
                        )
                        .build()
        );

        processFormAppService.start(StartProcessFormCommand.builder()
                .actionId("submit")
                .operatorId(creator)
                .processInstanceId(TEST)
                .processInstanceDetail(detail)
                .formIndex(
                        TestFormIndex.Builder.newBuilder()
                                .withName(TEST)
                                .build()
                )
                .build()
        );

        // 审批人查看
        ProcessFormModel model = processFormAppService.formOf(TEST, TEST);
        System.out.println(JSON.toJSONString(model));

        Assert.assertEquals(detail, model.getInstance().getProcessInstanceDetail().toDetailObject(detail.getClass()));
        Assert.assertEquals("[reject, approve]", model.getActions().toString());

        Assert.assertEquals("[nodeId=created,actionId=submit,nextNodeId=pending,remark='null',operator='test1']", model.getEvents().toString());

        Assert.assertNotNull(model.getFormIndex().getProcessInstanceId());
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
                StartProcessFormCommand.builder()
                        .actionId("reject")
                        .operatorId(TEST)
                        .remark(TEST)
                        .processInstanceId(TEST)
                        .processInstanceDetail(new Dict().set("x", TEST1))
                        .formIndex(
                                TestFormIndex.Builder.newBuilder()
                                        .build()
                        )
                        .build()
        );

        // 申请人查看
        ProcessFormModel model = processFormAppService.formOf(TEST, TEST1);

        Assert.assertEquals(
                new Dict().set("x", TEST),
                model.getInstance().getProcessInstanceDetail().toDetailObject(Dict.class)
        );
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

        ProcessInstance instance = newInstance()
                .setCurrentNode(definition.processNodeOf("created"))
                .setProcessDefinitionId(ProcessDefinitionId.of("simple"));

        Set<String> permissions = processFormAppService.operatorPermissionsOf(
                null,
                instance,
                definition,
                null,
                operator
        );

        List<ProcessAction> actions = processFormAppService.availableActionsOf(
                null,
                instance,
                permissions,
                definition,
                operator
        );
        Assert.assertEquals(expectedActions, actions.toString());
    }

    @Override
    protected String[] ddlResourcePaths() {
        return new String[]{"sql/test_form_index.sql", "sql/process_instance.sql"};
    }
}