package edu.temple.pilitandroidclient.Objects;

public class ColorObj {
    int r; //r value; value must be integer between 1-255
    int g; //g value; value must be integer between 1-255
    int b; //b value; value is an integer between 1-255

    public void setColor(int redValue, int greenValue, int blueValue){  //constructor
        this.r = redValue;
        this.g = greenValue;
        this.b = blueValue;
    }


}
