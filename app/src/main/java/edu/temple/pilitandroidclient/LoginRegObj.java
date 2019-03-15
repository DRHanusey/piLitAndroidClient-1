package edu.temple.pilitandroidclient;

import java.util.ArrayList;

public class LoginRegObj {

    private String userName;
    private String userPassword;


    public LoginRegObj(String userName, String userPassword) {
        this.userName = userName;
        this.userPassword = userPassword;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    //for testing
    @Override
    public String toString() {
        return "LoginRegObj{" +
                "userName='" + userName + '\'' +
                ", userPassword='" + userPassword + '\'' +
                '}';
    }
}
