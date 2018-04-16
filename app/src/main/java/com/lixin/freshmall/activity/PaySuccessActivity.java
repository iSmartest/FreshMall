package com.lixin.freshmall.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.lixin.freshmall.R;
import com.lixin.freshmall.model.Constant;
import com.lixin.freshmall.uitls.AppManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by 小火
 * Create time on  2018/4/8
 * My mailbox is 1403241630@qq.com
 */

public class PaySuccessActivity extends BaseActivity {
    private TextView tvTotalAmount,mData,mTime,mText01,mText02,mText03;
    private String oderPayPrice,data,time,orderId,orderState,dateString;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_success);
        oderPayPrice = getIntent().getStringExtra("oderPayPrice");
        data = getIntent().getStringExtra("data");
        time = getIntent().getStringExtra("time");
        orderId = getIntent().getStringExtra("orderId");
        orderState = getIntent().getStringExtra("orderState");
        initView();
    }

    private void initView() {
        findViewById(R.id.ll_see_order).setOnClickListener(this);
        findViewById(R.id.ll_back_home).setOnClickListener(this);
        findViewById(R.id.iv_success_back).setOnClickListener(this);
        findViewById(R.id.text_see_user_agreement).setOnClickListener(this);
        tvTotalAmount = findViewById(R.id.pay_order_amount);
        mText01 = findViewById(R.id.text_text01);
        mText02 = findViewById(R.id.text_text02);
        mText03 = findViewById(R.id.text_text03);
        mData = findViewById(R.id.text_data);
        mTime = findViewById(R.id.text_time);
        tvTotalAmount.setText("￥"+oderPayPrice);
        mData.setText(data + "(明天)");
        mTime.setText(time);
        Date date = new Date();//取时间
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        date = calendar.getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        dateString = formatter.format(date);
        if (orderState.equals("2")){
            mText01.setText("请您于");
            mText02.setText("前往指定取货点取货");
            mText03.setText(R.string.get_reminder_notice);
        }else {
            mText01.setText("您购买的货物将于");
            mText02.setText("送达，请您注意查收");
            mText03.setText(R.string.send_reminder_notice);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_success_back:
                finish();
                break;
            case R.id.ll_see_order:
                Bundle bundle = new Bundle();
                bundle.putString("orderState",orderState);
                bundle.putString("orderId",orderId);
                bundle.putString("payTime",dateString);
                MyApplication.openActivity(context,OrderDecActivity.class,bundle);
                finish();
                break;
            case R.id.ll_back_home:
                AppManager.finishAllActivity();
                MyApplication.openActivity(context,MainActivity.class);
                break;
            case R.id.text_see_user_agreement:
                Bundle bundle1 = new Bundle();
                bundle1.putString("about", Constant.REGISTER);
                bundle1.putString("title","用户协议");
                MyApplication.openActivity(context,SettingAboutUsActivity.class,bundle1);
                break;
            default:
                break;
        }
    }
}
