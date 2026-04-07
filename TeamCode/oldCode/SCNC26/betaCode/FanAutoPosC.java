package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.rev.Rev9AxisImuOrientationOnRobot;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;

@Autonomous(name = "FanAutoPosC", group = "Autonomous")
public class FanAutoPosC extends LinearOpMode {

    private DcMotor frontLeft, backLeft, frontRight, backRight;
    private Servo pivotServo, leftClaw, rightClaw;
    private CRServo verticalServo;
    private DistanceSensor distanceSensor;
    private IMU imu;

    private static final double COUNTS_PER_MOTOR_REV = 1440;
    private static final double WHEEL_DIAMETER_MM = 100;
    private static final double COUNTS_PER_MM = (COUNTS_PER_MOTOR_REV) / (WHEEL_DIAMETER_MM * Math.PI);

    private static final double P_DRIVE_COEFF = 0.012;

    private ElapsedTime clawTime = new ElapsedTime();
    private ElapsedTime matchTime = new ElapsedTime();

    private static final double LEFT_CLAW_OPEN = 0.7, LEFT_CLAW_CLOSE = 0.3;
    private static final double RIGHT_CLAW_OPEN = 0.7, RIGHT_CLAW_CLOSE = 0.3;
    private static final double DRIVE_SPEED = 0.6;
    private static final double TURN_SPEED = 0.5;
    private static final double pivotUp = 1.0;
    private static final double pivotDown = 0.0;
    private static final double pivotStraight = 0.5;


    @Override
    public void runOpMode() {
        initializeHardware();

        telemetry.addData("Status", "Ready. IMU Initialized via Orientation.");
        telemetry.update();

        waitForStart();

        if (opModeIsActive()) {
            matchTime.reset();
            // // Sequence: speed, leftCm, rightCm, targetHeading
            sleep(30000);
            encoderDrive(DRIVE_SPEED, 90, 90, 0); //S1
            encoderDrive(TURN_SPEED, 26, -26, -90); //T1
            breakmotor();
            encoderDrive(DRIVE_SPEED, 115, 115, -90); //S2
            encoderDrive(TURN_SPEED, 24, -24, 180); //T2
            breakmotor();
            encoderDrive(DRIVE_SPEED, 65, 65, 180); //S3
            encoderDrive(TURN_SPEED, 24, -24, 90); //T2
            encoderDrive(DRIVE_SPEED, 40, 40, 90); //S3

            

            
            // encoderDrive(TURN_SPEED, 24, -24, 90); //T3
            // breakmotor();
            // encoderDrive(DRIVE_SPEED, 30, 30, 90);
            

        }
    }

    public void encoderDrive(double speed, double leftCm, double rightCm, double targetAngle) {
        int newLeftTarget = frontLeft.getCurrentPosition() + (int) (leftCm * COUNTS_PER_MM * 10);
        int newRightTarget = frontRight.getCurrentPosition() + (int) (rightCm * COUNTS_PER_MM * 10);

        frontLeft.setTargetPosition(newLeftTarget);
        backLeft.setTargetPosition(newLeftTarget);
        frontRight.setTargetPosition(newRightTarget);
        backRight.setTargetPosition(newRightTarget);

        setMode(DcMotor.RunMode.RUN_TO_POSITION);

        while (opModeIsActive() && (frontLeft.isBusy() && frontRight.isBusy())) {
            YawPitchRollAngles orientation = imu.getRobotYawPitchRollAngles();
            double currentAngle = orientation.getYaw(AngleUnit.DEGREES);

            double error = targetAngle - currentAngle;
            double correction = error * P_DRIVE_COEFF;

            double leftPower = speed - correction;
            double rightPower = speed + correction;

            double max = Math.max(Math.abs(leftPower), Math.abs(rightPower));
            if (max > 1.0) {
                leftPower /= max;
                rightPower /= max;
            }

            frontLeft.setPower(leftPower);
            backLeft.setPower(leftPower);
            frontRight.setPower(rightPower);
            backRight.setPower(rightPower);

            telemetry.addData("Actual Heading", "%.2f", currentAngle);
            telemetry.update();
        }

        stopMotors();
        setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
    
    private void breakmotor(){
        frontLeft.setPower(0); backLeft.setPower(0);
        frontRight.setPower(0); backRight.setPower(0);
    }

    private void initializeHardware() {
        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        backLeft = hardwareMap.get(DcMotor.class, "backLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        backRight = hardwareMap.get(DcMotor.class, "backRight");

        imu = hardwareMap.get(IMU.class, "imu");

        // Define orientation using an Orientation object to satisfy the constructor
        Orientation hubRotation = new Orientation(AxesReference.INTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES, 0, 0, 0, 0);
        Rev9AxisImuOrientationOnRobot orientationOnRobot = new Rev9AxisImuOrientationOnRobot(hubRotation);

        imu.initialize(new IMU.Parameters(orientationOnRobot));
        imu.resetYaw();

        pivotServo = hardwareMap.get(Servo.class, "pivotServo");
        leftClaw = hardwareMap.get(Servo.class, "leftClaw");
        rightClaw = hardwareMap.get(Servo.class, "rightClaw");
        verticalServo = hardwareMap.get(CRServo.class, "verticalServo");
        distanceSensor = hardwareMap.get(DistanceSensor.class, "distanceSensor");

        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        backLeft.setDirection(DcMotor.Direction.REVERSE);
        rightClaw.setDirection(Servo.Direction.REVERSE);
        pivotServo.setDirection(Servo.Direction.REVERSE);

        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    private void setMode(DcMotor.RunMode mode) {
        frontLeft.setMode(mode); backLeft.setMode(mode);
        frontRight.setMode(mode); backRight.setMode(mode);
    }

    private void stopMotors() {
        frontLeft.setPower(0); backLeft.setPower(0);
        frontRight.setPower(0); backRight.setPower(0);
    }

    private void closeClaws() {
        leftClaw.setPosition(LEFT_CLAW_CLOSE);
        rightClaw.setPosition(RIGHT_CLAW_CLOSE);
    }

    private void openClaws() {
        leftClaw.setPosition(LEFT_CLAW_OPEN);
        rightClaw.setPosition(RIGHT_CLAW_OPEN);
    }

    private boolean isObjectDetected() {
        double distance = distanceSensor.getDistance(DistanceUnit.CM);
        return distance < 3.0;
    }

    private void pivotServoUp() {
        pivotServo.setPosition(pivotUp);
    }
    private void pivotServoDown() {
        pivotServo.setPosition(pivotDown);
    }
    private void pivotServoStraight() {
        pivotServo.setPosition(pivotStraight);
    }

    private void verticalServoDown(double duration) {
        clawTime.reset();
        while(opModeIsActive() && clawTime.seconds() < duration) {
            verticalServo.setPower(-1.0);
        }
        verticalServo.setPower(0);
    }

    private void verticalServoUp(double duration) {
        clawTime.reset();
        while(opModeIsActive() && clawTime.seconds() < duration) {
            verticalServo.setPower(1.0);
        }
        verticalServo.setPower(0);
    }
} 
