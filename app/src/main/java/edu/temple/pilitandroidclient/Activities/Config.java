package edu.temple.pilitandroidclient.Activities;


import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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

import edu.temple.pilitandroidclient.Objects.ColorObj;
import edu.temple.pilitandroidclient.Objects.Command;
import edu.temple.pilitandroidclient.Objects.LEDConfigPattern;
import edu.temple.pilitandroidclient.Objects.Timestamp;
import edu.temple.pilitandroidclient.R;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class Config extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Button buttonApply, buttonExample, color1, color2;
    int btnCount = 30;
    Spinner effects1, effects2;
    EditText range1, range2;
    ArrayList<Button> previewButtons;
    LEDConfigPattern stripConfig;
    Gson gson = new Gson();
    SeekBar seekBarTime;
    LinearLayout ll2;
    private Socket socket;
    JSONObject outgoingJson;
    JSONObject incomingJson = new JSONObject();
    final int MAX_DISPLAY_TIME = 9999;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        assignGUIelementsToJavaObjects();

        createPreviewButtons();

        createEffectsSpinner();


        color1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int indexOfCommand = 0;     //each frag will need this to point to its corresponding command

                if (seekBarTime.getProgress() == 0) {
                    String selectedEffect = effects1.getSelectedItem().toString();
                    Command command = new Command(selectedEffect);

                    //Gives command.range its value from the EditText box
                    ArrayList<Integer> values = parseRange(range1.getText().toString());
                    command.setRangeSize(values.size());
                    for (int i = 0; i < values.size(); i++) {
                        command.range[i] = values.get(i);
                    }

                    colorPicker(color1, command);
                    stripConfig.commandArray.add(command);
                } else {                                                             //<--Create timestamp
                    Timestamp timestamp = new Timestamp(seekBarTime.getProgress());
                    stripConfig.commandArray.get(indexOfCommand).timestamps.add(timestamp);
                    colorPicker(color1, stripConfig.commandArray.get(indexOfCommand));

                }
                //Log.i("*****SeekBar value:", "" + seekBarTime.getProgress() );
            }
        });

        color2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int indexOfCommand = 1;     //each frag will need this to point to its corresponding command

                if (seekBarTime.getProgress() == 0) {
                    String selectedEffect = effects2.getSelectedItem().toString();
                    Command command = new Command(selectedEffect);

                    //Gives command.range its value from the EditText box
                    ArrayList<Integer> values = parseRange(range2.getText().toString());
                    command.setRangeSize(values.size());
                    for (int i = 0; i < values.size(); i++) {
                        command.range[i] = values.get(i);
                    }

                    colorPicker(color2, command);
                    stripConfig.commandArray.add(command);
                } else {                                                             //<--Create timestamp
                    Timestamp timestamp = new Timestamp(seekBarTime.getProgress());
                    stripConfig.commandArray.get(indexOfCommand).timestamps.add(timestamp);
                    colorPicker(color2, stripConfig.commandArray.get(indexOfCommand));

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

    public void assignGUIelementsToJavaObjects(){
        buttonExample = findViewById(R.id.example);
        buttonApply = findViewById(R.id.buttonApply);
        stripConfig = new LEDConfigPattern("new custom");
        seekBarTime = findViewById(R.id.seekBarTime);
        previewButtons = new ArrayList<Button>();
        ll2 = findViewById(R.id.linLay2);
        // TODO: 4/7/2019 Put into fragment and creates dynamically by using an "add" button
        effects1 = findViewById(R.id.spinnerEffects1);
        effects2 = findViewById(R.id.spinnerEffects2);
        range1 = findViewById(R.id.editRange1);
        range2 = findViewById(R.id.editRange2);
        color1 = findViewById(R.id.buttonColor1);
        color2 = findViewById(R.id.buttonColor2);
    }

    public void createPreviewButtons(){
        //This creates the LED preview at the top of the activity.
        //Each index of the arrayList previewButtons corresponds to a preview element(bulb)
        //EXAMPLE change color of bulb at index 3:
        //       previewButtons.get(3).setBackgroundColor(*color*);
        for (int i = 0; i < btnCount; i++) {
            previewButtons.add(new Button(this));
            previewButtons.get(i).setLayoutParams(buttonExample.getLayoutParams());
            ll2.addView(previewButtons.get(i));
        }
    }

    public void createEffectsSpinner(){
        //Creates effects spinner
        ArrayAdapter<String> effectAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, Command.effectList);
        effectAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        effects1.setOnItemSelectedListener(this);
        effects1.setAdapter(effectAdapter);
        effects2.setAdapter(effectAdapter);
    }

    public ArrayList<Integer> parseRange(String strInput) {
        ArrayList<Integer> range = new ArrayList<>();

        if (strInput.contains("-")) {                                            //range of number (ie 3 - 6)
            int startVal = isMultiDigit(strInput, 0, 1);
            int endVal = isMultiDigit(strInput, strInput.length() - 1, strInput.length() - 2);

            for (int i = startVal; i <= endVal; i++) {
                range.add(i);
            }
        } else {                                                                //list of numbers (ie 3,4,6)
            for (int i = 0; i < strInput.length(); i++) {
                try {
                    //Does not evaluate last digit if the last digit is part of a double digit number
                    //ie if last number is 18, break so that "8" is not added after adding "18"
                    if (i == strInput.length() - 1 && range.get(range.size() - 1) > 9) {
                        break;
                    }
                } catch (Exception e) {
                }
                char x = strInput.charAt(i);
                if (Character.isDigit(x)) {
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
    public int isMultiDigit(String strInput, int initialDigIndex, int nextDigIndex) {
        String strIntVal;

        if (nextDigIndex > strInput.length() - 1) {
            return Integer.parseInt(String.valueOf(strInput.charAt(initialDigIndex)));
        }

        if (Character.isDigit(strInput.charAt(nextDigIndex))) {
            if (initialDigIndex < nextDigIndex) {
                strIntVal = String.valueOf(strInput.charAt(initialDigIndex)) + String.valueOf(strInput.charAt(nextDigIndex));
            } else {
                strIntVal = String.valueOf(strInput.charAt(nextDigIndex) + String.valueOf(strInput.charAt(initialDigIndex)));
            }
        } else {
            strIntVal = String.valueOf(strInput.charAt(initialDigIndex));
        }

        //Log.i("***MULTI DIG", "return: " + Integer.parseInt(strIntVal) );
        return Integer.parseInt(strIntVal);
    }

    public void sendConfigToServer(final JSONObject outgoingJson) {
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
                Log.i("******* outgoingJson", outgoingJson.toString());      //Print JSON to Logcat(bottom of screen
            }

        }).on("login", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                incomingJson = (JSONObject) args[0];
                Log.i("&&&&&&& incomingJson:", incomingJson.toString());     //Print JSON to Logcat(bottom of screen
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

    public void colorPicker(final Button btn, final Command command) {
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


    public void colorSelected(int col, Command command) {

        if (command.color.r < 0) {                              //enter values for command
            for (int i = 0; i < command.range.length; i++) {
                previewButtons.get(command.range[i]).setBackgroundColor(col);
            }
            command.color.setRGBfromHex(col);
        } else {                                                 //enter values for timestamp
            for (int i = 0; i < command.range.length; i++) {
                previewButtons.get(command.range[i]).setBackgroundColor(col);
            }
            int indexOfLastTimestampAdded = command.timestamps.size();
            command.timestamps.get(indexOfLastTimestampAdded - 1).color.setRGBfromHex(col);
        }
    }

    public void changeBulbColor(int index, int hexColor) {
        previewButtons.get(index).setBackgroundColor(hexColor);
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();

        if (item.equals("rainbow")) {

            //rainbowEffect();
        }
        // Showing selected spinner item
        //Toast.makeText(parent.getContext(), "Selected:" + item, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void rainbowEffect() {

        String[] rainbow = {"#ff0000", "#ffa500", "#ffff00", "#008000", "#0000ff", "#4b0082", "#ee82ee"};
        int counter = 0;
        for (int j = 0; j < 10; j++) {
            counter = j % 7;
            for (int i = 0; i < 10; i++) {
                if ((i + j) % 7 == 0) {
                    counter = 0;
                    previewButtons.get(counter).setBackgroundColor(Color.parseColor(rainbow[counter]));
                    System.out.println(".get(" + counter + ")  " + "setBGColor(" + rainbow[counter] + ")");
                } else {
                    //System.out.println("counter = " + counter+ " j = "+ j+ " i = " + i);
                    previewButtons.get(counter).setBackgroundColor(Color.parseColor(rainbow[counter]));
                    System.out.println(".get(" + counter + ")  " + "setBGColor(" + rainbow[counter] + ")");
                }
                counter++;
            }

            try {
                System.out.println("good night");
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.print("Sleep error here");
            }
        }

        Log.wtf("reading rainbow", "color");
    }

    public void startPreview(View v){
        AsyncTaskRunner runner = new AsyncTaskRunner();
        runner.execute();
    }

    boolean flag = true;
    int x= 0x1;
    int rainbowIndex = 0;
    String[] rainbowStr = {"#ff0000", "#ffa500", "#ffff00", "#008000", "#0000ff", "#4b0082", "#ee82ee"};

    //seekBarValue = milliseconds, range 0-9999
    public void updatePreviewButtons(int seekBarValue){

        if ( effects1.getSelectedItem().equals("rainbow") ) {
            dansRainbowEffect(seekBarValue);
        } else if( effects1.getSelectedItem().equals("flash") ) {
            //TODO

        } else if ( effects1.getSelectedItem().equals("custom") ){
            //TODO

        } 

        //otherRainbow(seekBarValue);
    }

    public void advanceSeekBar(int newVal){
        seekBarTime.setProgress(newVal);
    }

    private class AsyncTaskRunner extends AsyncTask<String, String, String> {

        private String resp;

        @Override
        protected String doInBackground(String... params) {

            try {
                int emulatorIncr = 40;
                int galaxyIncr = 1;


                for (int time = 0; time < MAX_DISPLAY_TIME; time+=galaxyIncr) {
                    updatePreviewButtons(time);
                    advanceSeekBar(time);
                    Thread.sleep(1);
                }





                resp = "Complete";
            } catch (InterruptedException e) {
                e.printStackTrace();
                resp = e.getMessage();
            } catch (Exception e) {
                e.printStackTrace();
                resp = e.getMessage();
            }
            return resp;
        }

    }


    public void otherRainbow(int seekBarValue){

        //String[] color = new String[btnCount];
        ColorObj[] colorList = new ColorObj[btnCount];

        //give initial values
        if (seekBarValue == 0) {
            double frequency = .2;
            for (int i = 0; i < btnCount; ++i) {
                double red = Math.sin(frequency * i + 0) * 127 + 128;
                double green = Math.sin(frequency * i + 2) * 127 + 128;
                double blue = Math.sin(frequency * i + 4) * 127 + 128;

                changeBulbColor(i, Color.rgb((int) red, (int) green, (int) blue));

                //color[i] = Color.rgb((int) red, (int) green, (int) blue);
                colorList[i].setColor((int) red, (int) green, (int) blue);
                //System.out.println(color[i]);
            }
        }

        if (seekBarValue % 100 == -1 ){

            for (int i = 0; i < previewButtons.size(); i++){
                if (rainbowIndex == 29){
                    rainbowIndex = 0;
                }
                //changeBulbColor(i, colorList[rainbowIndex]);
                previewButtons.get(i).setBackgroundColor(Color.rgb( colorList[rainbowIndex].r, colorList[i].g, colorList[i].b) );
                rainbowIndex++;
            }
        }



    }

    public  void dansRainbowEffect(int seekBarValue){

        if (seekBarValue % 100 == 0 ){

            for (int i = 0; i < previewButtons.size(); i++){
                if (rainbowIndex == 7){
                    rainbowIndex = 0;
                }
                changeBulbColor(i,Color.parseColor(rainbowStr[rainbowIndex]));
                rainbowIndex++;
            }
        }
    }
}