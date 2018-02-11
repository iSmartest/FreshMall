package com.lixin.freshmall.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

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
 * Create time on  2017/5/19
 * My mailbox is 1403241630@qq.com
 */

public class SettingFeedbackActivity extends BaseActivity{
    private String uid;
    private EditText mTitle,mContent;
    private TextView mSure;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activtiy_feedback);
        uid = SPUtil.getString(context,"uid");
        hideBack(2);
        setTitleText("意见反馈");
        initView();
    }

    private void initView() {
        mTitle = findViewById(R.id.edi_feedback_title);
        mContent = findViewById(R.id.edit_feedback_dec);
        mSure = findViewById(R.id.text_feedback_submit);
        mSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });
    }

    private void submit() {
        String contentTitle = mTitle.getText().toString().trim();
        String content = mContent.getText().toString().trim();
        if (TextUtils.isEmpty(contentTitle)){
            ToastUtils.makeText(context,"请输入反馈内容标题");
        }else if (TextUtils.isEmpty(content)){
            ToastUtils.makeText(context,"请输入反馈内容");
        }else {
            getdata(contentTitle,content);
        }
    }

    private void getdata(String contentTitle, String content){
        Map<String, String> params = new HashMap<>();
        final String json = "{\"cmd\":\"feedbackOption\",\"uid\":\"" + uid + "\",\"contentTitle\":\"" + contentTitle + "\",\"content\":\"" +
                "" + content + "\"}";
        params.put("json", json);
        OkHttpUtils.post().url(Constant.THE_SERVER_URL).params(params)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ToastUtils.makeText(context, "网络异常");
            }
            @Override
            public void onResponse(String response, int id) {
                Gson gson = new Gson();
                UserInfo userInfo = gson.fromJson(response, UserInfo.class);
                if (userInfo.getResult().equals("1")) {
                    ToastUtils.makeText(context, userInfo.getResultNote());
                }
                ToastUtils.makeText(context,"意见反馈成功！");
                finish();
            }
        });
    }
}
