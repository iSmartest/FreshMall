package com.lixin.freshmall.activity;


import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

public class ForgetPwdActivity extends BaseActivity implements View.OnClickListener{
    private static final String TAG = "ForgetPwdActivity";
    private EditText edi_phone_number,edi_verification_code,edi_password,edi_password_again;
    private Button btn_confirm_modification,btn_verification_code;
    private Context mContext;
    private String code;
    private String token;
    private String password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgrt_password);
        mContext = this;
        hideBack(2);
        setTitleText("找回密码");
        initView();
    }
    private void initView() {

        edi_phone_number = findViewById(R.id.edi_phone_number);
        edi_verification_code = findViewById(R.id.edi_verification_code);
        edi_password = findViewById(R.id.edi_password);
        edi_password_again = findViewById(R.id.edi_password_again);
        btn_verification_code = findViewById(R.id.btn_verification_code);
        btn_confirm_modification = findViewById(R.id.btn_confirm_modification);
        edi_phone_number.setOnClickListener(this);
        edi_verification_code.setOnClickListener(this);
        edi_password.setOnClickListener(this);
        edi_password_again.setOnClickListener(this);
        btn_verification_code.setOnClickListener(this);
        btn_confirm_modification.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_verification_code:
                //验证手机号不能为空
                String userphone = edi_phone_number.getText().toString().trim();
                if (TextUtils.isEmpty(userphone)) {
                    ToastUtils.makeText(mContext, "电话号码不能为空");
                    return;
                }
                //验证电话号码是否正确
                if (!StringUtils.isMatchesPhone(userphone)) {
                    ToastUtils.makeText(mContext, "电话号码不正确，请核对后重新输入");
                    return;
                }
                code = TimerUtil.getNum();
                getPin(userphone,code);
                TimerUtil timerUtil = new TimerUtil(btn_verification_code);
                timerUtil.timers();
                break;
            case R.id.btn_confirm_modification:
                submit();
                break;
        }
    }

    private void submit() {
        //验证验证码不能为空
        String passPin = edi_verification_code.getText().toString().trim();
        if (TextUtils.isEmpty(passPin)) {
            ToastUtils.makeText(mContext, "验证码不能为空");
            return;
        }
        //验证验证码是否正确
        if (!passPin.equals(code)) {
            ToastUtils.makeText(mContext, "验证码不正确");
            return;
        }
        //验证密码不能为空
        password = edi_password.getText().toString().trim();
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
        //验证密码格式是否正确
        if (!StringUtils.isPwd(password)) {
            ToastUtils.makeText(mContext, "密码格式不正确，请核对后重新输入");
            return;
        }
        //验证电话号码不能为空
        String userphone = edi_phone_number.getText().toString().trim();
        try {
            findPassword(userphone, Md5Util.md5Encode(password));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 获取短信验证码
     * @param phone
     */
    public void getPin(String phone, String CODE) {
        OkHttpUtils.post().url("https://v.juhe.cn/sms/send?").addParams("mobile", phone).addParams("tpl_id", "67568").addParams("tpl_value", "%23code%23%3d" + CODE).addParams("key", "140a6059cf053418d7b67543eeb4c17d").build().execute(new StringCallback() {
            @Override
            public void onResponse(String response, int id) {
                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.getString("error_code").equals("0")) {
                        ToastUtils.makeText(ForgetPwdActivity.this,"短信已发送，请注意查收");
                    } else {
                        ToastUtils.makeText(ForgetPwdActivity.this,obj.getString("reason"));
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




    /**
     * 忘记密码
     * @param phone
     * @param passWord
     */
    private void findPassword(final String phone, final String passWord) {
        Map<String, String> params = new HashMap<>();
        String json="{\"cmd\":\"forgetPassword\",\"phoneNum\":\"" + phone + "\"," +
                "\"password\":\"" + passWord +"\"}";
        params.put("json", json);
        dialog1.show();
        OkHttpUtils.post().url(Constant.THE_SERVER_URL).params(params).build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        ToastUtils.makeText(mContext, e.getMessage());
                        dialog1.dismiss();
                    }
                    @Override
                    public void onResponse(String response, int id) {
                        dialog1.dismiss();
                        Gson gson = new Gson();
                        UserInfo bean = gson.fromJson(response, UserInfo.class);
                        if ("0".equals(bean.getResult())) {
                            ToastUtils.makeText(mContext,"找回密码成功");
                            SPUtil.putString(mContext,"phoneNum",phone);
                            SPUtil.putString(mContext,"password",password);
                            Bundle bundle = new Bundle();
                            bundle.putString("phone", phone);
                            bundle.putString("password", password);
                            MyApplication.openActivity(mContext, LoginActivity.class, bundle);
                            finish();
                        } else {
                            ToastUtils.makeText(mContext,bean.getResultNote());
                        }
                    }
                });
    }
}


