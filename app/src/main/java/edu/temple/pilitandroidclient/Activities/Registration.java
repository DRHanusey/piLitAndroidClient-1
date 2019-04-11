package edu.temple.pilitandroidclient.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

import edu.temple.pilitandroidclient.Objects.LoginRegObj;
import edu.temple.pilitandroidclient.R;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class Registration extends AppCompatActivity {
    Button signUp;
    EditText UserName,Password, ConfirmPassword, Email;
    String passwordStr;
    String confirmPasswordStr;
    LoginRegObj userReg;
    String passwordIsGood;
    String strUserName;
    String email;

    private Socket socket;
    //Gson gson = new Gson();
    JSONObject outgoingJson;
    JSONObject incomingJson = new JSONObject();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

     signUp =  findViewById(R.id.SignUpID);
     UserName = findViewById(R.id.UserNameID);
     Email = findViewById(R.id.editText2);
     Password = (EditText)findViewById(R.id.PasswordID);
     Password.setTransformationMethod(new AsteriskPasswordTransformationMethod());
     ConfirmPassword =  findViewById(R.id.ConfirmPasswordID);
     ConfirmPassword.setTransformationMethod(new AsteriskPasswordTransformationMethod());

    signUp.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            passwordStr = Password.getText().toString();
            confirmPasswordStr = ConfirmPassword.getText().toString();
            strUserName = UserName.getText().toString();
            passwordIsGood  = PasswordVerification(passwordStr,confirmPasswordStr);
            email = Email.getText().toString();
            //Log.i("passwordIsGood", passwordIsGood);
            if (passwordIsGood.equals("N/A")){


                //Log.i("password","it didnt work");
                Toast.makeText(getApplicationContext(),"Passwords do not match",Toast.LENGTH_LONG).show();
            }else  {
                //Log.i("password","it work");

                userReg = new LoginRegObj(strUserName,passwordStr);
                try {
                    outgoingJson = new JSONObject();
                    outgoingJson.put("userName", strUserName);
                    outgoingJson.put("password",passwordStr);
                    outgoingJson.put("email", email);
                    //outgoingJson.put("userName", "testUserNAme222");
                    //outgoingJson.put("password","testPassWord");
                    //outgoingJson.put("email", "testEmail@email.com");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //Log.i("******Json Object:",outgoingJson.toString());
                sendMsgToServer(outgoingJson);

                //Toast.makeText(getApplicationContext(),userReg.getUserName()+" "+userReg.getPassword(),Toast.LENGTH_LONG).show();
            }
        }
    });

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
                socket.emit("register", outgoingJson);
                Log.i("******* outgoingJson",outgoingJson.toString());      //Print JSON to Logcat(bottom of screen
            }

        }).on("register", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                incomingJson = (JSONObject)args[0];
                Log.i("&&&&&&& incomingJson:",incomingJson.toString());     //Print JSON to Logcat(bottom of screen
                //toast();
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
    public void toast(){
        Toast.makeText(getApplicationContext(), "Registration complete", Toast.LENGTH_SHORT).show();
    }

    public String PasswordVerification(String passwor, String confirmPas){
        Log.i("password",passwordStr);
        if (passwordStr.length() <6){
            System.out.println("error");

        }
        else if (!passwordStr.equals(confirmPasswordStr)){
            Log.e("bad",passwordStr);
            Log.e("badcon",confirmPasswordStr);

        }else {
            return passwordStr;
        }
        return "N/A";


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
