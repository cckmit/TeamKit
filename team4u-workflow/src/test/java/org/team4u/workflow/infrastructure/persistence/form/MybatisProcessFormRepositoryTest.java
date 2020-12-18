package org.team4u.workflow.infrastructure.persistence.form;

import cn.hutool.core.lang.Dict;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.team4u.workflow.domain.form.ProcessForm;
import org.team4u.workflow.domain.form.ProcessFormItem;
import org.team4u.workflow.infrastructure.DbTest;

import static org.team4u.workflow.TestUtil.TEST;

public class MybatisProcessFormRepositoryTest extends DbTest {
    @Autowired
    private TestFormMapper testFormMapper;
    @Autowired
    private ProcessFormItemMapper formItemMapper;

    private MybatisProcessFormRepository<TestForm, TestFormDo> repository;

    @Before
    public void repository() {
        repository = new MybatisProcessFormRepository<TestForm, TestFormDo>(formItemMapper) {
            @Override
            protected TestForm toProcessForm(TestFormDo processFormDo) {
                return new TestForm();
            }

            @Override
            protected BaseMapper<TestFormDo> processFormMapper() {
                return testFormMapper;
            }

            @Override
            protected TestFormDo toProcessFormDo(ProcessForm form) {
                return new TestFormDo();
            }
        };
    }

    @Test
    public void formOf() {
        save();
        TestForm form = repository.formOf(TEST);
        System.out.println(JSON.toJSONString(form));
        Assert.assertEquals(Dict.create().set("x", 1), form.getFormItem().toFormItem());
        Assert.assertEquals(TEST, form.getName());
    }

    private void save() {
        TestForm form = new TestForm();
        form.setName(TEST);
        form.setFormId(TEST);
        form.setProcessInstanceId(TEST);

        ProcessFormItem item = new ProcessFormItem();
        item.toFormBody(Dict.create().set("x", 1));
        form.setFormItem(item);

        repository.save(form);

        Assert.assertNotNull(form.getId());
    }

    @Override
    protected String[] ddlResourcePaths() {
        return new String[]{"sql/process_form.sql"};
    }
}