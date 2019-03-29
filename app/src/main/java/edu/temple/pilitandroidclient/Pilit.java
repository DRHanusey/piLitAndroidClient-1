package edu.temple.pilitandroidclient;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Spinner;
import android.widget.TextView;

public class Pilit extends AppCompatActivity {
    private TextView currentPi;
    private Spinner spinnerLED;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pilit);

        currentPi = findViewById(R.id.textCurrentPi);
        spinnerLED = findViewById(R.id.spinnerPiSelect);

        //The PiObj which has been passed from the User screen
        PiObj piObj = (PiObj) getIntent().getSerializableExtra(User.PI_OBJ);

        //Display name of Pilit
        currentPi.setText(piObj.customName);


    }
}
