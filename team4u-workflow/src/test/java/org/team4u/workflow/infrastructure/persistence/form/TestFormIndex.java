package org.team4u.workflow.infrastructure.persistence.form;

import org.team4u.workflow.domain.form.FormIndex;

public class TestFormIndex extends FormIndex {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public String toString() {
        return "TestFormIndex{" +
                "name='" + name + '\'' +
                '}';
    }

    public static final class Builder {
        private String processInstanceId;
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

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public TestFormIndex build() {
            TestFormIndex testForm = new TestFormIndex();
            testForm.setProcessInstanceId(processInstanceId);
            testForm.setName(name);
            return testForm;
        }
    }
}
