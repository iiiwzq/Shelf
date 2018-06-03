package com.example.recyclerviewtest.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.recyclerviewtest.myclass.Constant;
import com.example.recyclerviewtest.R;
import com.example.recyclerviewtest.myclass.User;
import com.example.recyclerviewtest.util.HttpUtil;
import com.example.recyclerviewtest.util.SharedPreferencesUtils;
import com.example.recyclerviewtest.util.Utility;
import com.example.recyclerviewtest.widget.LoadingDialog;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 登录界面
 * Created by lenovo on 2018/5/21.
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private EditText et_name;
    private EditText et_password;
    private Button mLoginBtn;
    private CheckBox checkBox_password;
    private CheckBox checkBox_login;
    private ImageView iv_see_password;

    private LoadingDialog mLoadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
//        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        /*if (Build.VERSION.SDK_INT >= 21) {
            //得到当前界面的装饰视图
            View decorView = getWindow().getDecorView();
//        SYSTEM_UI_FLAG_FULLSCREEN表示全屏的意思，也就是会将状态栏隐藏
            //设置系统UI元素的可见性
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);

            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }*/
        initViews();
        setupEvents();
        initData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                loadUserName();//无论如何保存一个用户名
                login();
                break;
            case R.id.iv_see_password:
                setPasswordVisibility();
                break;
        }
    }

    /**
     * CheckBox点击时的回调方法 ,不管是勾选还是取消勾选都会得到回调
     *
     * @param buttonView
     * @param isChecked
     */
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        if (buttonView == checkBox_password) {  //记住密码选框发生改变时
            if (!isChecked) {   //如果取消“记住密码”，那么同样取消自动登陆
                checkBox_login.setChecked(false);
            }
        } else if (buttonView == checkBox_login) {   //自动登陆选框发生改变时
            if (isChecked) {   //如果选择“自动登录”，那么同样选中“记住密码”
                checkBox_password.setChecked(true);
            }
        }

    }

    private void initData() {
        //判断用户第一次登录
        if (firstLogin()) {
            checkBox_password.setChecked(false);
            checkBox_login.setChecked(false);
        }
        //判断是否记住密码
        if (rememberPassword()) {
            checkBox_password.setChecked(true);
            setTextNameAndPassword();//把账号密码放入输入框
        } else {
            setTextName();//把账号放入输入框
        }

        //判断是否自动登录
        if (autoLogin()) {
            checkBox_login.setChecked(true);
            login();
        }
    }

    /**
     * 把本地保存的数据设置到输入框中
     */
    public void setTextNameAndPassword() {
        et_name.setText(getLocalName());
        et_password.setText(getLocalPassword());
        et_name.requestFocus();
        et_name.setSelection(et_name.length());
    }

    /**
     * 设置数据到输入框中
     */
    public void setTextName() {
        et_name.setText(getLocalName());
        et_name.requestFocus();
        et_name.setSelection(et_name.length());
    }

    /**
     * 获得保存在本地的用户名
     */
    public String getLocalName() {
        //获取SharedPreferences对象，使用自定义的方法来获取对象
        SharedPreferencesUtils helper = new SharedPreferencesUtils(this, "setting");
        String name = helper.getString("name");
        return name;
    }

    /**
     * 获取保存在本地的密码
     */
    public String getLocalPassword() {
        SharedPreferencesUtils helper = new SharedPreferencesUtils(this, "setting");
        String password = helper.getString("password");
        return password;
    }

    /**
     * 判断是否自动登录
     */
    private boolean autoLogin() {
        SharedPreferencesUtils helper = new SharedPreferencesUtils(this, "setting");
        boolean autoLogin = helper.getBoolean("autoLogin", false);
        return autoLogin;
    }

    /**
     * 判断是否记住密码
     */
    private boolean rememberPassword() {
        SharedPreferencesUtils helper = new SharedPreferencesUtils(this, "setting");
        boolean rememberPassword = helper.getBoolean("rememberPassword", false);
        return rememberPassword;
    }

    private void initViews() {
        mLoginBtn = (Button) findViewById(R.id.btn_login);
        et_name = (EditText) findViewById(R.id.et_account);
        et_password = (EditText) findViewById(R.id.et_password);
        checkBox_password = (CheckBox) findViewById(R.id.checkBox_password);
        checkBox_login = (CheckBox) findViewById(R.id.checkBox_login);
        iv_see_password = (ImageView) findViewById(R.id.iv_see_password);

        et_name.requestFocus();
        et_name.setSelection(et_name.length());
    }

    private void setupEvents() {
        mLoginBtn.setOnClickListener(this);
        checkBox_password.setOnCheckedChangeListener(this);
        checkBox_login.setOnCheckedChangeListener(this);
        iv_see_password.setOnClickListener(this);
    }

    /**
     * 判断是否是第一次登录
     */
    private boolean firstLogin() {
        SharedPreferencesUtils helper = new SharedPreferencesUtils(this, "setting");
        boolean first = helper.getBoolean("first", true);
        if (first) {
            //创建一个ContentVa对象（自定义的）设置不是第一次登录，,并创建记住密码和自动登录是默认不选，创建账号和密码为空
            helper.putValues(new SharedPreferencesUtils.ContentValue("first", false),
                    new SharedPreferencesUtils.ContentValue("rememberPassword", false),
                    new SharedPreferencesUtils.ContentValue("autoLogin", false),
                    new SharedPreferencesUtils.ContentValue("name", ""),
                    new SharedPreferencesUtils.ContentValue("password", ""),
                    new SharedPreferencesUtils.ContentValue("userName", ""),
                    new SharedPreferencesUtils.ContentValue("userEmail", ""));
            return true;
        }
        return false;
    }

    /**
     * 模拟登录情况
     * 用户名csdn 密码123456
     */
    private void login() {
        if (getAccount().isEmpty()) {
            showToast("你输入的账号为空!");
            return;
        }
        if (getPassword().isEmpty()) {
            showToast("你输入的密码为空!");
            return;
        }
        //登录一般都是请求服务器来判断密码是否正确，要请求网络，要子线程
        showLoading();
        Thread loginRunnable = new Thread() {

            @Override
            public void run() {
                super.run();
                setLoginBtnClickable(false);//点击登录后，设置登录按钮不可点击状态

                //睡眠3秒
                /*try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }*/
                login(et_name.getText().toString(), et_password.getText().toString());
                Log.d("USER", et_name.getText().toString() + "  " + et_password.getText().toString());
                setLoginBtnClickable(true);//解放登录按钮，设置为可点击
                hideLoading();//隐藏加载框
            }
        };
        loginRunnable.start();
    }

    private void login(String account, String password) {
        String LoginUrlStr = Constant.URL_Login + "?account=" + account + "&password=" + password;

        HttpUtil.sendOkHttpRequest(LoginUrlStr, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(LoginActivity.this, "连接错误", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                Log.d("USER", "response" + responseText);
                User user = Utility.handleUserResponse(responseText);

                if (user != null) {
                    loadCheckBoxState();//记录下当前用户记住密码和自动登录的状态

                    SharedPreferencesUtils helper = new SharedPreferencesUtils(LoginActivity.this, "setting");
                    helper.putValues(new SharedPreferencesUtils.ContentValue("userName", user.getUserName()));
                    helper.putValues(new SharedPreferencesUtils.ContentValue("userEmail", user.getEmail()));

                    Log.d("USER", user.getUserName() + " " + user.getEmail());

                    startActivity(new Intent(LoginActivity.this, GoodsActivity.class));
                    finish();//关闭页面
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showToast("输入的账号或密码不正确");
                        }
                    });

                }
            }

        });
    }

    /**
     * 保存用户账号
     */
    public void loadUserName() {
        if (!getAccount().equals("") || !getAccount().equals("请输入登录账号")) {
            SharedPreferencesUtils helper = new SharedPreferencesUtils(this, "setting");
            helper.putValues(new SharedPreferencesUtils.ContentValue("name", getAccount()));
        }
    }

    /**
     * 设置密码可见与不可见的相互转换
     */
    private void setPasswordVisibility() {

        if (iv_see_password.isSelected()) {
            iv_see_password.setSelected(false);
            //密码不可见
            et_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            if(et_name.isFocusable()) {
                et_name.requestFocus();
                et_name.setSelection(et_name.length());
            }
        } else {
            iv_see_password.setSelected(true);
            //密码可见
            et_password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            if(et_password.isFocusable()) {
                et_password.requestFocus();
                et_password.setSelection(et_password.length());
            }
        }


    }

    /**
     * 获取账号
     */
    public String getAccount() {
        return et_name.getText().toString().trim();//去掉空格
    }

    /**
     * 获取密码
     */
    public String getPassword() {
        return et_password.getText().toString().trim();
    }

    /**
     * 保存用户选择"记住密码"和"自动登录"的状态
     */
    private void loadCheckBoxState() {
        loadCheckBoxState(checkBox_password, checkBox_login);
    }

    /**
     * 保存按钮的状态值
     */
    public void loadCheckBoxState(CheckBox checkBox_password, CheckBox checkBox_login) {

        //获取SharedPreferences对象，使用自定义类的方法来获取对象
        SharedPreferencesUtils helper = new SharedPreferencesUtils(this, "setting");

        //如果设置自动登录
        if (checkBox_login.isChecked()) {
            //创建记住密码和自动登录是都选择,保存密码数据
            helper.putValues(
                    new SharedPreferencesUtils.ContentValue("rememberPassword", true),
                    new SharedPreferencesUtils.ContentValue("autoLogin", true),
                    new SharedPreferencesUtils.ContentValue("password", getPassword()));

        } else if (!checkBox_password.isChecked()) { //如果没有保存密码，那么自动登录也是不选的
            //创建记住密码和自动登录是默认不选,密码为空
            helper.putValues(
                    new SharedPreferencesUtils.ContentValue("rememberPassword", false),
                    new SharedPreferencesUtils.ContentValue("autoLogin", false),
                    new SharedPreferencesUtils.ContentValue("password", ""));
        } else if (checkBox_password.isChecked()) {   //如果保存密码，没有自动登录
            //创建记住密码为选中和自动登录是默认不选,保存密码数据
            helper.putValues(
                    new SharedPreferencesUtils.ContentValue("rememberPassword", true),
                    new SharedPreferencesUtils.ContentValue("autoLogin", false),
                    new SharedPreferencesUtils.ContentValue("password", getPassword()));
        }
    }

    /**
     * 是否可以点击登录按钮
     */
    public void setLoginBtnClickable(boolean clickable) {
        mLoginBtn.setClickable(clickable);
    }

    /**
     * 显示加载的进度框
     */
    public void showLoading() {
        if (mLoadingDialog == null) {
            mLoadingDialog = new LoadingDialog(this, getString(R.string.loading), false);
        }
        mLoadingDialog.show();
    }

    /**
     * 隐藏加载的进度框
     */
    public void hideLoading() {
        if (mLoadingDialog != null) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mLoadingDialog.hide();
                }
            });
        }
    }

    /**
     * 监听回退键
     */
    @Override
    public void onBackPressed() {
        if (mLoadingDialog != null) {
            if (mLoadingDialog.isShowing()) {
                mLoadingDialog.cancel();
            } else {
                finish();
            }
        } else {
            finish();
        }
    }

    /**
     * 页面销毁前回调的方法
     */
    protected void onDestroy() {
        if (mLoadingDialog != null) {
            mLoadingDialog.cancel();
            mLoadingDialog = null;
        }
        super.onDestroy();
    }

    public void showToast(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
