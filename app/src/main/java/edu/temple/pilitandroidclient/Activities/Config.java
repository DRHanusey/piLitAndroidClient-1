package edu.temple.pilitandroidclient.Activities;


import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
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

import java.util.ArrayList;

import edu.temple.pilitandroidclient.Objects.ColorObj;
import edu.temple.pilitandroidclient.Objects.Command;
import edu.temple.pilitandroidclient.Objects.LEDConfigPattern;
import edu.temple.pilitandroidclient.Objects.Timestamp;
import edu.temple.pilitandroidclient.Objects.commandRequest;
import edu.temple.pilitandroidclient.R;
import io.socket.emitter.Emitter;

public class Config extends AppCompatActivity {
    Button buttonApply, buttonExample, color1, color2, buttonClear, buttonSavePublic;
    CheckBox makePublic; //for the makePublic checkbox
    int btnCount = 30;
    Spinner effects1, effects2;
    EditText range1, range2, configName;
    ArrayList<Button> previewButtons;
    LEDConfigPattern stripConfig;
    Gson gson = new Gson();
    SeekBar seekBarTime;
    LinearLayout ll2;
    JSONObject piAndCommandJson;
    JSONObject incomingJson = new JSONObject();
    JSONObject incomingJson2 = new JSONObject();
    final int MAX_DISPLAY_TIME = 9999;
    final int SEEK_BAR_SPEED = 1;    //Change to 40 for emulator or Maliks slow ass phone
    final int FLASH_SPEED = 200;     //smaller = faster
    final int RAINBOW_SPEED = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        //Methods used to clean up and organize onCreate
        assignGUIelementsToJavaObjects();
        createPreviewButtons();
        createEffectsSpinner();
        populateConfigScreen();

        color1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colorBtnClick(0, range1, effects1, color1);
            }
        });

        color2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colorBtnClick(1, range2, effects2, color2);
            }
        });


        //SEND TO PI
        buttonApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //final commandRequest testRequest = new commandRequest("testpi", "testuser", stripConfig);
                final commandRequest testRequest = new commandRequest(Login.piName, Login.userName, stripConfig);
                String piAndCommandString = gson.toJson(testRequest);

                try {
                    piAndCommandJson = new JSONObject(piAndCommandString);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //sendConfigToServer(loginInfoJson, piAndCommandJson);
                sendConfigToPi(piAndCommandJson);
            }
        });
    }


    public void populateConfigScreen() {
        //Load config if one is passed
        boolean loadConfig = getIntent().getBooleanExtra(User.EXTRA_PRESENT, false);
        if (loadConfig) {
            stripConfig = (LEDConfigPattern) getIntent().getSerializableExtra(User.CONFIG_OBJ);

            //if loadedConfig is public make sure checkbox appears checked on config screen
            checkTheCheckboxIfPublic(stripConfig.isPublic); //passes true or false depending on if the config is public and checks or unchecks the checkbox accordingly

            if (!stripConfig.configName.equals("default")) {
                configName.setText(stripConfig.configName);

                for (int i = 0; i < stripConfig.commandArray.size(); i++) {
                    String effectName = stripConfig.commandArray.get(i).effect;
                    //System.out.println(effectName);
                    //System.out.println("range length " + stripConfig.commandArray.get(i).range.length );

                    //TODO put effect spinner in array
                    for (int j = 1; j < Command.effectList.length; j++) {
                        if (effectName.equals(Command.effectList[j])) {
                            if (i == 0) {
                                effects1.setSelection(j);
                                //range1.setText(stripConfig.commandArray.get(j).range.toString());
                                //deactivate box
                            } else {
                                effects2.setSelection(j);
                                //range2.setText(stripConfig.commandArray.get(j).range.toString());
                                //deactivate box
                            }
                        }
                    }
                }
                //System.out.println(gson.toJson(stripConfig));
            }
        }
    }

    public void checkTheCheckboxIfPublic(boolean isPublic){
        if (isPublic == true){ //yes, the loaded config is publicly available on the marketplace - uncheck the checkbox
            makePublic.setChecked(true);
        }
        else{ //no, the loaded config is a private config- make sure that the visual checkbox is unchecked
            makePublic.setChecked(false);
        }
    }

    public void saveConfig(View v) throws JSONException {
        shouldConfigBeAddedToMarketplace(); //if checkbox is checked, change config variable isPublic to true
        stripConfig.configName = configName.getText().toString();
        String stripConfigString = gson.toJson(stripConfig);
        JSONObject stripConfigJson = new JSONObject(stripConfigString);

        Login.socket.emit("saveConfig", stripConfigJson);
        Log.i("&&&&&&& outgoingJson:", stripConfigJson.toString());

        Login.socket.on("saveConfig", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                incomingJson = (JSONObject) args[0];
                Log.i("&&&&&&& incomingJson2:", incomingJson.toString());
            }
        });
    }

    public void sendConfigToPi(final JSONObject configMsg) {
        Login.socket.emit("command", configMsg);
        Log.i("&&&&&&& outgoingJson:", configMsg.toString());

        Login.socket.on("command", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                incomingJson = (JSONObject) args[0];
                Log.i("&&&&&&& incomingJson:", incomingJson.toString());
            }
        });
    }

    public void colorBtnClick(int indexOfCommand, EditText range, Spinner effects, Button button) {
        //int indexOfCommand = indexOfCmd;     //each frag will need this to point to its corresponding command
        String selectedEffect = effects.getSelectedItem().toString();
        String strRange;
        strRange = range.getText().toString();

        //if a effect hasn't been selected, exit onClick
        if (selectedEffect.equals("Select effect")) {
            Toast.makeText(getApplicationContext(),
                    "Select an effect from the dropdown!", Toast.LENGTH_LONG).show();
            return;
        }


        if (strRange.equalsIgnoreCase("")) {
            Toast.makeText(getApplicationContext(),
                    "Enter a range using '-' ',' 'odd' 'even' 'all'", Toast.LENGTH_LONG).show();
            return;
        }

        if (selectedEffect.equalsIgnoreCase("custom")) {

            if (stripConfig.commandArray.size() == indexOfCommand) {

                Command command = new Command(selectedEffect);

                //Gives command.range its value from the EditText box
                ArrayList<Integer> values = parseRange(strRange);
                command.setRangeSize(values.size());
                for (int i = 0; i < values.size(); i++) {
                    command.range[i] = values.get(i);
                }
                stripConfig.commandArray.add(command);
            }

            //Create timestamp and assigns time from progress bar
            Timestamp timestamp = new Timestamp(seekBarTime.getProgress());
            //Add timestamp to commandArray
            stripConfig.commandArray.get(indexOfCommand).timestamps.add(timestamp);
            //Assigns color value to timestamp
            colorPicker(button, stripConfig.commandArray.get(indexOfCommand));
        }

        if (selectedEffect.equalsIgnoreCase("solid") ||
                selectedEffect.equalsIgnoreCase("flash")) {
            //No timestamps created

            Command command = new Command(selectedEffect);

            //Gives command.range its value from the EditText box
            ArrayList<Integer> values = parseRange(strRange);
            command.setRangeSize(values.size());
            for (int i = 0; i < values.size(); i++) {
                command.range[i] = values.get(i);
            }

            colorPicker(button, command);
            stripConfig.commandArray.add(command);
        }

        if (selectedEffect.equalsIgnoreCase("rainbow")) {
            //No color selection is necessary

            Command command = new Command(selectedEffect);

            //Gives command.range its value from the EditText box
            ArrayList<Integer> values = parseRange(strRange);
            command.setRangeSize(values.size());
            for (int i = 0; i < values.size(); i++) {
                command.range[i] = values.get(i);
            }
            stripConfig.commandArray.add(command);
        }

        range.setFocusable(false);
        range.setEnabled(false);
        effects.setFocusable(false);
        effects.setEnabled(false);
    }

    public void shouldConfigBeAddedToMarketplace(){
        if (makePublic.isChecked()){ //the checkbox is checked, indicating that the user wants the config to be displayed in the marketplace
            stripConfig.isPublic = true; //indicate that the configuration should be made public
        }
        else{
            stripConfig.isPublic = false; //indicate that the configuration should be kept private
        }
    }
    public void assignGUIelementsToJavaObjects() {
        buttonSavePublic = findViewById(R.id.buttonSavePublic);
        buttonExample = findViewById(R.id.example);
        buttonApply = findViewById(R.id.buttonApply);
        stripConfig = new LEDConfigPattern("default custom", btnCount);
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
        configName = findViewById(R.id.configNameEditText);
        buttonClear = findViewById(R.id.buttonClear);
        makePublic = findViewById(R.id.makePublic); //assign xml checkbox to makePublic checkbox variable

    }

    public void createPreviewButtons() {
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

    public void createEffectsSpinner() {
        //Creates effects spinner
        ArrayAdapter<String> effectAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, Command.effectList);
        effectAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //effects1.setOnItemSelectedListener(this);
        effects1.setAdapter(effectAdapter);
        effects2.setAdapter(effectAdapter);
    }

    public ArrayList<Integer> parseRange(String strInput) {
        ArrayList<Integer> range = new ArrayList<>();

        if (strInput.equalsIgnoreCase("all")) {
            for (int i = 0; i < btnCount; i++) {
                range.add(i);
            }
            return range;
        }

        if (strInput.equalsIgnoreCase("even")) {
            for (int i = 0; i < btnCount; i++) {
                if (i % 2 == 0) {
                    range.add(i);
                }
            }
            return range;
        }

        if (strInput.equalsIgnoreCase("odd")) {
            for (int i = 0; i < btnCount; i++) {
                if (i % 2 != 0) {
                    range.add(i);
                }
            }
            return range;
        }

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
                        //btn.setBackgroundColor(selectedColor);
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

        //command.color.r < 0
        if (!command.effect.equalsIgnoreCase("custom")) {     //enter values for command
            for (int i = 0; i < command.range.length; i++) {
                previewButtons.get(command.range[i]).setBackgroundColor(col);
            }
            command.color.setRGBfromHex(col);
        } else {                                                             //enter values for timestamp
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

    public void changeBulbColor(int index, ColorObj colorObj) {
        previewButtons.get(index).setBackgroundColor(Color.rgb(colorObj.r, colorObj.g, colorObj.b));
    }

    public void startPreview(View v) {
        //seekBarTime.setProgress(0);
        AsyncTaskRunner runner = new AsyncTaskRunner();
        runner.execute();
    }

    public void saveNameBtnClick(View v) {
        stripConfig.configName = configName.getText().toString();
    }

    public void advanceSeekBar(int newVal) {
        seekBarTime.setProgress(newVal);
    }

    private class AsyncTaskRunner extends AsyncTask<String, String, String> {

        private String resp;

        @Override
        protected String doInBackground(String... params) {

            try {
                //GALAXY_SEEKBAR_INCR
                for (int time = 0; time < MAX_DISPLAY_TIME; time += SEEK_BAR_SPEED) {
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

    //Called from AsyncTask once/milisecond
    public void updatePreviewButtons(int seekBarValue) {

        //TODO effects1.getSelected.... is wrong. What if first comand is solid, second command is custom?

        if (effects1.getSelectedItem().equals("rainbow") || effects2.getSelectedItem().equals("rainbow")) {
            rainbowEffect(seekBarValue, stripConfig);
        }

        if (effects1.getSelectedItem().equals("flash") || effects2.getSelectedItem().equals("flash")) {
            flashEffect(seekBarValue, stripConfig);
        }

        if (effects1.getSelectedItem().equals("custom") || effects2.getSelectedItem().equals("custom")) {
            customEffect(seekBarValue, stripConfig);
        }

        if (effects1.getSelectedItem().equals("solid") || effects2.getSelectedItem().equals("solid")) {
            solidEffect(seekBarValue, stripConfig);
        }

    }

    private void solidEffect(int seekBarValue, LEDConfigPattern stripConfig) {
        if (seekBarValue == 0) {
            for (int i = 0; i < stripConfig.commandArray.size(); i++) {
                if (stripConfig.commandArray.get(i).effect.equalsIgnoreCase("solid")) {
                    Command currentCommand = stripConfig.commandArray.get(i);
                    for (int j = 0; j < currentCommand.range.length; j++) {
                        changeBulbColor(currentCommand.range[j], currentCommand.color);
                    }
                }
            }
        }
    }

    int rainbowIndex = 0;
    String[] rainbowStr = {"#ff0000", "#ffa500", "#ffff00", "#008000", "#0000ff", "#4b0082", "#ee82ee"};

    public void rainbowEffect(int seekBarValue, LEDConfigPattern stripConfig) {

        if (seekBarValue == 0) {
            //stripConfig.createRainbowCommandArray();
            stripConfig.createRainbowRangeArray();
        }

        //System.out.println("seekbar mod rainbow sp :: " + seekBarValue % RAINBOW_SPEED);
        if (seekBarValue % RAINBOW_SPEED == 0) {

            for (int i = 0; i < stripConfig.rangeForRainbowEffect.size(); i++) {
                if (rainbowIndex == 7) {
                    rainbowIndex = 0;
                }
                changeBulbColor(stripConfig.rangeForRainbowEffect.get(i), Color.parseColor(rainbowStr[rainbowIndex]));
                rainbowIndex++;
            }
        }
    }

    public void customEffect(int seekBarValue, LEDConfigPattern stripCon) {

        //Create ArrayList of Timestamps on initial call (at time 0)
        if (seekBarValue == 0) {
            stripCon.createCustomTimestampArray();
            System.out.println("COLOR DEPLOYED SET TO FALSE");
            for (int i = 0; i < stripCon.allCustomTimestamps.size(); i++) {
                stripCon.allCustomTimestamps.get(i).colorDeployed = false;
            }
        }


        for (int i = 0; i < stripCon.allCustomTimestamps.size(); i++) {

            //System.out.println("stripCon.allCustomTimestamps.get("+i+").time = " + stripCon.allCustomTimestamps.get(i).colorDeployed);
            if (stripCon.allCustomTimestamps.get(i).time <= seekBarValue && !stripCon.allCustomTimestamps.get(i).colorDeployed) {
                stripCon.allCustomTimestamps.get(i).colorDeployed = true;

                //System.out.println("stripCon.allCustomTimestamps.get(i).time = " + stripCon.allCustomTimestamps.get(i).time);
                for (int j = 0; j < stripCon.allCustomTimestamps.get(i).range.length; j++) {
                    //System.out.println("stripCon.allCustomTimestamps.get(i).range[j] = " + stripCon.allCustomTimestamps.get(i).range[j]);
                    changeBulbColor(stripCon.allCustomTimestamps.get(i).range[j],
                            stripCon.allCustomTimestamps.get(i).color);
                }
            }
        }

        /*
        if (seekBarValue > MAX_DISPLAY_TIME - SEEK_BAR_SPEED) {
            System.out.println("COLOR DEPLOYED SET TO FALSE");
            for (int i = 0; i < stripCon.allCustomTimestamps.size(); i++) {
                stripCon.allCustomTimestamps.get(i).colorDeployed = false;
            }
        }
        */
    }

    private void flashEffect(int seekBarValue, LEDConfigPattern stripConfig) {
        if (seekBarValue == 0) {
            stripConfig.createFlashCommandArray();
        }

        if (seekBarValue % FLASH_SPEED == 0 && !stripConfig.flashOn) {
            for (int i = 0; i < stripConfig.flashCommands.size(); i++) {
                for (int j = 0; j < stripConfig.flashCommands.get(i).range.length ; j++) {
                    changeBulbColor(stripConfig.flashCommands.get(i).range[j],
                            stripConfig.flashCommands.get(i).color);
                }
            }
            stripConfig.flashOn = true;
        } else if (seekBarValue % FLASH_SPEED == 0 && stripConfig.flashOn) {
            for (int i = 0; i < stripConfig.flashCommands.size(); i++) {
                for (int j = 0; j < stripConfig.flashCommands.get(i).range.length ; j++) {
                    changeBulbColor(stripConfig.flashCommands.get(i).range[j], Color.BLACK);
                }
            }
            stripConfig.flashOn = false;
        }

    }

    //change a public config to private
    public void savePublicToPrivate(View v) {
       // if user indicates that a public config should be made private{
       //     stripConfig.isPublic = false; //indicate that the configuration should be made private
       // }
       // else{
       //     stripConfig.isPublic = true; //indicate that the configuration should remain public
       // }
    }

    public void buttonClearClick(View v) {
        LEDConfigPattern clearConfig = new LEDConfigPattern("clear", btnCount);
        Command command = new Command("solid");
        JSONObject clearStripJson = new JSONObject();

        //set command range
        command.setRangeSize(btnCount);
        for (int i = 0; i < btnCount; i++) {
            command.range[i] = i;
        }
        //set command color = black
        command.color = new ColorObj(0,0,0);
        //add command to config
        clearConfig.commandArray.add(command);
        //make strip config to not public
        clearConfig.isPublic = false;

        //Send to pi
        final commandRequest clearStrip = new commandRequest(Login.piName, Login.userName, clearConfig);
        String clearStripString = gson.toJson(clearStrip);
        try {
            clearStripJson = new JSONObject(clearStripString);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        sendConfigToPi(clearStripJson);

    }
}