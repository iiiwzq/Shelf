package com.example.recyclerviewtest.myclass;

import com.google.gson.annotations.SerializedName;

import java.sql.Blob;

/**
 * Created by lenovo on 2018/5/19.
 */

public class Goods{

    @SerializedName("ID")
    private int ID;


    @SerializedName("goodsId")
    private String goodsName;

    @SerializedName("classification")
    private String classification;

    @SerializedName("price")
    private String price;

    @SerializedName("image")
    private String image;

    public Goods(){
        super();
    }
    public Goods(int ID,String goodsName, String classification, String price,String image) {
        this.ID = ID;
        this.goodsName = goodsName;
        this.classification = classification;
        this.price = price;
        this.image = image;
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

    public String getImage(){
        return image;
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

    public void setImage(String image){
        this.image = image;
    }
    public void setID(int ID) {
        this.ID = ID;
    }

    public int getID() {

        return ID;
    }

}
