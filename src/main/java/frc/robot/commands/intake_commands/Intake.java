// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.intake_commands;

import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants.IntakeConstants;
import frc.robot.cosmetics.PwmLEDs;
import frc.robot.subsystems.IntakeSystem;

public class Intake extends Command {

    private final IntakeSystem intakeSystem;
    private final PwmLEDs lightSystem;

    /** Creates a new Intake. */
    public Intake(final IntakeSystem intakeSystem, final PwmLEDs lightSystem) {
        // Use addRequirements() here to declare subsystem dependencies.
        addRequirements(intakeSystem);

        this.intakeSystem = intakeSystem;
        this.lightSystem = lightSystem;
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        intakeSystem.set(-IntakeConstants.INTAKE_SPEED);
        lightSystem.setClimb(Color.kOrange, Color.kBlack, 3, 2, 2);
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        intakeSystem.set(0);

        if (!interrupted) {
            intakeSystem.hasNote = true;
            lightSystem.setSolid(Color.kOrange);
        } else {
            lightSystem.setDefault();
        }
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return intakeSystem.checkForNote();
    }
}
