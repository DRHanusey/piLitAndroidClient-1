package edu.temple.pilitandroidclient.Objects;

import java.util.ArrayList;

public class LEDConfigPattern {
    String description;                     //description of pattern, e.g. party lights, sleep lights, etc.
    public ArrayList<Command> commandArray;        //array of commands that make up the configuration pattern


    public LEDConfigPattern(String description) {
        this.description = description;
        this.commandArray = new ArrayList<Command>();
        }
}
