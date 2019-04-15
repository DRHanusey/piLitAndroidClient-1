package edu.temple.pilitandroidclient.Objects;

import android.graphics.Color;

public class ColorObj {
    public int r = -1; //r value; value must be integer between 1-255
    public int g = -1; //g value; value must be integer between 1-255
    public int b = -1; //b value; value is an integer between 1-255

    public void setColor(int redValue, int greenValue, int blueValue){  //constructor
        this.r = redValue;
        this.g = greenValue;
        this.b = blueValue;
    }

    public void setRGBfromHex(int hexColor){
        this.r = Color.red(hexColor);
        this.b = Color.blue(hexColor);
        this.g = Color.green(hexColor);
    }

    @Override
    public String toString() {
        return "red: " + r + " green: " + g + " blue: " + b;
    }
}
