package com.example.recyclerviewtest.Activity;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.recyclerviewtest.R;
import com.example.recyclerviewtest.adapter.GoodsAdapter;
import com.example.recyclerviewtest.adapter.ShoppingCartAdapter;
import com.example.recyclerviewtest.myclass.Constant;
import com.example.recyclerviewtest.myclass.Message;
import com.example.recyclerviewtest.myclass.ShoppingCartBean;
import com.example.recyclerviewtest.util.HttpUtil;
import com.example.recyclerviewtest.util.SharedPreferencesUtils;
import com.example.recyclerviewtest.util.Utility;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by lenovo on 2018/5/28.
 */

public class ShoppingCartActivity extends AppCompatActivity implements ShoppingCartAdapter.ModifyCountInterface,
        View.OnClickListener,ShoppingCartAdapter.CheckInterface {

    //全选
    private CheckBox check_all;
    //返回
    private ImageView btnBack;
    //总额
    private TextView tvShowPrice;
    //结算
    private TextView tvSettlement;
    //编辑
    private TextView btnEdit;


    private List<ShoppingCartBean> ShoppingList = new ArrayList<>();

    private ShoppingCartAdapter shoppingCartAdapter;

    private  RecyclerView recyclerView;

    //账号
    private String account;

    private double totalPrice = 0.00;// 购买的商品总价
    private int totalCount = 0;// 购买的商品总数量
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.layout_shopping_cart_activity);
        if (Build.VERSION.SDK_INT >= 21) {
            //得到当前界面的装饰视图
            View decorView = getWindow().getDecorView();
//        SYSTEM_UI_FLAG_FULLSCREEN表示全屏的意思，也就是会将状态栏隐藏
            //设置系统UI元素的可见性
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);

            getWindow().setStatusBarColor(Color.TRANSPARENT);

            SharedPreferencesUtils helper = new SharedPreferencesUtils(this, "setting");
            account = helper.getString("name");

            tvShowPrice = (TextView) findViewById(R.id.tv_show_price);
            tvSettlement = (TextView) findViewById(R.id.tv_settlement);
            check_all = (CheckBox) findViewById(R.id.ch_all);
            check_all.setOnClickListener(this);
            btnBack = (ImageView) findViewById(R.id.btn_back);
            btnBack.setOnClickListener(this);
            btnEdit = (TextView) findViewById(R.id.bt_edit);
            btnEdit.setOnClickListener(this);
            tvSettlement = (TextView) findViewById(R.id.tv_settlement);
            tvSettlement.setOnClickListener(this);
            getShopping(account);
           /* GoodsList.add(new ShoppingCartBean("酒红色纯红色纯羊毛西服套装","上衣","￥390",
                    "http://a2.qpic.cn/psb?/V11xaLZT1BtjmH/lZLlGTESrMSgLZFXIE5PfhlmWqv4fDXHj4kWhwIjpZI!/m/dC0BAAAAAAAAnull&b"));
            GoodsList.add( new ShoppingCartBean("棕黄色裙子","裙子","￥300",
                    "http://a3.qpic.cn/psb?/" +
                            "V11xaLZT1BtjmH/VbJsCjavUGZq893WA2rCeeElKjVhXS40b0P1KjPBhMc!/m/dFoAAAAAAAAAnull&bo=3AWkA9wFpAMRBzA!&rf=photolist&t=5"));
            */

        }

    }

    /**
     * 统计操作
     * 1.先清空全局计数器<br>
     * 2.遍历所有子元素，只要是被选中状态的，就进行相关的计算操作
     * 3.给底部的textView进行数据填充
     */
    public void statistics() {
        totalCount = 0;
        totalPrice = 0.00;
        if(ShoppingList==null || ShoppingList.size()==0){
            check_all.setChecked(false);
        }else {
            for (int i = 0; i < ShoppingList.size(); i++) {
                ShoppingCartBean shoppingCartBean = ShoppingList.get(i);
                if (shoppingCartBean.isChoosed()) {
                    totalCount++;
                    totalPrice += shoppingCartBean.getPrice() * shoppingCartBean.getCount();
                }
            }
        }
        DecimalFormat format = new DecimalFormat("0.00");
        String strPrice = format.format(totalPrice);
        tvShowPrice.setText("合计:" + strPrice);
        tvSettlement.setText("结算(" + totalCount + ")");
    }
    private void showShoppingInfo() {

        recyclerView = (RecyclerView) findViewById(R.id.list_shopping_cart);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(layoutManager);
        shoppingCartAdapter = new ShoppingCartAdapter(ShoppingList);
        shoppingCartAdapter.setModifyCountInterface(this);
        shoppingCartAdapter.setCheckInterface(this);
        recyclerView.setAdapter(shoppingCartAdapter);
        statistics();
    }

    private void getShopping(String account) {
        String url = Constant.URL_MyBuy + "?account=" + account;
        HttpUtil.sendOkHttpRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ShoppingCartActivity.this, "获取失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                ShoppingList = Utility.handleShoppingCartResponse(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showShoppingInfo();
                    }
                });
            }
        });
    }

    @Override
    public void doIncrease(int position, TextView showCountView) {
        ShoppingCartBean shoppingCartBean = ShoppingList.get(position);
        int Count = shoppingCartBean.getCount();
        Count++;
        changeNum(account,shoppingCartBean.getID(),Count);
        shoppingCartBean.setCount(Count);
//        shoppingCartAdapter.notifyItemChanged(position);
        shoppingCartAdapter.notifyDataSetChanged();
        statistics();
    }

    @Override
    public void doDecrease(int position, TextView showCountView) {
        ShoppingCartBean shoppingCartBean = ShoppingList.get(position);
        int Count = shoppingCartBean.getCount();
        Count--;
        if(Count < 0 ){
            Count = 0;
        }
        changeNum(account,shoppingCartBean.getID(),Count);
        shoppingCartBean.setCount(Count);
        //有动画(闪烁)
//        shoppingCartAdapter.notifyItemChanged(position);
        shoppingCartAdapter.notifyDataSetChanged();
        statistics();
    }

    private void changeNum(String account, int goodsID, int num) {

        String NumUrl = Constant.URL_NUM + "?account=" + account + "&ID=" + goodsID + "&num=" + num;

        HttpUtil.sendOkHttpRequest(NumUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ShoppingCartActivity.this, "连接错误", Toast.LENGTH_SHORT).show();
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
                    if ("".equals(code)||"200".equals(code)) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(ShoppingCartActivity.this, msg, Toast.LENGTH_LONG).show();
                            }
                        });
                    }

                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //全选按钮
            case R.id.ch_all:
                if(ShoppingList == null || ShoppingList.size() == 0){
                    check_all.setChecked(false);
                }
                else {
                    if (check_all.isChecked()) {
                        for (int i = 0; i < ShoppingList.size(); i++) {
                            ShoppingList.get(i).setChoosed(true);
                        }
                    } else {
                        for (int i = 0; i < ShoppingList.size(); i++) {
                            ShoppingList.get(i).setChoosed(false);
                        }
                    }
                    shoppingCartAdapter.notifyDataSetChanged();
                }
                statistics();
                break;
            case R.id.btn_back:
                finish();
                break;
            case R.id.bt_edit:
                AlertDialog alert = new AlertDialog.Builder(ShoppingCartActivity.this).create();
                alert.setTitle("删除提示");
                alert.setMessage("将从购物车中移除");
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

                        for (int i = 0; i < ShoppingList.size(); i++) {
                            ShoppingCartBean shoppingCartBean = ShoppingList.get(i);

                            if (shoppingCartBean.isChoosed()) {
                                deleteGoods(account, shoppingCartBean.getID());
                                ShoppingList.remove(i);
                                i--;
                            }
                        }
                        shoppingCartAdapter.notifyDataSetChanged();
                        statistics();
                    }
                });

                alert.show();
                break;
            case R.id.tv_settlement:
                AlertDialog alert1 = new AlertDialog.Builder(ShoppingCartActivity.this).create();
                alert1.setTitle("结算提示");
                DecimalFormat format = new DecimalFormat("0.00");
                String strPrice = format.format(totalPrice);
                alert1.setMessage("共计 "+strPrice);
                //添加取消按钮
                alert1.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                //添加"确定"按钮
                alert1.setButton(DialogInterface.BUTTON_POSITIVE, "确认", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        if (ShoppingList == null || ShoppingList.size() == 0) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(ShoppingCartActivity.this, "购物车为空", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            for (int i = 0; i < ShoppingList.size(); i++) {
                                ShoppingCartBean shoppingCartBean = ShoppingList.get(i);

                                if (shoppingCartBean.isChoosed()) {
                                    deleteGoods(account, shoppingCartBean.getID());
                                    ShoppingList.remove(i);
                                    i--;
                                }
                            }
                            shoppingCartAdapter.notifyDataSetChanged();
                            statistics();
                        }
                    }
                });
                alert1.show();
        }
    }

    /**
     * 遍历list集合
     * @return
     */
    private boolean isAllCheck() {

        for (ShoppingCartBean group : ShoppingList) {
            if (!group.isChoosed())
                return false;
        }
        return true;
    }
    /**
     * 单选
     * @param position  组元素位置
     * @param isChecked 组元素选中与否
     */
    @Override
    public void checkGroup(int position, boolean isChecked) {
        ShoppingList.get(position).setChoosed(isChecked);
        if (isAllCheck())
            check_all.setChecked(true);
        else
            check_all.setChecked(false);
        shoppingCartAdapter.notifyDataSetChanged();
        statistics();
    }
    private void deleteGoods(String account,int goodsID){

        String deleteUrl = Constant.URL_DEL+ "?account=" + account + "&ID=" + goodsID;

        HttpUtil.sendOkHttpRequest(deleteUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ShoppingCartActivity.this, "连接错误", Toast.LENGTH_SHORT).show();
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
                    if (!"".equals(code)&&"200".equals(code)) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(ShoppingCartActivity.this, msg, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }
        });
    }
}

