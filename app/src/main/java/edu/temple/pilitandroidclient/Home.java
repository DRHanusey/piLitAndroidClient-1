package edu.temple.pilitandroidclient;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.TextView;

public class Home extends AppCompatActivity{
    private TextView currentUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        UserProfileObj userProfileObj = (UserProfileObj) getIntent().getSerializableExtra(Login.USER_OBJ);

        currentUser = findViewById(R.id.textCurrentUser);
        currentUser.setText(userProfileObj.userEmail);



    }

}
