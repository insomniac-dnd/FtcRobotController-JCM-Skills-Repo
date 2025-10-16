package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.CRServo;

import com.qualcomm.robotcore.util.Range;

@TeleOp(name = "sqrECUBot", group = "FTCROBOTCONTROLLER-JCM-SKILLS-REPO")
public class sqrECUBot extends LinearOpMode {
    //region Hardware Declarations
    private CRServo vertical1Servo;
    private Servo pivot3Servo;
    private Servo wrist4Servo;
    private Servo claw5Servo;
    private DcMotor frontLeft;
    private DcMotor frontRight;
    private DcMotor backLeft;
    private DcMotor backRight;
    //endregion

    //region Servo Positions
    private double pivot3ServoPosition = 0.5;
    private double wrist4ServoPosition = 0.5;
    private double claw5ServoPosition = 0.2;
    //endregion

    //region Servo Variables
    private final double servoincrement = 0.05;
    private final double servomin = 0.0;
    private final double servomax = 1.0;
    private double servopower = 0.0;
    private double power1 = 1.0;
    private double power0 = 0.0;
    //endregion
    @Override
    public void runOpMode() throws InterruptedException {

        //region Hardware Map Classes

        vertical1Servo = hardwareMap.get(CRServo.class,"vertical1Servo");
        pivot3Servo = hardwareMap.get(Servo.class,"pivot3Servo");
        wrist4Servo = hardwareMap.get(Servo.class,"wrist4Servo");
        claw5Servo = hardwareMap.get(Servo.class,"claw5Servo");

        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        backLeft = hardwareMap.get(DcMotor.class, "backLeft");
        backRight = hardwareMap.get(DcMotor.class, "backRight");

        frontRight.setDirection(DcMotor.Direction.REVERSE);
        backRight.setDirection(DcMotor.Direction.REVERSE);

        //endregion

        waitForStart();

        while (opModeIsActive()) {

            claw5ServoPosition = gamepad1.right_trigger;
            claw5Servo.setPosition(claw5ServoPosition);

            //region Claw Macro Movement

            if (gamepad1.y) { // raise claw
                servopower = power1;
                vertical1Servo.setPower(servopower);
            } else if (gamepad1.x) { // lower claw
                servopower = -power1;
                vertical1Servo.setPower(-servopower);
            }   else {
                servopower = power0;
                vertical1Servo.setPower(servopower);
            }

            //endregion

            //region Claw Micro Adjustments

            if (gamepad1.dpad_down) { // pitch down
                pivot3ServoPosition -= servoincrement;
                pivot3Servo.setPosition(pivot3ServoPosition);
            } else if (gamepad1.dpad_up) { // pitch up
                pivot3ServoPosition += servoincrement;
                pivot3Servo.setPosition(pivot3ServoPosition);
            }

            if (gamepad1.dpad_left) { // rotate left
                wrist4ServoPosition -= servoincrement;
                wrist4Servo.setPosition(wrist4ServoPosition);
            } else if (gamepad1.dpad_right) { // rotate right
                wrist4ServoPosition += servoincrement;
                wrist4Servo.setPosition(wrist4ServoPosition);
            }

            //endregion

            //region Servo Range Clip

            pivot3ServoPosition = Range.clip(pivot3ServoPosition, servomin, servomax);
            wrist4ServoPosition = Range.clip(wrist4ServoPosition, servomin, servomax);
            claw5ServoPosition = Range.clip(claw5ServoPosition, 0.2, 0.7);

            //endregion

            //region Driving

            double x = (-gamepad1.left_stick_x);
            double y = (gamepad1.left_stick_y);
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

            //endregion

            //region Telemetry

            telemetry.addData("vertical1Servo:", servopower);
            telemetry.addData("pivot3Servo:", pivot3ServoPosition);
            telemetry.addData("wrist4Servo:", wrist4ServoPosition);
            telemetry.addData("claw5Servo:", claw5ServoPosition);

            telemetry.addData("Front Left Power", frontLeftPower);
            telemetry.addData("Front Right Power", frontRightPower);
            telemetry.addData("Back Left Power", backLeftPower);
            telemetry.addData("Back Right Power", backRighttPower);

            telemetry.update();

            //endregion

            sleep(20);
        }
    }
}