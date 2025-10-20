package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@TeleOp(name = "PuzzleBot", group = "Robot")
public class PuzzleBot extends LinearOpMode {

    private DcMotor frontLeft;
    private DcMotor frontRight;
    private DcMotor backMiddle;

    @Override
    public void runOpMode() throws InterruptedException {

        // Initialize motors
        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        backMiddle = hardwareMap.get(DcMotor.class, "backMiddle");

        // Reverse direction if needed
        frontRight.setDirection(DcMotor.Direction.REVERSE);

        waitForStart();

        while (opModeIsActive()) {

            // Get joystick values
            double y = -gamepad1.left_stick_y;     // Forward/backward
            double x = gamepad1.left_stick_x;      // Strafing (left/right)
            double rx = -gamepad1.right_stick_x;   // Rotation

            // For Kiwi drive or 3-wheel omni, you'll need special kinematics.
            // Here's a basic test layout (not final tuning):

            // This is a placeholder and may need tuning per your robot geometry.
            double frontLeftPower  = y + rx;
            double frontRightPower = y - rx;
            double backMiddlePower = x;  // Strafing handled by the rear wheel

            // Normalize powers if needed
            double max = Math.max(Math.abs(frontLeftPower),
                         Math.max(Math.abs(frontRightPower), Math.abs(backMiddlePower)));

            if (max > 1.0) {
                frontLeftPower  /= max;
                frontRightPower /= max;
                backMiddlePower /= max;
            }

            // Set motor powers
            frontLeft.setPower(frontLeftPower);
            frontRight.setPower(frontRightPower);
            backMiddle.setPower(backMiddlePower);

            // Telemetry for debugging
            telemetry.addData("Front Left Power", frontLeftPower);
            telemetry.addData("Front Right Power", frontRightPower);
            telemetry.addData("Back Middle Power", backMiddlePower);
            telemetry.update();

            sleep(20);
        }
    }
}