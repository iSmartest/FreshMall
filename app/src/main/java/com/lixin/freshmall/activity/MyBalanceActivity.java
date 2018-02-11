package com.lixin.freshmall.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lixin.freshmall.R;
import com.lixin.freshmall.dialog.ProgressDialog;
import com.lixin.freshmall.model.Constant;
import com.lixin.freshmall.model.MyWelletBean;
import com.lixin.freshmall.okhttp.OkHttpUtils;
import com.lixin.freshmall.okhttp.budiler.StringCallback;
import com.lixin.freshmall.uitls.SPUtil;
import com.lixin.freshmall.uitls.StatusBarUtil;
import com.lixin.freshmall.uitls.ToastUtils;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by 小火
 * Create time on  2017/11/30
 * My mailbox is 1403241630@qq.com
 */

public class MyBalanceActivity extends Activity implements View.OnClickListener {
    protected Context context;
    protected Dialog dialog;
    private TextView mWalletNum;
    private int nowPage = 1;
    private String uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance);
        context = this;
        StatusBarUtil.transparentStatusBar(MyBalanceActivity.this);
        dialog = ProgressDialog.createLoadingDialog(context, "加载中.....");
        uid = SPUtil.getString(context,"uid");
        initView();
        getMyWellet();
    }

    private void initView() {
        RelativeLayout top_iag = findViewById(R.id.rl_balance_top_bar);
        StatusBarUtil.setHeightAndPadding(MyBalanceActivity.this, top_iag);
        mWalletNum = findViewById(R.id.text_my_wallet_num);
        findViewById(R.id.tv_my_wallet_money_dec).setOnClickListener(this);
        findViewById(R.id.iv_balance_back).setOnClickListener(this);
        findViewById(R.id.linear_balance_recharge).setOnClickListener(this);
        findViewById(R.id.linear_balance_withdrawals).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_balance_back:
                finish();
                break;
            case R.id.tv_my_wallet_money_dec:
                Intent intent = new Intent(MyBalanceActivity.this, MoneyDecActivity.class);
                startActivity(intent);
                break;
            case R.id.linear_balance_recharge:

                break;
            case R.id.linear_balance_withdrawals:

                break;
            default:
                break;
        }

    }

    private void getMyWellet() {
        Map<String, String> params = new HashMap<>();
        final String json="{\"cmd\":\"getWalletInfo\",\"nowPage\":\"" + nowPage +"\",\"uid\":\""
                + uid + "\"}";
        params.put("json", json);
        dialog.show();
        OkHttpUtils.post().url(Constant.THE_SERVER_URL).params(params)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ToastUtils.makeText(context, e.getMessage());
                dialog.dismiss();
            }
            @Override
            public void onResponse(String response, int id) {
                Log.i("MyWalletActivity", "onResponse: " + response);
                Gson gson = new Gson();
                dialog.dismiss();
                MyWelletBean myWelletBean = gson.fromJson(response, MyWelletBean.class);
                if (myWelletBean.result.equals("1")) {
                    ToastUtils.makeText(context, myWelletBean.resultNote);
                    return;
                }
                mWalletNum.setText(myWelletBean.getTotalMoney());
            }
        });
    }

}
