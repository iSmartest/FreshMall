package com.lixin.freshmall.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lixin.freshmall.R;
import com.lixin.freshmall.model.Constant;
import com.lixin.freshmall.okhttp.OkHttpUtils;
import com.lixin.freshmall.okhttp.budiler.StringCallback;
import com.lixin.freshmall.uitls.SPUtil;
import com.lixin.freshmall.uitls.ToastUtils;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by 小火
 * Create time on  2017/12/4
 * My mailbox is 1403241630@qq.com
 */

public class WantRefundActivity extends BaseActivity {
    private String orderId;
    private String totalPrice;
    private EditText mRefundTitle,mRefundDec;
    private TextView mRefundMoney,mRefundSure;
    private String uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_want_refund);
        hideBack(2);
        setTitleText("我要退款");
        Intent intent = getIntent();
        orderId = intent.getStringExtra("orderId");
        totalPrice = intent.getStringExtra("totalPrice");
        uid = SPUtil.getString(WantRefundActivity.this,"uid");
        initView();
    }

    private void initView() {
        mRefundTitle = findViewById(R.id.a_refund_foot_leaving_a_message);
        mRefundMoney = findViewById(R.id.text_refund_foot_balance);
        mRefundMoney.setText(totalPrice);
        mRefundDec = findViewById(R.id.edit_refund_add_address_dec);
        mRefundSure = findViewById(R.id.text_refund_sure);
        mRefundSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });

    }
    private void submit() {
        String mTitle = mRefundTitle.getText().toString().trim();
        String mDec = mRefundDec.getText().toString().trim();
        if (TextUtils.isEmpty(mTitle)){
            ToastUtils.makeText(context,"请输入退款标题");
        }else if (TextUtils.isEmpty(mDec)){
            ToastUtils.makeText(context,"请输入退款详情");
        }else {
            submitdata(mTitle,mDec);
        }
    }

    private void submitdata(String mTitle, String mDec) {
        Map<String,String> params = new HashMap<>();
        final String json = "{\"cmd\":\"drawbackMoney\",\"orderId\":\"" + orderId + "\",\"uid\":\""
                + uid + "\",\"drawbackTitle\":\"" + mTitle + "\",\"drawbackContent\":\"" + mDec +"\"}";
        params.put("json", json);
        Log.i("WaitPaymentFragment", "onResponse: " + json);
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
                Log.i("WaitPaymentFragment", "onResponse: " + response);
                Gson gson = new Gson();
                dialog1.dismiss();
//                UserInfo userInfo = gson.fromJson(response, UserInfo.class);
//                if (userInfo.getResult().equals("1")) {
//                    ToastUtils.makeText(context, userInfo.getResultNote());
//                    return;
//                }
                ToastUtils.makeText(WantRefundActivity.this,"退款申请已提交！");
                Intent intent = new Intent();
                intent.setAction("com.freshmall.code.changed");
                getApplicationContext().sendBroadcast(intent);
                finish();
            }
        });
    }
}
