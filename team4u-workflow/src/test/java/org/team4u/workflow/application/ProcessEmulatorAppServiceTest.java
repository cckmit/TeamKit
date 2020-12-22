package org.team4u.workflow.application;

import org.junit.Test;
import org.team4u.workflow.infrastructure.emulator.ProcessEmulatorFactory;
import org.team4u.workflow.infrastructure.persistence.form.TestForm;

import static org.team4u.workflow.TestUtil.TEST;

public class ProcessEmulatorAppServiceTest {

    private final ProcessEmulatorAppService emulatorAppService = ProcessEmulatorFactory.create();

    @Test
    public void simulate() {
        emulatorAppService.simulate(
                "test_simple_completed_script",
                TestForm.Builder
                        .newBuilder()
                        .withFormId(TEST)
                        .build(),
                null
        );
    }
}