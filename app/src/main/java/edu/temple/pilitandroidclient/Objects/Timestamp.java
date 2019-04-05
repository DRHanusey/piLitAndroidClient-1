package edu.temple.pilitandroidclient.Objects;

public class Timestamp {
    Color colorState = new Color(); //color object (contains R, G, B values)
    int time; //time at which color change will take effect
    int brightness; //brightness of all lights being changed by command

    public void setTimestamp(Color colorState, int time, int brightness){ //constructor for Timestamp object
        this.colorState = colorState;
        this.time = time;
        this.brightness = brightness;
    }

}
