package org.team4u.workflow.infrastructure.persistence.form;

public class TestFormIndexRepository extends MybatisFormIndexRepository<TestFormIndex, TestFormIndexDo> {

    public TestFormIndexRepository(TestFormIndexMapper testFormIndexMapper) {
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