package edu.temple.pilitandroidclient.Objects;

import android.graphics.Color;

public class ColorObj {
    int r; //r value; value must be integer between 1-255
    int g; //g value; value must be integer between 1-255
    int b; //b value; value is an integer between 1-255

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


}
