package edu.temple.pilitandroidclient;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

public class Marketplace extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marketplace);

        ListView list;
        list = findViewById(R.id.listConfig);

        ArrayList<LEDconfigObj> LEDlist = new ArrayList<LEDconfigObj>();
        //LEDlist.add();
        //test
        //hello


    }
}
