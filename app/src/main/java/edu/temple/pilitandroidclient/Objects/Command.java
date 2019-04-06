package edu.temple.pilitandroidclient.Objects;

import java.util.ArrayList;

public class Command {
    enum effect                         //name of light pattern effect
    {
        RAINBOW, CUSTOM, SOLID, FLASH
    }

    int range[];                        //range of LED lights being changed, each item is an individual light
    int brightness;                     //level of brightness for all lights unchanged by command
    Color otherLights = new Color();    //color that all lights unchanged by command display
    ArrayList<Timestamp> timestampArray;         //an array of Timestamp objects(time, color, brightness); each object represents color at a particular time



}