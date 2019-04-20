package edu.temple.pilitandroidclient.Objects;

import java.io.Serializable;
import java.util.ArrayList;

public class UserProfileObj implements Serializable {

    public String userName;
    public ArrayList<PiObj> PiList;
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

    //For use when loading user with PiLits already initialized
    public UserProfileObj(String userName, ArrayList<PiObj> piList) {
        this.userName = userName;
        PiList = piList;
    }

    //For use when user has no PiLits set up
    //For use when loading user with PiLits already initialized
    public UserProfileObj(String userName) {
        this.userName = userName;
        PiList = new ArrayList<PiObj>();
        //NEW
        configs = new ArrayList<LEDConfigPattern>();
        //OLD configs = new ArrayList<LEDconfigObj>();
    }

    public void addPi (String piAddress, String name){
        //int newPiId = PiList.size();                                //index where new configObj will be placed
        PiObj newPiObj = new PiObj(piAddress,name );       //initialize new configObj
        PiList.add(newPiObj);                                   //add new configObj to list
    }


    //param i: index of LED pi (same as pi Id)
    //returns: PiObj of the LED strip selected
    public PiObj getPiObj(int i){
        return PiList.get(i);
    }


}
