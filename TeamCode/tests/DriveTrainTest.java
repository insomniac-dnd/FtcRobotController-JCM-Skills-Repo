package org.firstinspires.ftc.teamcode.tests;

import org.firstinspires.ftc.teamcode.DriveTrain;
import com.qualcomm.robotcore.hardware.Gamepad;
import org.junit.Test;
import static org.junit.Assert.*;

public class DriveTrainTest {

    @Test
    public void testForwardDriveSetsAllMotorsPositive() {
        MockHardware hw = new MockHardware();
        hw.put("frontLeft", new MockHardware.MockMotor());
        hw.put("frontRight", new MockHardware.MockMotor());
        hw.put("backLeft", new MockHardware.MockMotor());
        hw.put("backRight", new MockHardware.MockMotor());

        DriveTrain drive = new DriveTrain(hw);
        Gamepad pad = new Gamepad();
        pad.left_stick_y = 1.0f; // forward

        drive.drive(pad);

        double fl = ((MockHardware.MockMotor) hw.get(com.qualcomm.robotcore.hardware.DcMotor.class, "frontLeft")).getPower();
        double fr = ((MockHardware.MockMotor) hw.get(com.qualcomm.robotcore.hardware.DcMotor.class, "frontRight")).getPower();

        assertTrue("Front left should be > 0", fl > 0);
        assertTrue("Front right should be > 0", fr > 0);
    }
}
