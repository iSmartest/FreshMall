package com.lixin.freshmall.activity;

import android.os.Bundle;
import android.text.TextUtils;
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
 * Create time on  2017/12/7
 * My mailbox is 1403241630@qq.com
 */

public class GoEvaluateActivity extends BaseActivity {
    private String uid;
    private EditText mContent;
    private String commentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_go_evaluate);
        uid = SPUtil.getString(context, "uid");
        commentId = getIntent().getStringExtra("commentId");
        hideBack(2);
        setTitleText("商品评价");
        initView();
    }
    private void initView() {
        mContent = findViewById(R.id.edit_evaluate_dec);
        findViewById(R.id.text_evaluate_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = mContent.getText().toString().trim();
                if (TextUtils.isEmpty(content)) {
                    ToastUtils.makeText(context, "请输入反馈内容");
                    return;
                }
                getdata(content);
            }
        });
    }

    private void getdata(String content) {
        Map<String, String> params = new HashMap<>();
        final String json = "{\"cmd\":\"goEvaluate\",\"uid\":\"" + uid + "\",\"commentId\":\"" + commentId + "\",\"content\":\"" +
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
                ToastUtils.makeText(context, "商品评价成功！");
                MyApplication.evaluate = 1;
                finish();
            }
        });
    }
}
