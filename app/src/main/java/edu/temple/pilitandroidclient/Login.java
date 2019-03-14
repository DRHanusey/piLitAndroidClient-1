package edu.temple.pilitandroidclient;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends AppCompatActivity {
    private EditText inputEmail, inputPassword;
    Button loginButton, registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inputEmail = (EditText) findViewById(R.id.inputEmail);
        inputPassword = (EditText) findViewById(R.id.inputPassword);


        // Perform login button action
        View.OnClickListener loginOCL = new View.OnClickListener(){
            @Override
            public void onClick(View v){
                UserObj userObj = new UserObj(inputEmail.getText().toString(),
                        inputPassword.getText().toString());

                Toast.makeText(getApplicationContext(), userObj.toString(), Toast.LENGTH_SHORT).show();

                //TODO: connect with server
                //TODO: send userObj for verification against DB
            }
        };
        loginButton = (Button) findViewById(R.id.buttonLogin);
        loginButton.setOnClickListener(loginOCL);


        // Perform register button action
        View.OnClickListener registerOCL = new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //TODO: display a registration screen
            }
        };
        registerButton = (Button) findViewById(R.id.buttonRegister);
        registerButton.setOnClickListener(registerOCL);


    }
}
