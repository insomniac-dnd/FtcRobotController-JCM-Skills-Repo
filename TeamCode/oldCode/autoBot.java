package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;

import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvWebcam;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

@Autonomous(name = "Autobot", group = "Robot")
public class Autobot extends LinearOpMode {

    private DcMotor frontLeft, frontRight, backLeft, backRight;

    OpenCvWebcam webcam;
    RedDetectionPipeline pipeline;

    @Override
    public void runOpMode() throws InterruptedException {

        // Initialize motors
        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        backLeft = hardwareMap.get(DcMotor.class, "backLeft");
        backRight = hardwareMap.get(DcMotor.class, "backRight");

        frontRight.setDirection(DcMotor.Direction.REVERSE);
        backRight.setDirection(DcMotor.Direction.REVERSE);

        // Setup webcam
        int cameraMonitorViewId = hardwareMap.appContext.getResources()
                .getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        webcam = OpenCvCameraFactory.getInstance().createWebcam(
                hardwareMap.get(WebcamName.class, "Webcam 1"), cameraMonitorViewId);

        pipeline = new RedDetectionPipeline();
        webcam.setPipeline(pipeline);

        webcam.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
            @Override
            public void onOpened() {
                webcam.startStreaming(320, 240, OpenCvCameraRotation.UPRIGHT);
            }

            @Override
            public void onError(int errorCode) {
                telemetry.addData("Camera error", errorCode);
                telemetry.update();
            }
        });

        telemetry.addLine("Waiting for start...");
        telemetry.update();
        waitForStart();

        while (opModeIsActive()) {
            double x = pipeline.getRedX();
            double size = pipeline.getRedSize();
            double error = x - 160; // 320px width, center is 160

            double forwardPower = 0.3;
            double turnPower = error / 160.0; // Normalize: -1 to 1

            if (x != -1 && size > 1000) {
                // Apply proportional steering toward the target
                double leftPower = forwardPower + turnPower;
                double rightPower = forwardPower - turnPower;

                // Clamp power
                leftPower = Math.max(-1.0, Math.min(1.0, leftPower));
                rightPower = Math.max(-1.0, Math.min(1.0, rightPower));

                frontLeft.setPower(leftPower);
                backLeft.setPower(leftPower);
                frontRight.setPower(rightPower);
                backRight.setPower(rightPower);
            } else {
                // Stop if no valid red target is detected
                frontLeft.setPower(0);
                backLeft.setPower(0);
                frontRight.setPower(0);
                backRight.setPower(0);
            }

            telemetry.addData("Red X", x);
            telemetry.addData("Red Area", size);
            telemetry.addLine(size > 1000);
            telemetry.update();

            sleep(30);
        }
    }

    // --------- Deep Red Detection Pipeline ---------
    class RedDetectionPipeline extends org.openftc.easyopencv.OpenCvPipeline {

        private volatile double redX = -1;
        private volatile double redSize = 0;

        private static final double MIN_RED_AREA = 1000.0;

        @Override
        public Mat processFrame(Mat input) {
            Mat hsv = new Mat();
            Imgproc.cvtColor(input, hsv, Imgproc.COLOR_RGB2HSV);

            // Filter for deep red only
            Mat lowerRed = new Mat();
            Mat upperRed = new Mat();
            Core.inRange(hsv, new Scalar(0, 150, 100), new Scalar(5, 255, 255), lowerRed);
            Core.inRange(hsv, new Scalar(175, 150, 100), new Scalar(180, 255, 255), upperRed);

            Mat redMask = new Mat();
            Core.bitwise_or(lowerRed, upperRed, redMask);

            // Morphological operations to clean up noise
            Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new org.opencv.core.Size(5, 5));
            Imgproc.morphologyEx(redMask, redMask, Imgproc.MORPH_CLOSE, kernel);
            Imgproc.morphologyEx(redMask, redMask, Imgproc.MORPH_OPEN, kernel);

            // Find contours
            List<org.opencv.core.MatOfPoint> contours = new ArrayList<>();
            Mat hierarchy = new Mat();
            Imgproc.findContours(redMask, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

            double maxArea = 0;
            Rect maxRect = null;

            for (org.opencv.core.MatOfPoint contour : contours) {
                Rect rect = Imgproc.boundingRect(contour);
                double area = rect.area();
                if (area > maxArea && area > MIN_RED_AREA) {
                    maxArea = area;
                    maxRect = rect;
                }
            }

            if (maxRect != null) {
                Imgproc.rectangle(input, maxRect, new Scalar(0, 255, 0), 2);
                redX = maxRect.x + maxRect.width / 2.0;
                redSize = maxRect.area();
            } else {
                redX = -1;
                redSize = 0;
            }

            return input;
        }

        public double getRedX() {
            return redX;
        }

        public double getRedSize() {
            return redSize;
        }
    }
}
