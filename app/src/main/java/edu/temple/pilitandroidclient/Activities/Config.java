package edu.temple.pilitandroidclient.Activities;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;

import edu.temple.pilitandroidclient.R;

public class Config extends AppCompatActivity {

    Button button;
    final Context context = this;
    int btnCount = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        button = findViewById(R.id.button);

        LinearLayout ll = (LinearLayout)findViewById(R.id.linLay);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        for(int i = 0; i < btnCount ; i++ ) {
            final Button myButton = new Button(this);
            myButton.setText(""+i);
            myButton.setWidth(10);

            View.OnClickListener clickOCL = new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    myButton.setBackgroundColor(0xD4FBCE);
                }
            };
            myButton.setOnClickListener(clickOCL);

            ll.addView(myButton, lp);
        }


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
                        button.setBackgroundColor(selectedColor);
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


}
