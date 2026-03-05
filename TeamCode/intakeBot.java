package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@TeleOp(name = "intakeBot", group = "Robot")
public class intakeBot extends LinearOpMode {

    private DcMotor frontLeft, frontRight, backLeft, backRight, intakeMotor;
    private double intakePower = 0.25;
    private boolean intakeActive = false;
    private Servo doorServoL, doorServoR;
    private boolean doorOpen = true;

    @Override
    public void runOpMode() throws InterruptedException {

        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        backLeft = hardwareMap.get(DcMotor.class, "backLeft");
        backRight = hardwareMap.get(DcMotor.class, "backRight");
        intakeMotor = hardwareMap.get(DcMotor.class, "intakeMotor";
        doorServoL = hardwareMap.get(DcMotor.class, "doorServoL");
        doorServoR = hardwareMap.get(DcMotor.class, "doorServoR");

        frontRight.setDirection(DcMotor.Direction.REVERSE);
        backRight.setDirection(DcMotor.Direction.REVERSE);

        waitForStart();

        while (opModeIsActive()) {

            if (gamepad1.x) {
                if (intakeActive == true) {
                    intakeActive = false;
                } else if (intakeActive == false) {
                    intakeActive = true;
                }
            }

            if (intakeActive == true) {
                intakeMotor.setPower(intakePower);
            } else {
                intakeMotor.setPower(0.0);
            }

            if (gamepad1.a) {
                if (doorOpen == true) {
                    doorOpen = false;
                } else if (doorOpen == false) {
                    doorOpen = true;
                }
            }
            if (doorOpen == true) {
                doorServoL.setPosition(1.0);
                doorServoR.setPosition(-1.0);
            } else {
                doorServoL.setPosition(0.0);
                doorServoR.setPosition(0.0);
            }

            double x = (-gamepad1.left_stick_x);
            double y = (-gamepad1.left_stick_y);
            double rx = (-gamepad1.right_stick_x);

            double frontLeftPower  = y + x + rx;
            double backLeftPower   = y - x + rx;
            double frontRightPower = y - x - rx;
            double backRightPower  = y + x - rx;

            double max = Math.max(
                    Math.max(Math.abs(frontLeftPower),
                            Math.abs(backLeftPower)),
                    Math.max(Math.abs(frontRightPower),
                            Math.abs(backRightPower)));

            if (max > 1.0) {
                frontLeftPower /= max;
                frontRightPower /= max;
                backLeftPower /= max;
                backRightPower /= max;
            }

            frontLeft.setPower(frontLeftPower);
            backLeft.setPower(backLeftPower);
            frontRight.setPower(frontRightPower);
            backRight.setPower(backRightPower);

            telemetry.addData("Front Left", leftPower);
            telemetry.addData("Back Left", leftPower);
            telemetry.addData("Front Right", rightPower);
            telemetry.addData("Back Right", rightPower);
            telemetry.addData("Intake Motor On:", intakeActive, "Power Level:", intakePower);
            telemetry.addData("Door Open:", doorOpen);
            telemetry.update();

            sleep(20);
        }
    }
}