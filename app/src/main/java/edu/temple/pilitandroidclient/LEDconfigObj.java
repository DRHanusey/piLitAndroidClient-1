package edu.temple.pilitandroidclient;

public class LEDconfigObj {
    public final int DEFAULT_BRIGHTNESS = 100;

    int stripId;                // to differentiate when multiple strips per pi. Is equal to the index of its location within an array list
    int ledCount;               // number of LEDs in a strip.
    int[] colorArray;           // 1 index per light. int color values (ie pink #ff69b4 = 16738740)
    int stripBrightness;        // 0-255

    /* PRESET EFFECTS which can be applied to a custom color set up */
    boolean brightnessBulge;    // brightness would shift around the strip  (1 1 1 2 3 4 3 2 1 1 1 ->)
    int bulgeSpeed;             // 1-10 how fast LEDs will transition between colors

    boolean colorShift;         // LED colors would shift (B, R, R) -> (R, B, R) -> (R, R, B)
    int shiftSpeed;
    int shiftGroup;             // How many LEDs to shift at a time
    boolean shiftDirection;     // True = Left, False = right

    boolean colorfade;          //LED colors would fade into their next color.


    /* PRELOADED CONFIGS would store like this */
    boolean rainbow;
    int rainbowSpeed;


    public LEDconfigObj(int ledCount, int[] colorArray, int stripBrightness, int stripId) {
        //TODO stripId should come from arrayList index not manual input
        this.ledCount = ledCount;
        this.colorArray = colorArray;
        this.stripBrightness = stripBrightness;
        this.stripId = stripId;
    }

    // USe when adding a new strip to a Pi
    public LEDconfigObj(int ledCount, int stripId){
        this.ledCount = ledCount;
        this.stripId = stripId;

        colorArray = new int[ledCount];
        this.stripBrightness = DEFAULT_BRIGHTNESS;
    }
}
