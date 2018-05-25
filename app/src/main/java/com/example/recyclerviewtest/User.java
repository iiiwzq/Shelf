package com.example.recyclerviewtest;

/**
 * Created by lenovo on 2018/5/23.
 */

import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("userName")
    private String userName;

    @SerializedName("userEmail")
    private String Email;

    public User(){
        super();
    }

    public User(String userName, String email) {
        this.userName = userName;
        Email = email;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getUserName() {

        return userName;
    }

    public String getEmail() {
        return Email;
    }
}
