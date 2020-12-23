package org.team4u.workflow.infrastructure.persistence.form;

import com.baomidou.mybatisplus.annotation.TableName;

@TableName("test_form")
public class TestFormIndexDo extends FormIndexDo {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
