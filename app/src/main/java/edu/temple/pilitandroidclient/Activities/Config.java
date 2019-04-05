package edu.temple.pilitandroidclient.Activities;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.Arrays;

import edu.temple.pilitandroidclient.Objects.LEDConfigPattern;
import edu.temple.pilitandroidclient.R;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class Config extends AppCompatActivity {

    Button buttonBig,buttonExample;
    final Context context = this;
    int btnCount = 20;
    Button testBtn;
    Drawable circle;

    private Socket socket;
    JSONObject outgoingJson = new JSONObject();
    JSONObject incomingJson = new JSONObject();
    LEDConfigPattern lcp = new LEDConfigPattern("Test Config");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        buttonBig = findViewById(R.id.buttonBig);
        buttonExample = findViewById(R.id.example);
        circle = getResources().getDrawable(R.drawable.circle);

        LinearLayout ll = findViewById(R.id.linLay);

        for(Integer i = 0; i < btnCount ; i++ ) {
            final Button myButton = new Button(context);
            myButton.setText(i.toString());
            myButton.setLayoutParams(buttonExample.getLayoutParams());
            //myButton.setBackground(circle);

            View.OnClickListener clickOCL = new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    changeColor(myButton);
                    buttonBig.setText(myButton.getText());
                }
            };
            myButton.setOnClickListener(clickOCL);

            ll.addView(myButton);
        }
        buttonExample.setVisibility(View.INVISIBLE);
        //testBtn = findViewById(R.id.buttonTest);


    }

    public void clickApply(View v){
        //Insert the https address into the socket
        try {
            socket = IO.socket(Login.SERVER_ADDRESS);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        //Create the Json to be sent to server
        try {
            //testJson.put("type","This is just a test!!");
            outgoingJson.put("userName","testuser");
            outgoingJson.put("password","password");

        } catch (JSONException e) {
            e.printStackTrace();
        }


        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {

            @Override
            public void call(Object... args) {
                socket.emit("login", outgoingJson);
                Log.i("******* outgoingJson",outgoingJson.toString());      //Print JSON to Logcat(bottom of screen
            }

        }).on("login", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                incomingJson = (JSONObject)args[0];
                Log.i("&&&&&&& incomingJson:",incomingJson.toString());     //Print JSON to Logcat(bottom of screen
                socket.disconnect();
            }
        }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
            }
        });
        socket.connect();
        Toast.makeText(getApplicationContext(), "test button pressed", Toast.LENGTH_SHORT).show();
    }


    public void changeColor(final Button btn){
        ColorPickerDialogBuilder
                .with(this)
                .setTitle("Choose color")
                .initialColor(0xFFFFFFFF)
                .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                .density(12)
                .setOnColorSelectedListener(new OnColorSelectedListener() {
                    @Override
                    public void onColorSelected(int selectedColor) {
                    }
                })
                .setPositiveButton("ok", new ColorPickerClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                        btn.setBackgroundColor(selectedColor);
                        int[] rgb = convertHexToRgb(selectedColor);
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

    public int[] convertHexToRgb(int hexVal){
        int r, g, b;
        r = Color.red(hexVal);
        g = Color.green(hexVal);
        b = Color.blue(hexVal);
        Log.i("**********rgb values:", r+" "+g+" "+b);
        int RGB[] = {r,g,b};
        return RGB;
    }


}
