package org.firstinspires.ftc.teamcode.tests;

import org.firstinspires.ftc.teamcode.ClawAdjuster;
import com.qualcomm.robotcore.hardware.Gamepad;
import org.junit.Test;
import static org.junit.Assert.*;

public class ClawTest {

    @Test
    public void testClawServoRespondsToTrigger() {
        MockHardware hw = new MockHardware();
        hw.put("pivot3Servo", new MockHardware.MockServo());
        hw.put("wrist4Servo", new MockHardware.MockServo());
        hw.put("claw5Servo", new MockHardware.MockServo());

        Claw adjuster = new Claw(hw);
        Gamepad pad = new Gamepad();

        pad.right_trigger = 0.5f; // halfway open
        adjuster.update(pad);

        double clawPos = ((MockHardware.MockServo) hw.get(com.qualcomm.robotcore.hardware.Servo.class, "claw5Servo")).getPosition();

        assertTrue("Claw should move to about 0.325", clawPos > 0.3 && clawPos < 0.35);
    }

    @Test
    public void testPivotMovesUpAndDown() {
        MockHardware hw = new MockHardware();
        hw.put("pivot3Servo", new MockHardware.MockServo());
        hw.put("wrist4Servo", new MockHardware.MockServo());
        hw.put("claw5Servo", new MockHardware.MockServo());

        ClawAdjuster adjuster = new ClawAdjuster(hw);
        Gamepad pad = new Gamepad();

        pad.dpad_down = true; // tilt down
        adjuster.update(pad);

        double pos1 = ((MockHardware.MockServo) hw.get(com.qualcomm.robotcore.hardware.Servo.class, "pivot3Servo")).getPosition();
        assertTrue(pos1 > 0.5);
    }
}
