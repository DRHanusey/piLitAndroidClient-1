package edu.temple.pilitandroidclient.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;
import java.net.URISyntaxException;

import edu.temple.pilitandroidclient.Objects.LEDConfigPattern;
import edu.temple.pilitandroidclient.Objects.UserProfileObj;
import edu.temple.pilitandroidclient.R;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class Login extends AppCompatActivity {
    //TODO make global variables local
    private EditText inputEmail, inputPassword;
    Button loginButton, registerButton, sendTestMsgButton, configButton;
    static public Socket socket;
    public static final String USER_OBJ = "passing user obj";
    JSONObject outgoingJson;
    JSONObject incomingJson;
    JSONObject testJson = new JSONObject();
    UserProfileObj userProfileObj;
    public static String userName, piName;
    Gson gson = new Gson();
    public boolean waitingForServerResponse;

    public static final String SERVER_ADDRESS =  "https://pi-lit.herokuapp.com";

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

        connectSocket();

        // Perform login button action
        View.OnClickListener loginOCL = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                outgoingJson = new JSONObject();
                incomingJson = new JSONObject();

                userName = inputEmail.getText().toString(); //"testuser"; //"danhan";  //
                String pword = inputPassword.getText().toString(); //  "password"; //"pppppp";

                try {
                    outgoingJson.put("userName",userName);
                    outgoingJson.put("password",pword);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                sendMsgToServer(outgoingJson);
                //connectAndSend();

            }
        };
        loginButton.setOnClickListener(loginOCL);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerMe();
            }
        });
    }

    public void handleServerResponse(JSONObject serverResponse){

        try {
            String error = serverResponse.get("error").toString();
            if ( error.equals("") ) {
                launchUserActivity();
            } else {
                System.out.println("WE HAVE AN ERROR::: " + error);
                //Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void launchUserActivity() throws JSONException {

        userProfileObj = new UserProfileObj();
        userProfileObj = gson.fromJson(incomingJson.toString(),UserProfileObj.class);
        if (!userProfileObj.piList.isEmpty()) {
            piName = userProfileObj.piList.get(0).piName;
        }

        //Launches the home activity and passes a user profile obj
        Intent intent = new Intent(Login.this, User.class);
        intent.putExtra(USER_OBJ, userProfileObj);
        startActivity(intent);

    }

    public void connectSocket(){
        //Insert the https address into the socket
        try {
            socket = IO.socket(Login.SERVER_ADDRESS);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        socket.connect();
    }

    public void sendMsgToServer(final JSONObject outgoingJson){
        waitingForServerResponse = true;

        socket.emit("login", outgoingJson);
        Log.i("******* outgoingJson",outgoingJson.toString());

        Login.socket.on("login", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                incomingJson = (JSONObject)args[0];
                Log.i("&&&&&&& incomingJson:",incomingJson.toString());     //Print JSON to Logcat(bottom of screen
                //waitingForServerResponse = false;
                handleServerResponse(incomingJson);
            }
        });

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
        userProfileObj.addPi("168.0.0.1", "living room");         //create mock PiLit for testing
        userProfileObj.addPi("168.0.0.2", "bed room");            //create mock PiLit for testing

        //NEW ADDED CODE
        userProfileObj.configs.add(new LEDConfigPattern("Eagles Party!!", 30));           //create mock LEDconfig obj
        userProfileObj.configs.add(new LEDConfigPattern("Red White and Blue", 30));       //create mock LEDconfig obj
        userProfileObj.configs.add(new LEDConfigPattern("Seizure inducing party!", 30));  //create mock LEDconfig obj
        userProfileObj.configs.add(new LEDConfigPattern("Get Lit with PiLit!!", 30));     //create mock LEDconfig obj
        userProfileObj.configs.add(new LEDConfigPattern("Sexy time lights", 30));         //create mock LEDconfig obj
        userProfileObj.configs.add(new LEDConfigPattern("Graduation celebration", 30));   //create mock LEDconfig obj
        userProfileObj.configs.add(new LEDConfigPattern("Happy Bday",30));               //create mock LEDconfig obj
            }

    //For TESTING
    public void configScreen(View v){
        Intent intent = new Intent(this, Config.class);
        startActivity(intent);
    }
}