package org.firstinspires.ftc.teamcode;

import java.nio.channels.InterruptedByTimeoutException;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name = "ServoArmTest", group = "FTCROBOTCONTROLLER-JCM-SKILLS-REPO")
public class GamepadTest extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException{

        waitForStart()

        while(opModeIsActive()) {

            telemetry.addData("gamepad1.right_stick_x:", gamepad1.right_stick_x);
            telemetry.update();
            telemetry.addData("gamepad1.right_stick_y:", gamepad1.right_stick_y);
            telemetry.update();
            telemetry.addData("---");
            telemetry.addData("gamepad1.left_stick_x:", gamepad1.right_stick_x);
            telemetry.update();
            telemetry.addData("gamepad1.left_stick_y:", gamepad1.right_stick_y);
            telemetry.update();
            telemetry.addData("---");
            telemetry.addData("gamepad1.right_trigger:", gamepad1.right_trigger);
            telemetry.update();
            telemetry.addData("gamepad1.left_trigger:", gamepad1.left_trigger);
            telemetry.update();

            sleep(20);
        }
    }
}