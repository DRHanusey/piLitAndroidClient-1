package edu.temple.pilitandroidclient.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import edu.temple.pilitandroidclient.Objects.LoginRegObj;
import edu.temple.pilitandroidclient.R;

public class Registration extends AppCompatActivity {
    Button signUp;
    EditText UserName,Password, ConfirmPassword;
    String passwordStr;
    String confirmPasswordStr;
    LoginRegObj userReg;
    String passwordIsGood;
    String strUserName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

     signUp =  findViewById(R.id.SignUpID);

     UserName = findViewById(R.id.UserNameID);

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
            //Log.i("passwordIsGood", passwordIsGood);
            if (passwordIsGood.equals("N/A")){


                //Log.i("password","it didnt work");
                Toast.makeText(getApplicationContext(),"Passwords do not match",Toast.LENGTH_LONG).show();
            }else  {
                //Log.i("password","it work");

                userReg = new LoginRegObj(strUserName,passwordStr);
                Toast.makeText(getApplicationContext(),userReg.getUserName()+" "+userReg.getUserPassword(),Toast.LENGTH_LONG).show();
            }
        }
    });


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
