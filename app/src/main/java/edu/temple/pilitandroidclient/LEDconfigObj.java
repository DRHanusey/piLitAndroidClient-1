package edu.temple.pilitandroidclient;

public class LEDconfigObj {

    int ledCount;
    int[] colorArray;
    int brightness;
    int stripId;

    

    public LEDconfigObj(int ledCount, int[] colorArray, int brightness, int stripId) {
        this.ledCount = ledCount;
        this.colorArray = colorArray;
        this.brightness = brightness;
        this.stripId = stripId;
    }
}
