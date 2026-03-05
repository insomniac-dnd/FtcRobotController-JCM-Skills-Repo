package org.firstinspires.ftc.teamcode.tests;

import org.firstinspires.ftc.teamcode.ClawLift;
import com.qualcomm.robotcore.hardware.Gamepad;
import org.junit.Test;
import static org.junit.Assert.*;

public class ClawLiftTest {

    @Test
    public void testYButtonRaisesLift() {
        MockHardware hw = new MockHardware();
        hw.put("vertical1Servo", new MockHardware.MockCRServo());
        ClawLift lift = new ClawLift(hw);

        Gamepad pad = new Gamepad();
        pad.y = true;

        lift.update(pad);
        double power = ((MockHardware.MockCRServo) hw.get(com.qualcomm.robotcore.hardware.CRServo.class, "vertical1Servo")).getPower();

        assertEquals("Servo should power upward", 1.0, power, 0.01);
    }

    @Test
    public void testNoInputStopsLift() {
        MockHardware hw = new MockHardware();
        hw.put("vertical1Servo", new MockHardware.MockCRServo());
        ClawLift lift = new ClawLift(hw);

        Gamepad pad = new Gamepad();
        lift.update(pad);

        double power = ((MockHardware.MockCRServo) hw.get(com.qualcomm.robotcore.hardware.CRServo.class, "vertical1Servo")).getPower();

        assertEquals("Servo should be stopped", 0.0, power, 0.01);
    }
}
