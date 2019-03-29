package edu.temple.pilitandroidclient.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

import edu.temple.pilitandroidclient.Objects.LEDconfigObj;
import edu.temple.pilitandroidclient.R;

public class Marketplace extends AppCompatActivity {

    //array of options -> array adapter -> use the adapter to populate the listView
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marketplace);

        populateListView();
    }

    private void populateListView(){
        //create list of items
        //String[] myItems = {"blue", "green", "red", "yellow"};
        ArrayList<LEDconfigObj> myPatterns = new ArrayList<LEDconfigObj>();
        int[] nums = {3,4,5};

        //LEDconfigObj partyLights = new LEDconfigObj(5, nums, 3, 4);
        //LEDconfigObj sleepLights = new LEDconfigObj(5, nums, 3, 4);
        //LEDconfigObj gameLights = new LEDconfigObj(5, nums, 3, 4);

        //myPatterns.add(partyLights);
        //myPatterns.add(sleepLights);
        //myPatterns.add(gameLights);

        //create the adapter
        //ArrayAdapter<String> colorList = new ArrayAdapter<String>(this, R.layout.activity_marketplace, myItems);
        ArrayAdapter<LEDconfigObj> ledConfigList = new ArrayAdapter<LEDconfigObj>(this, R.layout.activity_marketplace, myPatterns);

        //create the listView
        //list.setAdapter(colorList);
        //ListView list = (ListView) findViewById(R.id.listScroll);
        //list.setAdapter(ledConfigList);



    }

}
