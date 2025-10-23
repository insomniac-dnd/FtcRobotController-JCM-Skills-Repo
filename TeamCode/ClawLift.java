package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.eventloop.opmode.Telemetry;

public class ClawLift {
    private final CRServo verticalServo;
    private double power1 = 1.0;
    private double power0 = 0.0;
    private double servoPower = 0.0;

    public ClawLift(HardwareMap hardwareMap) {
        verticalServo = hardwareMap.get(CRServo.class, "vertical1Servo");
    }

    public void update(Gamepad gamepad) {
        if (gamepad.y) {
            servoPower = power1; // raise
        } else if (gamepad.x) {
            servoPower = -power1; // lower
        } else {
            servoPower = power0; // stop
        }
        verticalServo.setPower(servoPower);
    }

    public void addTelemetry(Telemetry telemetry) {
        telemetry.addData("vertical1Servo Power", servoPower);
    }
}
