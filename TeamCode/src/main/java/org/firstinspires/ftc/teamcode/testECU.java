package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Subsystems.clawArm;
import org.firstinspires.ftc.teamcode.Subsystems.driveBase;
@TeleOp(name="testECU", group="ECU")
public class testECU extends LinearOpMode {
    driveBase drive = new driveBase();
    clawArm arm = new clawArm();


    public void runOpMode() {
        double y = 0;
        double x = 0;
        double rx = 0;
        double divisor = 1.0;
        boolean precision = false;
        double increment = 0.01;
        double threshold = 0.3;

        drive.init();
        arm.init();

        waitForStart();

        while (opModeIsActive()) {
            // precision toggle
            if (gamepad1.a) {
                precision = !precision;
            }
            if (precision) {
                divisor = 2.5;
            } else {
                divisor = 1.0;
            }

            // driving
            y = gamepad1.left_stick_y;
            x = gamepad1.left_stick_x;
            rx = gamepad1.right_stick_x;
            drive.driveRobot(y,x,rx,divisor);

            // vertical
            if (gamepad1.x) {
                arm.verticalMove(1);
            } else if (gamepad1.y) {
                arm.verticalMove(-1);
            } else {
                arm.verticalMove(0);
            }

            // claw
            if (gamepad1.right_trigger > threshold) {
                arm.clawMove(1);
            } else if (gamepad1.left_trigger > threshold) {
                arm.clawMove(-1);
            } else {
                arm.clawMove(0);
            }

            // pivot
            if (gamepad1.dpad_up) arm.pivotMove(increment);
            if (gamepad1.dpad_down) arm.pivotMove(-increment);

            // wrist
            if (gamepad1.dpad_left) arm.wristMove(-increment);
            if (gamepad1.dpad_right) arm.wristMove(increment);

            // telemetry
            drive.telemetry();
            arm.telemetry();
            telemetry.update();
        }
    }
}
