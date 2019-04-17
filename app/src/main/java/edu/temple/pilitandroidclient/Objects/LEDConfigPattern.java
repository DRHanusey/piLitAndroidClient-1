package edu.temple.pilitandroidclient.Objects;

import com.google.gson.Gson;

import java.util.ArrayList;

public class LEDConfigPattern {
    public String description;                     //description of pattern, e.g. party lights, sleep lights, etc.
    public ArrayList<Command> commandArray;        //array of commands that make up the configuration pattern
    public ArrayList<Timestamp> allCustomTimestamps;
    public ArrayList<Integer> rangeForRainbowEffect;
    public ArrayList<Command> flashCommands;
    public boolean flashOn = false;

    public LEDConfigPattern(String description) {
        this.description = description;
        this.commandArray = new ArrayList<Command>();
    }

    public void createFlashCommandArray(){
        this.flashCommands = new ArrayList<>();

        for (int i = 0; i < commandArray.size(); i++){
            if (commandArray.get(i).effect.equalsIgnoreCase("flash")){
                flashCommands.add(commandArray.get(i));
            }
        }
    }


    public void createRainbowRangeArray() {
        this.rangeForRainbowEffect = new ArrayList<>();
        for (int i = 0; i < commandArray.size(); i++){
            if (commandArray.get(i).effect.equalsIgnoreCase("rainbow")){
                for (int j = 0; j < commandArray.get(i).range.length-1; j++){
                    rangeForRainbowEffect.add(commandArray.get(i).range[j]);
                }
            }
        }
    }


    public void createCustomTimestampArray(){
        Gson gson = new Gson();
        String jsonCmdStr = gson.toJson(this.commandArray);
        //System.out.println("GSON COMMAND STR" + jsonCmdStr);
        allCustomTimestamps = new ArrayList<>();

        for (int i = 0; i < this.commandArray.size(); i++){
            for (int j = 0; j < this.commandArray.get(i).timestamps.size(); j++){
                if (this.commandArray.get(i).effect.equalsIgnoreCase("custom")) {
                    allCustomTimestamps.add(this.commandArray.get(i).timestamps.get(j));
                    //System.out.println("ADDED TIME STAMP: " + this.commandArray.get(i).timestamps.get(j).toString());
                }
            }
            this.commandArray.get(i).injectRangeIntoChildTimestamps();
        }
    }

}
