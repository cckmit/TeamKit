package org.team4u.workflow.infrastructure.persistence.form;

import cn.hutool.core.lang.Dict;
import com.alibaba.fastjson.JSON;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.team4u.workflow.TestUtil;
import org.team4u.workflow.domain.form.ProcessForm;
import org.team4u.workflow.domain.form.ProcessFormItem;
import org.team4u.workflow.infrastructure.DbTest;

public class MybatisProcessFormRepositoryTest extends DbTest {
    @Autowired
    private TestFormMapper testFormMapper;

    @Autowired
    private ProcessFormItemMapper formItemMapper;

    private MybatisProcessFormRepository<TestFormDo> repository;

    @Before
    public void repository() {
        repository = new MybatisProcessFormRepository<>(testFormMapper, formItemMapper);
    }

    @Test
    public void formOf() {
        save();
        ProcessForm form = repository.formOf(TestUtil.TEST);
        System.out.println(JSON.toJSONString(form));
        Assert.assertEquals(Dict.create().set("x", 1), form.getFormItem().toFormItem());
    }

    @Test
    public void save() {
        ProcessForm form = new ProcessForm();
        TestFormDo formDo = new TestFormDo();
        formDo.setFormId(TestUtil.TEST);
        formDo.setProcessInstanceId(TestUtil.TEST);
        form.setFormHeader(formDo);

        ProcessFormItem item = new ProcessFormItem();
        item.setFormId(TestUtil.TEST);
        item.toFormBody(Dict.create().set("x", 1));
        form.setFormItem(item);
        repository.save(form);
    }

    @Override
    protected String[] ddlResourcePaths() {
        return new String[]{"sql/process_form.sql"};
    }
}