package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@TeleOp(name = "SingleMotor", group = "Robot")
public class SingleMotor extends LinearOpMode {
    private DcMotor motor;
    @Override
    public void runOpMode() throws InterruptedException {
        waitforStart();
        while (opModeIsActive()) {
                motor.setPower(speed);
            sleep(20);
        }
    }
}