package com.example.recyclerviewtest;

import com.google.gson.annotations.SerializedName;

/**
 * Created by lenovo on 2018/5/19.
 */

public class Goods{

    @SerializedName("goodsId")
    private String goodsName;

    @SerializedName("classification")
    private String classification;

    @SerializedName("price")
    private String price;

    public Goods(){
        super();
    }
    public Goods(String goodsName, String classification, String price) {
        this.goodsName = goodsName;
        this.classification = classification;
        this.price = price;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public String getClassification() {
        return classification;
    }

    public String getPrice() {
        return price;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
