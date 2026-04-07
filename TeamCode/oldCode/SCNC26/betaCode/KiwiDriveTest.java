package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@TeleOp(name = "KiwiDriveTest", group = "Robot")
public class KiwiDriveTest extends LinearOpMode {

    private DcMotor frontLeft;
    private DcMotor frontRight;
    private DcMotor backMiddle;
    private double x = 1.0;
    private double y = 1.0;
    private double theta = Math.atan2(y, x);

    @Override
    public void runOpMode() throws InterruptedException {

        // Initialize motors
        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        backMiddle = hardwareMap.get(DcMotor.class, "backMiddle");

        waitForStart();

        while (opModeIsActive()) {

            double powerx = (gamepad.left_stick_x);
            double powery = (gamepad.left_stick_y);

            double vx = Math.cos(theta)*powerx;
            double vy = Math.sin(theta)*powery;

            double frontLeftPower = -vx;
            double frontRightPower = 0.5*vx-Math.sqrt(3/2)*vy;
            double backMiddlePower = 0.5*vx+Math.sqrt(3.2)*vy;

            double max = Math.max(Math.abs(frontLeftPower), Math.max(Math.abs(frontRightPower), Math.abs(backMiddlePower)));

            if (max > 1.0) {
                frontLeftPower /= max;
                frontRightPower /= max;
                backMiddlePower /= max;
            }

            frontLeft.setPower(frontLeftPower);
            frontRight.setPower(frontRightPower);
            backMiddle.setPower(backMiddlePower);

            sleep(20)

        }
    }
}