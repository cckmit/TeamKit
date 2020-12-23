package org.team4u.workflow.application;

import org.junit.Test;
import org.team4u.workflow.domain.form.ProcessForm;
import org.team4u.workflow.infrastructure.emulator.ProcessEmulatorFactory;
import org.team4u.workflow.infrastructure.persistence.form.TestForm;

import static org.team4u.workflow.TestUtil.TEST;

public class ProcessEmulatorTest {

    private final ProcessEmulator emulator = ProcessEmulatorFactory.create();

    @Test
    public void simple() {
        emulator.start(
                "test_simple_completed_script",
                TestForm.Builder
                        .newBuilder()
                        .withProcessInstanceId(TEST)
                        .build(),
                null
        );
    }

    @Test
    public void vms() {
        emulator.start("test_vms_completed_script", new ProcessForm(), null);
    }
}