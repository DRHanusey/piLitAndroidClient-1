package edu.temple.pilitandroidclient.Objects;

public class Timestamp {
    public ColorObj color = new ColorObj(); //color object (contains R, G, B values)
    public int time;                       //time at which color change will take effect
    int brightness = Command.DEFAULT_BRIGHTNESS;                 //brightness of all lights being changed by command
    public int range[];
    public boolean colorDeployed = false;

    public void setTimestamp(ColorObj color, int time, int brightness){ //constructor for Timestamp object
        this.color = color;
        this.time = time;
        this.brightness = brightness;
    }

    public Timestamp(int time) {
        this.time = time;
    }


    @Override
    public String toString() {
        return "Time: " + time + ", Color(RGB): " + color.r + " " + color.g  + " " + color.b;
    }
}

