package org.firstinspires.ftc.teamcode.Subsystems;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

public class driveBase {

    private LinearOpMode myOpMode = null;
    private DcMotor frontLeft = null;
    private DcMotor frontRight= null;
    private DcMotor backLeft = null;
    private DcMotor backRight = null;

    public void init() {
        frontLeft = myOpMode.hardwareMap.get(DcMotor.class, "frontLeft");
        frontRight = myOpMode.hardwareMap.get(DcMotor.class, "frontRight");
        backLeft = myOpMode.hardwareMap.get(DcMotor.class, "backLeft");
        backRight = myOpMode.hardwareMap.get(DcMotor.class, "backRight");

        frontRight.setDirection(DcMotor.Direction.REVERSE);
        backRight.setDirection(DcMotor.Direction.REVERSE);
    }
    public void driveRobot(double y, double x, double rx, double divisor) {
        double frontLeftPower = y + x + rx;
        double backLeftPower = y - x + rx;
        double frontRightPower = y - x - rx;
        double backRightPower = y + x - rx;

        double max = Math.max(
                Math.max(Math.abs(frontLeftPower),
                        Math.abs(backLeftPower)),
                Math.max(Math.abs(frontRightPower),
                        Math.abs(backRightPower)));

        if (max > 1.0) {
            frontLeftPower /= max;
            backLeftPower /= max;
            frontRightPower /= max;
            backRightPower /= max;
        }

        frontLeftPower /= divisor;
        frontRightPower /= divisor;
        backLeftPower /= divisor;
        backRightPower /= divisor;

        setDrivePower(frontLeftPower,backLeftPower,frontRightPower,backRightPower);

        myOpMode.telemetry.addData("Front Left", frontLeftPower);
        myOpMode.telemetry.addData("Back Left", backLeftPower);
        myOpMode.telemetry.addData("Front Right", frontRightPower);
        myOpMode.telemetry.addData("Back Right", backRightPower);

        myOpMode.telemetry.update();
    }
    public void setDrivePower(double fl, double bl, double fr, double br) {
        frontLeft.setPower(fl);
        backLeft.setPower(bl);
        frontRight.setPower(fr);
        backRight.setPower(br);
    }
    public void telemetry() {
        myOpMode.telemetry.addLine("--- Driving Subsystem ---");
        myOpMode.telemetry.addData("Front Left", frontLeft.getPower());
        myOpMode.telemetry.addData("Back Left", backLeft.getPower());
        myOpMode.telemetry.addData("Front Right", frontRight.getPower());
        myOpMode.telemetry.addData("Back Right", backRight.getPower());
    }

}




