package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@TeleOp(name = "IntakeTorqueNado", group = "Robot")
public class IntakeTorqueNado extends LinearOpMode {

    private DcMotor frontLeft;
    private DcMotor frontRight;
    private DcMotor backLeft;
    private DcMotor backRight;
    private DcMotor intakeMotor;
    private Servo doorServoL;
    private Servo doorServoR;
    private boolean doorOpen = false;

    // RPM Control Variables
    private static final double MaxRPM = 6000.0;
    private static final double RPMstep = 500.0;
    private static final double StartingRPM = 6000.0;
    private static final double RPMboost = 800.0;
    private double targetRPM = StartingRPM;
    private boolean dpadUpPressed = false;
    private boolean dpadDownPressed = false;
    private boolean robotstarted = false;

    // 8-directional deadzone and speed
    private static final double DEADZONE = 0.3;
    private static final double MOVE_SPEED = 1.0;

    @Override
    public void runOpMode() throws InterruptedException {

        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        backLeft = hardwareMap.get(DcMotor.class, "backLeft");
        backRight = hardwareMap.get(DcMotor.class, "backRight");
        intakeMotor = hardwareMap.get(DcMotor.class, "intakeMotor");
        doorServoL = hardwareMap.get(Servo.class, "doorServoL");
        doorServoR = hardwareMap.get(Servo.class, "doorServoR");

        // Mecanum wheel direction setup
        frontLeft.setDirection(DcMotor.Direction.FORWARD);
        backLeft.setDirection(DcMotor.Direction.FORWARD);
        frontRight.setDirection(DcMotor.Direction.REVERSE);
        backRight.setDirection(DcMotor.Direction.REVERSE);

        doorServoR.setDirection(Servo.Direction.REVERSE);

        waitForStart();

        while (opModeIsActive()) {

            double triggerInput = gamepad1.right_trigger;
            double intakePower = 0.0;

            if (!robotstarted && triggerInput > 0.1) {
                targetRPM = StartingRPM + RPMboost;
                intakePower = targetRPM / MaxRPM;
                intakeMotor.setPower(-intakePower);
                sleep(500);
                targetRPM = StartingRPM;
                robotstarted = true;
            } else if (triggerInput > 0.1) {
                intakePower = (triggerInput * targetRPM) / MaxRPM;
            }

            intakeMotor.setPower(-intakePower);

            // ===== RPM ADJUSTMENT (D-Pad) =====
            if (gamepad1.dpad_up && !dpadUpPressed) {
                targetRPM += RPMstep;
                if (targetRPM > MaxRPM) {
                    targetRPM = MaxRPM;
                }
                dpadUpPressed = true;
            } else if (!gamepad1.dpad_up) {
                dpadUpPressed = false;
            }

            if (gamepad1.dpad_down && !dpadDownPressed) {
                targetRPM -= RPMstep;
                if (targetRPM < 0) {
                    targetRPM = 0;
                }
                dpadDownPressed = false;
            } else if (!gamepad1.dpad_down) {
                dpadDownPressed = false;
            }

            // ===== DOOR CONTROL =====
            if (gamepad1.a) {
                doorOpen = !doorOpen;
                sleep(250);
            }

            if (doorOpen) {
                doorServoL.setPosition(1.0);
                doorServoR.setPosition(1.0);
            }
            else {
                doorServoL.setPosition(0.0);
                doorServoR.setPosition(0.0);
            }



            double leftX = -gamepad1.left_stick_x;
            double leftY = gamepad1.left_stick_y;
            double rightX = -gamepad1.right_stick_x; // Rotation only

            // Apply deadzone to left stick
            double magnitude = Math.sqrt(leftX * leftX + leftY * leftY);

            double x = 0; // Strafe
            double y = 0; // Forward/Back

            if (magnitude > DEADZONE) {
                // Calculate angle in degrees (0 = right, 90 = up, etc.)
                double angle = Math.toDegrees(Math.atan2(leftY, leftX));

                // Snap to 8 directions (0, 45, 90, 135, 180, -135, -90, -45)
                double snappedAngle = Math.round(angle / 45.0) * 45.0;

                // Convert back to x/y components at fixed speed
                double rad = Math.toRadians(snappedAngle);
                x = Math.cos(rad) * MOVE_SPEED; // Strafe component
                y = Math.sin(rad) * MOVE_SPEED; // Forward component
            }

            // Right stick X is pure rotation (smooth, not 8-directional)
            double turn = rightX * 0.7; // Scale rotation for better control

            // Mecanum wheel power calculations
            double frontLeftPower = y + x + turn;
            double frontRightPower = y - x - turn;
            double backLeftPower = y - x + turn;
            double backRightPower = y + x - turn;

            // Normalize wheel powers
            double max = Math.max(Math.abs(frontLeftPower),
                    Math.max(Math.abs(frontRightPower),
                            Math.max(Math.abs(backLeftPower),
                                    Math.abs(backRightPower))));

            if (max > 1.0) {
                frontLeftPower /= max;
                frontRightPower /= max;
                backLeftPower /= max;
                backRightPower /= max;
            }

            frontLeft.setPower(frontLeftPower);
            frontRight.setPower(frontRightPower);
            backLeft.setPower(backLeftPower);
            backRight.setPower(backRightPower);

            // ===== TELEMETRY =====
            telemetry.addData("=== DRIVE (Fortnite Style) ===", "");
            telemetry.addData("Left Stick", "8-directional");
            telemetry.addData("Raw Left X/Y", "%.2f / %.2f", leftX, leftY);
            telemetry.addData("Snapped X/Y", "%.2f / %.2f", x, y);
            telemetry.addData("Right X (Rotate)", "%.2f", turn);
            telemetry.addData("Front Left", "%.2f", frontLeftPower);
            telemetry.addData("Front Right", "%.2f", frontRightPower);
            telemetry.addData("Back Left", "%.2f", backLeftPower);
            telemetry.addData("Back Right", "%.2f", backRightPower);
            telemetry.addData("=== INTAKE ===", "");
            telemetry.addData("Target RPM", "%.0f", targetRPM);
            telemetry.addData("Current RPM", "%.0f", (intakePower * MaxRPM));
            telemetry.addData("Motor Power", "%.2f", intakePower);
            telemetry.addData("=== DOOR ===", "");
            telemetry.addData("Door Open", doorOpen);
            telemetry.update();

            sleep(20);
        }
    }
}