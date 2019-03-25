package edu.temple.pilitandroidclient;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.logging.Level;

public class Home extends AppCompatActivity{
    private TextView currentUser;
    private Spinner spinner;
    private Button selectPi, marketPlace;
    private ListView configList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //connect java items with their GUI representations
        currentUser = findViewById(R.id.textCurrentUser);
        spinner = findViewById(R.id.spinnerPiSelect);
        selectPi = findViewById(R.id.buttonSelectPi);
        marketPlace = findViewById(R.id.buttonMarketPlace);
        configList = findViewById(R.id.listSavedConfigs);


        //The userProfile which has been passed from the login screen
        UserProfileObj userProfileObj = (UserProfileObj) getIntent().getSerializableExtra(Login.USER_OBJ);

        //Display name of user
        currentUser.setText(userProfileObj.userEmail);

        //Displays the User's Pis in a drop down
        createPiDropdown(userProfileObj);

        //Displays the user's saved configurations
        createSavedConfigList(userProfileObj);

        View.OnClickListener selectPiOCL = new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //Launches the home activity and passes a user profile obj
                //Intent intent = new Intent(Login.this, Home.class);
                //intent.putExtra(USER_OBJ,userProfileObj);
                //startActivity(intent);

            }
        };
        selectPi.setOnClickListener(selectPiOCL);


    }

    public void createSavedConfigList(UserProfileObj userProfileObj){

        //Creates adapter which populates the list view
        ArrayAdapter<LEDconfigObj> configAdpater = new ArrayAdapter<LEDconfigObj>(this,
                android.R.layout.simple_list_item_1, userProfileObj.savedConfigs);
        //Passes the adapter to the list view
        configList.setAdapter(configAdpater);
    }


    public void createPiDropdown(UserProfileObj userProfileObj){
        //Creates adapter which populates the spinner(dropdown)
        ArrayAdapter<PiObj> piAdapter = new ArrayAdapter<PiObj>(this,
                android.R.layout.simple_spinner_item, userProfileObj.PiList);
        //Changes the way names are displayed when the drop down is rendered
        piAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Passes the adapter to the spinner
        spinner.setAdapter(piAdapter);
    }

}
