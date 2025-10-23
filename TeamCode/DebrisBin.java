package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.eventloop.opmode.Telemetry;
import com.qualcomm.robotcore.util.Range;

public class DebrisBin {
    private final Servo binServo;

    private double binPos = 0.0;

    public Claw(HardwareMap hardwareMap) {
        binServo = hardwareMap.get(Servo.class, "bin2Servo");
    }

    public void update(Gamepad gamepad) {
        // Fine adjustments
        if (gamepad.a) {
            binPos = 0.5;
        } else {
            binPos = 0.0;
        }

        binPos = Range.clip(binPos, 0.0, 1.0);

        // Set servo positions
        binServo.setPosition(binPos);
    }

    public void addTelemetry(Telemetry telemetry) {
        telemetry.addData("bin2Servo", binPos);
    }
}
