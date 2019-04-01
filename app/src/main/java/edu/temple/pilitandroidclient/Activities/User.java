package edu.temple.pilitandroidclient.Activities;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import edu.temple.pilitandroidclient.Objects.UserProfileObj;
import edu.temple.pilitandroidclient.Objects.LEDconfigObj;
import edu.temple.pilitandroidclient.Objects.PiObj;
//import edu.temple.pilitandroidclient.Objects.UserProfileObj;
import edu.temple.pilitandroidclient.R;

public class User extends AppCompatActivity{
    private TextView currentUser;
    private Spinner spinner;
    private Button selectPi, marketPlace;
    private ListView configList;
    public static final String PI_OBJ = "passing pi obj";


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
        currentUser.setText(userProfileObj.userEmail);

        //Displays the User's Pis in a drop down
        createPiDropdown(userProfileObj);

        //Displays the user's saved configurations
        createSavedConfigList(userProfileObj);

        marketPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(User.this, Marketplace.class);
                startActivity(intent);
            }
        });

    }

    public void selectPi(View v){
        //Launches the home activity and passes a user profile obj
        Intent intent = new Intent(User.this, Pilit.class);
        intent.putExtra(PI_OBJ,(PiObj)spinner.getSelectedItem());
        startActivity(intent);
    }

    public void createSavedConfigList(UserProfileObj userProfileObj){

        //Creates adapter which populates the list view
        ArrayAdapter<LEDconfigObj> configAdapter = new ArrayAdapter<LEDconfigObj>(this,
                android.R.layout.simple_list_item_1, userProfileObj.savedConfigs);

        //Passes the adapter to the list view
        configList.setAdapter(configAdapter);
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
