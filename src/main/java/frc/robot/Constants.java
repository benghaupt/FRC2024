package frc.robot;

import com.pathplanner.lib.util.PIDConstants;
import com.revrobotics.CANSparkBase.IdleMode;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.math.util.Units;
import swervelib.math.Matter;

public class Constants {

    public static class ControllerConstants {

        public static final int DRIVER_CONTROLLER_PORT = 0;
        public static final int OPERATOR_CONTROLLER_PORT = 1;

        // Joystick Deadband
        public static final double LEFT_X_DEADBAND = 0.1;
        public static final double LEFT_Y_DEADBAND = 0.1;
        public static final double RIGHT_X_DEADBAND = 0.1;
        public static final double TURN_CONSTANT = 6; // TODO: figure out if this is a good value

    }

    public static class DriveConstants {

        public static final boolean FIELD_ORIENTED_ENABLED = true;

        public static final IdleMode DRIVE_IDLE_MODE = IdleMode.kBrake;
        public static final IdleMode ANGLE_IDLE_MODE = IdleMode.kCoast;

        // Hold time on motor brakes when disabled
        public static final double WHEEL_LOCK_TIME = 10; // seconds

        // TODO: find proper values for the following three
        public static final double ROBOT_MASS = (111) * 0.453592; // lbs * kg per pound
        public static final Matter CHASSIS = new Matter(new Translation3d(0, 0, Units.inchesToMeters(8)), ROBOT_MASS);
        public static final double LOOP_TIME = 0.13; // s, 20ms + 110ms spark max velocity lag

        public static final double WHEEL_DIAMETER = 4;
        public static final double DRIVE_GEAR_RATIO = 6.75;
        public static final double ANGLE_GEAR_RATIO = 12.8; // This is the Gear Ratio of the motor encoder. Not
                                                            // absolute.
        public static final double ANGLE_PULSE_PER_ROTATION = 42; // This is the pulse/rev of the motor encoder. Not
                                                                  // absolute.
        public static final double MAX_SPEED = 17.3; // set to 17.3 ft per second
        public static final double MAX_TRANSLATIONAL_ACCELERATION = 4.0; // in meters per second squared
        public static final double MAX_ANGLULAR_ACCELERATION = 720; // in degrees per second squared

        public record RobotProperties(
                double drivePulsePerRotation) {
            public static final RobotProperties ALPHA = new RobotProperties(42);
            public static final RobotProperties Beta = new RobotProperties(7168);
        }

        public record XboxSpeeds(
                double drive,
                double slowDrive) {
            public static final XboxSpeeds COMPETITION = new XboxSpeeds(1, 0.5);
            public static final XboxSpeeds DEMO = new XboxSpeeds(0.3, 0.3);
        }

        public static final int POSE_ESTIMATE_FREQUENCY = 100;

    }

    public static class AutonConstants {

        public static final PIDConstants TRANSLATION_PID = new PIDConstants(0.7, 0, 0);
        public static final PIDConstants ANGLE_PID = new PIDConstants(0.004, 0, 1.5);

        public static final double MAX_MODULE_SPEED = 3;

    }

    public static class VisionConstants {

        public static final String CAMERA_NICKNAME = ""; // TODO: find proper value
        public static final Transform3d CAMERA_PLACEMENT = new Transform3d(); // TODO: find proper value

    }

    public static class IntakeConstants {

        public static final int INTAKE_MOTOR_ID = 21;
        public static final int NOTE_SENSOR_CHANNEL = 0;

        public static final IdleMode INTAKE_MOTOR_IDLE_MODE = IdleMode.kBrake;
        public static final double INTAKE_SPEED = 1;
        public static final double FEED_SPEED = 0.625;
        public static final double LOAD_SPEED = 0.25;

    }

    public static class ShooterConstants {

        public static final int SHOOTER_MOTOR_LEFT_ID = 31;
        public static final int SHOOTER_MOTOR_RIGHT_ID = 32;

        public static final IdleMode SHOOTER_MOTOR_IDLE_MODE = IdleMode.kBrake;

        public static final double SHOOTER_TARGET_RPM_HIGH = 5250; // max RPM of neo is 5676
        public static final double SHOOTER_TARGET_RPM_LOW = 1250; // max RPM of neo is 5676
        public static final double SHOOTER_TARGET_RPM_PASS = 750; // max RPM of neo is 5676
        public static final double SHOOTER_TARGET_RPM_EJECT = 2750; // max RPM of neo is 5676

        public static final double SHOOTER_START_VELOCITY_TOLERANCE = 50; // in RPM as well
        public static final double SHOOTER_SENSOR_WAIT_TIME = 0.5; // in seconds

        public static final PIDController ALPHA_SHOOTER_MOTOR_PID_LEFT = new PIDController(0.00001, 0, 0.000001);
        public static final PIDController ALPHA_SHOOTER_MOTOR_PID_RIGHT = new PIDController(0.00001, 0, 0.000001);

        public static final PIDController BETA_SHOOTER_MOTOR_PID_LEFT = new PIDController(0.00001, 0, 0.000001);
        public static final PIDController BETA_SHOOTER_MOTOR_PID_RIGHT = new PIDController(0.00001, 0, 0.000001);

    }

    public static class HangerConstants {

        public static final int HANGER_MOTOR_LEFT_ID = 41;
        public static final int HANGER_MOTOR_RIGHT_ID = 42;

        public static final int HANGER_LEFT_EXT_LIMIT_ID = 2;
        public static final int HANGER_LEFT_RET_LIMIT_ID = 3;
        public static final int HANGER_RIGHT_EXT_LIMIT_ID = 4;
        public static final int HANGER_RIGHT_RET_LIMIT_ID = 5;

        public static final double LIFT_EXTEND_SPEED = 0.75;
        public static final double LIFT_RETRACT_SPEED = 1;

    }

    public static class PneumaticsConstants {

        public static final int PNEUMATICS_HUB_ID = 52;

        public static final int INTAKE_SOLENOID_RETRACT_CHANNEL = 14;
        public static final int INTAKE_SOLENOID_EXTEND_CHANNEL = 15;

        public static final int DEFLECTOR_LEFT_SOLENOID_RETRACT_CHANNEL = 10;
        public static final int DEFLECTOR_LEFT_SOLENOID_EXTEND_CHANNEL = 11;

        public static final int DEFLECTOR_RIGHT_SOLENOID_RETRACT_CHANNEL = 12;
        public static final int DEFLECTOR_RIGHT_SOLENOID_EXTEND_CHANNEL = 13;

        public static final int HANGER_PIN_SOLENOID_RETRACT_CHANNEL = 0;
        public static final int HANGER_PIN_SOLENOID_EXTEND_CHANNEL = 1;

    }

    public static class CosmeticConstants {

        public static final int LIGHT_ID = 9;
        public static final double SOLID_YELLOW_VALUE = 0.69;
        public static final double SOLID_PURPLE_VALUE = 0.91;
        public static final int LIGHT_LENGTH = 76;

    }

}
// This has to be here
