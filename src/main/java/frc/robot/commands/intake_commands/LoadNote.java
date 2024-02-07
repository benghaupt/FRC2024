// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.intake_commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants.IntakeConstants;
import frc.robot.subsystems.IntakeSystem;

public class LoadNote extends Command {

    private IntakeSystem intakeSystem;

    /** Creates a new LoadNote. */
    public LoadNote(final IntakeSystem intakeSystem) {
        // Use addRequirements() here to declare subsystem dependencies.
        addRequirements(intakeSystem);

        this.intakeSystem = intakeSystem;
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        intakeSystem.set(IntakeConstants.INTAKE_SPEED * 0.05);
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        intakeSystem.set(0);
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return !(intakeSystem.checkForNote());
    }
}