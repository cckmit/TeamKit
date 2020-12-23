package org.team4u.workflow.application;

import org.junit.Test;
import org.team4u.workflow.infrastructure.emulator.ProcessEmulatorFactory;

public class ProcessEmulatorTest {

    private final ProcessEmulator emulator = ProcessEmulatorFactory.create();

    @Test
    public void simple() {
        emulator.start("test_simple_completed_script", null, null);
    }

    @Test
    public void vms() {
        emulator.start("test_vms_completed_script", null, null);
    }
}