package edu.temple.pilitandroidclient;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;

import java.util.ArrayList;

public class Pilit extends AppCompatActivity {
    private TextView currentPi;
    private Spinner spinnerStrip, spinnerEffects;
    Context context = this;
    Button changeColorBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pilit);

        currentPi = findViewById(R.id.textCurrentPi);
        spinnerStrip = findViewById(R.id.spinnerStripList);
        changeColorBtn = findViewById(R.id.buttonColor);
        spinnerEffects = findViewById(R.id.spinnerEffectsList);

        //The PiObj which has been passed from the User screen
        PiObj piObj = (PiObj) getIntent().getSerializableExtra(User.PI_OBJ);

        //Display name of Pilit
        currentPi.setText(piObj.customName);

        //Displays the strips associated with the current PiLit
        createStripDropdown(piObj);

        ArrayList<String> effects = new ArrayList<String>();
        effects.add("Strobe");
        effects.add("Pulse");
        effects.add("Fade");
        effects.add("Running");
        effects.add("Shifting");
        createEffectsDropdown(effects);


    }

    public void changeColor(View v){
        ColorPickerDialogBuilder
                .with(context)
                .setTitle("Choose color")
                .initialColor(0xFFFFFFFF)
                .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                .density(12)
                .setOnColorSelectedListener(new OnColorSelectedListener() {
                    @Override
                    public void onColorSelected(int selectedColor) {
                        //Toast.makeText(context, "onColorSelected: 0x" + Integer.toHexString(selectedColor),
                        //Toast.LENGTH_LONG).show();
                    }
                })
                .setPositiveButton("ok", new ColorPickerClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                        changeColorBtn.setBackgroundColor(selectedColor);
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .build()
                .show();
    }

    //NOTE: lights strips are saved as LEDconfigObj
    public void createStripDropdown(PiObj piObj){
        //Creates adapter which populates the spinner(dropdown)
        ArrayAdapter<LEDconfigObj> stripAdapter = new ArrayAdapter<LEDconfigObj>(this,
                android.R.layout.simple_spinner_item, piObj.LEDstripList);
        //Changes the way names are displayed when the drop down is rendered
        stripAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Passes the adapter to the spinner
        spinnerStrip.setAdapter(stripAdapter);
    }

    public void createEffectsDropdown(ArrayList effects){
        //Creates adapter which populates the spinner(dropdown)
        ArrayAdapter<String> effectsAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, effects);
        //Changes the way names are displayed when the drop down is rendered
        effectsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Passes the adapter to the spinner
        spinnerEffects.setAdapter(effectsAdapter);
    }
}
