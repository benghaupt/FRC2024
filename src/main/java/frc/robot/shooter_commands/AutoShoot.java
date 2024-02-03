// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.shooter_commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.inputs.Inputs;
import frc.robot.subsystems.ShooterSystem;
import frc.robot.subsystems.VisionSystem;

public class AutoShoot extends Command {
    private final ShooterSystem shooter;
    private final Inputs inputs;
    private final VisionSystem vision;

    /** Creates a new Shoot. */
    public AutoShoot(final ShooterSystem shooter, final Inputs inputs, final VisionSystem vision) {
        // Use addRequirements() here to declare subsystem dependencies.
        addRequirements(shooter);

        this.shooter = shooter;
        this.inputs = inputs;
        this.vision = vision;
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        // calculate the apropreate velocity of the shooter motors based on data from
        // the vision subsystem

        // shooter.set(velocity);
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return false;
    }
}