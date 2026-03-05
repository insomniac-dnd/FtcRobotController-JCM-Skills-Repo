package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.CRServo;

import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.util.ElapsedTime

@Autonomous(name = "autoBot2", group = "FTCROBOTCONTROLLER-JCM-SKILLS-REPO")
public  class autoBot2 extends LinearOpMode {
    //region Hardware Declarations
    private CRServo vertical1Servo;
    private Servo pivot2Servo;
    private Servo claw4Servo;
    private Servo claw5Servo;
    private DcMotor frontLeft;
    private DcMotor frontRight;
    private DcMotor backLeft;
    private DcMotor backRight;
    //endregion

    //region Servo Positions
    private double pivot2ServoPosition = 0.5;
    private double clawServosOpen = 0.2;
    private double clawServosClosed = 0.65;
    //endregion

    //region Servo Variables
    private final double servoincrement = 0.05;
    private final double servomin = 0.0;
    private final double servomax = 1.0;
    private double servopower = 0.0;
    private double power1 = 1.0;
    private double power0 = 0.0;
    //endregion

    private double autoX = 0.0;
    private double autoY = 0.0;
    private double autoRX = 0.0;

    private ElapsedTime matchTime = new ElapsedTime();
    private double matchDuration = 240.0;
    private ElapsedTime moveTime = new ElapsedTime();

    @Override
    public void runOpMode() throws InterruptedException {
        //region Hardware Map Classes

        vertical1Servo = hardwareMap.get(CRServo.class,"vertical1Servo");
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

        claw5Servo.setDirection(Servo.Direction.REVERSE);

        //endregion

        public void driveForward {

            autoY = 1.0

            double frontLeftPower  = autoY + autoX + autoRX;
            double backLeftPower   = autoY - autoX + autoRX;
            double frontRightPower = autoY - autoX - autoRX;
            double backRightPower  = autoY + autoX - autoRX;

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

        }

        public void driveBackwards {

            autoY = -1.0

            double frontLeftPower  = autoY + autoX + autoRX;
            double backLeftPower   = autoY - autoX + autoRX;
            double frontRightPower = autoY - autoX - autoRX;
            double backRightPower  = autoY + autoX - autoRX;

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

        }

        public void strafeRight {

            autoX = 1.0

            double frontLeftPower  = autoY + autoX + autoRX;
            double backLeftPower   = autoY - autoX + autoRX;
            double frontRightPower = autoY - autoX - autoRX;
            double backRightPower  = autoY + autoX - autoRX;

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

        }

        public void strafeLeft {

            autoX = -1.0

            double frontLeftPower  = autoY + autoX + autoRX;
            double backLeftPower   = autoY - autoX + autoRX;
            double frontRightPower = autoY - autoX - autoRX;
            double backRightPower  = autoY + autoX - autoRX;

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

        }

        public void rotateCW {

            autoX = 1.0

            double frontLeftPower  = autoY + autoX + autoRX;
            double backLeftPower   = autoY - autoX + autoRX;
            double frontRightPower = autoY - autoX - autoRX;
            double backRightPower  = autoY + autoX - autoRX;

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

        }

        public void rotateCCW {

            autoX = -1.0

            double frontLeftPower  = autoY + autoX + autoRX;
            double backLeftPower   = autoY - autoX + autoRX;
            double frontRightPower = autoY - autoX - autoRX;
            double backRightPower  = autoY + autoX - autoRX;

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

        }

        public void clawUp {
            servopower = power1;
            vertical1Servo.setPower(servopower);
        }

        public void clawDown {
            servopower = -power1;
            vertical1Servo.setPower(servopower);
        }

        public void pivotUp {
            pivot2ServoPosition += servoincrement;
            pivot2Servo.setPosition(pivot2ServoPosition);
        }

        public void pivotDown {
            pivot2ServoPosition -= servoincrement;
            pivot2Servo.setPosition(pivot2ServoPosition);
        }

        public void clawOpen {
            claw4Servo.setPosition(clawServosOpen);
            claw5Servo.setPosition(clawServosOpen);
        }

        public void clawClosed {
            claw4Servo.setPosition(clawServosClosed);
            claw5Servo.setPosition(clawServosClosed);
        }

        waitForStart();

        while (opModeIsActive()) {
            while (matchTime.seconds < matchDuration) {
                runTime.reset();
                while (runTime.seconds < 5) {
                    driveForward;
                }
            }
        }
    }
}