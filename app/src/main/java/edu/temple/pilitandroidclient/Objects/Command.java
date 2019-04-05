package edu.temple.pilitandroidclient.Objects;

public class Command {
    int range[];                        //range of LED lights being changed, each item is an individual light
    enum effect                         //name of light pattern effect
    {
        RAINBOW, CUSTOM
    }
    int brightness;                     //level of brightness for all lights unchanged by command
    Color otherLights = new Color();    //color that all lights unchanged by command display
    Timestamp timestampArray[];         //an array of Timestamp objects(time, color, brightness); each object represents color at a particular time



}