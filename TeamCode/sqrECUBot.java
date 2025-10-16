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
//    private Servo rotate0Servo;
    private CRServo vertical1Servo;
    //    private Servo bin2Servo;
    private Servo pivot3Servo;
    private Servo wrist4Servo;
    private Servo claw5Servo;

    private DcMotor frontLeft;
    private DcMotor frontRight;
    private DcMotor backLeft;
    private DcMotor backRight;
    //endregion

    //region Servo Positions
//    private double rotate0ServoPosition = 0.5;
//    private double bin2ServoPosition = 0.5;
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
//        rotate0Servo = hardwareMap.get(Servo.class,"rotate0Servo");
        vertical1Servo = hardwareMap.get(CRServo.class,"vertical1Servo");
//        bin2Servo = hardwareMap.get(Servo.class, "bin2Servo");
        pivot3Servo = hardwareMap.get(Servo.class,"pivot3Servo");
        wrist4Servo = hardwareMap.get(Servo.class,"wrist4Servo");
        claw5Servo = hardwareMap.get(Servo.class,"claw5Servo");

        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        backLeft = hardwareMap.get(DcMotor.class, "backLeft");
        backRight = hardwareMap.get(DcMotor.class, "backRight");

        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        frontRight.setDirection(DcMotor.Direction.REVERSE);
        backLeft.setDirection(DcMotor.Direction.REVERSE);
        backRight.setDirection(DcMotor.Direction.REVERSE);
        //endregion

        waitForStart();

        while (opModeIsActive()) {

            claw5ServoPosition = gamepad1.right_trigger;
            claw5Servo.setPosition(claw5ServoPosition);

            //region Claw Macro Movement
//            if (gamepad1.left_bumper){ // claw left-right rotation
//                if (gamepad1.dpad_left) {
//                    rotate0ServoPosition -= servoincrement;
//                    rotate0Servo.setPosition(rotate0ServoPosition);
//                }
//                if (gamepad1.dpad_right) {
//                    rotate0ServoPosition += servoincrement;
//                    rotate0Servo.setPosition(rotate0ServoPosition);
//                }
//            }

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

//            if (gamepad1.a) { // bin activation
//                bin2ServoPosition = 1.0;
//                bin2Servo.setPosition(bin2ServoPosition);
//                sleep(100);
//                bin2ServoPosition = 0.5;
//                bin2Servo.setPosition(bin2ServoPosition);
//            }

            //region Servo Range Clip
//            rotate0ServoPosition = Range.clip(rotate0ServoPosition, servomin, servomax);
//            bin2ServoPosition = Range.clip(bin2ServoPosition, 0.5, 1.0);
            pivot3ServoPosition = Range.clip(pivot3ServoPosition, servomin, servomax);
            wrist4ServoPosition = Range.clip(wrist4ServoPosition, servomin, servomax);
            claw5ServoPosition = Range.clip(claw5ServoPosition, 0.2, 0.7);
            //endregion

            //region Driving
            double x = (-gamepad1.left_stick_x);
            double y = (gamepad1.left_stick_y);
            double r = (gamepad1.right_stick_x);

//            double frontLeftPower = -0.5 * x + 0.866 * y + r;
//            double frontRightPower = -0.5 * x - 0.866 * y + r;
//            double backMiddlePower = x + r;

            double max = Math.max(Math.abs(frontLeftPower),
                    Math.max(Math.abs(frontRightPower), Math.abs(backMiddlePower)));

            if (max > 1.0) {
                frontLeftPower /= max;
                frontRightPower /= max;
                backLeftPower /= max;
                backRightPower /= max;
            }

            frontLeft.setPower(frontLeftPower);
            frontRight.setPower(frontRightPower);
            backMiddle.setPower(backMiddlePower);
            //endregion

            //region Telemetry
//            telemetry.addData("rotation0Servo:", rotate0ServoPosition);
            telemetry.addData("vertical1Servo:", servopower);
//            telemetry.addData ("bin2Servo:", bin2ServoPosition);
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