package edu.temple.pilitandroidclient.Objects;

import java.io.Serializable;
import java.util.ArrayList;

public class PiObj implements Serializable {

    public String piName;
    public String userName;

    public PiObj(String piName, String userName) {
        this.piName = piName;
        this.userName = userName;
    }

    //Must override so the name is displayed in drop down on User screen
    @Override
    public String toString() {
        return piName;
    }
}
