package org.team4u.workflow.application;

import org.junit.Test;

public class ProcessEmulatorTest {

    private static final ProcessEmulator emulator = ProcessEmulator.create();

    @Test
    public void simple() {
        emulator.start("test_simple_completed_script");
    }

    @Test
    public void vms() {
        emulator.start("test_vms_completed_script");
    }
}