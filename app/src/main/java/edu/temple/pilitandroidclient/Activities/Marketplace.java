package edu.temple.pilitandroidclient.Activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
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
    ArrayList<LEDConfigPattern> publicConfigArrayList;
    ListView list;
    Context context = this;
    public static final String MP_CONFIG = "marketplace config";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marketplace);
        list = findViewById(R.id.listConfig);
        homeButton = findViewById(R.id.homeButton);


        //The userProfile which has been passed from the login screen
        publicConfigArrayList = (ArrayList<LEDConfigPattern>) getIntent().getSerializableExtra(User.JSON_ARRAY);

        populateListViewFromAL();
        //sendRequestToServer();
        //populateListViewFromJson();

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("A config has been clicked at position: " + position);
                //System.out.println(configList.getItemAtPosition(position));

                Intent intent = new Intent(context, Config.class);
                intent.putExtra(User.CONFIG_OBJ, (LEDConfigPattern) list.getItemAtPosition(position));
                intent.putExtra(User.EXTRA_PRESENT,true);
                startActivity(intent);
            }
        });

    }

    private void populateListViewFromAL() {
        //create the adapter
        ArrayAdapter<LEDConfigPattern> ledConfigList = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, publicConfigArrayList);


        //create the listView
        ListView list = (ListView) findViewById(R.id.listConfig);
        list.setAdapter(ledConfigList);
    }
}
