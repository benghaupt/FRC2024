// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.assabet.aztechs157.auton;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import com.fasterxml.jackson.databind.ObjectMapper;

/** Add your docs here. */
public class AutonList {

    private static final HashMap<Integer, String> autos = new HashMap<>();
    public static AutonListJson autonListJson;
    public static String defualtAutoName;

    public AutonList(File directory) throws IOException {
        checkDirectory(directory);
        autonListJson = new ObjectMapper()
                .readValue(new File(directory, "autonlist.json"), AutonListJson.class);

        defualtAutoName = autonListJson.defaultAuto;

        for (int i = 0; i < autonListJson.otherAutos.length; i++) {
            autos.put(i, autonListJson.otherAutos[i]);
        }
    }

    private void checkDirectory(File directory) {
        assert new File(directory, "autonlist.json").exists();
        for (int i = 0; i < autonListJson.otherAutos.length; i++) {
            assert new File(directory, autos.get(i)).exists();
        }
    }

    public String getDefualtAuto() {
        return defualtAutoName;
    }

    public int getAutonListSize() {
        return autos.size();
    }

    public String getAutonNameFromIndex(int index) {
        return autos.get(index);
    }

}
