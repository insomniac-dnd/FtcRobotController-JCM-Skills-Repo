package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;

import com.qualcomm.robotcore.util.Range;

@TeleOp(name = "ServoClawTest", group = "FTCROBOTCONTROLLER-JCM-SKILLS-REPO")
public class ServoClawTest extends LinearOpMode {
    private CRServo vertical1Servo;

    private double servopower = 0.0;
    private double power1 = 1.0;
    private double power0 = 0.0;

    @Override
    public void runOpMode() throws InterruptedException {
        vertical1Servo = hardwareMap.get(CRServo.class,"vertical1Servo");
        vertical1Servo.setDirection(DcMotor.Direction.FORWARD);

        waitForStart();

        while (opModeIsActive()) {
            if (gamepad1.y) { // raise claw
                servopower = power1;
                vertical1Servo.setPower(servopower);
            }   else {
                servopower = power0;
                vertical1Servo.setPower(servopower);
            }

            if (gamepad1.x) { // lower claw
                servopower = power1;
                vertical1Servo.setPower(-servopower);
            }   else {
                servopower = power0;
                vertical1Servo.setPower(servopower);
            }

            telemetry.addData("vertical1Servo:", vertical1Servo.getPower);
            telemetry.update
                    z
            sleep(20);
        }
    }
}