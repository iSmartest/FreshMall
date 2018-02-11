package com.lixin.freshmall.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.lixin.freshmall.R;
import com.lixin.freshmall.adapter.OrdeMoneyDecAdapter;
import com.lixin.freshmall.model.OrderDec;

import java.util.List;

/**
 * Created by 小火
 * Create time on  2017/12/18
 * My mailbox is 1403241630@qq.com
 */

public class OrderMoneyDecActivity extends BaseActivity {
    private ListView order_detailed;
    private View headView;
    private TextView mGiveChange, mRealMoney;
    private String totalPrice,giveChange;
    private List<? extends OrderDec.OrderDetailed> mList;
    private OrdeMoneyDecAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_money_dec);
        hideBack(2);
        setTitleText("订单明细");
        totalPrice = getIntent().getStringExtra("totalPrice");
        giveChange = getIntent().getStringExtra("giveChange");
        initView();
    }

    private void initView() {
        order_detailed = findViewById(R.id.order_detailed);
        headView = LayoutInflater.from(context).inflate(R.layout.foot_order_dec, null);
        mGiveChange = headView.findViewById(R.id.text_give_change);
        mRealMoney = headView.findViewById(R.id.text_real_money);
        headView.findViewById(R.id.order_dec_money_dec).setVisibility(View.GONE);
        mGiveChange.setText("￥" + giveChange);
        mRealMoney.setText("￥" + totalPrice);
        mList = getIntent().getParcelableArrayListExtra("orderDetailedList");
        if (headView != null){
            order_detailed.addHeaderView(headView);
        }
        mAdapter = new OrdeMoneyDecAdapter(context,mList);
        order_detailed.setAdapter(mAdapter);
    }
}
