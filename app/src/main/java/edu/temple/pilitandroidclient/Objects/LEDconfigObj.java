package edu.temple.pilitandroidclient.Objects;

import java.io.Serializable;

public class LEDconfigObj implements Serializable {
    public final int DEFAULT_BRIGHTNESS = 100;
    public final String DEFAULT_COLOR = "FFFFFF";            //16777215; //white

    int stripId;                // to differentiate when multiple strips per pi. Is equal to the index of its location within an array list (first strip id = 0)
    int ledCount;               // number of LEDs in a strip.
    String[] colorArray;           // 1 index per light. int color values (ie pink #ff69b4 = 16738740)
    int stripBrightness;        // 0-255
    String description;


    // Use when importing objects from Server/DB
    public LEDconfigObj(int ledCount, String[] colorArray, int stripBrightness, int stripId, String desc) {
        this.stripId = stripId;
        this.ledCount = ledCount;
        this.colorArray = colorArray;
        this.stripBrightness = stripBrightness;
        this.description = desc;
    }

    // Use when adding a new strip to a Pi
    public LEDconfigObj(int ledCount, int stripId){
        this.ledCount = ledCount;
        this.stripId = stripId;

        colorArray = new String[ledCount];
        this.stripBrightness = DEFAULT_BRIGHTNESS;

        //Set all LEDs to white
        for (int i = 0; i < ledCount; i++){
            colorArray[i] = DEFAULT_COLOR;
        }
    }

    //for testing
    public LEDconfigObj(String desc){
        this.stripId = 10;
        this.ledCount = 30;
        this.stripBrightness = DEFAULT_BRIGHTNESS;
        colorArray = new String[ledCount];
        this.description = desc;

        //Set all LEDs to white
        for (int i = 0; i < ledCount; i++){
            colorArray[i] = DEFAULT_COLOR;
        }
    }

    @Override
    public String toString() {
        return description;
    }
}
