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
    Button loginButton, registerButton, sendTestMsgButton, configButton;
    static public Socket socket;
    public static final String USER_OBJ = "passing user obj";
    JSONObject outgoingJson = new JSONObject();
    JSONObject incomingJson = new JSONObject();
    JSONObject testJson = new JSONObject();
    UserProfileObj userProfileObj;


    public static final String SERVER_ADDRESS =  "https://pi-lit.herokuapp.com";
    //public static final String SERVER_ADDRESS =  "http://192.168.0.7:3000";

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
        configButton = findViewById(R.id.buttonConfig);

        //Insert the https address into the socket
        try {
            socket = IO.socket(SERVER_ADDRESS);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }


        // Perform login button action
        View.OnClickListener loginOCL = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: on successful login create UserProfileObj and launches User activity

                String email = inputEmail.getText().toString();
                String pword = inputPassword.getText().toString();


                //String loggedInUser = "";


                try {
                    outgoingJson.put("userName",email);
                    outgoingJson.put("password",pword);
                    sendMsgToServer(outgoingJson);
                    //todo get user info from server
                    //incomingJson



                } catch (JSONException e) {
                    e.printStackTrace();
                }


                //createTestObj(loggedInUser);


                //Launches the home activity and passes a user profile obj
                //Intent intent = new Intent(Login.this, User.class);
                //intent.putExtra(USER_OBJ,userProfileObj);
                //startActivity(intent);

            }
        };
        loginButton.setOnClickListener(loginOCL);



        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerMe();
            }
        });

        //****************SEND TEST MSG**************************
        View.OnClickListener testOCL = new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                //Insert the https address into the socket\
                /*
                try {
                    socket = IO.socket(SERVER_ADDRESS);
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
                */

                //Create the Json to be sent to server
                try {
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

                        //socket.disconnect();
                    }
                }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
                    @Override
                    public void call(Object... args) {
                        Log.i("EVENT_DISCONNET", "disconnet from login screen");
                    }
                });
                socket.connect();
                Toast.makeText(getApplicationContext(), "test button pressed", Toast.LENGTH_SHORT).show();
            }
        };
        sendTestMsgButton.setOnClickListener(testOCL);
    }

    public void launchUserActivity() throws JSONException {

        String loggedInUser = (String)incomingJson.get("userName");
        createTestObj(loggedInUser);

        //Launches the home activity and passes a user profile obj
        Intent intent = new Intent(Login.this, User.class);
        intent.putExtra(USER_OBJ,userProfileObj);
        startActivity(intent);
    }

    public void sendMsgToServer(final JSONObject outgoingJson){
        //Insert the https address into the socket
        try {
            socket = IO.socket(Login.SERVER_ADDRESS);
        } catch (URISyntaxException e) {
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
                try {
                    launchUserActivity();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                socket.disconnect();
            }
        }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
            }
        });
        socket.connect();
        //Toast.makeText(getApplicationContext(), "test button pressed", Toast.LENGTH_SHORT).show();

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

    public void createTestObj (String testEmail){
        // USER OBJ FOR TESTING
        userProfileObj = new UserProfileObj(testEmail);
        userProfileObj.addPi("168.0.0.1", 400, "living room");         //create mock PiLit for testing
        userProfileObj.addPi("168.0.0.2", 400, "bed room");            //create mock PiLit for testing
        userProfileObj.savedConfigs.add(new LEDconfigObj("Eagles Party!!"));           //create mock LEDconfig obj
        userProfileObj.savedConfigs.add(new LEDconfigObj("Red White and Blue"));       //create mock LEDconfig obj
        userProfileObj.savedConfigs.add(new LEDconfigObj("Seizure inducing party!"));  //create mock LEDconfig obj
        userProfileObj.savedConfigs.add(new LEDconfigObj("Get Lit with PiLit!!"));     //create mock LEDconfig obj
        userProfileObj.savedConfigs.add(new LEDconfigObj("Sexy time lights"));         //create mock LEDconfig obj
        userProfileObj.savedConfigs.add(new LEDconfigObj("Graduation celebration"));   //create mock LEDconfig obj
        userProfileObj.savedConfigs.add(new LEDconfigObj("Happy Bday"));               //create mock LEDconfig obj

    }

    //For TESTING
    public void configScreen(View v){
        Intent intent = new Intent(this, Config.class);
        startActivity(intent);
    }

}



