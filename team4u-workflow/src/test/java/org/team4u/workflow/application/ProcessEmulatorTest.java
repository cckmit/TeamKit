package org.team4u.workflow.application;

import org.junit.Test;
import org.team4u.workflow.infrastructure.emulator.ProcessEmulatorFactory;
import org.team4u.workflow.infrastructure.persistence.form.TestForm;

import static org.team4u.workflow.TestUtil.TEST;

public class ProcessEmulatorTest {

    private final ProcessEmulator emulator = ProcessEmulatorFactory.create();

    @Test
    public void simulate() {
        emulator.simulate(
                "test_simple_completed_script",
                TestForm.Builder
                        .newBuilder()
                        .withFormId(TEST)
                        .build(),
                null
        );
    }
}