package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.rev.Rev9AxisImuOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;

@TeleOp(name = "imuDebugger", group = "Utilities")
public class imuDebugger extends LinearOpMode {

    private IMU imu;

    @Override
    public void runOpMode() {
        // Initialize IMU
        imu = hardwareMap.get(IMU.class, "imu");

        // Using the same Orientation logic as your Auto
        Orientation hubRotation = new Orientation(
                AxesReference.INTRINSIC,
                AxesOrder.XYZ,
                AngleUnit.DEGREES,
                0, 0, 0, 0
        );

        Rev9AxisImuOrientationOnRobot orientationOnRobot =
                new Rev9AxisImuOrientationOnRobot(hubRotation);

        imu.initialize(new IMU.Parameters(orientationOnRobot));

        telemetry.addLine("IMU Initialized.");
        telemetry.addLine("Press START to see Heading (Yaw).");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {
            // Get the specific angles
            YawPitchRollAngles angles = imu.getRobotYawPitchRollAngles();

            telemetry.addData("HEADING (Yaw)", "%.2f Deg", angles.getYaw(AngleUnit.DEGREES));
            telemetry.addData("PITCH", "%.2f Deg", angles.getPitch(AngleUnit.DEGREES));
            telemetry.addData("ROLL", "%.2f Deg", angles.getRoll(AngleUnit.DEGREES));

            telemetry.addLine("\n--- Calibration Check ---");
            telemetry.addLine("1. Point robot forward.");
            telemetry.addLine("2. Turn 90 deg RIGHT. Yaw should be ~ -90.");
            telemetry.addLine("3. Turn 90 deg LEFT. Yaw should be ~ 90.");

            telemetry.update();
        }
    }
}