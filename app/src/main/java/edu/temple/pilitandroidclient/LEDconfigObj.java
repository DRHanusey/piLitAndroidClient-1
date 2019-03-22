package edu.temple.pilitandroidclient;

public class LEDconfigObj {
    public final int DEFAULT_BRIGHTNESS = 100;
    public final int DEFAULT_COLOR = 16777215; //white (FFFFFF)

    int stripId;                // to differentiate when multiple strips per pi. Is equal to the index of its location within an array list (first strip id = 0)
    int ledCount;               // number of LEDs in a strip.
    int[] colorArray;           // 1 index per light. int color values (ie pink #ff69b4 = 16738740)
    int stripBrightness;        // 0-255


    // Use when importing objects from Server/DB
    public LEDconfigObj(int ledCount, int[] colorArray, int stripBrightness, int stripId) {
        this.stripId = stripId;
        this.ledCount = ledCount;
        this.colorArray = colorArray;
        this.stripBrightness = stripBrightness;
        this.stripId = stripId;
    }

    // Use when adding a new strip to a Pi
    public LEDconfigObj(int ledCount, int stripId){
        this.ledCount = ledCount;
        this.stripId = stripId;

        colorArray = new int[ledCount];
        this.stripBrightness = DEFAULT_BRIGHTNESS;

        //Set all LEDs to white
        for (int i = 0; i < ledCount; i++){
            colorArray[i] = DEFAULT_COLOR;
        }
    }
}
