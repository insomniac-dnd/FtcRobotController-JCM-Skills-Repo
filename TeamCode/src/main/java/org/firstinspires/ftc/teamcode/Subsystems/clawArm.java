package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.CRServo;

public class clawArm {
    private LinearOpMode myOpMode = null;
    private CRServo verticalServo = null;
    private Servo pivotServo = null;
    private Servo wristServo = null;
    private CRServo clawServo = null;
    private double pivotPosition = 0.5;
    private double wristPosition = 0.5;
    public void init() {
        verticalServo = myOpMode.hardwareMap.get(CRServo.class,"verticalServo");
        pivotServo = myOpMode.hardwareMap.get(Servo.class,"pivotServo");
        wristServo = myOpMode.hardwareMap.get(Servo.class,"wristServo");
        clawServo = myOpMode.hardwareMap.get(CRServo.class,"clawServo");
    }

    public void verticalMove(double power) {
        verticalServo.setPower(power);
    }
    public void pivotMove(double increment) {
        pivotPosition += increment;
        pivotServo.setPosition(pivotPosition);
    }
    public void wristMove(double increment) {
        wristPosition += increment;
        wristServo.setPosition(wristPosition);
    }
    public void clawMove(double power) {
        clawServo.setPower(power);
    }
    public void telemetry() {
            myOpMode.telemetry.addLine("--- Claw Subsystem ---");
            myOpMode.telemetry.addData("vertical1Servo:", verticalServo.getPower());
            myOpMode.telemetry.addData("pivot3Servo:", pivotServo.getPosition());
            myOpMode.telemetry.addData("wrist4Servo:", wristServo.getPosition());
            myOpMode.telemetry.addData("claw5Servo:", clawServo.getPower());
    }
}
