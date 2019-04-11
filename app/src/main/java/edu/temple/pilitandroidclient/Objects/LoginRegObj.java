package edu.temple.pilitandroidclient.Objects;

import java.util.ArrayList;

public class LoginRegObj {

    private String userName;
    private String password;


    public LoginRegObj(String userName, String userPassword) {
        this.userName = userName;
        this.password = userPassword;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    //for testing
    @Override
    public String toString() {
        return "LoginRegObj{" +
                "userName='" + userName + '\'' +
                ", userPassword='" + password + '\'' +
                '}';
    }
}
