package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.CRServo;

import com.qualcomm.robotcore.util.Range;

@TeleOp(name = "dualServoClawBot", group = "FTCROBOTCONTROLLER-JCM-SKILLS-REPO")
public class dualServoClawBot extends LinearOpMode {
    //region Hardware Declarations
    private CRServo vertical0Servo;
    private Servo bin1Servo;
    private Servo pivot2Servo;
    private Servo wrist3Servo;
    private Servo claw4Servo;
    private Servo claw5Servo;

    private DcMotor frontLeft;
    private DcMotor frontRight;
    private DcMotor backLeft;
    private DcMotor backRight;
    //endregion

    //region Servo Positions
    private double bin1ServoPosition = 0.0;
    private double pivot2ServoPosition = 0.5;
    private double wrist3ServoPosition = 0.5;
    private double clawServoPosition = 0.2;

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

        vertical0Servo = hardwareMap.get(CRServo.class,"vertical0Servo");
        bin1Servo = hardwareMap.get(Servo.class,"bin1Servo");
        pivot2Servo = hardwareMap.get(Servo.class,"pivot2Servo");
        wrist3Servo = hardwareMap.get(Servo.class,"wrist3Servo");
        claw4Servo = hardwareMap.get(Servo.class,"claw4Servo");
        claw5Servo = hardwareMap.get(Servo.class,"claw5Servo");


        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        backLeft = hardwareMap.get(DcMotor.class, "backLeft");
        backRight = hardwareMap.get(DcMotor.class, "backRight");

        frontRight.setDirection(DcMotor.Direction.REVERSE);
        backRight.setDirection(DcMotor.Direction.REVERSE);

        pivot2Servo.setDirection(Servo.Direction.REVERSE);
        claw5Servo.setDirection(Servo.Direction.REVERSE);

        //endregion

        waitForStart();

        while (opModeIsActive()) {

            threshold = 0.75;

            if (gamepad1.right_trigger() > threshold) {
                claw4Servo.setPosition(0.5);
                claw5Servo.setPosition(0.5);
            } else {
                claw4Servo.setPosition(0.15);
                claw5Servo.setPosition(0.15);
            }

            //region Claw Macro Movement

            if (gamepad1.y) { // raise claw
                servopower = power1;
                vertical0Servo.setPower(servopower);
            } else if (gamepad1.x) { // lower claw
                servopower = -power1;
                vertical0Servo.setPower(servopower);
            }   else {
                servopower = power0;
                vertical0Servo.setPower(servopower);
            }

            //endregion

            //region Claw Micro Adjustments

            if (gamepad1.dpad_down) { // pitch down
                pivot2ServoPosition -= servoincrement;
                pivot2Servo.setPosition(pivot2ServoPosition);
            } else if (gamepad1.dpad_up) { // pitch up
                pivot2ServoPosition += servoincrement;
                pivot2Servo.setPosition(pivot2ServoPosition);
            }

            if (gamepad1.dpad_left) { // rotate left
                wrist3ServoPosition -= servoincrement;
                wrist3Servo.setPosition(wrist3ServoPosition);
            } else if (gamepad1.dpad_right) { // rotate right
                wrist3ServoPosition += servoincrement;
                wrist3Servo.setPosition(wrist3ServoPosition);
            }

            if (gamepad1.a) {
                bin1ServoPosition = 0.5;
                bin1Servo.setPosition(bin1ServoPosition);
                sleep(400);
                bin1ServoPosition = 0.0;
                bin1Servo.setPosition(bin1ServoPosition);
            }

            //endregion

            //region Servo Range Clip

            bin1ServoPosition = Range.clip(bin1ServoPosition, servomin, servomax);
            pivot2ServoPosition = Range.clip(pivot2ServoPosition, servomin, servomax);
            wrist3ServoPosition = Range.clip(wrist3ServoPosition, servomin, servomax);
            clawServoPosition = Range.clip(clawServoPosition, 0.1, 0.5);

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

            telemetry.addData("vertical0Servo:", servopower);
            telemetry.addData("bin1Servo:", bin1ServoPosition);
            telemetry.addData("pivot2Servo:", pivot2ServoPosition);
            telemetry.addData("wrist3Servo:", wrist3ServoPosition);
            telemetry.addData("clawServos:", clawServoPosition);

            telemetry.addData("Front Left Power", frontLeftPower);
            telemetry.addData("Front Right Power", frontRightPower);
            telemetry.addData("Back Left Power", backLeftPower);
            telemetry.addData("Back Right Power", backRightPower);

            telemetry.update();

            //endregion

            sleep(20);
        }
    }
}