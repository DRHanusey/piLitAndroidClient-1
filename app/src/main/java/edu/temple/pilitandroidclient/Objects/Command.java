package edu.temple.pilitandroidclient.Objects;

import java.util.ArrayList;

public class Command {
    public int range[];  //range of LED lights being changed, each item is an individual light
    public enum effect{
        RAINBOW, CUSTOM
    } //name of light pattern effect
    public effect effectTitle;
    public int brightness; //level of brightness for all lights unchanged by command
    public ColorObj otherLights = new ColorObj(); //color that all lights unchanged by command display
    public ArrayList<Timestamp> timestampList = new ArrayList<Timestamp>(); //an array of Timestamp objects(time, color, brightness); each object represents color at a particular time

    public void setCommand(int[] range, effect title, int brightness, ColorObj otherLights, ArrayList<Timestamp> timestampList){
        this.range = range;
        this.effectTitle = title;
        this.otherLights = otherLights;
        this.timestampList = timestampList;
    }
}
