package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name = "AutobotClawTest", group = "Autonomous")

public class AutobotClawTest extends LinearOpMode {

    private DcMotor frontLeft, backLeft, frontRight, backRight;

    private Servo pivotServo, leftClaw, rightClaw;
    private CRServo verticalServo;
    private DistanceSensor distanceSensor;

    private static final double COUNTS_PER_MOTOR_REV = 1440;
    private static final double DRIVE_GEAR_REDUCTION = 1.0;
    private static final double WHEEL_DIAMETER_MM = 100;
    private static final double COUNTS_PER_MM = (COUNTS_PER_MOTOR_REV) / (WHEEL_DIAMETER_MM * Math.PI);

    private ElapsedTime clawTime = new ElapsedTime();

    // Claw positions

    private static final double LEFT_CLAW_OPEN = 0.7;
    private static final double LEFT_CLAW_CLOSE = 0.3;

    private static final double RIGHT_CLAW_OPEN = 0.7;
    private static final double RIGHT_CLAW_CLOSE = 0.3;

    private static final double pivotUp = 0.1;
    private static final double pivotDown = 0.0;
    private static final double pivotStraight = 0.5;

    private static final double DRIVE_SPEED = 0.6;
    private static final double TURN_SPEED = 0.5;


    @Override

    public void runOpMode() {
        initializeHardware();
        waitForStart();

        if (opModeIsActive()) {

//            while (opModeIsActive()) {
//                if (isObjectDetected() == true) {
//                    closeClaws();
//                }
//                telemetry.addData("distance", distanceSensor.getDistance(DistanceUnit.CM));
//                telemetry.update();
//            }

//            pivotServoUp();

            verticalServoUp(2);
            sleep(300000);
        }
    }

    public void encoderDrive(double speed, double leftCm, double rightCm) {

        int newLeftTarget = frontLeft.getCurrentPosition() + (int) (leftCm * COUNTS_PER_MM * 10);

        int newRightTarget = frontRight.getCurrentPosition() + (int) (rightCm * COUNTS_PER_MM * 10);

        frontLeft.setTargetPosition(newLeftTarget);
        backLeft.setTargetPosition(newLeftTarget);
        frontRight.setTargetPosition(newRightTarget);
        backRight.setTargetPosition(newRightTarget);


        setMode(DcMotor.RunMode.RUN_TO_POSITION);

        frontLeft.setPower(speed);
        backLeft.setPower(speed);
        frontRight.setPower(speed);
        backRight.setPower(speed);


        while (opModeIsActive() && (frontLeft.isBusy() && frontRight.isBusy())) {

            telemetry.addData("Status", "Driving to Position");
            telemetry.update();

        }

        stopMotors();
        setMode(DcMotor.RunMode.RUN_USING_ENCODER);

    }


    private void initializeHardware() {

        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        backLeft = hardwareMap.get(DcMotor.class, "backLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        backRight = hardwareMap.get(DcMotor.class, "backRight");
        pivotServo = hardwareMap.get(Servo.class, "pivotServo");
        leftClaw = hardwareMap.get(Servo.class, "leftClaw");
        rightClaw = hardwareMap.get(Servo.class, "rightClaw");
        verticalServo = hardwareMap.get(CRServo.class, "verticalServo");
        distanceSensor = hardwareMap.get(DistanceSensor.class, "distanceSensor");

        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        backLeft.setDirection(DcMotor.Direction.REVERSE);
        rightClaw.setDirection(Servo.Direction.REVERSE);
        setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        setMode(DcMotor.RunMode.RUN_USING_ENCODER);

    }

    private void setMode(DcMotor.RunMode mode) {
        frontLeft.setMode(mode);
        backLeft.setMode(mode);
        frontRight.setMode(mode);
        backRight.setMode(mode);
    }

    private void stopMotors() {
        frontLeft.setPower(0);
        backLeft.setPower(0);
        frontRight.setPower(0);
        backRight.setPower(0);
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