package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.rev.Rev9AxisImuOrientationOnRobot;
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

@Autonomous(name = "DirectAuto", group = "Autonomous")
public class DirectAuto extends LinearOpMode {

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
    private static final double pivotUp = 0.1;
    private static final double pivotDown = 0.0;
    private static final double pivotStraight = 0.5;
    private static final double DRIVE_SPEED = 0.6;
    private static final double TURN_SPEED = 0.5;
    private static final double MIN_TURN_SPEED = 0.15;

    @Override
    public void runOpMode() {
        initializeHardware();

        telemetry.addData("Status", "Ready. IMU Initialized via Orientation.");
        telemetry.update();

        waitForStart();

        if (opModeIsActive()) {
            imu.resetYaw();
            matchTime.reset();
            while (matchTime.seconds() < 250)
                distanceDrive(93, 0);
                imuTurn(-90);
                breakmotor(1000);
                distanceDrive(120, -90);
                imuTurn(180);
                breakmotor(1000);
                distanceDrive(70, 180);
                imuTurn(90);
                distanceDrive(30, 90);
            }
        }
    }

    public void distanceDrive(double stopDistanceCm, double targetAngle) {

        setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        while (opModeIsActive() && distanceSensor.getDistance(DistanceUnit.CM) > stopDistanceCm) {

            YawPitchRollAngles orientation = imu.getRobotYawPitchRollAngles();
            double currentAngle = orientation.getYaw(AngleUnit.DEGREES);

            double error = targetAngle - currentAngle;
            double correction = error * P_DRIVE_COEFF;

            double leftPower = DRIVE_SPEED - correction;
            double rightPower = DRIVE_SPEED + correction;

            double max = Math.max(Math.abs(leftPower), Math.abs(rightPower));
            if (max > 1.0) {
                leftPower /= max;
                rightPower /= max;
            }

            frontLeft.setPower(leftPower);
            backLeft.setPower(leftPower);
            frontRight.setPower(rightPower);
            backRight.setPower(rightPower);

            telemetry.addData("Status", "Driving to Distance");
            telemetry.addData("Current Distance", "%.2f cm", distanceSensor.getDistance(DistanceUnit.CM));
            telemetry.addData("Target Angle", targetAngle);
            telemetry.addData("Current Angle", "%.2f", currentAngle);
            telemetry.update();
        }

        stopMotors();
        telemetry.addData("Status", "Distance Reached");
        telemetry.update();
    }

    public void imuTurn(double targetAngle) {
        setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        double error = getError(targetAngle);

        while (opModeIsActive() && Math.abs(error) > 1.0) {

            double steer = error * P_DRIVE_COEFF;

            double motorPower = Math.max(Math.abs(steer), MIN_TURN_SPEED);
            motorPower = Math.min(motorPower, TURN_SPEED);

            if (error < 0) {
                motorPower *= -1;
            }

            frontLeft.setPower(-motorPower);
            backLeft.setPower(-motorPower);
            frontRight.setPower(motorPower);
            backRight.setPower(motorPower);

            error = getError(targetAngle);

            telemetry.addData("Target", targetAngle);
            telemetry.addData("Current", "%.2f", imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES));
            telemetry.addData("Error", "%.2f", error);
            telemetry.update();
        }

        stopMotors();
        sleep(100);
    }

    public double getError(double targetAngle) {
        double robotError = targetAngle - imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES);

        while (robotError > 180)  robotError -= 360;
        while (robotError <= -180) robotError += 360;
        return robotError;
    }

    private void breakmotor(double stopTime){
        frontLeft.setPower(0); backLeft.setPower(0);
        frontRight.setPower(0); backRight.setPower(0);
        sleep(stopTime);
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

    private void verticalServoUp(double duration) {
        clawTime.reset();
        while(opModeIsActive() && clawTime.seconds() < duration) {
            verticalServo.setPower(1.0);
        }
        verticalServo.setPower(0);
    }
}