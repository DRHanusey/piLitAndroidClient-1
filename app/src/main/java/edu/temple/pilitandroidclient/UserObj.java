package edu.temple.pilitandroidclient;

import java.util.ArrayList;

public class UserObj {

    private String userName;
    private String userPassword;


    public UserObj(String userName, String userPassword) {
        this.userName = userName;
        this.userPassword = userPassword;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    @Override
    public String toString() {
        return "UserObj{" +
                "userName='" + userName + '\'' +
                ", userPassword='" + userPassword + '\'' +
                '}';
    }
}
