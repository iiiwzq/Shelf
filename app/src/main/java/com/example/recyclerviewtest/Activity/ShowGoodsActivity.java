package com.example.recyclerviewtest.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.recyclerviewtest.R;
import com.example.recyclerviewtest.myclass.Constant;
import com.example.recyclerviewtest.myclass.Message;
import com.example.recyclerviewtest.myclass.User;
import com.example.recyclerviewtest.util.HttpUtil;
import com.example.recyclerviewtest.util.SharedPreferencesUtils;
import com.example.recyclerviewtest.util.Utility;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ShowGoodsActivity extends AppCompatActivity {

    public static final String GOODS_ID= "goods_id";
    public static final String GOODS_NAME = "goods_name";
    public static final String PRICE = "price";
    public static final String IMAGE = "image";

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem){
        switch (menuItem.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods);
        Intent intent = getIntent();
        String goodsName = intent.getStringExtra(GOODS_NAME);
        String price = intent.getStringExtra(PRICE);
        String image = intent.getStringExtra(IMAGE);
        final int goodsId = intent.getIntExtra(GOODS_ID,0);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        ImageView goodsImageView = (ImageView) findViewById(R.id.goods_image_view);
        TextView goodsPrice = (TextView) findViewById(R.id.price);
        TextView goodsCoupons = (TextView) findViewById(R.id.coupons);
        TextView goodsPromote = (TextView) findViewById(R.id.promote);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alert = new AlertDialog.Builder(ShowGoodsActivity.this).create();
                alert.setTitle("购物提示");
                alert.setMessage("是否加入购物车");
                //添加取消按钮
                alert.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                //添加"确定"按钮
                alert.setButton(DialogInterface.BUTTON_POSITIVE, "确认", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        if(goodsId != 0){
                            SharedPreferencesUtils helper = new SharedPreferencesUtils(ShowGoodsActivity.this, "setting");
                            final String account = helper.getString("name");

                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    addBuy(account,goodsId);
                                }
                            }).start();

                        } else{
                            Toast.makeText(ShowGoodsActivity.this,"编号为零",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                alert.show();
            }
        });
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        collapsingToolbar.setTitle(goodsName);
        Glide.with(this).load(image).into(goodsImageView);
        goodsPrice.setText(price);
        goodsCoupons.setText(price);
        goodsPromote.setText(price);
    }

    private void addBuy(String account,int goodsID){

        String BuyUrlStr = Constant.URL_Buy + "?account=" + account + "&ID=" + goodsID;

        HttpUtil.sendOkHttpRequest(BuyUrlStr, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ShowGoodsActivity.this, "连接错误", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                Message message = Utility.handleMessageResponse(responseText);
                if (message != null) {
                    String code = message.getCode();
                    final String msg = message.getMessage();
                    if(!"".equals(code))
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(ShowGoodsActivity.this,msg,Toast.LENGTH_SHORT).show();
                            }
                        });

                }
            }

        });
    }
}
