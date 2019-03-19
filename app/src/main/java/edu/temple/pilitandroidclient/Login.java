package edu.temple.pilitandroidclient;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class Login extends AppCompatActivity implements Serializable{
    private EditText inputEmail, inputPassword;
    Button loginButton, registerButton, sendTestMsgButton;
    private Socket socket;
    JSONObject msgJson = new JSONObject();
    public static final String USER_OBJ = "passing user obj";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        inputEmail = findViewById(R.id.inputEmail);
        inputPassword = findViewById(R.id.inputPassword);
        loginButton =  findViewById(R.id.buttonLogin);
        registerButton =  findViewById(R.id.buttonRegister);
        sendTestMsgButton = findViewById(R.id.buttonTest);


        // Perform login button action
        View.OnClickListener loginOCL = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginRegObj loginRegObj = new LoginRegObj(inputEmail.getText().toString(),
                        inputPassword.getText().toString());

                //TODO: connect with server
                //TODO: send loginRegObj for verification against DB
                //TODO: on succesful login create UserProfileObj and launches Home activity

                //For testing purposes
                String testEmail;
                if (loginRegObj.getUserName().contentEquals("Email")){
                    testEmail = "TestEmail@test.com";
                } else {
                    testEmail = loginRegObj.getUserName();
                }

                UserProfileObj userProfileObj = new UserProfileObj(testEmail);

                Intent intent = new Intent(Login.this, Home.class);
                intent.putExtra(USER_OBJ,userProfileObj);
                startActivity(intent);


            }
        };
        loginButton.setOnClickListener(loginOCL);


        // Perform register button action
        View.OnClickListener registerOCL = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: display a registration screen with email, password1, and password2 (must match, certain length...etc)
                //TODO: clicking register (on the reg screen) should 1)validate email is in correct format 2)passwords match 3)connect/send userObj to server
            }
        };
        registerButton.setOnClickListener(registerOCL);

        View.OnClickListener testOCL = new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                try {
                    socket = IO.socket("http://192.168.0.7:3000");  //local ip of server, can not use localhost from A.S.
                } catch (URISyntaxException e) {
                    Log.e("URISyntaxException", e.toString());
                }

                String msg = "This is a test";
                try {
                    msgJson.put("type",msg);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {

                    @Override
                    public void call(Object... args) {
                        //socket.emit("command", "hi");
                        socket.emit("command",msgJson );
                        socket.disconnect();
                    }

                }).on("event", new Emitter.Listener() {

                    @Override
                    public void call(Object... args) {
                    }

                }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {

                    @Override
                    public void call(Object... args) {
                    }

                });
                socket.connect();


                Toast.makeText(getApplicationContext(), "Msg sent", Toast.LENGTH_SHORT).show();

            }
        };
        sendTestMsgButton.setOnClickListener(testOCL);


    }
}
