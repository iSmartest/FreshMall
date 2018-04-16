package com.lixin.freshmall.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lixin.freshmall.R;
import com.lixin.freshmall.dialog.ProgressDialog;
import com.lixin.freshmall.model.Constant;
import com.lixin.freshmall.model.UserInfo;
import com.lixin.freshmall.okhttp.OkHttpUtils;
import com.lixin.freshmall.okhttp.budiler.StringCallback;
import com.lixin.freshmall.uitls.AppManager;
import com.lixin.freshmall.uitls.Md5Util;
import com.lixin.freshmall.uitls.SPUtil;
import com.lixin.freshmall.uitls.StringUtils;
import com.lixin.freshmall.uitls.ToastUtils;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;
import okhttp3.Call;

/**
 * Created by 小火
 * Create time on  2017/10/26
 * My mailbox is 1403241630@qq.com
 */

public class LoginActivity extends BaseActivity implements View.OnClickListener{
    private static final String TAG = "ssss";
    private EditText et_userphone,et_password;
    private TextView tv_forgetPassword,tv_register, iv_qqlogin, iv_weixinlogin;
    private Button btn_login;
    private Dialog progressDlg;
    private UMShareAPI mShareAPI;
    private SHARE_MEDIA platform;

    private String type = null;
    private String screen_name = null, profile_image_url = null, openid = null,phoneNum = null;
    // 定义一个变量，来标识是否退出
    private static boolean isExit = false;
    private int isSkip = 0;
    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isExit = false;
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mShareAPI = UMShareAPI.get(this);
        isSkip = getIntent().getIntExtra("isSkip",0);
        initView();
        initData();
        initListener();

    }
    private void initView() {
        iv_qqlogin = findViewById(R.id.iv_qq_login);
        iv_weixinlogin = findViewById(R.id.iv_wx_login);
        et_userphone = findViewById(R.id.et_user_phone);
        et_password = findViewById(R.id.et_user_password);
        if (!TextUtils.isEmpty(SPUtil.getString(LoginActivity.this,"userPhone"))){
            et_userphone.setText(SPUtil.getString(LoginActivity.this,"userPhone"));
        }
        if (!TextUtils.isEmpty(SPUtil.getString(LoginActivity.this,"password"))){
            et_password.setText(SPUtil.getString(LoginActivity.this,"password"));
        }
        tv_forgetPassword = findViewById(R.id.tv_forgetPassword);
        tv_register = findViewById(R.id.tv_register);
        btn_login = findViewById(R.id.btn_login);
    }
    private void initData() {
        Intent intent = getIntent();
        if (intent == null) {
            return;
        }
        String phone = intent.getStringExtra("phone");
        String password = intent.getStringExtra("password");
        et_userphone.setText(phone);
        et_password.setText(password);
    }

    private void initListener() {
        tv_forgetPassword.setOnClickListener(this);
        tv_register.setOnClickListener(this);
        tv_forgetPassword.setOnClickListener(this);
        btn_login.setOnClickListener(this);
        iv_qqlogin.setOnClickListener(this);
        iv_weixinlogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                submit();
                break;
            case R.id.iv_qq_login://QQ登录
                type = "1";
                progressDlg = ProgressDialog.createLoadingDialog(context, "登录跳转中...");
                progressDlg.show();
                ToastUtils.makeText(context, "正在跳转QQ登录,请稍后...");
                mShareAPI.isInstall(this, SHARE_MEDIA.QQ);
                mShareAPI.getPlatformInfo(LoginActivity.this, SHARE_MEDIA.QQ, umAuthListener);
                break;
            case R.id.iv_wx_login://微信登录
                type = "0";
                if (!isWeixinAvilible(this)) {
                    ToastUtils.makeText(this, "请安装微信客户端");
                    return;
                }
                progressDlg = ProgressDialog.createLoadingDialog(context, "登录跳转中...");
                progressDlg.show();
                ToastUtils.makeText(this, "正在跳转微信登录,请稍后...");
                mShareAPI.isInstall(this, SHARE_MEDIA.WEIXIN);
                mShareAPI.getPlatformInfo(LoginActivity.this, SHARE_MEDIA.WEIXIN, umAuthListener);
                break;
            case R.id.tv_forgetPassword:
                MyApplication.openActivity(context, ForgetPwdActivity.class);
                break;
            case R.id.tv_register:
                MyApplication.openActivity(context, RegisterActivity.class);
                break;
        }
    }
    private void submit() {
        //验证电话号码是否正确
        String userphone = et_userphone.getText().toString().trim();//电话号码
        if (TextUtils.isEmpty(userphone)) {
            ToastUtils.makeText(context, "电话号码不能为空");
            return;
        }
        if (!StringUtils.isMatchesPhone(userphone)) {
            ToastUtils.makeText(context, "电话号码不正确，请核对后重新输入");
            return;
        }
        //验证密码是否为空
        String password = et_password.getText().toString().trim();//密码
        if (TextUtils.isEmpty(password)) {
            ToastUtils.makeText(context, "密码不能为空");
            return;
        }
        //验证密码格式是否正确
        if (!StringUtils.isPwd(password)) {
            ToastUtils.makeText(context, "密码格式不正确，请核对后重新输入");
            return;
        }
        try {
            userLogin(userphone, Md5Util.md5Encode(password));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mShareAPI.onActivityResult(requestCode, resultCode, data);
        if (progressDlg != null && progressDlg.isShowing()) {
            progressDlg.dismiss();
        }
    }
    private UMAuthListener umAuthListener = new UMAuthListener() {
        @Override
        public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
            if (SHARE_MEDIA.QQ.equals(share_media)) {
                screen_name = map.get("screen_name");//昵称
                profile_image_url = map.get("profile_image_url");//头像
                openid = map.get("openid");//第三方平台id
            } else if (SHARE_MEDIA.WEIXIN.equals(share_media)) {
                Log.i(TAG, "onComplete: " + map);
                screen_name = map.get("screen_name");//昵称
                profile_image_url = map.get("profile_image_url");//头像
                openid = map.get("openid");//第三方平台id
                phoneNum = map.get("phoneNum");
                Log.i(TAG, "thirdLogin: " + screen_name);
                Log.i("ssss", "onComplete: " + "授权成功了");
            }
            thirdLogin(openid, screen_name, profile_image_url,"");
        }

        @Override
        public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
            Log.i(TAG, "onError: " + "授权失败");
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media, int i) {
            Log.i(TAG, "onCancel: " + "授权取消");
        }
    };
    /**
     * 判断 用户是否安装微信客户端
     */
    public static boolean isWeixinAvilible(Context context) {
        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mm")) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 用户登录
     * @param phone
     * @param password
     */
    private void userLogin(String phone, String password) {
        String token = JPushInterface.getRegistrationID(context);
        Map<String, String> params = new HashMap<>();
        String json="{\"cmd\":\"userLogin\",\"phone\":\"" + phone + "\",\"password\":\""
                + password + "\",\"token\":\"" + token + "\"}";
        params.put("json", json);
        dialog1.show();
        Log.i("userLogin", "onResponse: "+ json);
        OkHttpUtils.post().url(Constant.THE_SERVER_URL).params(params).build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        ToastUtils.makeText(context, e.getMessage());
                    }
                    @Override
                    public void onResponse(String response, int id) {
                        Gson gson = new Gson();
                        dialog1.dismiss();
                        UserInfo bean = gson.fromJson(response, UserInfo.class);
                        if ("0".equals(bean.getResult())) {
                            ToastUtils.makeText(context, "登录成功");
                            Log.i(TAG, "onResponse: " + response);
                            SPUtil.putString(context, "uid", bean.getUid());
                            Intent intent = new Intent();
                            intent.setAction("com.freshmall.mine.changed");
                            getApplicationContext().sendBroadcast(intent);
                            if (isSkip == 0){
                                finish();
                            }else {
                                finish();
                                MyApplication.openActivity(context, MainActivity.class);
                            }
                        } else {
                            ToastUtils.makeText(context, bean.getResultNote());
                        }
                    }
                });
    }
    /**
     * 第三方登录
     * @param thirdUid
     * @param nickName
     * @param userIcon
     */
    private void thirdLogin(final String thirdUid, final String nickName, final String userIcon, String phoneNum) {
        String token= JPushInterface.getRegistrationID(context);
        String password = "";
        Map<String, String> params = new HashMap<>();
        final String json="{\"cmd\":\"thirdLogin\",\"thirdUid\":\"" + thirdUid + "\"," +
                "\"nickName\":\"" + nickName +"\",\"userIcon\":\""+userIcon+"\",\"phoneNum\":\""+phoneNum+"\"," +
                "\"type\":\""+type+"\",\"token\":\""+token+"\",\"password\":\""+password+"\"}";
        params.put("json", json);
        dialog1.show();
        Log.i(TAG, "thirdLogin: " + json);
        OkHttpUtils.post().url(Constant.THE_SERVER_URL).params(params).build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        ToastUtils.makeText(context, e.getMessage());
                    }
                    @Override
                    public void onResponse(String response, int id) {
                        Log.i(TAG, "thirdLogin: " + response);
                        Gson gson = new Gson();
                        dialog1.dismiss();
                        UserInfo bean = gson.fromJson(response, UserInfo.class);
                        if (bean.getResult().equals("0")) {
                            if (bean.getIsFirst().equals("0")){
                                ToastUtils.makeText(context, "登录成功");
                                SPUtil.putString(context, "uid", bean.getUid());
                                SPUtil.putString(context, "nickName", bean.getNickName());
                                SPUtil.putString(context, "userIcon", bean.getUserIcon());
                                Intent intent = new Intent();
                                intent.setAction("com.freshmall.mine.changed");
                                intent.setAction("com.freshmall.code.changed");
                                getApplicationContext().sendBroadcast(intent);
                                if (isSkip == 0){
                                    finish();
                                }else {
                                    finish();
                                    MyApplication.openActivity(context, MainActivity.class);
                                }
                            }else if (bean.getIsFirst().equals("1")){
                                Bundle bundle = new Bundle();
                                bundle.putString("thirdUid",thirdUid);
                                bundle.putString("nickName",nickName);
                                bundle.putString("userIcon",userIcon);
                                bundle.putString("type",type);
                                MyApplication.openActivity(LoginActivity.this,BindingPhoneActivity.class,bundle);
                            }
                        } else {
                            ToastUtils.makeText(context, bean.getResultNote());
                        }
                    }
                });
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void exit() {
        if (!isExit) {
            isExit = true;
            Toast.makeText(getApplicationContext(), "再按一次退出程序",
                    Toast.LENGTH_SHORT).show();
            mHandler.sendEmptyMessageDelayed(0, 3000);
        } else {
            AppManager.finishAllActivity();
            System.exit(0);
        }
    }
}
