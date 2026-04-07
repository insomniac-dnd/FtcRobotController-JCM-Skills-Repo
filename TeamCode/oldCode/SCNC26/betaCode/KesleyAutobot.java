package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import com.qualcomm.robotcore.hardware.DistanceSensor;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@Autonomous(name = "KesleyAutobot", group = "Robot")
public class KesleyAutobot extends LinearOpMode {

    // DRIVE
    private DcMotor frontLeft, frontRight, backLeft, backRight;

    // CLAW
    private CRServo vertical1Servo;
    private Servo pivot2Servo, wrist3Servo, claw4Servo, claw5Servo;

    // SENSORS
    private NormalizedColorSensor colorSensor;
    private DistanceSensor frontDistance;

    // CLAW CONSTANTS
    private static final double PIVOT_UP = 0.7;
    private static final double PIVOT_DOWN = 0.3;
    private static final double WRIST_UP = 0.65;
    private static final double WRIST_DOWN = 0.35;
    private static final double CLAW_OPEN = 0.2;
    private static final double CLAW_CLOSED = 0.75;
    private static final double LIFT_UP = 1.0;
    private static final double LIFT_DOWN = -1.0;

    // SCAN CONSTANTS
    private static final double FAN_DISTANCE_CM = 8.0;
    private static final int CONFIRM_COUNT_REQUIRED = 5;
    private static final long MAX_OBJECT_TIME_MS = 700; // wall reject

    private int confirmCount = 0;
    private long objectStartTime = 0;

    // FSM
    private enum State {
        FOLLOW_LINE,
        SCAN_FOR_FAN,
        GRAB_FAN,
        GO_TO_ECU,
        PLACE_FAN,
        DONE
    }

    private State state = State.FOLLOW_LINE;

    @Override
    public void runOpMode() {

        frontLeft  = hardwareMap.get(DcMotor.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        backLeft   = hardwareMap.get(DcMotor.class, "backLeft");
        backRight  = hardwareMap.get(DcMotor.class, "backRight");

        frontRight.setDirection(DcMotor.Direction.REVERSE);
        backRight.setDirection(DcMotor.Direction.REVERSE);

        colorSensor   = hardwareMap.get(NormalizedColorSensor.class, "colorSensor");
        frontDistance = hardwareMap.get(DistanceSensor.class, "frontDistance");

        vertical1Servo = hardwareMap.get(CRServo.class, "vertical1Servo");
        pivot2Servo    = hardwareMap.get(Servo.class, "pivot2Servo");
        wrist3Servo    = hardwareMap.get(Servo.class, "wrist3Servo");
        claw4Servo     = hardwareMap.get(Servo.class, "claw4Servo");
        claw5Servo     = hardwareMap.get(Servo.class, "claw5Servo");
        claw5Servo.setDirection(Servo.Direction.REVERSE);

        resetClaw();

        telemetry.addLine("Wall-Safe SmartScan READY");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {

            switch (state) {

                case FOLLOW_LINE:
                    followLine();
                    if (frontDistance.getDistance(DistanceUnit.CM) < 30) {
                        stopDrive();
                        sleep(300);
                        state = State.SCAN_FOR_FAN;
                    }
                    break;

                case SCAN_FOR_FAN:
                    scanForFanWallSafe();
                    break;

                case GRAB_FAN:
                    grabFan();
                    state = State.GO_TO_ECU;
                    break;

                case GO_TO_ECU:
                    driveForwardTimed(1200);
                    state = State.PLACE_FAN;
                    break;

                case PLACE_FAN:
                    placeFan();
                    state = State.DONE;
                    break;

                case DONE:
                    stopDrive();
                    telemetry.addLine("✅ Autonomous Complete");
                    telemetry.update();
                    sleep(999999);
                    break;
            }

            telemetry.addData("State", state);
            telemetry.addData("Distance", frontDistance.getDistance(DistanceUnit.CM));
            telemetry.addData("Confirm", confirmCount);
            telemetry.update();
        }
    }

    // ---------------- WALL-SAFE FAN SCAN ----------------
    private void scanForFanWallSafe() {
        xDrive(0, 0.2, 0); // slow strafe

        double d = frontDistance.getDistance(DistanceUnit.CM);

        if (d < FAN_DISTANCE_CM) {

            if (confirmCount == 0) {
                objectStartTime = System.currentTimeMillis();
            }

            confirmCount++;

            // wall rejection (too wide)
            if (System.currentTimeMillis() - objectStartTime > MAX_OBJECT_TIME_MS) {
                confirmCount = 0;
                return;
            }

            if (confirmCount >= CONFIRM_COUNT_REQUIRED) {
                stopDrive();
                sleep(200);
                state = State.GRAB_FAN;
            }

        } else {
            confirmCount = 0;
        }
    }

    // ---------------- LINE FOLLOW ----------------
    private void followLine() {
        NormalizedRGBA c = colorSensor.getNormalizedColors();
        float brightness = (c.red + c.green + c.blue) / 3f;
        if (brightness < 0.08) xDrive(0.25, 0, 0);
        else xDrive(0, -0.25, 0);
    }

    // ---------------- CLAW ----------------
    private void grabFan() {
        pivot2Servo.setPosition(PIVOT_DOWN);
        wrist3Servo.setPosition(WRIST_DOWN);
        sleep(300);

        claw4Servo.setPosition(CLAW_CLOSED);
        claw5Servo.setPosition(CLAW_CLOSED);
        sleep(400);

        vertical1Servo.setPower(LIFT_UP);
        sleep(600);
        vertical1Servo.setPower(0);
    }

    private void placeFan() {
        vertical1Servo.setPower(LIFT_DOWN);
        sleep(400);
        vertical1Servo.setPower(0);

        claw4Servo.setPosition(CLAW_OPEN);
        claw5Servo.setPosition(CLAW_OPEN);
        sleep(300);

        pivot2Servo.setPosition(PIVOT_UP);
        wrist3Servo.setPosition(WRIST_UP);
    }

    private void resetClaw() {
        pivot2Servo.setPosition(PIVOT_UP);
        wrist3Servo.setPosition(WRIST_UP);
        claw4Servo.setPosition(CLAW_OPEN);
        claw5Servo.setPosition(CLAW_OPEN);
        vertical1Servo.setPower(0);
    }

    // ---------------- DRIVE ----------------
    private void driveForwardTimed(long ms) {
        xDrive(0.3, 0, 0);
        sleep(ms);
        stopDrive();
    }

    private void xDrive(double f, double s, double r) {
        frontLeft.setPower(f + s + r);
        frontRight.setPower(f - s - r);
        backLeft.setPower(f - s + r);
        backRight.setPower(f + s - r);
    }

    private void stopDrive() {
        xDrive(0, 0, 0);
    }
}