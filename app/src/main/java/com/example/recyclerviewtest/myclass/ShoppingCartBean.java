package com.example.recyclerviewtest.myclass;

import com.google.gson.annotations.SerializedName;

/**
 * Created by lenovo on 2018/5/30.
 */

public class ShoppingCartBean {

    //ID
    @SerializedName("ID")
    private int ID;

    //货物名称
    @SerializedName("goodsId")
    private String goodsName;

    //分类
    @SerializedName("classification")
    private String classification;

    //价格
    @SerializedName("price")
    private double price;

    //图片
    @SerializedName("image")
    private String image;

    //数量
    @SerializedName("num")
    private int count;

    public boolean isChoosed;

//    public boolean isCheck = false;



    public ShoppingCartBean(){
        super();
    }



    //默认购买一件
    public ShoppingCartBean(int ID,String goodsName, String classification, double price, String image) {
        this.ID = ID;

        this.goodsName = goodsName;
        this.classification = classification;
        this.price = price;
        this.image = image;
        this.count = 1;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public String getClassification() {
        return classification;
    }

    public double getPrice() {
        return this.price;
    }

    public String getImage() {
        return image;
    }


    public int getCount() {
        return count;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setImageUrl(String image) {
        this.image = image;
    }

    public void setCount(int count) {
        this.count = count;
    }
    public int getID() {
        return ID;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setID(int ID) {

        this.ID = ID;
    }

    public void setChoosed(boolean choosed) {
        isChoosed = choosed;
    }

    public boolean isChoosed() {
        return isChoosed;
    }
}
