package org.team4u.workflow.infrastructure.persistence.form;

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