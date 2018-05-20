package com.example.recyclerviewtest.util;

import android.text.TextUtils;

import com.example.recyclerviewtest.Goods;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2018/5/20.
 */

public class Utility {

    /**

     * 将返回的JSON数据解析成Goods实体类

     */

    public static List<Goods> handleGoodsResponse(String response) {

        try {
            Gson gson = new Gson();
            List<Goods> list = gson.fromJson(response,new TypeToken< ArrayList<Goods> >(){}.getType());
            return list;

        } catch (Exception e) {

            e.printStackTrace();

        }

        return null;

    }
}
