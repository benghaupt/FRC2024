// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.shooter_commands;

import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.cosmetics.PwmLEDs;
import frc.robot.subsystems.ShooterSystem;

public class SpinUpShooter extends Command {

    private final ShooterSystem shooterSystem;
    private final PwmLEDs lightSystem;
    private final double setPoint;

    /** Creates a new StartShooter. */
    public SpinUpShooter(final ShooterSystem shooterSystem, final PwmLEDs lightSystem, final double setPoint) {
        // Use addRequirements() here to declare subsystem dependencies.
        addRequirements(shooterSystem);

        this.shooterSystem = shooterSystem;
        this.lightSystem = lightSystem;
        this.setPoint = setPoint;
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        lightSystem.setClimb(Color.kRed, Color.kBlack, 1, 9, 2);
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        int colorLength = (int) Math.floor(10 * shooterSystem.getLeftEncoderVelocity() / setPoint);

        shooterSystem.currentLeftMotorSet += shooterSystem.leftMotorPID(setPoint);
        shooterSystem.currentRightMotorSet += shooterSystem.rightMotorPID(setPoint);
        shooterSystem.setLeftMotor(shooterSystem.currentLeftMotorSet);
        shooterSystem.setRightMotor(shooterSystem.currentRightMotorSet);

        lightSystem.setColor1Length(colorLength);
        lightSystem.setColor2Length(10 - colorLength);
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        if (interrupted) {
            shooterSystem.setLeftMotor(0);
            shooterSystem.setRightMotor(0);
            lightSystem.setDefault();
        }
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return false;
    }
}
