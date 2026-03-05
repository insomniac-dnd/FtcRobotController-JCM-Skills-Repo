package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.eventloop.opmode.Telemetry;
import com.qualcomm.robotcore.util.Range;

public class Claw {
    private final Servo pivotServo;
    private final Servo wristServo;
    private final Servo clawServo;

    private double pivotPos = 0.5;
    private double wristPos = 0.5;
    private double clawPos = 0.2;
    private final double increment = 0.05;

    public Claw(HardwareMap hardwareMap) {
        pivotServo = hardwareMap.get(Servo.class, "pivot3Servo");
        wristServo = hardwareMap.get(Servo.class, "wrist4Servo");
        clawServo = hardwareMap.get(Servo.class, "claw5Servo");
    }

    public void update(Gamepad gamepad) {
        // Fine adjustments
        if (gamepad.dpad_down) pivotPos += increment;
        if (gamepad.dpad_up)   pivotPos -= increment;
        if (gamepad.dpad_left) wristPos -= increment;
        if (gamepad.dpad_right) wristPos += increment;

        // Claw open/close
        clawPos = Range.clip(gamepad.right_trigger * 0.65, 0.2, 0.5);

        // Clip ranges
        pivotPos = Range.clip(pivotPos, 0.0, 1.0);
        wristPos = Range.clip(wristPos, 0.0, 1.0);

        // Set servo positions
        pivotServo.setPosition(pivotPos);
        wristServo.setPosition(wristPos);
        clawServo.setPosition(clawPos);
    }

    public void addTelemetry(Telemetry telemetry) {
        telemetry.addData("pivot3Servo", pivotPos);
        telemetry.addData("wrist4Servo", wristPos);
        telemetry.addData("claw5Servo", clawPos);
    }
}
