// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.shooter_commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj.Timer;
import frc.robot.Constants.IntakeConstants;
import frc.robot.Constants.ShooterConstants;
import frc.robot.subsystems.IntakeSystem;
import frc.robot.subsystems.ShooterSystem;

public class ManualShoot extends Command {

    private final ShooterSystem shooterSystem;
    private final IntakeSystem intakeSystem;
    private final double setPoint;
    private boolean seenNote = false;
    private Timer timer = new Timer();

    /** Creates a new Shoot. */
    public ManualShoot(final ShooterSystem shooterSystem, final IntakeSystem intakeSystem, final double setPoint) {
        // Use addRequirements() here to declare subsystem dependencies.
        addRequirements(intakeSystem);

        this.setPoint = setPoint;
        this.shooterSystem = shooterSystem;
        this.intakeSystem = intakeSystem;
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        intakeSystem.set(-IntakeConstants.INTAKE_SPEED * 1.25);
        timer.reset();
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        shooterSystem.currentLeftMotorSet += shooterSystem.leftMotorPID(this.setPoint);
        shooterSystem.currentRightMotorSet += shooterSystem.rightMotorPID(this.setPoint);
        shooterSystem.setLeftMotor(shooterSystem.currentLeftMotorSet);
        shooterSystem.setRightMotor(shooterSystem.currentRightMotorSet);

        if (intakeSystem.checkForNote()) {
            seenNote = true;
        }
        if (seenNote && !(intakeSystem.checkForNote())) {
            timer.start();
        }
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        shooterSystem.currentLeftMotorSet = 0;
        shooterSystem.currentRightMotorSet = 0;
        shooterSystem.setMotors(0);
        intakeSystem.set(0);
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {

        return timer.hasElapsed(ShooterConstants.SHOOTER_SENSOR_WAIT_TIME);
    }
}
