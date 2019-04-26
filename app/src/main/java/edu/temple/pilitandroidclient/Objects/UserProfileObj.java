package edu.temple.pilitandroidclient.Objects;

import java.io.Serializable;
import java.util.ArrayList;

public class UserProfileObj implements Serializable {

    public String userName;
    public ArrayList<PiObj> piList;
    public ArrayList<LEDConfigPattern> configs;
    //------------------------------------------
    public String error;
    public String _id;
    public String password;
    public String email;
    public int _v;


    @Override
    public String toString() {
        return "LEDconfig - userName: " + userName;
    }

    public UserProfileObj(){

    }

    //For use when user has no PiLits set up
    //For use when loading user with PiLits already initialized
    public UserProfileObj(String userName) {
        this.userName = userName;
        piList = new ArrayList<PiObj>();
        //NEW
        configs = new ArrayList<LEDConfigPattern>();
        //OLD configs = new ArrayList<LEDconfigObj>();
    }

    public void addPi (String piAddress, String name){
        //int newPiId = piList.size();                                //index where new configObj will be placed
        PiObj newPiObj = new PiObj(piAddress,name );       //initialize new configObj
        piList.add(newPiObj);                                   //add new configObj to list
    }

}
