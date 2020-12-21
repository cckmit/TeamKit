package org.team4u.workflow.infrastructure.persistence.form;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.team4u.workflow.domain.form.ProcessForm;

public class TestProcessFormRepository extends MybatisProcessFormRepository<TestForm, TestFormDo> {

    private final TestFormMapper testFormMapper;

    public TestProcessFormRepository(TestFormMapper testFormMapper, ProcessFormItemMapper formItemMapper) {
        super(formItemMapper);
        this.testFormMapper = testFormMapper;
    }

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
}