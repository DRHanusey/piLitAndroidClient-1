package edu.temple.pilitandroidclient;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Registration extends AppCompatActivity {
    Button signUp;
    EditText UserName,Password, ConfirmPassword;
     String passwordStr;
     String confirmPasswordStr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

     signUp = (Button) findViewById(R.id.SignUpID);

     UserName= (EditText)findViewById(R.id.UserNameID);
     Password = (EditText) findViewById(R.id.PasswordID);
     ConfirmPassword = (EditText) findViewById(R.id.ConfirmPasswordID);
     passwordStr = Password.getText().toString();
     confirmPasswordStr = ConfirmPassword.getText().toString();

    signUp.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            PasswordVerification(passwordStr,confirmPasswordStr);
        }
    });

    }

    public boolean PasswordVerification(String passowrd, String confirmPassword){

        if (passowrd.length() <6){
            System.out.println("error");
            return false;
        }
        if (!passowrd.equals(confirmPassword)){
            return false;
        }


        return true;
    }
}
