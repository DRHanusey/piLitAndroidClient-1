package edu.temple.pilitandroidclient.Objects;

import java.util.ArrayList;

public class LEDConfigPattern {
    ArrayList<Command> commandArray;        //array of commands that make up the configuration pattern
    String description;                     //description of pattern, e.g. party lights, sleep lights, etc.


    public LEDConfigPattern(String description) {
        this.commandArray = new ArrayList<Command>();
        this.description = description;
    }
}
