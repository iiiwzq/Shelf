package com.example.recyclerviewtest.util;

import com.example.recyclerviewtest.myclass.Goods;
import com.example.recyclerviewtest.myclass.Message;
import com.example.recyclerviewtest.myclass.ShoppingCartBean;
import com.example.recyclerviewtest.myclass.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2018/5/20.
 */

public class Utility {

    private static Gson gson = new GsonBuilder().create();

    /**
     * 将返回的JSON数据解析成Goods实体类
     */

    public static List<Goods> handleGoodsResponse(String response) {

        try {
            List<Goods> list = gson.fromJson(response, new TypeToken<ArrayList<Goods>>() {
            }.getType());
            return list;

        } catch (Exception e) {

            e.printStackTrace();

        }

        return null;

    }

    /**
     * 将返回的JSON数据解析成User实体类
     */
    public static User handleUserResponse(String response) {
        try {
            return gson.fromJson(response, User.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 将返回的JSON数据解析成Message实体类
     */
    public static Message handleMessageResponse(String response){
        try{
            return gson.fromJson(response,Message.class);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 将返回的JSON数据解析成ShoppingCartAdapter
     */
    public static List<ShoppingCartBean> handleShoppingCartResponse(String response){

        try {
            List<ShoppingCartBean> list = gson.fromJson(response, new TypeToken<ArrayList<ShoppingCartBean>>() {
            }.getType());
            return list;

        } catch (Exception e) {

            e.printStackTrace();

        }
        return null;
    }
}
