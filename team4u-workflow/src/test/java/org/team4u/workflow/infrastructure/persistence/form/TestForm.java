package org.team4u.workflow.infrastructure.persistence.form;

import org.team4u.workflow.domain.form.ProcessForm;
import org.team4u.workflow.domain.instance.ProcessInstanceDetail;

public class TestForm extends ProcessForm {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public String toString() {
        return "TestForm{" +
                "name='" + name + '\'' +
                '}';
    }

    public static final class Builder {
        private String processInstanceId;
        private ProcessInstanceDetail formItem;
        private String name;

        private Builder() {
        }

        public static Builder newBuilder() {
            return new Builder();
        }

        public Builder withProcessInstanceId(String processInstanceId) {
            this.processInstanceId = processInstanceId;
            return this;
        }

        public Builder withFormItem(ProcessInstanceDetail formItem) {
            this.formItem = formItem;
            return this;
        }

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public TestForm build() {
            TestForm testForm = new TestForm();
            testForm.setProcessInstanceId(processInstanceId);
            testForm.setName(name);
            return testForm;
        }
    }
}
