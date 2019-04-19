package edu.temple.pilitandroidclient.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import edu.temple.pilitandroidclient.Objects.LEDConfigPattern;
import edu.temple.pilitandroidclient.Objects.LEDconfigObj;
import edu.temple.pilitandroidclient.Objects.PiObj;
import edu.temple.pilitandroidclient.Objects.commandRequest;
import edu.temple.pilitandroidclient.R;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class Marketplace extends AppCompatActivity {

    //array of options -> array adapter -> use the adapter to populate the listView
    Button homeButton;
    JSONObject incomingJson = new JSONObject();
    JSONArray incomingJsonArray = new JSONArray();
    Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marketplace);


        homeButton = findViewById(R.id.homeButton);

        //populateListView();
        sendRequestToServer();
        populateListViewFromJson();

    }

    private void sendRequestToServer(){

        Login.socket.emit("getPublicConfigs", "");

        Login.socket.on("getPublicConfigs", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                incomingJsonArray = (JSONArray) args[0];
                Log.i("&&&&&&& incomingJson:",incomingJsonArray.toString());
            }
        });

    }

    private void populateListViewFromJson(){

        //TODO convert JSON to JAVA object
        /*
        "incomingJson"  <- should be an array of LEDConfigPattern objects (but in json form)
        Convert from "incomingJson" to "myPatterns" (see line 77)
         */



        //create list of items
        ArrayList<LEDConfigPattern> myPatterns = new ArrayList<LEDConfigPattern>();

        //create the adapter
        ArrayAdapter<LEDConfigPattern> ledConfigList = new ArrayAdapter<LEDConfigPattern>(this,
                android.R.layout.simple_list_item_1, myPatterns);


        //create the listView
        ListView list = (ListView) findViewById(R.id.listConfig);
        list.setAdapter(ledConfigList);
    }

    private void populateListView(){

        LEDconfigObj partyLights = new LEDconfigObj("partyLights");
        LEDconfigObj sleepLights = new LEDconfigObj("sleepLights");
        LEDconfigObj gameLights = new LEDconfigObj("gameLights");

        //create list of items
        ArrayList<LEDconfigObj> myPatterns = new ArrayList<LEDconfigObj>();
        myPatterns.add(partyLights);
        myPatterns.add(sleepLights);
        myPatterns.add(gameLights);

        //create the adapter
        ArrayAdapter<LEDconfigObj> ledConfigList = new ArrayAdapter<LEDconfigObj>(this,
                android.R.layout.simple_list_item_1, myPatterns);


        //create the listView
        ListView list = (ListView) findViewById(R.id.listConfig);
        list.setAdapter(ledConfigList);

    }

    public void goHome(View v){

        Intent intent = new Intent(this, User.class);
        startActivity(intent);
    }

}
