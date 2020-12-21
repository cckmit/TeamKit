package org.team4u.workflow.infrastructure.persistence.form;

public class TestFormRepository extends MybatisProcessFormRepository<TestForm, TestFormDo> {

    public TestFormRepository(TestFormMapper testFormMapper, ProcessFormItemMapper formItemMapper) {
        super(testFormMapper, formItemMapper);
    }

    @Override
    protected TestForm toProcessForm(TestFormDo processFormDo) {
        return new TestForm();
    }

    @Override
    protected TestFormDo toProcessFormDo(TestForm form) {
        return new TestFormDo();
    }
}