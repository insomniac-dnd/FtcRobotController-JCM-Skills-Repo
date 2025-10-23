package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "ECUClassTesting", group = "FTCROBOTCONTROLLER-JCM-SKILLS-REPO")
public class ECUClassTesting extends LinearOpMode {

    private DriveTrain driveTrain;
    private ClawLift clawLift;
    private Claw claw;
//    private DebrisBin debrisBin;

    @Override
    public void runOpMode() throws InterruptedException {
        // Initialize subsystems
        driveTrain = new DriveTrain(hardwareMap);
        clawLift = new ClawLift(hardwareMap);
        claw = new Claw(hardwareMap);
//        debrisBin = new DebrisBin(hardwareMap);
        waitForStart();

        while (opModeIsActive()) {
            // Each subsystem handles its own logic
            driveTrain.drive(gamepad1);
            clawLift.update(gamepad1);
            claw.update(gamepad1);
//            bin.update(gamepad1);

            // Telemetry
            telemetry.addLine("--- SqrECUBot Telemetry ---");
            driveTrain.addTelemetry(telemetry);
            clawLift.addTelemetry(telemetry);
            claw.addTelemetry(telemetry);
//            bin.addTelemetry(telemetry);
            telemetry.update();

            sleep(20);
        }
    }
}
