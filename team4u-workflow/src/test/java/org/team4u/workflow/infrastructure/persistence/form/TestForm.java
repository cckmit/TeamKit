package org.team4u.workflow.infrastructure.persistence.form;

import org.team4u.workflow.domain.form.ProcessForm;
import org.team4u.workflow.domain.form.ProcessFormItem;

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
        private String formId;
        private String processInstanceId;
        private ProcessFormItem formItem;
        private String name;

        private Builder() {
        }

        public static Builder newBuilder() {
            return new Builder();
        }

        public Builder withFormId(String formId) {
            this.formId = formId;
            return this;
        }

        public Builder withProcessInstanceId(String processInstanceId) {
            this.processInstanceId = processInstanceId;
            return this;
        }

        public Builder withFormItem(ProcessFormItem formItem) {
            this.formItem = formItem;
            return this;
        }

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public TestForm build() {
            TestForm testForm = new TestForm();
            testForm.setFormId(formId);
            testForm.setProcessInstanceId(processInstanceId);
            testForm.setFormItem(formItem);
            testForm.setName(name);
            return testForm;
        }
    }
}
