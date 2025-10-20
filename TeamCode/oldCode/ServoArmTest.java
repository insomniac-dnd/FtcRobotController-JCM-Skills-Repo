package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name = "ServoArmTest", group = "FTCROBOTCONTROLLER-JCM-SKILLS-REPO")
public class ServoArmTest extends LinearOpMode {
    private Servo rotate0Servo;
    private Servo arm1Servo;
    private Servo arm2Servo;
    private Servo pivot3Servo;
    private Servo wrist4Servo;
    private Servo claw5Servo;

    private double rotate0ServoPosition = 0.00;
    private double arm1ServoPosition = 0.0;
    private double arm2ServoPosition = 0.0;
    private double pivot3ServoPosition = 0.0;
    private double wrist4ServoPosition = 0.0;
    private double claw5ServoPosition = 0.0;

    // determine appropriate starting positions for each servo

    private final double servoincrement = 0.05;
    private final double servomin = 0.0;
    private final double servomax = 1.0;

    @Override
    public void runOpMode() throws InterruptedException {
    
        rotate0Servo = hardwareMap.get(Servo.class,"rotate0Servo");
        arm1Servo = hardwareMap.get(Servo.class,"arm1Servo");
        arm2Servo = hardwareMap.get(Servo.class,"arm2Servo");
        pivot3Servo = hardwareMap.get(Servo.class,"pivot3Servo");
        wrist4Servo = hardwareMap.get(Servo.class,"wrist4Servo");
        claw5Servo = hardwareMap.get(Servo.class,"claw5Servo");
        
        waitForStart();

        while (opModeIsActive()) {

            double claw5ServoPosition = gamepad1.right_trigger;
            claw5Servo.setPosition(claw5ServoPosition);
            telemetry.addData("claw5Servo:", claw5ServoPosition);
            telemetry.update();

            if (!gamepad1.right_bumper && gamepad1.a) {
                double rotate0ServoPosition = gamepad1.right_stick_x;
                rotate0Servo.setPosition(rotate0ServoPosition);
                telemetry.addData("rotate0Servo:", rotate0ServoPosition);
                telemetry.update();
                
            }

            if (!gamepad1.right_bumper && !gamepad1.y) {
                double arm1ServoPosition = gamepad1.right_stick_y;
                arm1Servo.setPosition(arm1ServoPosition);
                telemetry.addData("arm1Servo:", arm1ServoPosition);
                telemetry.update();
            }

            if (gamepad1.right_bumper) {
                double arm2ServoPosition = gamepad1.right_stick_y;
                arm2Servo.setPosition(arm2ServoPosition);
                telemetry.addData("arm2Servo:", arm2ServoPosition);
                telemetry.update();
            }

            if (gamepad1.y) {
                double pivot3ServoPosition = gamepad1.right_stick_y;
                pivot3Servo.setPosition(pivot3ServoPosition);
                telemetry.addData("pivot3Servo:", pivot3ServoPosition);
                telemetry.update();
            }
           
            if (gamepad1.right_bumper) {
                double wrist4ServoPosition = gamepad1.right_stick_x;
                wrist4Servo.setPosition(wrist4ServoPosition);
                telemetry.addData("wrist4Servo:", wrist4ServoPosition);
                telemetry.update();
            }
            
            rotate0ServoPosition = Range.clip(rotate0ServoPosition, servomin, servomax);
            arm1ServoPosition = Range.clip(arm1ServoPosition, servomin, servomax);
            arm2ServoPosition = Range.clip(arm2ServoPosition, servomin, servomax);
            pivot3ServoPosition = Range.clip(pivot3ServoPosition, servomin, servomax);
            wrist4ServoPosition = Range.clip(wrist4ServoPosition, servomin, servomax);
            claw5ServoPosition = Range.clip(claw5ServoPosition, servomin, servomax);
        }
    }

}