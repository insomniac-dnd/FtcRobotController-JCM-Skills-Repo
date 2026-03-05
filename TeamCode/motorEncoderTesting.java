package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import com.qualcomm.robotcore.util.Range;

@TeleOp(name = "motorEncoderTesting", group = "FTCROBOTCONTROLLER-JCM-SKILLS-REPO")
public class motorEncoderTesting extends LinearOpMode {

    private DcMotor motorA;

    @Override
    public void runOpMode() throws InterruptedException {

        motorA = hardwareMap.get(DcMotor.class, "motorA");

        motorA.setMode(DcMotor.STOP_AND_RESET_ENCODER);

        waitForStart();
        while (opModeIsActive()) {

            motorA.setPower(gamepad1.right_trigger);

            telemetry.addData(motorA.getCurrentPosition());
            telemetry.update();
        }
    }
}