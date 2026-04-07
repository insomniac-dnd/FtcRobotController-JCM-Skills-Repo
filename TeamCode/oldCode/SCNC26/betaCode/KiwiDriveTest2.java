package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@TeleOp(name = "KiwiDriveTest2", group = "Robot")
public class KiwiDriveTest2 extends LinearOpMode {

    private DcMotor frontLeft;
    private DcMotor frontRight;
    private DcMotor backMiddle;

    @Override
    public void runOpMode() throws InterruptedException {

        // Initialize motors
        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        backMiddle = hardwareMap.get(DcMotor.class, "backMiddle");

        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        frontRight.setDirection(DcMotor.Direction.REVERSE);
        backMiddle.setDirection(DcMotor.Direction.REVERSE);

        waitForStart();

        while (opModeIsActive()) {

            double x = (-gamepad1.left_stick_x);
            double y = (gamepad1.left_stick_y);
            double r = (gamepad1.right_stick_x);

            double frontLeftPower = -0.5 * x + 0.866 * y + r;
            double frontRightPower = -0.5 * x - 0.866 * y + r;
            double backMiddlePower = x + r;

            double max = Math.max(Math.abs(frontLeftPower),
                    Math.max(Math.abs(frontRightPower), Math.abs(backMiddlePower)));

            if (max > 1.0) {
                frontLeftPower /= max;
                frontRightPower /= max;
                backMiddlePower /= max;
            }

            frontLeft.setPower(frontLeftPower);
            frontRight.setPower(frontRightPower);
            backMiddle.setPower(backMiddlePower);

            sleep(20);

        }
    }
}