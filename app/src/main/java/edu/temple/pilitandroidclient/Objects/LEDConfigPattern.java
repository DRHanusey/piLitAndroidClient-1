package edu.temple.pilitandroidclient.Objects;

import com.google.gson.Gson;

import java.util.ArrayList;

public class LEDConfigPattern {
    String description;                     //description of pattern, e.g. party lights, sleep lights, etc.
    public ArrayList<Command> commandArray;        //array of commands that make up the configuration pattern
    public ArrayList<Timestamp> allTimestamps;

    public LEDConfigPattern(String description) {
        this.description = description;
        this.commandArray = new ArrayList<Command>();
    }

    public void createTimestampArray(){
        Gson gson = new Gson();
        String jsonCmdStr = gson.toJson(this.commandArray);
        System.out.println("GSON COMMAND STR" + jsonCmdStr);
        allTimestamps = new ArrayList<>();

        for (int i = 0; i < this.commandArray.size(); i++){
            for (int j = 0; j < this.commandArray.get(i).timestamps.size(); j++){
                allTimestamps.add(this.commandArray.get(i).timestamps.get(j));
                //System.out.println("ADDED TIME STAMP: " + this.commandArray.get(i).timestamps.get(j).toString());
            }
            this.commandArray.get(i).injectRangeIntoChildTimestamps();
        }
    }

}
