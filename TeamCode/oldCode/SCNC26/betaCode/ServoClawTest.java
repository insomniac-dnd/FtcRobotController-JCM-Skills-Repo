package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.CRServo;

import com.qualcomm.robotcore.util.Range;

@TeleOp(name = "ServoClawTest", group = "FTCROBOTCONTROLLER-JCM-SKILLS-REPO")
public class ServoClawTest extends LinearOpMode {
    private Servo rotate0Servo;
    private CRServo vertical1Servo;
    private Servo bin2Servo;
    private Servo pivot3Servo;
    private Servo wrist4Servo;
    private Servo claw5Servo;

    private double rotate0ServoPosition = 0.5;
    private double bin2ServoPosition = 0.0;
    private double pivot3ServoPosition = 0.5;
    private double wrist4ServoPosition = 0.5;
    private double claw5ServoPosition = 0.0; 
    
    private final double servoincrement = 0.05;
    private final double servomin = 0.0;
    private final double servomax = 1.0;
    private double servopower = 0.0;

    @Override
    public void runOpMode() throws InterruptedException {
    
        rotate0Servo = hardwareMap.get(Servo.class,"rotate0Servo");
        vertical1Servo = hardwareMap.get(CRServo.class,"vertical1Servo");
        bin2Servo = hardwareMap.get(Servo.class, "bin2Servo");
        pivot3Servo = hardwareMap.get(Servo.class,"pivot3Servo");
        wrist4Servo = hardwareMap.get(Servo.class,"wrist4Servo");
        claw5Servo = hardwareMap.get(Servo.class,"claw5Servo");
        
        waitForStart();

        while (opModeIsActive()) {

            claw5ServoPosition = gamepad1.right_trigger;
            claw5Servo.setPosition(claw5ServoPosition);
            telemetry.addData("claw5Servo:", claw5ServoPosition);
            telemetry.update();

            if (gamepad1.left_bumper){

                if (gamepad1.dpad_left < 0.0) {
                    rotate0ServoPosition -= servoincrement;
                    rotate0Servo.setPosition(rotate0ServoPosition);
                    telemetry.addData("rotation0Servo:", rotate0ServoPosition);
                    telemetry.update();
                }

                if (gamepad1.dpad_right) {
                    rotate0ServoPosition += servoincrement;
                    rotate0Servo.setPosition(rotate0ServoPosition);
                    telemetry.addData("rotation0Servo:", rotate0ServoPosition);
                    telemetry.update();
                }
            }

            if (gamepad1.left_bumper) {

                if (gamepad1.dpad_down) {
                    servopower = 1.0;
                    vertical1Servo.setPower(-servopower);
                    telemetry.addData("vertical1Servo:", -servopower);
                    telemetry.update();
                }

                if (gamepad1.dpad_up) {
                    servopower = 1.0;
                    vertical1Servo.setPower(servopower);
                    telemetry.addData("vertical1Servo:", servopower);
                    telemetry.update();
                }
            }

            if (gamepad1.right_bumper) {

                if (gamepad1.dpad_down) {
                    pivot3ServoPosition -= servoincrement;
                    pivot3Servo.setPosition(pivot3ServoPosition);
                    telemetry.addData("pivot3Servo:", pivot3ServoPosition);
                    telemetry.update();
                }

                if (gamepad1.dpad_up) {
                    pivot3ServoPosition += servoincrement;
                    pivot3Servo.setPosition(pivot3ServoPosition);
                    telemetry.addData("pivot3Servo:", pivot3ServoPosition);
                    telemetry.update();
                }
            }            

            if (gamepad1.right_bumper) {

                if (gamepad1.dpad_left) {
                    wrist4ServoPosition -= servoincrement;
                    wrist4Servo.setPosition(wrist4ServoPosition);
                    telemetry.addData("wrist4Servo:", wrist4ServoPosition);
                    telemetry.update();
                }

                if (gamepad1.dpad_right) {
                    wrist4ServoPosition += servoincrement;
                    wrist4Servo.setPosition(wrist4ServoPosition);
                    telemetry.addData("wrist4Servo:", wrist4ServoPosition);
                    telemetry.update();
                }  
            }

            if (gamepad1.a) {
                bin2ServoPosition = 1.0;
                bin2Servo.setPosition(bin2ServoPosition);
                telemetry.addData ("bin2Servo:", bin2ServoPosition);
                telemetry.update();

                wait(100);

                bin2ServoPosition = 0.0;
                bin2Servo.setPosition(bin2ServoPosition);
                telemetry.addData ("bin2Servo:", bin2ServoPosition);
                telemetry.update();
            }
            
            rotate0ServoPosition = Range.clip(rotate0ServoPosition, servomin, servomax);
            bin2ServoPosition = Range.clip(bin2ServoPosition, servomin, servomax);
            pivot3ServoPosition = Range.clip(pivot3ServoPosition, servomin, servomax);
            wrist4ServoPosition = Range.clip(wrist4ServoPosition, servomin, servomax);
            claw5ServoPosition = Range.clip(claw5ServoPosition, servomin, servomax);

            sleep(20);
        }
    }
}