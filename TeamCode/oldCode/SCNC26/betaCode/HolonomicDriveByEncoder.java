package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name = "HolonomicDriveByEncoder", group = "FTCROBOTCONTROLLER-JCM-SKILLS-REPO")
public class HolonomicDriveByEncoder extends LinearOpMode {

    //region Hardware Declarations
    private DcMotor frontLeft, frontRight, backLeft, backRight;
    private ElapsedTime runtime = new ElapsedTime();
    //endregion

    //region Robot Constants (Tetrix MAX + Studica 100mm omni)
    static final double COUNTS_PER_MOTOR_REV = 1440.0;   // Tetrix MAX 300 RPM motor
    static final double DRIVE_GEAR_REDUCTION = 1.0;      // direct drive
    static final double WHEEL_DIAMETER_INCHES = 3.937;   // 100 mm omni
    static final double COUNTS_PER_INCH =
            (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
                    (WHEEL_DIAMETER_INCHES * Math.PI);           // ≈116.4 counts/in

    static final double DRIVE_SPEED = 0.5;
    static final double TURN_SPEED = 0.4;

    // Track width for rotation math (center of left to right wheel)
    static final double ROBOT_TRACK_WIDTH_INCHES = 14.5;
    static final double TURN_CIRCUMFERENCE = Math.PI * ROBOT_TRACK_WIDTH_INCHES;
    //endregion

    @Override
    public void runOpMode() throws InterruptedException {

        //region Hardware Map
        frontLeft  = hardwareMap.get(DcMotor.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        backLeft   = hardwareMap.get(DcMotor.class, "backLeft");
        backRight  = hardwareMap.get(DcMotor.class, "backRight");

        frontRight.setDirection(DcMotor.Direction.REVERSE);
        backRight.setDirection(DcMotor.Direction.REVERSE);

        resetEncoders();
        //endregion

        telemetry.addData("Status", "Initialized and ready");
        telemetry.update();
        waitForStart();

        //region Example Autonomous Movements
        driveForward(12, DRIVE_SPEED, 5.0);   // forward 24"
//        strafeRight(12, DRIVE_SPEED, 5.0);    // right 12"
        turnDegrees(180, TURN_SPEED, 6.0);     // turn right 90°
        driveForward(12, DRIVE_SPEED, 4.0);  // backward 12"
        telemetry.addData("Path", "Complete");
        telemetry.update();
        //endregion
    }

    //region Encoder Utility
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

    /** Core encoder drive for 4-wheel holonomic base */
    public void encoderDrive(double speed,
                             double flInches, double frInches,
                             double blInches, double brInches,
                             double timeoutS) {

        int newFL = frontLeft.getCurrentPosition()  + (int)(flInches * COUNTS_PER_INCH);
        int newFR = frontRight.getCurrentPosition() + (int)(frInches * COUNTS_PER_INCH);
        int newBL = backLeft.getCurrentPosition()   + (int)(blInches * COUNTS_PER_INCH);
        int newBR = backRight.getCurrentPosition()  + (int)(brInches * COUNTS_PER_INCH);

        frontLeft.setTargetPosition(newFL);
        frontRight.setTargetPosition(newFR);
        backLeft.setTargetPosition(newBL);
        backRight.setTargetPosition(newBR);

        frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        runtime.reset();
        frontLeft.setPower(Math.abs(speed));
        frontRight.setPower(Math.abs(speed));
        backLeft.setPower(Math.abs(speed));
        backRight.setPower(Math.abs(speed));

        while (opModeIsActive() &&
                (runtime.seconds() < timeoutS) &&
                (frontLeft.isBusy() && frontRight.isBusy() &&
                        backLeft.isBusy() && backRight.isBusy())) {

            telemetry.addData("Target", "FL:%d FR:%d BL:%d BR:%d",
                    newFL, newFR, newBL, newBR);
            telemetry.addData("Current", "FL:%d FR:%d BL:%d BR:%d",
                    frontLeft.getCurrentPosition(),
                    frontRight.getCurrentPosition(),
                    backLeft.getCurrentPosition(),
                    backRight.getCurrentPosition());
            telemetry.update();
        }

        stopAll();
    }

    private void stopAll() {
        frontLeft.setPower(0);
        frontRight.setPower(0);
        backLeft.setPower(0);
        backRight.setPower(0);

        frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
    //endregion

    //region Movement Wrappers
    public void driveForward(double inches, double speed, double timeoutS) {
        encoderDrive(speed, inches, inches, inches, inches, timeoutS);
    }

    public void strafeRight(double inches, double speed, double timeoutS) {
        // Right strafe: frontLeft/backRight forward, frontRight/backLeft backward
        encoderDrive(speed, inches, -inches, -inches, inches, timeoutS);
    }

    public void strafeLeft(double inches, double speed, double timeoutS) {
        strafeRight(-inches, speed, timeoutS);
    }

    public void turnDegrees(double degrees, double speed, double timeoutS) {
        // Positive = right turn, negative = left
        double direction = Math.signum(degrees);
        double turnInches = (Math.abs(degrees) / 360.0) * TURN_CIRCUMFERENCE;

        encoderDrive(speed,
                turnInches * direction, -turnInches * direction,
                turnInches * direction, -turnInches * direction,
                timeoutS);
    }
    //endregion
}