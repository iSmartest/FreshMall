package com.lixin.freshmall.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lixin.freshmall.R;
import com.lixin.freshmall.model.Constant;
import com.lixin.freshmall.model.MyIntegralBean;
import com.lixin.freshmall.okhttp.OkHttpUtils;
import com.lixin.freshmall.okhttp.budiler.StringCallback;
import com.lixin.freshmall.uitls.SPUtil;
import com.lixin.freshmall.uitls.ToastUtils;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by 小火
 * Create time on  2017/11/30
 * My mailbox is 1403241630@qq.com
 */

public class MyIntegralActivity extends BaseActivity {
    private int nowPage = 1;
    private String uid;
    private TextView mIntegralNum,mIntegralGrade;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_integral);
        uid = SPUtil.getString(context,"uid");
        hideBack(2);
        setTitleText("我的积分");
        initView();
        getIntegral();
    }

    private void getIntegral() {
        Map<String, String> params = new HashMap<>();
        final String json="{\"cmd\":\"getIntegralInfo\",\"nowPage\":\"" + nowPage +"\",\"uid\":\""
                + uid + "\"}";
        params.put("json", json);
        dialog1.show();
        OkHttpUtils.post().url(Constant.THE_SERVER_URL).params(params)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ToastUtils.makeText(context, e.getMessage());
                dialog1.dismiss();
            }
            @Override
            public void onResponse(String response, int id) {
                Log.i("MyIntegralActivity", "onResponse: " + response);
                Gson gson = new Gson();
                dialog1.dismiss();
                MyIntegralBean myIntegralBean = gson.fromJson(response, MyIntegralBean.class);
                if (myIntegralBean.getResult().equals("1")) {
                    ToastUtils.makeText(context, myIntegralBean.getResultNote());
                    return;
                }
                if (TextUtils.isEmpty(myIntegralBean.getTotalMoney())){
                    mIntegralNum.setText("0");
                }else {
                    mIntegralNum.setText(myIntegralBean.getTotalMoney());
                }
                if (TextUtils.isEmpty(myIntegralBean.getIntegralGrade())){
                    mIntegralGrade.setText(R.string.ordinary_member);
                }else {
                    mIntegralGrade.setText(myIntegralBean.getIntegralGrade());
                }

            }
        });
    }

    private void initView() {
        mIntegralNum = findViewById(R.id.text_my_integral_num);
        mIntegralGrade = findViewById(R.id.text_my_integral_grade);
        findViewById(R.id.linear_my_integral).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.linear_my_integral:
                MyApplication.openActivity(context,MyIntegralDecActivity.class);
                break;
        }
    }
}
