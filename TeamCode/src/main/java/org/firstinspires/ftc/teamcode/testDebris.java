package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Subsystems.clawArm;
import org.firstinspires.ftc.teamcode.Subsystems.driveBase;
@TeleOp(name="testDebris", group="Debris")
public class testDebris extends LinearOpMode {
    driveBase drive = new driveBase();
    clawArm arm = new clawArm();


    public void runOpMode() {
        double y = 0;
        double x = 0;
        double rx = 0;
        double divisor = 1.0;

        drive.init();

        waitForStart();

        while (opModeIsActive()) {
            // driving
            y = gamepad1.left_stick_y;
            x = gamepad1.left_stick_x;
            rx = gamepad1.right_stick_x;
            drive.driveRobot(y,x,rx,divisor);

            // telemetry
            drive.telemetry();
            arm.telemetry();
            telemetry.update();

            sleep(50);
        }

    }
}
