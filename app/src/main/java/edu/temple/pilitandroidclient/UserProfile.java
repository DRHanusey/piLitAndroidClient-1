package edu.temple.pilitandroidclient;

import java.util.ArrayList;

public class UserProfile {

    String userEmail;
    ArrayList<PiObj> PiList;

    public UserProfile(String userEmail, ArrayList<PiObj> piList) {
        this.userEmail = userEmail;
        PiList = piList;
    }
}
