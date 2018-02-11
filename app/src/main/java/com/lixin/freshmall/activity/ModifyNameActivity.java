package com.lixin.freshmall.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.gson.Gson;
import com.lixin.freshmall.R;
import com.lixin.freshmall.model.Constant;
import com.lixin.freshmall.model.UserInfo;
import com.lixin.freshmall.okhttp.OkHttpUtils;
import com.lixin.freshmall.okhttp.budiler.StringCallback;
import com.lixin.freshmall.uitls.SPUtil;
import com.lixin.freshmall.uitls.ToastUtils;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by 小火
 * Create time on  2017/10/24
 * My mailbox is 1403241630@qq.com
 */

public class ModifyNameActivity extends BaseActivity{
    private EditText et_myname;
    private String userIcon = "";
    private String userName;
    private String uid;
    private String userSex;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_name);
        hideBack(2);
        userSex = getIntent().getStringExtra("userSex");
        setTitleText("修改昵称");
        initView();
        initData();
    }
    /**
     * 初始化控件
     */
    private void initView() {
        et_myname = findViewById(R.id.et_my_name);
        findViewById(R.id.btn_confirm).setOnClickListener(this);
    }
    /**
     * 初始化数据
     */
    private void initData() {
        uid = SPUtil.getString(context,"uid");
        String nickName = SPUtil.getString(context,"nickName");
        et_myname.setHint(nickName);
    }

    /**
     * 点击事件处理
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_confirm:
                submit();
                break;
        }
    }
    /**
     * 提交信息验证
     */
    private void submit() {
        userName = et_myname.getText().toString().trim();
        if (TextUtils.isEmpty(userName)) {
            ToastUtils.makeText(context,"昵称不能为空");
            return;
        }
        setdata(userName);
    }
    private void setdata(final String userName) {
        Map<String, String> params = new HashMap<>();
        String json = "{\"cmd\":\"commitUserInfoDeatil\",\"uid\":\"" + uid + "\"" +
                ",\"userIcon\":\""+userIcon+"\",\"userName\":\""+userName+"\",\"userSex\":\""+userSex+"\"}";
        params.put("json", json);
        Log.i("json", "updateUserMessage: " + json);
        dialog1.show();
        OkHttpUtils.post().url(Constant.THE_SERVER_URL).params(params).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                dialog1.dismiss();
                ToastUtils.makeText(context,e.getMessage());
            }
            @Override
            public void onResponse(String response, int id) {
                Log.i("response", "updateUserMessage: " + response);
                Gson gson = new Gson();
                dialog1.dismiss();
                UserInfo mUserInfo = gson.fromJson(response,UserInfo.class);
                if (mUserInfo.getResult().equals("1")){
                    ToastUtils.makeText(context,mUserInfo.getResultNote());
                    return;
                }

                ToastUtils.makeText(context,"修改成功！");
                SPUtil.putString(context,"nickName",userName);
                Intent intent=new Intent();
                intent.putExtra("result", userName);
                setResult(3002,intent);
                finish();
                SPUtil.putString(context,"userIcon",mUserInfo.getUserIconUrl());
            }
        });
    }
}
