// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.assabet.aztechs157.auton;

import java.io.File;
import java.io.IOException;
import org.assabet.aztechs157.DynamicString;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;

/** Add your docs here. */
public class AutonChooser extends DynamicString {

    public static AutonChooser createFromChooser(File directory) throws IOException {
        final AutonList autonList = new AutonList(directory);

        final SendableChooser<String> chooser = new SendableChooser<>();
        chooser.setDefaultOption(autonList.getDefualtAuto(), autonList.getDefualtAuto());
        for (int i = 0; i < autonList.getAutonListSize(); i++) {
            chooser.addOption(autonList.getAutonNameFromIndex(i), autonList.getAutonNameFromIndex(i));
        }
        Shuffleboard.getTab("Autos").add("Auton Choose", chooser);

        return new AutonChooser(chooser);
    }

    private AutonChooser(final SendableChooser<String> chooser) {
        super(chooser::getSelected);
    }

}
