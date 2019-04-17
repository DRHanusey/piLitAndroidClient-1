package edu.temple.pilitandroidclient.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

import edu.temple.pilitandroidclient.Objects.LEDconfigObj;
import edu.temple.pilitandroidclient.R;

public class Marketplace extends AppCompatActivity {

    //array of options -> array adapter -> use the adapter to populate the listView
    Button homeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marketplace);

        //todo 1. connect to server 2. return is list of configs 3.for loop to convert configs to java obj

        homeButton = findViewById(R.id.homeButton);

        populateListView();

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
