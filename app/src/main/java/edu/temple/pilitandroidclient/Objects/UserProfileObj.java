package edu.temple.pilitandroidclient.Objects;

import java.io.Serializable;
import java.util.ArrayList;

import edu.temple.pilitandroidclient.Objects.LEDconfigObj;
import edu.temple.pilitandroidclient.Objects.PiObj;

public class UserProfileObj implements Serializable {

    public String userEmail;
    public ArrayList<PiObj> PiList;
    public ArrayList<LEDconfigObj> savedConfigs;       //For what a user creates a configs OR saves a public commandArray.

    //For use when loading user with PiLits already initialized
    public UserProfileObj(String userEmail, ArrayList<PiObj> piList) {
        this.userEmail = userEmail;
        PiList = piList;
    }

    //For use when user has no PiLits set up
    //For use when loading user with PiLits already initialized
    public UserProfileObj(String userEmail) {
        this.userEmail = userEmail;
        PiList = new ArrayList<PiObj>();
        savedConfigs = new ArrayList<LEDconfigObj>();
    }

    public void addPi (String piAddress, int port, String name){
        int newPiId = PiList.size();                                //index where new configObj will be placed
        PiObj newConfigObj = new PiObj(piAddress,port,name );       //initialize new configObj
        PiList.add(newConfigObj);                                   //add new configObj to list
    }

    public void removePi (int piId) {
        PiList.remove(piId);

        //updates the stripID of each configObj to match it's index in the array
        for(int i = 0; i < PiList.size(); i++){
            PiList.get(i).piId = i;
        }
    }

    //param i: index of LED pi (same as pi Id)
    //returns: PiObj of the LED strip selected
    public PiObj getPiObj(int i){
        return PiList.get(i);
    }


}
