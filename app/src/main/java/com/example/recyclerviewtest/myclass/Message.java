package com.example.recyclerviewtest.myclass;

import com.google.gson.annotations.SerializedName;

/**
 * Created by lenovo on 2018/6/1.
 */

public class Message {
    @SerializedName("code")
    private String code;

    @SerializedName("message")
    private String message;

    public Message(){
        super();
    }
    public Message(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
