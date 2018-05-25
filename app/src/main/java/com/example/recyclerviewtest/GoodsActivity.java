package com.example.recyclerviewtest;

import android.content.DialogInterface;
import android.content.Intent;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.recyclerviewtest.util.HttpUtil;
import com.example.recyclerviewtest.util.SharedPreferencesUtils;
import com.example.recyclerviewtest.util.Utility;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class GoodsActivity extends AppCompatActivity {

    private List<Goods> GoodsList = new ArrayList<>();

    private DrawerLayout mDrawerLayout;

    private TextView userName;

    private TextView userEmail;

    private TextView toolbar_title;

    private String TitleName = "货单列表";

    private NavigationView navigationView;

    //重写返回键功能
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                mDrawerLayout.closeDrawers();
            }else {
                finish();
            }
        }
        return super.onKeyUp(keyCode, event);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.exit:
                        AlertDialog alert = new AlertDialog.Builder(GoodsActivity.this).create();
                        alert.setTitle("注销");
                        alert.setMessage("确定要退出当前账号？");
                        //添加取消按钮
                        alert.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        //添加"确定"按钮
                        alert.setButton(DialogInterface.BUTTON_POSITIVE, "确认退出", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                logOff();
                            }
                        });
                        alert.show();
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
        View view = navigationView.inflateHeaderView(R.layout.nav_header);

        userName = (TextView) view.findViewById(R.id.userName);
        userEmail = (TextView) view.findViewById(R.id.userEmail);
        SharedPreferencesUtils helper = new SharedPreferencesUtils(GoodsActivity.this, "setting");
        userName.setText(helper.getString("userName"));
        userEmail.setText(helper.getString("userEmail"));

        toolbar_title = (TextView) findViewById(R.id.title_toolbar);

//        ActionBar actionBar = getSupportActionBar();
//        if (actionBar != null) {
//            actionBar.setDisplayHomeAsUpEnabled(false);
            toolbar_title.setText(TitleName);
//        }
        findViewById(R.id.head_icon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(GravityCompat.START);
            }
        });
        getInfo();
    }

    /**
     * 返回登录界面，消除用密码
     */

    public void logOff() {

        //置空密码即可

        //获取SharedPreferences对象，使用自定义类的方法来获取对象

        SharedPreferencesUtils helper = new SharedPreferencesUtils(this, "setting");

        //创建记住密码和自动登录是默认不选,密码为空

        helper.putValues(
                new SharedPreferencesUtils.ContentValue("rememberPassword", false),
                new SharedPreferencesUtils.ContentValue("autoLogin", false),
                new SharedPreferencesUtils.ContentValue("password", ""),
                new SharedPreferencesUtils.ContentValue("userName", ""),
                new SharedPreferencesUtils.ContentValue("userEmail", ""));

        startActivity(new Intent(this, LoginActivity.class));

    }

    private void showGoodsInfo() {

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManager);

        GoodsAdapter adapter = new GoodsAdapter(GoodsList);
        recyclerView.setAdapter(adapter);
    }

    private void getInfo() {

//        String url = "http://192.168.137.1:8080/ServletTest/RegisterServlet";
        String url = Constant.URL_Goods;
        HttpUtil.sendOkHttpRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(GoodsActivity.this, "获取失败1", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                GoodsList = Utility.handleGoodsResponse(responseText);
                Log.d("responseData", GoodsList.get(1).getGoodsName());
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
