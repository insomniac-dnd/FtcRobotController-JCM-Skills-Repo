package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import com.qualcomm.robotcore.hardware.DcMotor;

import com.qualcomm.robotcore.hardware.Servo;


@Autonomous(name = "AutobotTorqueNado", group = "Autonomous")

public class Autobot extends LinearOpMode {


    private DcMotor frontLeft, backLeft, frontRight, backRight;

    private Servo leftClaw, rightClaw;


    private static final double COUNTS_PER_MOTOR_REV = 1440;

    private static final double DRIVE_GEAR_REDUCTION = 1.0;

    private static final double WHEEL_DIAMETER_CM = 8.5;

    private static final double COUNTS_PER_CM = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /

            (WHEEL_DIAMETER_CM * Math.PI);


    // Claw positions

    private static final double LEFT_CLAW_OPEN = 0.3;

    private static final double LEFT_CLAW_CLOSE = 0.7;

    private static final double RIGHT_CLAW_OPEN = 0.7;

    private static final double RIGHT_CLAW_CLOSE = 0.3;


    private static final double DRIVE_SPEED = 0.6;

    private static final double TURN_SPEED = 0.5;


    @Override

    public void runOpMode() {

        initializeHardware();


        waitForStart();


        if (opModeIsActive()) {



            encoderDrive(DRIVE_SPEED, 85, 85);

            encoderDrive(DRIVE_SPEED, 0, 0);

            encoderDrive(TURN_SPEED, 0, 0);

            encoderDrive(TURN_SPEED, 27, -27);        // Turn 1

            encoderDrive(DRIVE_SPEED, 0, 0);

            encoderDrive(TURN_SPEED, 0, 0);



            encoderDrive(DRIVE_SPEED, 94, 94);

            encoderDrive(DRIVE_SPEED, 0, 0);

            encoderDrive(TURN_SPEED, 0, 0);

            encoderDrive(TURN_SPEED, 26, -26);       // TUrn 2

            encoderDrive(DRIVE_SPEED, 0, 0);

            encoderDrive(TURN_SPEED, 0, 0);



            encoderDrive(DRIVE_SPEED, 55, 55);

            encoderDrive(DRIVE_SPEED, 0, 0);

            encoderDrive(TURN_SPEED, 0, 0);

            encoderDrive(TURN_SPEED, 26, -26);        //  Turn 3

            encoderDrive(DRIVE_SPEED, 0, 0);

            encoderDrive(TURN_SPEED, 0, 0);



            encoderDrive(DRIVE_SPEED, 35, 35);  // driveForward(42.15)

            encoderDrive(DRIVE_SPEED, 0, 0);

            encoderDrive(TURN_SPEED, 0, 0);

            encoderDrive(TURN_SPEED, 26, -26);        // rotateRight(90

            encoderDrive(DRIVE_SPEED, 0, 0);

            encoderDrive(TURN_SPEED, 0, 0);



            encoderDrive(DRIVE_SPEED, 9.14, 9.14);    // driveForward(9.14)

            closeClaws();

            sleep(300);


            encoderDrive(TURN_SPEED, 50, -50);        // rotateRight(180)

            encoderDrive(DRIVE_SPEED, 134.58, 134.58); // driveForward(134.58)


            encoderDrive(TURN_SPEED, -27, 27);        // rotateLeft(90)

            encoderDrive(DRIVE_SPEED, 34, 34);        // driveForward(34)



            openClaws();

            sleep(300);



        }

    }



    public void encoderDrive(double speed, double leftCm, double rightCm) {

        int newLeftTarget = frontLeft.getCurrentPosition() + (int)(leftCm * COUNTS_PER_CM);

        int newRightTarget = frontRight.getCurrentPosition() + (int)(rightCm * COUNTS_PER_CM);


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

        leftClaw = hardwareMap.get(Servo.class, "leftClaw");

        rightClaw = hardwareMap.get(Servo.class, "rightClaw");


        // Reverse left side motors

        frontLeft.setDirection(DcMotor.Direction.REVERSE);

        backLeft.setDirection(DcMotor.Direction.REVERSE);


        setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        setMode(DcMotor.RunMode.RUN_USING_ENCODER);

    }


    private void setMode(DcMotor.RunMode mode) {

        frontLeft.setMode(mode); backLeft.setMode(mode);

        frontRight.setMode(mode); backRight.setMode(mode);

    }


    private void stopMotors() {

        frontLeft.setPower(0); backLeft.setPower(0);

        frontRight.setPower(0); backRight.setPower(0);

    }


    private void closeClaws() {

        leftClaw.setPosition(LEFT_CLAW_CLOSE);

        rightClaw.setPosition(RIGHT_CLAW_CLOSE);

    }


    private void openClaws() {

        leftClaw.setPosition(LEFT_CLAW_OPEN);

        rightClaw.setPosition(RIGHT_CLAW_OPEN);

    }

}

