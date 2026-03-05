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

    private double autoX = 0.0;
    private double autoY = 0.0;
    private double autoRX = 0.0;

    private double Up = 1.0;
    private double Down = 1.0;
    private double Left = 1.0;
    private double Right = 1.0;
    private double RotateLeft = 1.0;
    private double RotateRight = 1.0;

    private ElapsedTime matchTime = new ElapsedTime();
    private double matchDuration = 240.0;

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

        static void

        while (opModeIsActive()) {
            while (matchTime.seconds < matchDuration) {

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
        }
    }
}