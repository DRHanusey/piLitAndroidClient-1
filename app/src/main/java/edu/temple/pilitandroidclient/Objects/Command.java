package edu.temple.pilitandroidclient.Objects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

public class Command implements Serializable {
    public static final int DEFAULT_BRIGHTNESS = 100;
    public static final String[] effectList = {"Select effect","solid", "rainbow","flash","custom"};

    public int range[];                                                     //range of LED lights being changed, each item is an individual light
    public int brightness = DEFAULT_BRIGHTNESS;                             //level of brightness for all lights unchanged by command
    public ColorObj color = new ColorObj();                                 //color that all lights unchanged by command display
    public ArrayList<Timestamp> timestamps = new ArrayList<Timestamp>();            //an array of Timestamp objects(time, color, brightness); each object represents color at a particular time
    public String effect;

    public Command(String effect) {
        this.effect = effect;
    }

    public void setRangeSize(int size){
        this.range = new int[size];
    }

    public void injectRangeIntoChildTimestamps(){

        System.out.println("timestamps.size = " + timestamps.size());

        for (int i = 0; i<timestamps.size(); i++){
            //timestamps.get(i).range = new int[this.range.length];
            timestamps.get(i).range = Arrays.copyOf(this.range,this.range.length);
            System.out.println("TS"+i+" range[0] = " + timestamps.get(i).range[0]);
            System.out.println("TS range[last] = " + timestamps.get(i).range[timestamps.get(i).range.length-1]);
        }
    }

}
