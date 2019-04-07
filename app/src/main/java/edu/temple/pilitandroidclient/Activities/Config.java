package edu.temple.pilitandroidclient.Activities;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;

import edu.temple.pilitandroidclient.Objects.ColorObj;
import edu.temple.pilitandroidclient.Objects.Command;
import edu.temple.pilitandroidclient.Objects.LEDConfigPattern;
import edu.temple.pilitandroidclient.Objects.Timestamp;
import edu.temple.pilitandroidclient.R;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class Config extends AppCompatActivity {
    String[] effects = {"SOLID", "RAINBOW","FLASH","CUSTOM"};
    //Command.effect[] effects =
    //        {Command.effect.SOLID, Command.effect.RAINBOW, Command.effect.FLASH,Command.effect.CUSTOM};
    Button buttonApply, buttonExample, buttonExamplePreview;
    int btnCount = 20;
    Drawable circle;
    Spinner effects1, effects2;
    EditText range1, range2;
    Button color1, color2;
    int selColor;
    ArrayList<Button> previewButtons;

    LEDConfigPattern stripConfig;

    private Socket socket;
    JSONObject outgoingJson;// = new JSONObject();
    JSONObject incomingJson = new JSONObject();

    LEDConfigPattern lcp = new LEDConfigPattern("Test Config");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        buttonExample = findViewById(R.id.example);
        stripConfig = new LEDConfigPattern("new custom");



        previewButtons = new ArrayList<Button>();
        LinearLayout ll2 = findViewById(R.id.linLay2);
        //buttonExamplePreview = findViewById(R.id.example);
        for (int i = 0; i < btnCount; i++){
            previewButtons.add(new Button(this));
            previewButtons.get(i).setLayoutParams(buttonExample.getLayoutParams());
            ll2.addView(previewButtons.get(i));
        }


        ArrayAdapter<String> effectAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, effects);
        effectAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        effects1 = findViewById(R.id.spinnerEffects1);
        effects1.setAdapter(effectAdapter);
        effects2 = findViewById(R.id.spinnerEffects2);
        effects2.setAdapter(effectAdapter);

        range1 = findViewById(R.id.editRange1);
        range2 = findViewById(R.id.editRange2);

        color1 = findViewById(R.id.buttonColor1);
        color2 = findViewById(R.id.buttonColor2);

        color1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedEffect = effects1.getSelectedItem().toString();
                Command command = new Command(selectedEffect);
                changeColor(color1,range1,command);
                stripConfig.commandArray.add(command);
            }
        });


        //outgoingJson = new JSONObject(stripConfig);

        Log.i("*********stripConfig",stripConfig.toString());


        color2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedEffect = effects2.getSelectedItem().toString();
                Command command = new Command(selectedEffect);
                changeColor(color2,range2,command);
                stripConfig.commandArray.add(command);
            }
        });







    }



    public ArrayList<Integer> parseRange(String strInput){
        ArrayList<Integer> range = new ArrayList<>();

        if (strInput.contains("-")){                                            //range of number (ie 3 - 6)
            int startVal = isMultiDigit(strInput,0,1);
            int endVal = isMultiDigit(strInput, strInput.length()-1, strInput.length()-2);

            for (int i = startVal; i <= endVal; i++){
                range.add(i);
            }
        } else {                                                                //list of numbers (ie 3,4,6)
            for (int i = 0; i < strInput.length(); i++){
                //Does not evaluate last digit if the last digit is part of a double digit number
                //ie if last number is 18, break so that "8" is not added after adding "18"
                if (i == strInput.length()-1 && range.get(range.size()-1) > 9){
                    break;
                }

                char x = strInput.charAt(i);
                if (Character.isDigit(x)){
                    int number = isMultiDigit(strInput, i, i + 1);
                    range.add(number);
                }
            }
        }
        Log.i("*****range", "range: " + range.toString());
        return range;

    }

    //Return val is either singe dig int val at index "initialDigIndex" or multi dig val
    //including "nextDigIndex"
    public int isMultiDigit(String strInput, int initialDigIndex, int nextDigIndex){
        String strIntVal;

        if (nextDigIndex > strInput.length()-1){
            return Integer.parseInt(String.valueOf(strInput.charAt(initialDigIndex)));
        }

        if (Character.isDigit(strInput.charAt(nextDigIndex))){
            if (initialDigIndex < nextDigIndex) {
                strIntVal = String.valueOf(strInput.charAt(initialDigIndex)) + String.valueOf(strInput.charAt(nextDigIndex));
            } else {
                strIntVal = String.valueOf(strInput.charAt(nextDigIndex) + String.valueOf(strInput.charAt(initialDigIndex)) );
            }
        } else{
            strIntVal = String.valueOf(strInput.charAt(initialDigIndex));
        }

        //Log.i("***MULTI DIG", "return: " + Integer.parseInt(strIntVal) );
        return Integer.parseInt(strIntVal);
    }

    public void clickApply(View v){
        //Insert the https address into the socket
        try {
            socket = IO.socket(Login.SERVER_ADDRESS);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        //Create the Json to be sent to server
        try {
            //testJson.put("type","This is just a test!!");
            outgoingJson.put("userName","testuser");
            outgoingJson.put("password","password");

        } catch (JSONException e) {
            e.printStackTrace();
        }


        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {

            @Override
            public void call(Object... args) {
                socket.emit("login", outgoingJson);
                Log.i("******* outgoingJson",outgoingJson.toString());      //Print JSON to Logcat(bottom of screen
            }

        }).on("login", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                incomingJson = (JSONObject)args[0];
                Log.i("&&&&&&& incomingJson:",incomingJson.toString());     //Print JSON to Logcat(bottom of screen
                socket.disconnect();
            }
        }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
            }
        });
        socket.connect();
        Toast.makeText(getApplicationContext(), "test button pressed", Toast.LENGTH_SHORT).show();
    }

    public void changeColor(final Button btn, final EditText edTx, final Command command){
        ColorPickerDialogBuilder
                .with(this)
                .setTitle("Choose color")
                .initialColor(0xFFFFFFFF)
                .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                .density(12)
                .setOnColorSelectedListener(new OnColorSelectedListener() {
                    @Override
                    public void onColorSelected(int selectedColor) {
                    }
                })
                .setPositiveButton("ok", new ColorPickerClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                        btn.setBackgroundColor(selectedColor);
                        //returnColor(selectedColor);
                        colorSelected(selectedColor,edTx, command);
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .build()
                .show();
    }

    public void colorSelected(int col,EditText edTx, Command command){
        ArrayList<Integer> values = parseRange(edTx.getText().toString());
        int[] range = new int[values.size()];

        for (int i = 0; i < values.size(); i++){
            int ledIndex = values.get(i);
            range[i] = ledIndex;
            previewButtons.get(ledIndex).setBackgroundColor(col);
            }

        command.range = range;
        command.color.setRGBfromHex(col);
    }

}

/////////////////////////////////////////
//creating a test LEDConfigPattern Object
        /*
        LEDConfigPattern testConfig = new LEDConfigPattern("party lights");
        //testConfig.setDescription("party lights");
        //light strip size 5
        Command twoFourSix = new Command();

        int testArr[] = {2,4,6};
        ColorObj unchangedLights = new ColorObj();
        unchangedLights.setColor(100,200,55);

        ColorObj changedLights = new ColorObj();
        changedLights.setColor(120,130,75);

        ColorObj secondChangedLights = new ColorObj();
        secondChangedLights.setColor(150,170,45);

        ArrayList<Timestamp> timestampArrayList = new ArrayList<Timestamp>();
        Timestamp testStampOne = new Timestamp();
        Timestamp testStampTwo = new Timestamp();

        testStampOne.setTimestamp(changedLights, 2, 5);
        testStampTwo.setTimestamp(secondChangedLights, 5, 7);


        timestampArrayList.add(testStampOne);
        timestampArrayList.add(testStampTwo);

        twoFourSix.setCommand(testArr, Command.effect.RAINBOW, 5, unchangedLights, timestampArrayList);

        testConfig.commandArray.add(twoFourSix);

                int ledsInRange = testConfig.commandArray.get(0).range.length;
        for (int i = 0; i<ledsInRange-1; i++){
            int index = testConfig.commandArray.get(0).range[i];
            Color color = new Color();//??????????????????????????????
            previewButtons.get(index).setBackgroundColor();
        }
        */