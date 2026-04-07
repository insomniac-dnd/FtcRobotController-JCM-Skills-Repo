package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name = "driveByEncoderTest", group = "FTCROBOTCONTROLLER-JCM-SKILLS-REPO")
public  class driveByEncoderTest extends LinearOpMode {
    //region Hardware Declarations
    private DcMotor frontLeft;
    private DcMotor frontRight;
    //endregion

    @Override
    public void runOpMode() throws InterruptedException {
        //region Hardware Map Classes

        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");

        frontLeft.setDirection(DcMotor.Direction.FORWARD);
        frontRight.setDirection(DcMotor.Direction.REVERSE);

        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        //endregion

        waitForStart();

        encoderDrive(DRIVE_SPEED, 24, 24, 5.0);

        // Example: drive backward 12 inches
        encoderDrive(DRIVE_SPEED, -12, -12, 4.0);

        telemetry.addData("Path", "Complete");
        telemetry.update();
    }

    /**
     * Drive method based on encoder counts
     */
    public void encoderDrive(double speed,
                             double leftInches, double rightInches,
                             double timeoutS) {

        int newLeftTarget;
        int newRightTarget;

        // Determine new target position
        newLeftTarget = frontLeft.getCurrentPosition() + (int) (leftInches * COUNTS_PER_INCH);
        newRightTarget = frontRight.getCurrentPosition() + (int) (rightInches * COUNTS_PER_INCH);

        frontLeft.setTargetPosition(newLeftTarget);
        frontRight.setTargetPosition(newRightTarget);

        // Turn On RUN_TO_POSITION
        frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        // Start motion
        frontLeft.setPower(Math.abs(speed));
        frontRight.setPower(Math.abs(speed));

        // Keep looping while both motors are busy and timeout not reached
        runtime.reset();
        while (opModeIsActive() &&
                (runtime.seconds() < timeoutS) &&
                (frontLeft.isBusy() && frontRight.isBusy())) {

            telemetry.addData("Target", "L:%d R:%d", newLeftTarget, newRightTarget);
            telemetry.addData("Current", "L:%d R:%d",
                    frontLeft.getCurrentPosition(),
                    frontRight.getCurrentPosition());
            telemetry.update();
        }

        // Stop all motion
        frontLeft.setPower(0);
        frontRight.setPower(0);

        // Turn off RUN_TO_POSITION
        frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
}