package com.example.recyclerviewtest;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.example.recyclerviewtest.util.HttpUtil;
import com.example.recyclerviewtest.util.Utility;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private List<Goods> GoodsList = new ArrayList<>();
//    private RecyclerView recyclerView ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        recyclerView = findViewById(R.id.recycler_view);

//        WaterfallToolbar mWaterfallToolbar = (WaterfallToolbar) findViewById(R.id.waterfall_toolbar);
//        mWaterfallToolbar.addRecyclerView(recyclerView);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("货单列表");

        setContentView(R.layout.activity_main);

        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,new String[] {Manifest.permission.INTERNET},1);
        } else{
            getInfo();
        }
       /*
        if(Build.VERSION.SDK_INT >= 21) {

            //得到当前界面的装饰视图
            View decorView = getWindow().getDecorView();
//        SYSTEM_UI_FLAG_FULLSCREEN表示全屏的意思，也就是会将状态栏隐藏
            //设置系统UI元素的可见性
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);

            getWindow().setStatusBarColor(Color.TRANSPARENT);
            //隐藏标题栏
            ActionBar actionBar = getSupportActionBar();
            actionBar.setTitle("货单列表");
            actionBar.hide();
        }
        */

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    getInfo();
                } else{
                    Toast.makeText(this,"You denied the permi",Toast.LENGTH_SHORT).show();
                }
                break;
            default:
            break;
        }
    }

    private void initGoods(){
        for(int i=0;i<5;i++){
            Goods NO1 = new Goods("苹果","水果","15.2 ￥");
            GoodsList.add(NO1);
            Goods NO2 = new Goods("葡萄","水果","14.2 ￥");
            GoodsList.add(NO2);
            Goods NO3 = new Goods("梨","水果","13.2 ￥");
            GoodsList.add(NO3);
            Goods NO4 = new Goods("橘子","水果","5.2 ￥");
            GoodsList.add(NO4);
            Goods NO5 = new Goods("甘蔗","水果","15.2 ￥");
            GoodsList.add(NO5);
            Goods NO6 = new Goods("芒果","水果","15.2 ￥");
            GoodsList.add(NO6);
            Goods NO7 = new Goods("香蕉","水果","14.2 ￥");
            GoodsList.add(NO7);
            Goods NO8 = new Goods("西瓜","水果","14.2 ￥");
            GoodsList.add(NO8);
            Goods NO9 = new Goods("樱桃","水果","14.2 ￥");
            GoodsList.add(NO9);

        }
    }
    public void requestGoods(){
        String url = "http://192.168.137.1:8080/ServletTest/RegisterServlet";

        HttpUtil.sendOkHttpRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this,"获取货单信息失败1", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                final List<Goods> list = Utility.handleGoodsResponse(responseText);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(list!=null&&list.size()!=0)
                                GoodsList = list;
                            else
                                Toast.makeText(MainActivity.this,"获取货单信息失败2",Toast.LENGTH_SHORT).show();
                        }
                    });
            }
        });
    }

    private void showGoodsInfo(){


        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManager);

        GoodsAdapter adapter = new GoodsAdapter(GoodsList);
        recyclerView.setAdapter(adapter);
    }

    private void getInfo(){

        String url = "http://192.168.137.1:8080/ServletTest/RegisterServlet";

        HttpUtil.sendOkHttpRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this,"获取失败1",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                GoodsList = Utility.handleGoodsResponse(responseText);
                Log.d("responseData",GoodsList.get(1).getGoodsName());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showGoodsInfo();
                    }
                });
            }
        });
    }
}
