// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.Constants.ControllerConstants;
import frc.robot.Constants.ShooterConstants;
import frc.robot.commands.drive_commands.drivebase.AbsoluteDriveAdv;
import frc.robot.commands.hanger_commands.ExtendHangerPin;
import frc.robot.commands.hanger_commands.LiftHanger;
import frc.robot.commands.hanger_commands.RetractHanger;
import frc.robot.commands.hanger_commands.RetractHangerPin;
import frc.robot.commands.intake_commands.Intake;
import frc.robot.commands.intake_commands.LoadNote;
import frc.robot.commands.shooter_commands.Shoot;
import frc.robot.commands.shooter_commands.StartShooter;
import frc.robot.cosmetics.PwmLEDs;
import frc.robot.inputs.Inputs;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.HangerSystem;
import frc.robot.subsystems.IntakeSystem;
import frc.robot.subsystems.PneumaticsSystem;
import frc.robot.subsystems.ShooterSystem;
import frc.robot.subsystems.VisionSystem;
import java.io.File;
import com.pathplanner.lib.auto.NamedCommands;

/**
 * This class is where the bulk of the robot should be declared. Since
 * Command-based is a "declarative" paradigm, very
 * little robot logic should actually be handled in the {@link Robot} periodic
 * methods (other than the scheduler calls).
 * Instead, the structure of the robot (including subsystems, commands, and
 * trigger mappings) should be declared here.
 */
public class RobotContainer {

    // The robot's subsystems and commands are defined here...
    private final DriveSubsystem drivebase = new DriveSubsystem(new File(Filesystem.getDeployDirectory(),
            "swerve"));
    private final Inputs inputs = Inputs.createFromChooser();
    private final PneumaticsSystem pneumaticsSystem = new PneumaticsSystem();
    private final IntakeSystem intakeSystem = new IntakeSystem();
    private final ShooterSystem shooterSystem = new ShooterSystem();
    private final HangerSystem hangerSystem = new HangerSystem();
    public final VisionSystem visionSystem = new VisionSystem();
    public final PwmLEDs lightSystem = new PwmLEDs();
    private final SendableChooser<Command> autoChooser = new SendableChooser<>();

    XboxController driverXbox = new XboxController(0);

    public Command intakeCommand() {
        return new Intake(intakeSystem, lightSystem)
                .alongWith(pneumaticsSystem.setIntakeFoward())
                .andThen(new LoadNote(intakeSystem, lightSystem).alongWith(pneumaticsSystem.setIntakeReverse()))
                .finallyDo(pneumaticsSystem.setIntakeReverse()::initialize);
    }

    public Command highShootCommand() {
        return new StartShooter(shooterSystem, lightSystem, ShooterConstants.SHOOTER_TARGET_RPM_HIGH)
                .andThen(new Shoot(shooterSystem, intakeSystem, lightSystem, ShooterConstants.SHOOTER_TARGET_RPM_HIGH));
    }

    public Command lowShootCommand() {
        return pneumaticsSystem.setDeflectorFoward()
                .andThen(new StartShooter(shooterSystem, lightSystem, ShooterConstants.SHOOTER_TARGET_RPM_LOW))
                .andThen(new Shoot(shooterSystem, intakeSystem, lightSystem, ShooterConstants.SHOOTER_TARGET_RPM_LOW))
                .andThen(pneumaticsSystem.setDeflectorReverse())
                .finallyDo(pneumaticsSystem.setDeflectorReverse()::initialize);
    }

    /**
     * The container for the robot. Contains subsystems, OI devices, and commands.
     */
    public RobotContainer() {

        // Register Named Commands
        NamedCommands.registerCommand("Intake", intakeCommand().withTimeout(4));

        NamedCommands.registerCommand("High_Shoot", highShootCommand().withTimeout(4));

        NamedCommands.registerCommand("Low_Shoot", lowShootCommand().withTimeout(4));

        autoChooser.setDefaultOption("Everything is Broken, Do Nothing",
                drivebase.getAutonomousCommand("Nothing_Auto"));
        autoChooser.addOption("Simple Auto From Center", drivebase.getAutonomousCommand("Simple_Center_Auto"));
        autoChooser.addOption("Auto From Center", drivebase.getAutonomousCommand("Center_Auto"));
        autoChooser.addOption("Simple Auto From Right", drivebase.getAutonomousCommand("Simple_Right_Auto"));
        autoChooser.addOption("Auto From Right", drivebase.getAutonomousCommand("Right_Auto"));
        autoChooser.addOption("Simple Auto From Left", drivebase.getAutonomousCommand("Simple_Left_Auto"));

        autoChooser.onChange((command) -> {
            System.out.println("Autonomous routine changed to: " + command.getName());
        });

        Shuffleboard.getTab("Driver").add(autoChooser);

        // Configure the trigger bindings
        configureBindings();

        AbsoluteDriveAdv closedAbsoluteDriveAdv = new AbsoluteDriveAdv(drivebase,
                () -> MathUtil.applyDeadband(driverXbox.getLeftY(),
                        ControllerConstants.LEFT_Y_DEADBAND),
                () -> MathUtil.applyDeadband(driverXbox.getLeftX(),
                        ControllerConstants.LEFT_X_DEADBAND),
                () -> MathUtil.applyDeadband(driverXbox.getRightX(),
                        ControllerConstants.RIGHT_X_DEADBAND),
                driverXbox::getYButtonPressed,
                driverXbox::getAButtonPressed,
                driverXbox::getXButtonPressed,
                driverXbox::getBButtonPressed);

        // Applies deadbands and inverts controls because joysticks
        // are back-right positive while robot
        // controls are front-left positive
        // left stick controls translation
        // right stick controls the desired angle NOT angular rotation
        Command driveFieldOrientedDirectAngle = drivebase.driveCommand(
                () -> MathUtil.applyDeadband(driverXbox.getLeftY(), ControllerConstants.LEFT_Y_DEADBAND),
                () -> MathUtil.applyDeadband(driverXbox.getLeftX(), ControllerConstants.LEFT_X_DEADBAND),
                () -> driverXbox.getRightX(),
                () -> driverXbox.getRightY());

        // Applies deadbands and inverts controls because joysticks
        // are back-right positive while robot
        // controls are front-left positive
        // left stick controls translation
        // right stick controls the angular velocity of the robot
        Command driveFieldOrientedAnglularVelocity = drivebase.driveCommand(
                () -> MathUtil.applyDeadband(-driverXbox.getLeftY(), ControllerConstants.LEFT_Y_DEADBAND),
                () -> MathUtil.applyDeadband(-driverXbox.getLeftX(), ControllerConstants.LEFT_X_DEADBAND),
                () -> -driverXbox.getRightX());

        Command driveFieldOrientedDirectAngleSim = drivebase.simDriveCommand(
                () -> MathUtil.applyDeadband(-driverXbox.getLeftY(), ControllerConstants.LEFT_Y_DEADBAND),
                () -> MathUtil.applyDeadband(-driverXbox.getLeftX(), ControllerConstants.LEFT_X_DEADBAND),
                () -> -driverXbox.getRightX());

        drivebase.setDefaultCommand(
                !RobotBase.isSimulation() ? driveFieldOrientedAnglularVelocity : driveFieldOrientedDirectAngleSim);
    }

    /**
     * Use this method to define your trigger->command mappings. Triggers can be
     * created via the
     * {@link Trigger#Trigger(java.util.function.BooleanSupplier)} constructor with
     * an arbitrary predicate, or via the
     * named factories in
     * {@link edu.wpi.first.wpilibj2.command.button.CommandGenericHID}'s subclasses
     * for
     * {@link CommandXboxController
     * Xbox}/{@link edu.wpi.first.wpilibj2.command.button.CommandPS4Controller PS4}
     * controllers or {@link edu.wpi.first.wpilibj2.command.button.CommandJoystick
     * Flight joysticks}.
     */
    private void configureBindings() {
        // Schedule `ExampleCommand` when `exampleCondition` changes to `true`

        /*
         * new JoystickButton(driverXbox, 1).onTrue((new
         * InstantCommand(drivebase::zeroGyro)));
         * new JoystickButton(driverXbox, 3).onTrue(new
         * InstantCommand(drivebase::addFakeVisionReading));
         * new JoystickButton(driverXbox,
         * 2).whileTrue(
         * Commands.deferredProxy(() -> drivebase.driveToPose(
         * new Pose2d(new Translation2d(4, 4), Rotation2d.fromDegrees(0)))));
         * // new JoystickButton(driverXbox, 3).whileTrue(new RepeatCommand(new
         * // InstantCommand(drivebase::lock, drivebase)));
         */

        inputs.button(Inputs.intake).toggleWhenPressed(intakeCommand());

        inputs.button(Inputs.loadNote).whenPressed(new LoadNote(intakeSystem, lightSystem));

        inputs.button(Inputs.highShot).toggleWhenPressed(highShootCommand());

        inputs.button(Inputs.lowShot).toggleWhenPressed(lowShootCommand());

        inputs.button(Inputs.liftHanger).toggleWhenPressed(
                new LiftHanger(hangerSystem, lightSystem)
                        .handleInterrupt(() -> new RetractHanger(hangerSystem, lightSystem).schedule()));

        inputs.button(Inputs.retractHanger).toggleWhenPressed(
                (new RetractHanger(hangerSystem, lightSystem).andThen(new ExtendHangerPin(pneumaticsSystem)))
                        .handleInterrupt(() -> new LiftHanger(hangerSystem, lightSystem).schedule()));

        inputs.button(Inputs.retractHangerPin).whenPressed(new RetractHangerPin(pneumaticsSystem));

    }

    /**
     * Use this to pass the autonomous command to the main {@link Robot} class.
     *
     * @return the command to run in autonomous
     */
    public Command getAutonomousCommand() {
        // An example command will be run in autonomous
        return autoChooser.getSelected();
    }

    public void setDriveMode() {
        // drivebase.setDefaultCommand();
    }

    public void setMotorBrake(boolean brake) {
        drivebase.setMotorBrake(brake);
    }
}
