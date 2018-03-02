package com.lixin.freshmall.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lixin.freshmall.R;
import com.lixin.freshmall.adapter.OrdeMoneyDecAdapter;
import com.lixin.freshmall.model.Constant;
import com.lixin.freshmall.model.OrderDec;
import com.lixin.freshmall.okhttp.OkHttpUtils;
import com.lixin.freshmall.okhttp.budiler.StringCallback;
import com.lixin.freshmall.uitls.ToastUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by 小火
 * Create time on  2017/12/18
 * My mailbox is 1403241630@qq.com
 */

public class OrderMoneyDecActivity extends BaseActivity {
    private ListView order_detailed;
    private View headView;
    private TextView mGiveChange, mRealMoney;
    private String orderId,totalPrice,giveChange;
    private List<OrderDec.OrderDetailed> mList;
    private OrdeMoneyDecAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_money_dec);
        hideBack(2);
        setTitleText("订单明细");
        orderId = getIntent().getStringExtra("orderId");
        mList = new ArrayList<>();
        initView();
        getdata();
    }

    private void initView() {
        order_detailed = findViewById(R.id.order_detailed);
        headView = LayoutInflater.from(context).inflate(R.layout.foot_order_dec, null);
        mGiveChange = headView.findViewById(R.id.text_give_change);
        mRealMoney = headView.findViewById(R.id.text_real_money);
        headView.findViewById(R.id.order_dec_money_dec).setVisibility(View.GONE);
        if (headView != null){
            order_detailed.addHeaderView(headView);
        }
        mAdapter = new OrdeMoneyDecAdapter(context,mList);
        order_detailed.setAdapter(mAdapter);
    }

    private void getdata() {
        Map<String, String> params = new HashMap<>();
        final String json = "{\"cmd\":\"orderDetailInfo\",\"orderId\":\"" + orderId + "\"}";
        params.put("json", json);
        dialog1.show();
        OkHttpUtils.post().url(Constant.THE_SERVER_URL).params(params).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                dialog1.dismiss();
                ToastUtils.makeText(context, e.getMessage());
            }
            @Override
            public void onResponse(String response, int id) {
                Log.i("getGoodsCode", "onResponse: " + response);
                Gson gson = new Gson();
                dialog1.dismiss();
                OrderDec orderDec = gson.fromJson(response, OrderDec.class);
                if (orderDec.getResult().equals("1")) {
                    ToastUtils.makeText(context, orderDec.getResultNote());
                    return;
                }
                List<OrderDec.OrderDetailed> orderDetaileds = orderDec.getOrderDetailed();
                mGiveChange.setText("￥" + orderDec.getGiveChange());
                mRealMoney.setText("￥" + orderDec.getAmountPaid());
                if (orderDetaileds != null && !orderDetaileds.isEmpty()) {
                    mList.addAll(orderDetaileds);
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
    }
}
