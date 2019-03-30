package edu.temple.pilitandroidclient.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

import edu.temple.pilitandroidclient.Objects.LEDconfigObj;
import edu.temple.pilitandroidclient.Objects.LoginRegObj;
import edu.temple.pilitandroidclient.Objects.UserProfileObj;
import edu.temple.pilitandroidclient.R;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class Login extends AppCompatActivity {
    private EditText inputEmail, inputPassword;
    Button loginButton, registerButton, sendTestMsgButton;
    private Socket socket;
    JSONObject msgJson = new JSONObject();
    public static final String USER_OBJ = "passing user obj";

    //TODO insert central server ip
    public static final String SERVER_IP_PORT = "http://192.168.0.7:3000";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        inputEmail = findViewById(R.id.inputEmail);
        inputPassword = findViewById(R.id.inputPassword);
        inputPassword.setTransformationMethod(new AsteriskPasswordTransformationMethod());

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
                //TODO: on successful login create UserProfileObj and launches User activity

                //For testing purposes
                String testEmail;
                if (loginRegObj.getUserName().contentEquals("Email")){
                    testEmail = "TestEmail@test.com";
                } else {
                    testEmail = loginRegObj.getUserName();
                }

                // USER OBJ FOR TESTING
                UserProfileObj userProfileObj = new UserProfileObj(testEmail);
                userProfileObj.addPi("168.0.0.1", 400, "living room");         //create mock PiLit for testing
                userProfileObj.addPi("168.0.0.2", 400, "bed room");            //create mock PiLit for testing
                userProfileObj.savedConfigs.add(new LEDconfigObj("Eagles Party!!"));           //create mock LEDconfig obj
                userProfileObj.savedConfigs.add(new LEDconfigObj("Red White and Blue"));       //create mock LEDconfig obj
                userProfileObj.savedConfigs.add(new LEDconfigObj("Seizure inducing party!"));  //create mock LEDconfig obj
                userProfileObj.savedConfigs.add(new LEDconfigObj("Get Lit with PiLit!!"));     //create mock LEDconfig obj
                userProfileObj.savedConfigs.add(new LEDconfigObj("Sexy time lights"));         //create mock LEDconfig obj
                userProfileObj.savedConfigs.add(new LEDconfigObj("Graduation celebration"));   //create mock LEDconfig obj
                userProfileObj.savedConfigs.add(new LEDconfigObj("Happy Bday"));               //create mock LEDconfig obj


              
                //Launches the home activity and passes a user profile obj
                Intent intent = new Intent(Login.this, User.class);
                intent.putExtra(USER_OBJ,userProfileObj);
                startActivity(intent);

            }
        };
        loginButton.setOnClickListener(loginOCL);



        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerMe();
            }
        });

        View.OnClickListener testOCL = new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                try {
                    socket = IO.socket(SERVER_IP_PORT);  //local ip of server, can not use localhost from A.S.
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

    public void registerMe(){

        Intent intent = new Intent(this, Registration.class);
        startActivity(intent);
    }


    public class AsteriskPasswordTransformationMethod extends PasswordTransformationMethod {
        @Override
        public CharSequence getTransformation(CharSequence source, View view) {
            return new PasswordCharSequence(source);
        }

        private class PasswordCharSequence implements CharSequence {
            private CharSequence mSource;
            public PasswordCharSequence(CharSequence source) {
                mSource = source; // Store char sequence
            }
            public char charAt(int index) {
                return '*'; // This is the important part
            }
            public int length() {
                return mSource.length(); // Return default
            }
            public CharSequence subSequence(int start, int end) {
                return mSource.subSequence(start, end); // Return default
            }
        }
    }
}



