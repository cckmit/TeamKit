20201221-轻量级流程表单设计

## 使用说明

### 引入依赖

#### maven

```xml
<dependency>
  <groupId>org.team4u</groupId>
  <artifactId>team4u-workflow</artifactId>
  
</dependency>
```

#### 数据模型

数据库脚本

```mysql
drop table `process_instance`;
create table `process_instance`
(
    `id`                         bigint(20) unsigned not null auto_increment comment '自增长标识',
    `process_instance_id`        varchar(32)         not null default '' comment '流程实例标识',
    `process_instance_name`      varchar(100)        not null default '' comment '流程实例名称',
    `process_definition_id`      varchar(32)         not null default '' comment '流程流程定义标识',
    `process_definition_version` int unsigned        not null default 0 comment '流程定义版本',
    `process_definition_name`    varchar(100)        not null default '' comment '流程流程定义名称',
    `current_node_id`            varchar(32)         not null default '' comment '当前流程节点标识',
    `current_node_name`          varchar(100)        not null default '' comment '当前流程节点名称',
    `create_by`                  varchar(32)         not null default '' comment '创建者标识',
    `update_by`                  varchar(32)         not null default '' comment '编辑者标识',
    `create_time`                timestamp           not null default '1970-01-01 23:59:59' comment '创建时间',
    `update_time`                timestamp           not null default current_timestamp on update current_timestamp comment '更新时间',
    primary key (`id`),
    unique index `uniq_process_instance_id` (`process_instance_id`),
    index `idx_process_instance_name` (`process_instance_name`),
    index `idx_current_node_id` (`current_node_id`),
    index `idx_process_definition_id` (`process_definition_id`),
    index `idx_create_by` (`create_by`),
    index `idx_update_time` (`update_time`)
) comment ='流程实例';

drop table `process_assignee`;
create table `process_assignee`
(
    `id`                  bigint(20) unsigned not null auto_increment comment '自增长标识',
    `process_instance_id` varchar(32)         not null default '' comment '流程实例标识',
    `node_id`             varchar(100)        not null default '' comment '流程节点标识',
    `action_id`           varchar(32)         not null default '' comment '动作',
    `assignee`            varchar(32)         not null default '' comment '审批人',
    `create_time`         timestamp           not null default '1970-01-01 23:59:59' comment '创建时间',
    `update_time`         timestamp           not null default current_timestamp on update current_timestamp comment '更新时间',
    primary key (`id`),
    unique index `uniq_process_assignee` (`process_instance_id`, node_id),
    index `idx_assignee` (`assignee`)
) comment ='流程处理人';

drop table `process_definition`;
create table `process_definition`
(
    `id`                         bigint(20) unsigned not null auto_increment comment '自增长标识',
    `process_definition_id`      varchar(32)         not null default '' comment '流程定义版本',
    `process_definition_version` BIGINT(20) unsigned not null default 0 comment '流程定义版本',
    `process_definition_name`    varchar(100)        not null default '' comment '流程定义名称',
    `process_definition_body`    varchar(21000)      not null default '' comment '流程定义内容',
    `create_by`                  varchar(32)         not null default '' comment '创建者标识',
    `update_by`                  varchar(32)         not null default '' comment '编辑者标识',
    `create_time`                timestamp           not null default '1970-01-01 23:59:59' comment '创建时间',
    `update_time`                timestamp           not null default current_timestamp on update current_timestamp comment '更新时间',
    primary key (`id`),
    unique index `uniq_process_definition_id` (`process_definition_id`, process_definition_version),
    index `idx_process_definition_name` (`process_definition_name`),
    index `idx_update_time` (`update_time`)
) comment ='流程定义';

drop table `process_instance_detail`;
create table if not exists `process_instance_detail`
(
    `id`                  bigint(20) unsigned not null auto_increment comment '自增长标识',
    `process_instance_id` varchar(32)         not null default '' comment '流程实例标识',
    `type`                varchar(100)        not null default '' comment '明细类型',
    `body`                varchar(21800)      not null default '' comment '明细内容',
    `create_time`         timestamp           not null default '1970-01-01 23:59:59' comment '创建时间',
    `update_time`         timestamp           not null default current_timestamp on update current_timestamp,
    primary key (`id`),
    unique index `uniq_process_instance_id` (`process_instance_id`)
) comment ='流程实例明细';


drop table `stored_event`;
create table `stored_event`
(
    `id`          bigint(20) unsigned not null auto_increment comment '自增长标识',
    `event_id`    bigint(20) unsigned not null default 0 comment '事件标识',
    `domain_id`   varchar(32)         not null default '' comment '领域标识',
    `type_name`   varchar(255)        not null default '' comment '事件类型名称',
    `event_body`  varchar(4000)       not null default '' comment '事件值',
    `occurred_on` timestamp           not null default '1970-01-01 23:59:59' comment '发生时间',
    `create_time` timestamp           not null default '1970-01-01 23:59:59' comment '创建时间',
    `update_time` timestamp           not null default current_timestamp on update current_timestamp comment '更新时间',
    primary key (`id`),
    unique index `uniq_event_id` (event_id),
    index `idx_domain_id` (domain_id)
)
    comment ='存储的事件'
;
```
数据模型说明

| 表名                    | 中文         | 备注                                   |
| ----------------------- | ------------ | -------------------------------------- |
| process_instance        | 流程实例     | 如当前节点、创建人等                   |
| process_assignee        | 流程处理人   | 保存当前流程处理人以及历史流程处理人   |
| process_definition      | 流程定义     | 保存流程节点、动作以及流转规则         |
| process_instance_detail | 流程实例明细 | 保存额外的业务属性                     |
| stored_event            | 领域事件     | 如流程节点变更事件，可用于展示审批日志 |

### Demo

#### 设计业务表单

数据模型

```sql
create table if not exists `test_form_index`
(
    `id`                  bigint(20) unsigned not null auto_increment comment '自增长标识',
    `process_instance_id` varchar(100)        not null default '' comment '表单名称',
    `name`                varchar(32)         not null default '' comment '流程实例标识',
    `create_time`         timestamp           not null default '1970-01-01 23:59:59' comment '创建时间',
    `update_time`         timestamp           not null default current_timestamp on update current_timestamp,
    primary key (`id`),
    unique index `uniq_process_instance_id` (`process_instance_id`)
) comment ='测试表单';
```

| 表名      | 中文         | 备注                           |
| --------- | ------------ | ------------------------------ |
| test_form | 测试表单表头 | 仅保存需要列表中搜索的字段信息 |

数据模型对象

用于将数据库字段和Java类做一一映射。

```java

@TableName("test_form_index")
public class TestFormIndexDo extends FormIndexDo {

  private String name;

    ...
}

```

领域模型对象

用于业务处理，避免与数据库模型紧耦合，更多细节可以参考DDD相关概念。

```java
public class TestFormIndex extends FormIndex {

  private String name;

    ...
}
```

#### 实现基础设施

表单资源库

- 用于持久化测试表单，包括表头和明细
- 负责数据模型与领域模型相互转换
  - 父类将自动复制数据模型与领域模型相同名称的字段
  - 转换方法仅需要处理差异字段

```java
@Repository
public interface TestFormIndexMapper extends BaseMapper<TestFormIndexDo> {
}

public class TestFormIndeRepository extends MybatisFormIndexRepository<TestFormIndex, TestFormIndexDo> {

    public TestFormIndeRepository(TestFormIndexMapper testFormIndexMapper) {
        super(testFormIndexMapper);
    }

    @Override
    protected TestFormIndex toFormIndex(TestFormIndexDo formIndexDo) {
        return new TestFormIndex();
    }

    @Override
    protected TestFormIndexDo toFormIndexDo(TestFormIndex formIndex) {
        return new TestFormIndexDo();
    }
}
```

#### 定义表单流程

表单流程定义，相当于表单类型，每一种表单需要对应一种流程定义（表单类型）

##### 流程表单动作

- 负责定义处理人可用动作，可配置权限要求 ，用于前端页面展示按钮
  - ProcessFormAction
- 动作权限
  - ProcessFormAction.Permission
    - EDIT，编辑权限，如表单创建者编辑草稿或者提交表单
    - REVIEW，审批权限，如表单审批人用于审批表单
    - VIEW，查看权限，如表单创建者和历史/当前审批人可查看表单
  - 可根据业务需求进行权限扩展
- 当前处理人权限
  - FormPermissionService负责初始化
  - 可扩展默认实现类以适应业务需求：DefaultFormPermissionService

##### 流程节点

流程节点用于描述当前流程的每一个环节，一般分为两种类型：

- 静态节点
  - 固定路由到下一个节点
  - 当下一个节点路由（nextNodeId）为空时，表示流程实例已全部完成
  - 可用于描述表单状态
- 动态节点
  - 根据条件动态路由到下一个节点
  - 仅用于临时路由使用

**静态节点**

StaticNode

- 标准静态节点
- 流程根节点必须为此类型

AssigneeNode

- 处理人节点
- 当ruleType=USER时，ruleExpression可填写审批人标识（多人使用逗号隔开）
- 通过扩展AssigneeNodeHandler可以支持如角色等配置

**动态节点**

ActionChoiceNode

- 动作选择器节点
- 根据当前触发的动作来决定下一个节点路由

AssigneeActionChoiceNode

- 处理人动作选择器节点
- 根据审批人触发的动作来决定下一个节点路由，同时记录审批动作历史
- 当存在多个审批人时，可以选择ANY（任意一人审批）或者ALL（所有人审批）策略

##### 代码示例

```json
{
  "actions": [
    {
      "@type": "org.team4u.workflow.domain.form.process.definition.ProcessFormAction",
      "actionId": "save",
      "actionName": "保存",
      "requiredPermissions": [
        "EDIT"
      ]
    },
    {
      "@type": "org.team4u.workflow.domain.form.process.definition.ProcessFormAction",
      "actionId": "submit",
      "actionName": "提交",
      "requiredPermissions": [
        "EDIT"
      ]
    },
    {
      "@type": "org.team4u.workflow.domain.form.process.definition.ProcessFormAction",
      "actionId": "approve",
      "actionName": "同意",
      "requiredPermissions": [
        "REVIEW"
      ]
    },
    {
      "@type": "org.team4u.workflow.domain.form.process.definition.ProcessFormAction",
      "actionId": "reject",
      "actionName": "拒绝",
      "requiredPermissions": [
        "REVIEW"
      ]
    }
  ],
  "nodes": [
    {
      "@type": "org.team4u.workflow.domain.definition.node.StaticNode",
      "nodeId": "created",
      "nodeName": "已保存",
      "nextNodeId": "createdActionNode"
    },
    {
      "@type": "org.team4u.workflow.domain.definition.node.ActionChoiceNode",
      "nodeId": "createdActionNode",
      "nodeName": "已保存动作选择器",
      "actionNodes": [
        {
          "actionId": "save",
          "nextNodeId": "created"
        },
        {
          "actionId": "submit",
          "nextNodeId": "pending"
        }
      ]
    },
    {
      "@type": "org.team4u.workflow.domain.form.process.definition.node.AssigneeStaticNode",
      "nodeId": "pending",
      "nodeName": "待审核",
      "ruleType": "USER",
      "ruleExpression": "test",
      "nextNodeId": "pendingActionNode"
    },
    {
      "@type": "org.team4u.workflow.domain.form.process.definition.node.AssigneeActionChoiceNode",
      "nodeId": "pendingActionNode",
      "nodeName": "待审核动作选择器",
      "choiceType": "ANY",
      "actionNodes": [
        {
          "actionId": "reject",
          "nextNodeId": "rejected"
        },
        {
          "actionId": "approve",
          "nextNodeId": "completed"
        }
      ]
    },
    {
      "@type": "org.team4u.workflow.domain.definition.node.StaticNode",
      "nodeId": "rejected",
      "nodeName": "审批拒绝"
    },
    {
      "@type": "org.team4u.workflow.domain.definition.node.StaticNode",
      "nodeId": "completed",
      "nodeName": "审批完成"
    }
  ]
}
```

#### 构建表单应用服务

```java
@Import(ProcessBeanConfig.class)
@Configuration
public class ProcessFormBeanConfig {
    @Bean
    public ProcessFormAppService testFormAppService(EventStore eventStore,
                                                    ProcessAppService processAppService) {
        return new ProcessFormAppService(
            eventStore,
            processAppService,
            new TestFormIndexRepository(testFormIndexMapper),
            new DefaultFormPermissionService()
        );
    }
}
```
##### 查看表单明细

表单明细模型包含以下属性：

- 流程实例，用于获取当前节点（状态）、创建人等
- 流程表单，用于展示业务属性
- 当前处理人可用动作集合，用于前端按钮展示
- 流程节点变更事件集合，用于前端审批日志展示

**代码示例**

申请人查看

```java
ProcessFormModel model=processFormAppService.formOf(TEST,TEST1);

        Assert.assertEquals("pending",model.getInstance().getCurrentNode().getNodeId());
        Assert.assertEquals("[]",model.getActions().toString());
```

审批人查看

```java
ProcessFormModel model=processFormAppService.formOf(formId,operatorId);

// 审批人可用动作
        Assert.assertEquals("[reject, approve]",model.getActions().toString());

// 审批日志
        Assert.assertEquals("[nodeId=created,actionId=submit,nextNodeId=pending,remark='null',operator='test1']",model.getEvents().toString());

// 当前节点
        Assert.assertEquals("pending",model.getInstance().getCurrentNode().getNodeId());
```
ProcessFormModel

```java
/**
 * 流程表单模型
 *
 * @author jay.wu
 */
public class ProcessFormModel {
    /**
     * 表单索引
     */
    private FormIndex formIndex;
    /**
     * 流程实例
     */
    private ProcessInstance instance;
    /**
     * 当前处理人可用动作集合
     */
    private List<ProcessAction> actions;
    /**
     * 流程节点变更事件集合
     */
    private List<ProcessNodeChangedEvent> events;
    
    ...
}
```

##### 新建表单

- 在创建表单前，需要收集当前处理人、触发动作、表单内容等，并配置流程定义（表单类型）
- 当表单创建后，将根据流程定义进行后续流转
- 每当路由到静态节点时，都将发布节点变更事件（ProcessNodeChangedEvent）

代码示例

```java
processFormAppService.create(
        CreateProcessFormCommand.Builder.newBuilder()
        .withActionId("submit")
        .withProcessDefinitionId("simple")
        .withProcessInstanceName(TEST)
        .withProcessInstanceDetail(new ProcessInstanceDetail(new Dict().set("x",TEST)))
        .withOperatorId(TEST1)
        .withFormIndex(
        TestForm.Builder.newBuilder()
        .withName(TEST)
        .withFormId(TEST)
        .build()
        )
        .build()
        );
```

##### 处理表单

- 处理表单与新建表单类似，需要收集当前处理人、触发动作、表单标识/内容等
- 默认的，只有EDIT权限的动作才会保存表单，其他动作仅保存流程实例，更多细节请参考DefaultFormIndexPermissionService.shouldSaveProcessForm

```java
processFormAppService.start(
        StartProcessFormCommand.Builder
        .newBuilder()
        .withActionId("reject")
        .withOperatorId(TEST)
        .withRemark(TEST)
        .withFormIndex(TestForm.Builder.newBuilder()
        .withFormId(TEST)
        .build())
        .build()
        );
```

##### 查询表单列表

- 对流程表单进行列表查询是非常常见的需求，查询时需要关联流程表

- 为了减少开发者对流程模型的学习成本，这里提供了常见的列表查询SQL

  - 将实例中的问号（?）调整为实际审批/申请人，再加上表单自身的查询条件(如f.name)即可完成查询

  - current_node_name可用于状态展示
  - process_definition_name可用于表单类型展示

```mysql
# 待审批列表
select pi.current_node_id,
       pi.current_node_name,
       pi.process_definition_id,
       pi.process_definition_name,
       f.*
from process_instance pi,
     process_assignee pa,
     test_form_index f
where pi.process_instance_id = f.process_instance_id
  and pi.process_instance_id = pa.process_instance_id
  and pi.current_node_id = pa.node_id
  and pa.action_id = ''
  and pa.assignee = '?';

# 历史审批列表
select pi.current_node_id,
       pi.current_node_name,
       pi.process_definition_id,
       pi.process_definition_name,
       f.*
from process_instance pi,
     process_assignee pa,
     test_form_index f
where pi.process_instance_id = f.process_instance_id
  and pi.process_instance_id = pa.process_instance_id
  and pa.action_id != ''
  and pa.assignee = '?';

# 申请列表
select pi.current_node_id,
       pi.current_node_name,
       pi.process_definition_id,
       pi.process_definition_name,
       f.*
from process_instance pi,
     test_form_index f
where pi.process_instance_id = f.process_instance_id
  and pi.create_by = '?';
```

### 流程模拟器

在定义流程时，我们希望尽快得到反馈，以便及时调整，为此，我们提供了流程模拟器。

流程模拟器无需依赖任何外部资源，只需要定义模拟脚本即可完成测试。

#### 流程模拟器脚本

脚本描述了需要测试的流程定义、执行步骤以及期望结果


脚本示例：test_simple_completed_script.json

```json
{
  "processDefinitionId": "simple",
  "steps": [
    {
      "operatorId": "a",
      "actionId": "save",
      "ext": {
        "a": 1
      },
      "expected": {
        "nodeId": "created"
      }
    },
    {
      "operatorId": "a",
      "actionId": "submit",
      "expected": {
        "nodeId": "pending"
      }
    },
    {
      "operatorId": "test",
      "actionId": "approve",
      "expected": {
        "nodeId": "completed"
      }
    }
  ]
}
```

流程定义示例：simple.json

参考【使用说明-Demo-定义表单流程】章节

#### 构建模拟器

模拟器将依次执行脚本中的步骤，并与期望结果对比，如果不一致则停止运行，并抛出错误提示。

代码示例：

```java
ProcessEmulator emulator=ProcessEmulatorFactory.create();

        emulator.start(
        "test_simple_completed_script",
        testFormIndex,
        ext
        );
```

End