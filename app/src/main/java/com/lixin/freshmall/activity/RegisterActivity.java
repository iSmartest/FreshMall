package com.lixin.freshmall.activity;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lixin.freshmall.R;
import com.lixin.freshmall.model.Constant;
import com.lixin.freshmall.model.UserInfo;
import com.lixin.freshmall.okhttp.OkHttpUtils;
import com.lixin.freshmall.okhttp.budiler.StringCallback;
import com.lixin.freshmall.uitls.Md5Util;
import com.lixin.freshmall.uitls.SPUtil;
import com.lixin.freshmall.uitls.StringUtils;
import com.lixin.freshmall.uitls.TimerUtil;
import com.lixin.freshmall.uitls.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by 小火
 * Create time on  2017/11/29
 * My mailbox is 1403241630@qq.com
 */

public class RegisterActivity extends BaseActivity implements View.OnClickListener{
    private static final String TAG = "ForgetPwdActivity";
    private EditText edi_phone_number,edi_verification_code,edi_password,edi_password_again,edi_recommendation_code;
    private Button btn_fast_register,btn_verification_code;
    private TextView mGoLogin;
    private Context mContext;
    private String code;
    private String logpwd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fast_register);
        hideBack(2);
        setTitleText("注册");
        mContext = this;
        initView();
    }
    private void initView() {
        edi_phone_number = findViewById(R.id.edi_phone_number);
        edi_verification_code = findViewById(R.id.edi_verification_code);
        edi_password = findViewById(R.id.edi_password);
        edi_password_again = findViewById(R.id.edi_password_again);
        btn_verification_code = findViewById(R.id.btn_verification_code);
        edi_recommendation_code = findViewById(R.id.edi_recommendation_code);
        btn_fast_register = findViewById(R.id.btn_fast_register);
        mGoLogin = findViewById(R.id.text_register_go_login);
        edi_phone_number.setOnClickListener(this);
        edi_verification_code.setOnClickListener(this);
        edi_password.setOnClickListener(this);
        edi_password_again.setOnClickListener(this);
        btn_verification_code.setOnClickListener(this);
        btn_fast_register.setOnClickListener(this);
        mGoLogin.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_verification_code:
                String user_phone_number = edi_phone_number.getText().toString().trim();
                //验证手机号是否正确
                if (!StringUtils.isMatchesPhone(user_phone_number)){
                    ToastUtils.makeText(mContext,"你输入的手机号格式不正确");
                    return;
                }
                //验证电话号码不能为空
                if (TextUtils.isEmpty(user_phone_number)){
                    ToastUtils.makeText(mContext,"请输入手机号！");
                    return;
                }
                code = TimerUtil.getNum();
                sendSMS(user_phone_number,code);
                TimerUtil mTimerUtil = new TimerUtil(btn_verification_code);
                mTimerUtil.timers();
                break;
            case R.id.btn_fast_register:
                submit();
                break;
            case R.id.text_register_go_login:
                MyApplication.openActivity(RegisterActivity.this,LoginActivity.class);
                finish();
                break;
        }
    }

    private void submit() {
        //验证密码不能为空
        String password = edi_password.getText().toString().trim();
        if (TextUtils.isEmpty(password)) {
            ToastUtils.makeText(mContext, "密码不能为空");
            return;
        }
        //验证确认密码不能为空
        String confirmpwd = edi_password_again.getText().toString().trim();
        if (TextUtils.isEmpty(confirmpwd)) {
            ToastUtils.makeText(mContext, "确认密码不能为空");
            return;
        }
        //验证密码和确认密码是否相同
        if (!password.equals(confirmpwd)) {
            ToastUtils.makeText(mContext, "两次输入密码不一致");
            return;
        }
//        验证密码格式是否正确
        String inviteCode = edi_verification_code.getText().toString().trim();
        if (TextUtils.isEmpty(inviteCode)){
            ToastUtils.makeText(mContext, "验证码不能为空");
        }
        //验证验证码是否正确
        if (!inviteCode.equals(code)) {
            ToastUtils.makeText(mContext, "验证码不正确");
            return;
        }
        if (!StringUtils.isPwd(password)) {
            ToastUtils.makeText(mContext, "密码格式不正确，请核对后重新输入");
            return;
        }
        String userphone = edi_phone_number.getText().toString().trim();
        logpwd = password;
        try {
            userRegister(userphone, Md5Util.md5Encode(logpwd));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 用户注册
     *
     * @param userPhone
     * @param password
     * @param
     */
    private void userRegister(final String userPhone, final String password) {
        String RecommendCode = edi_recommendation_code.getText().toString().trim();
        Map<String, String> params = new HashMap<>();
        final String json="{\"cmd\":\"userRegister\",\"userPhone\":\"" + userPhone + "\"," +
                "\"password\":\"" + password +"\",\"invitation\":\""+RecommendCode+"\"}";
        params.put("json", json);
        Log.i("6666", "userRegister: " + json);

        OkHttpUtils.post().url(Constant.THE_SERVER_URL).params(params).build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        ToastUtils.makeText(mContext, e.getMessage());
                    }
                    @Override
                    public void onResponse(String response, int id) {
                        Log.i("6666", "onResponse: " + response);
                        Gson gson = new Gson();
                        UserInfo bean = gson.fromJson(response, UserInfo.class);
                        if ("0".equals(bean.getResult())) {
                            ToastUtils.makeText(mContext, "注册成功");
                            SPUtil.putString(RegisterActivity.this,"uid",bean.getUid());
                            SPUtil.putString(RegisterActivity.this,"userPhone",userPhone);
                            SPUtil.putString(RegisterActivity.this,"password",password);
                            Bundle bundle = new Bundle();
                            bundle.putString("phone", userPhone);
                            bundle.putString("password",logpwd);
                            MyApplication.openActivity(mContext, LoginActivity.class, bundle);
                            finish();
                        } else {
                            ToastUtils.makeText(mContext,bean.getResultNote());
                        }
                    }
                });
    }
    /**
     * 获取短信验证码
     * @param phone
     */
    public void sendSMS(String phone, String CODE) {
        OkHttpUtils.post().url("https://v.juhe.cn/sms/send?").addParams("mobile", phone).addParams("tpl_id", "55288").addParams("tpl_value", "%23code%23%3d" + CODE).addParams("key", "140a6059cf053418d7b67543eeb4c17d").build().execute(new StringCallback() {
            @Override
            public void onResponse(String response, int id) {
                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.getString("error_code").equals("0")) {
                        ToastUtils.makeText(RegisterActivity.this,"短信已发送，请注意查收");
                    } else {
                        ToastUtils.makeText(RegisterActivity.this,obj.getString("reason"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onError(Call call, Exception e, int id) {

            }
        });

    }
}


