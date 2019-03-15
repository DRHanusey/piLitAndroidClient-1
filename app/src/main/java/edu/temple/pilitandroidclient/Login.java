package edu.temple.pilitandroidclient;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
                LoginRegObj loginRegObj = new LoginRegObj(inputEmail.getText().toString(),
                        inputPassword.getText().toString());

                Toast.makeText(getApplicationContext(), loginRegObj.toString(), Toast.LENGTH_SHORT).show();

                //TODO: connect with server
                //TODO: send loginRegObj for verification against DB
                //TODO: on succesful login create UserProfileObj
            }
        };
        loginButton = (Button) findViewById(R.id.buttonLogin);
        loginButton.setOnClickListener(loginOCL);


        // Perform register button action
        View.OnClickListener registerOCL = new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //TODO: display a registration screen with email, password1, and password2 (must match, certain length...etc)
                //TODO: clicking register (on the reg screen) should 1)validate email is in correct format 2)passwords match 3)connect/send userObj to server
            }
        };
        registerButton = (Button) findViewById(R.id.buttonRegister);
        registerButton.setOnClickListener(registerOCL);


    }
}
