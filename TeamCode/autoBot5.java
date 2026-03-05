package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.CRServo;

@Autonomous(name = "autoBot5 X-Omni", group = "Robot")
public class autoBot5 extends LinearOpMode {

    private DcMotor frontLeft, frontRight, backLeft, backRight;
    private CRServo vertical1Servo;
    private Servo pivot3Servo, claw4Servo, claw5Servo;

    // ============================================
    // SET THIS TO TRUE TO RUN ENCODER TEST FIRST
    private static final boolean RUN_ENCODER_TEST = false;
    // ============================================

    // TETRIX MAX TorqueNADO Motor: 100 RPM, 1440 CPR
    private static final double TICKS_PER_REV = 1440.0;

    // Omni Wheel Specs: 100mm diameter, X-configuration (45° mounted)
    private static final double WHEEL_DIAMETER_MM = 100.0;
    private static final double WHEEL_CIRCUMFERENCE_MM = Math.PI * WHEEL_DIAMETER_MM;

    // X-CONFIGURATION CORRECTION: wheels at 45° to robot axes
    // Effective forward speed = cos(45°) = 0.707 of wheel speed
    private static final double COS_45 = 0.70710678;
    private static final double TICKS_PER_MM_FORWARD = (TICKS_PER_REV / WHEEL_CIRCUMFERENCE_MM) / COS_45;

    // Drive constants - higher power to compensate for 45° efficiency loss
    private static final double DRIVE_POWER = 0.85;
    private static final double TURN_POWER = 0.6;

    // Path distances in meters
    private static final double DIST_SOUTH_M = 0.70;
    private static final double DIST_WEST_M = 1.3;
    private static final double DIST_NORTH_M = 1.2;

    @Override
    public void runOpMode() {
        initializeHardware();

        if (RUN_ENCODER_TEST) {
            runEncoderTest();
            return;
        }

        telemetry.addLine("Robot ready - X-Configuration Omni");
        telemetry.addLine("1440 ticks/rev | 100mm wheels @ 45°");
        telemetry.addLine("Path: S 0.70m -> W 1.3m -> N 1.2m");
        telemetry.update();

        waitForStart();

        if (opModeIsActive()) {
            setClaw(0.65);
            sleep(300);

            telemetry.addLine("Moving SOUTH 0.70m");
            telemetry.update();
            driveDistanceMeters(DIST_SOUTH_M, DRIVE_POWER);

            telemetry.addLine("Turning WEST (90° clockwise)");
            telemetry.update();
            turnDegreesX(-90, TURN_POWER);

            telemetry.addLine("Moving WEST 1.3m");
            telemetry.update();
            strafeDistanceMeters(DIST_WEST_M, DRIVE_POWER);

            telemetry.addLine("Turning NORTH (90° clockwise)");
            telemetry.update();
            turnDegreesX(90, TURN_POWER);

            telemetry.addLine("Moving NORTH 1.2m");
            telemetry.update();
            driveDistanceMeters(DIST_NORTH_M, DRIVE_POWER);

            stopRobot();
            telemetry.addLine("Path complete!");
            telemetry.update();
        }
    }

    // ================= ENCODER TEST =================
    private void runEncoderTest() {
        telemetry.addLine("=== X-OMNI ENCODER TEST ===");
        telemetry.addLine("Expected: ~1440 ticks/rev");
        telemetry.addLine("Press START to begin test");
        telemetry.update();

        waitForStart();

        if (!opModeIsActive()) return;

        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        telemetry.addLine("Running 1 wheel rotation...");
        telemetry.update();

        frontLeft.setPower(0.3);
        sleep(1000);
        frontLeft.setPower(0);

        int ticks = Math.abs(frontLeft.getCurrentPosition());

        telemetry.addLine("=== TEST COMPLETE ===");
        telemetry.addData("Measured Ticks", ticks);
        telemetry.addData("Expected", 1440);
        telemetry.addLine("Update TICKS_PER_REV if needed");
        telemetry.update();

        sleep(30000);
    }

    private void initializeHardware() {
        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        backLeft = hardwareMap.get(DcMotor.class, "backLeft");
        backRight = hardwareMap.get(DcMotor.class, "backRight");

        // X-CONFIGURATION: All motors same direction or alternating
        // Standard: FL and BR forward, FR and BL reverse (for X pattern)
        // Adjust based on your actual wiring
        frontRight.setDirection(DcMotor.Direction.REVERSE);
        backLeft.setDirection(DcMotor.Direction.REVERSE);

        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        resetEncoders();

        vertical1Servo = hardwareMap.get(CRServo.class, "vertical1Servo");
        pivot3Servo = hardwareMap.get(Servo.class, "pivot3Servo");
        claw4Servo = hardwareMap.get(Servo.class, "claw4Servo");
        claw5Servo = hardwareMap.get(Servo.class, "claw5Servo");
        claw5Servo.setDirection(Servo.Direction.REVERSE);
    }

    private void resetEncoders() {
        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    // X-OMNI: Forward/Backward (all wheels drive, reduced efficiency)
    public void driveDistanceMeters(double meters, double power) {
        double mm = meters * 1000.0;
        // Account for 45° angle: need more ticks to travel same distance
        int targetTicks = (int)(mm * TICKS_PER_MM_FORWARD);

        resetEncoders();

        // X-config forward: all wheels same direction
        frontLeft.setTargetPosition(targetTicks);
        frontRight.setTargetPosition(targetTicks);
        backLeft.setTargetPosition(targetTicks);
        backRight.setTargetPosition(targetTicks);

        setRunToPosition();
        setPowerAll(Math.abs(power));

        waitForDriveComplete(targetTicks, meters, 5.5);
        stopRobot();
        resetEncoders();
    }

    // X-OMNI: Strafe/Left/Right (all wheels drive, alternating directions)
    public void strafeDistanceMeters(double meters, double power) {
        double mm = meters * 1000.0;
        int targetTicks = (int)(mm * TICKS_PER_MM_FORWARD);

        resetEncoders();

        // X-config strafe right (WEST in your case):
        // FL: reverse, FR: forward, BL: forward, BR: reverse
        frontLeft.setTargetPosition(-targetTicks);
        frontRight.setTargetPosition(targetTicks);
        backLeft.setTargetPosition(targetTicks);
        backRight.setTargetPosition(-targetTicks);

        setRunToPosition();
        setPowerAll(Math.abs(power));

        waitForDriveComplete(targetTicks, meters, 6.0);
        stopRobot();
        resetEncoders();
    }

    // X-OMNI: Turn in place (all wheels drive, most efficient for X-config)
    public void turnDegreesX(double degrees, double power) {
        // X-configuration turn: wheels drive perpendicular to radius
        // More efficient than plus-config, but different calculation

        // Track width for X-config (diagonal distance)
        // Typical 360mm robot = ~255mm effective radius * sqrt(2)
        double effectiveTrackMm = 360.0; // Measure your robot's wheel diagonal
        double turnCircumferenceMm = Math.PI * effectiveTrackMm;
        double turnMmPerDegree = turnCircumferenceMm / 360.0;

        double turnMm = degrees * turnMmPerDegree;
        // No 45° correction needed for turning in X-config (wheels roll perpendicular)
        int targetTicks = (int)(turnMm * (TICKS_PER_REV / WHEEL_CIRCUMFERENCE_MM));

        resetEncoders();

        // Clockwise turn: FL/BR forward, FR/BL reverse
        frontLeft.setTargetPosition(targetTicks);
        backRight.setTargetPosition(targetTicks);
        frontRight.setTargetPosition(-targetTicks);
        backLeft.setTargetPosition(-targetTicks);

        setRunToPosition();
        setPowerAll(Math.abs(power));

        ElapsedTime timer = new ElapsedTime();
        while (opModeIsActive() && timer.seconds() < 4.0 &&
                (frontLeft.isBusy() || frontRight.isBusy() || backLeft.isBusy() || backRight.isBusy())) {
            telemetry.addData("Turning X-config", degrees + "°");
            telemetry.update();
        }

        stopRobot();
        resetEncoders();
    }

    // Helper methods
    private void setRunToPosition() {
        frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

    private void setPowerAll(double power) {
        frontLeft.setPower(power);
        frontRight.setPower(power);
        backLeft.setPower(power);
        backRight.setPower(power);
    }

    private void waitForDriveComplete(int targetTicks, double meters, double timeoutMult) {
        double timeoutSec = meters * timeoutMult;
        ElapsedTime timer = new ElapsedTime();

        while (opModeIsActive() && timer.seconds() < timeoutSec &&
                (frontLeft.isBusy() || frontRight.isBusy() || backLeft.isBusy() || backRight.isBusy())) {
            telemetry.addData("Target m", meters);
            telemetry.addData("Current m", "%.2f",
                    Math.abs(frontLeft.getCurrentPosition()) / TICKS_PER_MM_FORWARD / 1000.0);
            telemetry.addData("Progress", "%.0f%%",
                    100.0 * Math.abs(frontLeft.getCurrentPosition()) / targetTicks);
            telemetry.update();
        }
    }

    public void stopRobot() {
        frontLeft.setPower(0);
        frontRight.setPower(0);
        backLeft.setPower(0);
        backRight.setPower(0);
    }

    public void setClaw(double position) {
        claw4Servo.setPosition(position);
        claw5Servo.setPosition(position);
    }

    public void setPivot(double position) {
        pivot3Servo.setPosition(position);
    }

    public void liftVertical(double power, int timeMs) {
        vertical1Servo.setPower(power);
        sleep(timeMs);
        vertical1Servo.setPower(0);
    }
}