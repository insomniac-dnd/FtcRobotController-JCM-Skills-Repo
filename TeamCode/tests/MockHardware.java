package org.firstinspires.ftc.teamcode.tests;

import com.qualcomm.robotcore.hardware.*;
import java.util.HashMap;

public class MockHardware extends HardwareMap {
    private final HashMap<String, Object> devices = new HashMap<>();

    public MockHardware() {
        super(null, null);
    }

    public void put(String name, Object device) {
        devices.put(name, device);
    }

    @Override
    public <T> T get(Class<? extends T> classOrInterface, String deviceName) {
        Object device = devices.get(deviceName);
        if (device == null) throw new IllegalArgumentException("No mock device: " + deviceName);
        return classOrInterface.cast(device);
    }

    // Simple mock servo class
    public static class MockServo implements Servo {
        private double position = 0.0;
        @Override public void setPosition(double pos) { this.position = pos; }
        @Override public double getPosition() { return position; }
        @Override public void scaleRange(double min, double max) {}
        @Override public void resetDeviceConfigurationForOpMode() {}
        @Override public Manufacturer getManufacturer() { return null; }
        @Override public String getDeviceName() { return "MockServo"; }
        @Override public String getConnectionInfo() { return "MockConnection"; }
        @Override public int getVersion() { return 1; }
        @Override public void close() {}
    }

    // Simple mock CRServo
    public static class MockCRServo implements CRServo {
        private double power = 0.0;
        @Override public void setPower(double p) { power = p; }
        @Override public double getPower() { return power; }
        @Override public Direction getDirection() { return Direction.FORWARD; }
        @Override public void setDirection(Direction direction) {}
        @Override public void resetDeviceConfigurationForOpMode() {}
        @Override public Manufacturer getManufacturer() { return null; }
        @Override public String getDeviceName() { return "MockCRServo"; }
        @Override public String getConnectionInfo() { return "MockConnection"; }
        @Override public int getVersion() { return 1; }
        @Override public void close() {}
    }

    // Simple mock motor
    public static class MockMotor extends DcMotorImplEx {
        private double power = 0.0;
        public MockMotor() { super(null); }
        @Override public void setPower(double p) { this.power = p; }
        @Override public double getPower() { return power; }
    }
}
