package org.team4u.workflow.infrastructure.persistence.form;

import com.alibaba.fastjson.JSON;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.team4u.test.spring.SpringDbTest;
import org.team4u.workflow.infrastructure.BeanConfig;

import javax.annotation.PostConstruct;

import static org.team4u.workflow.TestUtil.TEST;

@ContextConfiguration(classes = BeanConfig.class)
public class MybatisFormIndexRepositoryTest extends SpringDbTest {
    @Autowired
    private TestFormIndexMapper testFormIndexMapper;

    private TestFormIndexRepository repository;

    @PostConstruct
    public void repository() {
        repository = new TestFormIndexRepository(testFormIndexMapper);
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
        return new String[]{"sql/test_form_index.sql"};
    }
}