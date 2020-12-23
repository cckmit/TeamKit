package org.team4u.workflow.infrastructure.persistence.form;

import com.alibaba.fastjson.JSON;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.team4u.workflow.infrastructure.DbTest;

import javax.annotation.PostConstruct;

import static org.team4u.workflow.TestUtil.TEST;

public class MybatisFormIndexRepositoryTest extends DbTest {
    @Autowired
    private TestFormIndexMapper testFormIndexMapper;

    private TestFormIndeRepository repository;

    @PostConstruct
    public void repository() {
        repository = new TestFormIndeRepository(testFormIndexMapper);
    }

    @Test
    public void formOf() {
        save();

        TestFormIndex form = repository.formOf(TEST);
        System.out.println(JSON.toJSONString(form));

        Assert.assertEquals(TEST, form.getName());
    }

    private void save() {
        TestFormIndex form = new TestFormIndex();
        form.setName(TEST);
        form.setProcessInstanceId(TEST);

        repository.save(form);

        Assert.assertNotNull(form.getId());
    }

    @Override
    protected String[] ddlResourcePaths() {
        return new String[]{"sql/process_form.sql"};
    }
}