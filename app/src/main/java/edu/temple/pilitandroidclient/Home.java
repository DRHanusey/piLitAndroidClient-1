package edu.temple.pilitandroidclient;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class Home extends AppCompatActivity{
    private TextView currentUser;
    private Spinner spinner;
    private Button addBtn, removeBtn, selectBtn, marketBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //connect java items with their GUI representations
        currentUser = findViewById(R.id.textCurrentUser);
        spinner = findViewById(R.id.spinnerPiSelect);
        selectBtn = findViewById(R.id.buttonSelectPi);
        addBtn = findViewById(R.id.buttonAddPiLit);
        removeBtn = findViewById(R.id.buttonRemovePiLit);
        marketBtn = findViewById(R.id.buttonMarketPlace);

        //The userProfile which has been passed from the login screen
        UserProfileObj userProfileObj = (UserProfileObj) getIntent().getSerializableExtra(Login.USER_OBJ);

        //Display name of user
        currentUser.setText(userProfileObj.userEmail);

        createPiDropdown(userProfileObj);

        //Takes you to marketplace activity
        marketBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, Marketplace.class);
                startActivity(intent);
            }//end onclick
        }//end

        );




    }


    public void createPiDropdown(UserProfileObj userProfileObj){
        //Creates adapter which populates the spinner(dropdown)
        ArrayAdapter<PiObj> piAdapter = new ArrayAdapter<PiObj>(this,android.R.layout.simple_spinner_item, userProfileObj.PiList);
        //Changes the way names are displayed when the drop down is rendered
        piAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Passes the adapter to the actual spinner
        spinner.setAdapter(piAdapter);
    }

}
