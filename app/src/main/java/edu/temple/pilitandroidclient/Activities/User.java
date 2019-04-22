package edu.temple.pilitandroidclient.Activities;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.lang.reflect.Type;
import java.util.ArrayList;

import edu.temple.pilitandroidclient.Objects.LEDConfigPattern;
import edu.temple.pilitandroidclient.Objects.UserProfileObj;
import edu.temple.pilitandroidclient.Objects.PiObj;
//import edu.temple.pilitandroidclient.Objects.UserProfileObj;
import edu.temple.pilitandroidclient.R;
import io.socket.emitter.Emitter;

public class User extends AppCompatActivity {
    private TextView currentUser;
    private Spinner spinner;
    private Button selectPi, marketPlace;
    private ListView configList;
    public static final String PI_OBJ = "passing pi obj";
    public Context context = this;
    public static final String CONFIG_OBJ = "passing config obj";
    public static final String JSON_ARRAY = "passing json array";
    JSONArray incomingJsonArray;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        //connect java items with their GUI representations
        currentUser = findViewById(R.id.textCurrentPi);
        spinner = findViewById(R.id.spinnerPiSelect);
        selectPi = findViewById(R.id.buttonSelectPi);
        marketPlace = findViewById(R.id.buttonMarketPlace);
        configList = findViewById(R.id.listSavedConfigs);


        //The userProfile which has been passed from the login screen
        UserProfileObj userProfileObj = (UserProfileObj) getIntent().getSerializableExtra(Login.USER_OBJ);

        //Display name of user
        currentUser.setText(userProfileObj.userName);

        //Displays the User's Pis in a drop down
        if (!userProfileObj.piList.isEmpty()) {
            createPiDropdown(userProfileObj);
        } else {
            Toast.makeText(getApplicationContext(), "There are no PiLits associated with this account", Toast.LENGTH_SHORT).show();
        }

        //Displays the user's saved configurations
        createSavedConfigList(userProfileObj);


        configList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //view.setBackgroundColor(Color.CYAN);

                System.out.println("A config has been clicked at position: " + position);
                //System.out.println(configList.getItemAtPosition(position));

                Intent intent = new Intent(context, Config.class);
                intent.putExtra(CONFIG_OBJ, (LEDConfigPattern) configList.getItemAtPosition(position));
                startActivity(intent);
            }
        });

        marketPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Login.socket.emit("getPublicConfigs", "");

                Login.socket.on("getPublicConfigs", new Emitter.Listener() {
                    @Override
                    public void call(Object... args) {
                        incomingJsonArray = (JSONArray) args[0];
                        Log.i("&&&&&&& incomingJson:", incomingJsonArray.toString());
                        System.out.println("incomingJsonArray.length()::: " + incomingJsonArray.length());


                        Type type = new TypeToken<ArrayList<LEDConfigPattern>>() {}.getType();
                        ArrayList<LEDConfigPattern> publicConfigsArray = new Gson().fromJson(incomingJsonArray.toString(), type);

                        System.out.println("pubConfigs size: " + publicConfigsArray.size());


                        Intent intent = new Intent(User.this, Marketplace.class);
                        intent.putExtra(JSON_ARRAY, publicConfigsArray);
                        startActivity(intent);
                    }
                });
            }
        });

    }


    public void selectPi(View v) {
        //Launches the home activity and passes a user profile obj
        Intent intent = new Intent(User.this, Pilit.class);
        intent.putExtra(PI_OBJ, (PiObj) spinner.getSelectedItem());
        startActivity(intent);
    }

    public void createSavedConfigList(UserProfileObj userProfileObj) {
        //Creates adapter which populates the list view
        ArrayAdapter<LEDConfigPattern> configAdapter = new ArrayAdapter<LEDConfigPattern>(this, android.R.layout.simple_list_item_1, userProfileObj.configs);

        configList.setAdapter(configAdapter);
    }


    public void createPiDropdown(UserProfileObj userProfileObj) {
        System.out.println(userProfileObj.piList.get(0).piName);

        //Creates adapter which populates the spinner(dropdown)
        ArrayAdapter<PiObj> piAdapter = new ArrayAdapter<PiObj>(this,
                android.R.layout.simple_spinner_item, userProfileObj.piList);

        //Changes the way names are displayed when the drop down is rendered
        piAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //Passes the adapter to the spinner
        spinner.setAdapter(piAdapter);
    }

}
