package edu.temple.pilitandroidclient.Objects;

import java.util.ArrayList;

public class Command {
    final int DEFAULT_BRIGHTNESS = 50;

    public int range[];                                //range of LED lights being changed, each item is an individual light
    public int brightness = DEFAULT_BRIGHTNESS;       //level of brightness for all lights unchanged by command
    public ColorObj color = new ColorObj();            //color that all lights unchanged by command display
    public ArrayList<Timestamp> timestampList = new ArrayList<Timestamp>(); //an array of Timestamp objects(time, color, brightness); each object represents color at a particular time
    public String effectTitle;
    /*
    public enum effect{
        SOLID, RAINBOW, FLASH, CUSTOM
    } //name of light pattern effect
    public effect effectTitle;
    */

    public Command(String effect) {
        this.effectTitle = effect;
    }

    public void setCommand(int[] range, String title, int brightness, ColorObj color, ArrayList<Timestamp> timestampList){
        this.range = range;
        this.effectTitle = title;
        this.color = color;
        this.timestampList = timestampList;
    }

}
