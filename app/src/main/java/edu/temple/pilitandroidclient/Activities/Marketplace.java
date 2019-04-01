package edu.temple.pilitandroidclient;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class Marketplace extends AppCompatActivity {

    //array of options -> array adapter -> use the adapter to populate the listView

    Button homeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marketplace);


        homeButton = findViewById(R.id.homeButton);

        populateListView();

    }

    private void populateListView(){
        //create list of items
        //String[] myItems = {"blue", "green", "red", "yellow"};
        ArrayList<LEDconfigObj> myPatterns = new ArrayList<LEDconfigObj>();
        int[] nums = {3,4,5};

        LEDconfigObj partyLights = new LEDconfigObj(5, nums, 3, 4);
        LEDconfigObj sleepLights = new LEDconfigObj(5, nums, 3, 4);
        LEDconfigObj gameLights = new LEDconfigObj(5, nums, 3, 4);

        myPatterns.add(partyLights);
        myPatterns.add(sleepLights);
        myPatterns.add(gameLights);

        //create the adapter
        //ArrayAdapter<String> colorList = new ArrayAdapter<String>(this, R.layout.activity_marketplace, myItems);
        ArrayAdapter<LEDconfigObj> ledConfigList = new ArrayAdapter<LEDconfigObj>(this, R.layout.activity_marketplace, myPatterns);

        //create the listView
        //list.setAdapter(colorList);
        ListView list = (ListView) findViewById(R.id.listScroll);
        list.setAdapter(ledConfigList);

    }

    public void goHome(View v){

        Intent intent = new Intent(this, Home.class);
        startActivity(intent);
    }

}
