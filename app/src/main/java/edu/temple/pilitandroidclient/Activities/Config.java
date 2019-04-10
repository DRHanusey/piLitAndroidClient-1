package edu.temple.pilitandroidclient.Activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;

import edu.temple.pilitandroidclient.Objects.Command;
import edu.temple.pilitandroidclient.Objects.LEDConfigPattern;
import edu.temple.pilitandroidclient.Objects.Timestamp;
import edu.temple.pilitandroidclient.R;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class Config extends AppCompatActivity {
    //String[] effects = {"SOLID", "RAINBOW","FLASH","CUSTOM"};
    //Command.effect[] effects =
    //        {Command.effect.SOLID, Command.effect.RAINBOW, Command.effect.FLASH,Command.effect.CUSTOM};
    Button buttonApply, buttonExample, buttonExamplePreview;
    int btnCount = 30;
    Spinner effects1, effects2;
    EditText range1, range2;
    Button color1, color2;
    ArrayList<Button> previewButtons;
    LEDConfigPattern stripConfig;
    Gson gson = new Gson();
    SeekBar seekBarTime;

    private Socket socket;
    JSONObject outgoingJson;// = new JSONObject();
    JSONObject incomingJson = new JSONObject();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        buttonExample = findViewById(R.id.example);
        buttonApply = findViewById(R.id.buttonApply);
        stripConfig = new LEDConfigPattern("new custom");
        seekBarTime = findViewById(R.id.seekBarTime);





        //This creates the LED preview at the top of the activity.
        //Each index of the arrayList previewButtons corresponds to a preview element(bulb)

        //EXAMPLE change color of bulb at index 3:
        //       previewButtons.get(3).setBackgroundColor(*color*);
        previewButtons = new ArrayList<Button>();
        LinearLayout ll2 = findViewById(R.id.linLay2);
        for (int i = 0; i < btnCount; i++){
            previewButtons.add(new Button(this));
            previewButtons.get(i).setLayoutParams(buttonExample.getLayoutParams());
            ll2.addView(previewButtons.get(i));
        }

        //Creates effects spinner
        ArrayAdapter<String> effectAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, Command.effectList);
        effectAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //Manually creates two input rows for changing colors.
        // TODO: 4/7/2019 Put into fragment and creates dynamically by using an "add" button
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
                int indexOfCommand = 0;     //each frag will need this to point to its corresponding command

                if(seekBarTime.getProgress() == 0) {
                    String selectedEffect = effects1.getSelectedItem().toString();
                    Command command = new Command(selectedEffect);

                    //Gives command.range its value from the EditText box
                    ArrayList<Integer> values = parseRange(range1.getText().toString());
                    command.setRangeSize(values.size());
                    for (int i = 0; i < values.size(); i++){
                        command.range[i] = values.get(i);
                    }

                    colorPicker(color1, command);
                    stripConfig.commandArray.add(command);
                } else{                                                             //<--Create timestamp
                    Timestamp timestamp = new Timestamp(seekBarTime.getProgress());
                    stripConfig.commandArray.get(indexOfCommand).timestamps.add(timestamp);
                    colorPicker(color1,stripConfig.commandArray.get(indexOfCommand));

                }
                //Log.i("*****SeekBar value:", "" + seekBarTime.getProgress() );
            }
        });

        color2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int indexOfCommand = 1;     //each frag will need this to point to its corresponding command

                if(seekBarTime.getProgress() == 0) {
                    String selectedEffect = effects2.getSelectedItem().toString();
                    Command command = new Command(selectedEffect);

                    //Gives command.range its value from the EditText box
                    ArrayList<Integer> values = parseRange(range2.getText().toString());
                    command.setRangeSize(values.size());
                    for (int i = 0; i < values.size(); i++){
                        command.range[i] = values.get(i);
                    }

                    colorPicker(color2, command);
                    stripConfig.commandArray.add(command);
                } else{                                                             //<--Create timestamp
                    Timestamp timestamp = new Timestamp(seekBarTime.getProgress());
                    stripConfig.commandArray.get(indexOfCommand).timestamps.add(timestamp);
                    colorPicker(color2,stripConfig.commandArray.get(indexOfCommand));

                }
                //Log.i("*****SeekBar value:", "" + seekBarTime.getProgress() );
            }
        });


        //Converts LEDConfigPattern to String
        //Converts String to JSON obj
        buttonApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String jsonStr = gson.toJson(stripConfig);
                //Log.i("******Json String:",jsonStr);
                try {
                    outgoingJson = new JSONObject(jsonStr);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //Log.i("******Json Object:",outgoingJson.toString());
                sendConfigToServer(outgoingJson);
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
                try {
                    //Does not evaluate last digit if the last digit is part of a double digit number
                    //ie if last number is 18, break so that "8" is not added after adding "18"
                    if (i == strInput.length()-1 && range.get(range.size()-1) > 9){
                        break;
                    }
                } catch (Exception e){}
                char x = strInput.charAt(i);
                if (Character.isDigit(x)){
                    int number = isMultiDigit(strInput, i, i + 1);
                    range.add(number);
                }
            }
        }
        //Log.i("*****range", "range: " + range.toString());
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

    public void sendConfigToServer(final JSONObject outgoingJson){
        //Insert the https address into the socket
        try {
            socket = IO.socket(Login.SERVER_ADDRESS);
        } catch (URISyntaxException e) {
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
        //Toast.makeText(getApplicationContext(), "test button pressed", Toast.LENGTH_SHORT).show();
    }

    public void colorPicker(final Button btn, final Command command){
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
                        colorSelected(selectedColor, command);
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

    public void colorSelected(int col, Command command){

        if (command.color.r < 0) {                              //enter values for command
            for (int i = 0; i < command.range.length; i++) {
                previewButtons.get(command.range[i]).setBackgroundColor(col);
            }
            command.color.setRGBfromHex(col);
        } else{                                                 //enter values for timestamp
            for (int i = 0; i < command.range.length; i++) {
                previewButtons.get(command.range[i]).setBackgroundColor(col);
            }
            int indexOfLastTimestampAdded = command.timestamps.size();
            command.timestamps.get(indexOfLastTimestampAdded-1).color.setRGBfromHex(col);
        }
    }

}