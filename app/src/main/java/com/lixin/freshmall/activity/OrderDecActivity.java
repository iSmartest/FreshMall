package com.lixin.freshmall.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lixin.freshmall.R;
import com.lixin.freshmall.adapter.OrderDecAdapter;
import com.lixin.freshmall.dialog.LogOutDialog;
import com.lixin.freshmall.model.Constant;
import com.lixin.freshmall.model.GenerateOrderBean;
import com.lixin.freshmall.model.MyWelletBean;
import com.lixin.freshmall.model.OrderDec;
import com.lixin.freshmall.model.StoreTimeBean;
import com.lixin.freshmall.model.UserInfo;
import com.lixin.freshmall.okhttp.OkHttpUtils;
import com.lixin.freshmall.okhttp.budiler.StringCallback;
import com.lixin.freshmall.uitls.DoubleCalculationUtil;
import com.lixin.freshmall.uitls.SPUtil;
import com.lixin.freshmall.uitls.ToastUtils;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by 小火
 * Create time on  2017/12/4
 * My mailbox is 1403241630@qq.com
 */

public class OrderDecActivity extends BaseActivity {
    private String uid, orderState, orderId, totalPrice, giveChange,payTime;
    private ListView order_dec_list;
    private TextView mOrderTotalPrice, mCancel, mPay;
    private LinearLayout LyActualTime, LyPlaceOrderTime, LyPayMethod, LyFoot,LyFree;
    private TextView mOrderNum, mOrderState, mStoreName, mStorePhone, mStoreAddress, mPlaceOrderTime,
            mReservationDate, mReservatioTime, mActualTime, mShoppingBag, mCoupon, mPayMethod, mAddressType
            ,mSendFree,mDataText,mTimeText,mActualTimeText;
    private TextView mGiveChange, mRealMoney;
    private View headView, footView;
    private OrderDecAdapter mAdapter;
    private List<OrderDec.Commoditys> mList;
    private List<GenerateOrderBean.commoditys> list;
    private List<OrderDec.OrderDetailed> orderDetailedList;
    private int nowPage = 1;
    private LogOutDialog mLogOutDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_dec);
        orderState = getIntent().getStringExtra("orderState");
        orderId = getIntent().getStringExtra("orderId");
        payTime = getIntent().getStringExtra("payTime");
        uid = SPUtil.getString(context, "uid");
        mList = new ArrayList<>();
        orderDetailedList = new ArrayList<>();
        hideBack(2);
        setTitleText("订单详情");
        initView();
        getdata();
    }

    private void initView() {
        order_dec_list = findViewById(R.id.order_dec_list);
        mOrderTotalPrice = findViewById(R.id.order_dec_total_price);
        mCancel = findViewById(R.id.order_dec_cancel);
        mCancel.setOnClickListener(this);
        mPay = findViewById(R.id.order_dec_pay);
        mPay.setOnClickListener(this);
        headView = LayoutInflater.from(context).inflate(R.layout.head_order_dec, null);
        mOrderNum = headView.findViewById(R.id.text_order_dec_num);
        mOrderState = headView.findViewById(R.id.text_order_dec_state);
        mAddressType = headView.findViewById(R.id.text_address_type);
        mStoreName = headView.findViewById(R.id.text_order_dec_name);
        mStorePhone = headView.findViewById(R.id.text_order_dec_store_phone);
        mStoreAddress = headView.findViewById(R.id.text_order_dec_address);
        LyPlaceOrderTime = headView.findViewById(R.id.linear_place_order_time);
        mDataText = headView.findViewById(R.id.text_order_data);
        mTimeText = headView.findViewById(R.id.text_order_time);
        mActualTimeText = headView.findViewById(R.id.text_actual_time);
        mPlaceOrderTime = headView.findViewById(R.id.text_place_order_time);
        mReservationDate = headView.findViewById(R.id.text_reservation_date);
        mReservatioTime = headView.findViewById(R.id.text_reservation_time);
        LyActualTime = headView.findViewById(R.id.linear_actual_get_goods_time);
        LyFree = headView.findViewById(R.id.linear_send_free);
        mSendFree = headView.findViewById(R.id.text_send_free);
        mActualTime = headView.findViewById(R.id.text_actual_get_goods_time);
        mShoppingBag = headView.findViewById(R.id.text_shopping_bag);
        mCoupon = headView.findViewById(R.id.text_order_dec_coupon);
        LyPayMethod = headView.findViewById(R.id.linear_payment_method);
        mPayMethod = headView.findViewById(R.id.text_payment_method);
        if (headView != null) {
            order_dec_list.addHeaderView(headView);
        }
        footView = LayoutInflater.from(context).inflate(R.layout.foot_order_dec, null);
        LyFoot = footView.findViewById(R.id.linear_foot_oder_dec);
        mGiveChange = footView.findViewById(R.id.text_give_change);
        mRealMoney = footView.findViewById(R.id.text_real_money);
        footView.findViewById(R.id.order_dec_money_dec).setOnClickListener(this);
        if (footView != null) {
            order_dec_list.addFooterView(footView);
        }
        mOrderNum.setText("订单号：" + orderId);
        switch (orderState) {
            case "1":
                mOrderState.setText("待付款");
                LyActualTime.setVisibility(View.GONE);
                LyPlaceOrderTime.setVisibility(View.GONE);
                LyPayMethod.setVisibility(View.GONE);
                LyFoot.setVisibility(View.GONE);
                mCancel.setVisibility(View.VISIBLE);
                mCancel.setText("取消订单");
                mPay.setText("去支付");
                mPay.setVisibility(View.VISIBLE);
                break;
            case "2":
                mOrderState.setText("待取货");
                mCancel.setVisibility(View.VISIBLE);
                mCancel.setText("我要退款");
                mPay.setVisibility(View.GONE);
                LyFoot.setVisibility(View.GONE);
                break;
            case "5":
                mOrderState.setText("待收货");
                mCancel.setVisibility(View.VISIBLE);
                mCancel.setText("我要退款");
                mPay.setVisibility(View.GONE);
                LyFoot.setVisibility(View.GONE);
                break;
            case "3":
                mOrderState.setText("已完成");
                mCancel.setVisibility(View.VISIBLE);
                mCancel.setText("删除订单");
                mPay.setVisibility(View.GONE);
                break;
            default:
                break;
        }
        mAdapter = new OrderDecAdapter(context, mList);
        order_dec_list.setAdapter(mAdapter);
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
                List<OrderDec.Commoditys> commoditys = orderDec.getCommoditys();
                List<OrderDec.OrderDetailed> orderDetaileds = orderDec.getOrderDetailed();
                if (orderDec.getStype().equals("0")){
                    mAddressType.setText("取货地址");
                    LyFree.setVisibility(View.GONE);
                    mStoreName.setText("【" + orderDec.getGetGoodsStore() + "】");
                    mStorePhone.setText(orderDec.getGetGoodsStorePhone());
                    mStoreAddress.setText(orderDec.getGetGoodsAddress());
                    mPlaceOrderTime.setText(orderDec.getPlaceOrderTime());
                    mDataText.setText("预约取货日期");
                    mTimeText.setText("预约取货时间");
                    mActualTimeText.setText("实际取货时间");
                }else {
                    mAddressType.setText("收货地址");
                    LyFree.setVisibility(View.VISIBLE);
                    mStoreName.setText("【" + orderDec.getUserName() + "】");
                    mStorePhone.setText(orderDec.getPhone());
                    mStoreAddress.setText(orderDec.getAddress());
                    mSendFree.setText(orderDec.getSendMoney()+"元");
                    mDataText.setText("预约收货日期");
                    mTimeText.setText("预约收货时间");
                    mActualTimeText.setText("实际收货时间");
                }
                mPlaceOrderTime.setText(orderDec.getPlaceOrderTime());
                String str = orderDec.getWantGetGoodsTime();
                str = str.substring(0, str.indexOf("日") + 1);
                mReservationDate.setText(str);
                String strTime = orderDec.getWantGetGoodsTime();
                strTime = strTime.substring(str.length(), strTime.length());
                mReservatioTime.setText(strTime);
                mActualTime.setText(orderDec.getActualGetGoodsTime());
                mShoppingBag.setText(orderDec.getShoppingBag());
                mCoupon.setText(orderDec.getCoupon());
                switch (orderDec.getPaymentMethod()) {
                    case "1":
                        mPayMethod.setText("余额");
                        break;
                    case "2":
                        mPayMethod.setText("余额加支付宝");
                        break;
                    case "3":
                        mPayMethod.setText("余额加微信");
                        break;
                    case "4":
                        mPayMethod.setText("支付宝");
                        break;
                    case "5":
                        mPayMethod.setText("微信");
                        break;
                    default:
                        break;
                }
                giveChange = orderDec.getGiveChange();
                mGiveChange.setText("￥" + orderDec.getGiveChange());
                totalPrice = orderDec.getAmountPaid();
                mRealMoney.setText("￥" + orderDec.getAmountPaid());
                mOrderTotalPrice.setText("￥" + orderDec.getOderTotalPrice());
                if (commoditys != null && !commoditys.isEmpty()) {
                    mList.addAll(commoditys);
                    mAdapter.notifyDataSetChanged();
                }
                if (orderDetaileds != null && !orderDetaileds.isEmpty()) {
                    orderDetailedList.addAll(orderDetaileds);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.order_dec_cancel:
                switch (orderState) {
                    case "1":
                        int cancel = 0;
                        getCancelOrder(orderId, cancel);
                        break;
                    case "2":
                        initData(orderId,totalPrice,payTime);
                        break;
                    case "3":
                        mLogOutDialog = new LogOutDialog(context, "确定删除订单吗？","取消","确定",new LogOutDialog.OnSureBtnClickListener() {
                            @Override
                            public void sure() {
                                mLogOutDialog.dismiss();
                                int delete = 1;
                                getCancelOrder(orderId, delete);
                            }
                        });
                        mLogOutDialog.show();
                        break;
                    case "5":
                        initData(orderId,totalPrice,payTime);
                        break;
                    default:
                        break;
                }
                break;
            case R.id.order_dec_pay:
                goPay();
                break;
            case R.id.order_dec_money_dec:
                Bundle bundle = new Bundle();
                bundle.putString("orderId", orderId);
                MyApplication.openActivity(context, OrderMoneyDecActivity.class, bundle);
                break;
            default:
                break;
        }
    }

    private void getCancelOrder(String orderId, final int type) {
        Map<String, String> params = new HashMap<>();
        final String json = "{\"cmd\":\"deleteOrder\",\"orderId\":\"" + orderId + "\",\"uid\":\""
                + uid + "\",\"type\":\"" + type + "\"}";
        params.put("json", json);
        Log.i("MyAllOrderFragment", "onResponse: " + json);
        OkHttpUtils.post().url(Constant.THE_SERVER_URL).params(params)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ToastUtils.makeText(context, e.getMessage());
            }

            @Override
            public void onResponse(String response, int id) {
                Log.i("MyAllOrderFragment", "onResponse: " + response);
                Gson gson = new Gson();
                UserInfo userInfo = gson.fromJson(response, UserInfo.class);
                if (userInfo.getResult().equals("1")) {
                    ToastUtils.makeText(context, userInfo.getResultNote());
                    return;
                }
                if (type == 0) {
                    ToastUtils.makeText(context, "订单取消成功！");
                    finish();
                } else if (type == 1) {
                    ToastUtils.makeText(context, "订单删除成功！");
                    finish();
                }
            }
        });
    }

    private void goPay() {
        Map<String, String> params = new HashMap<>();
        final String json = "{\"cmd\":\"getWalletInfo\",\"nowPage\":\"" + nowPage + "\",\"uid\":\""
                + uid + "\"}";
        params.put("json", json);
        OkHttpUtils.post().url(Constant.THE_SERVER_URL).params(params)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ToastUtils.makeText(context, e.getMessage());
            }

            @Override
            public void onResponse(String response, int id) {
                Log.i("MyWalletActivity", "onResponse: " + response);
                Gson gson = new Gson();
                MyWelletBean myWelletBean = gson.fromJson(response, MyWelletBean.class);
                if (myWelletBean.result.equals("1")) {
                    ToastUtils.makeText(context, myWelletBean.resultNote);
                    return;
                }
                Double temp = 0.00;
                for (int i = 0; i < mList.size(); i++) {
                    temp = DoubleCalculationUtil.add(temp, DoubleCalculationUtil.mul(mList.get(i).getCommodityNewPrice(), mList.get(i).getCommodityBuyNum()));
                }
                list = new ArrayList<>();
                for (int i = 0; i < mList.size(); i++) {
                    OrderDec.Commoditys commoditys = mList.get(i);
                    GenerateOrderBean.commoditys comm = new GenerateOrderBean.commoditys(commoditys.getCommodityid(),
                            commoditys.getCommodityTitle(), commoditys.getCommodityIcon()
                            , commoditys.getCommodityFirstParameter(), commoditys.getCommoditySecondParameter(), commoditys.getCommodityNewPrice(), commoditys.getCommodityBuyNum(), commoditys.getCommodityUnit());
                    list.add(comm);
                }
                SPUtil.putString(context, "mATime", myWelletBean.getSendAtmTime());
                SPUtil.putString(context, "mPTime", myWelletBean.getSendPtmTime());
                String mMoney = myWelletBean.getTotalMoney();
                Intent intent = new Intent(context, NowBuyActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("orderId", orderId);
                bundle.putString("mMoney", mMoney);
                bundle.putDouble("totalPrice", temp);
                bundle.putSerializable("OrderShop", (Serializable) list);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
    }

    private void initData(final String orderId, final String totalPrice,final String paytime) {
        String storeId = SPUtil.getString(context,"storeId");
        Map<String,String> params = new HashMap<>();
        String json = "{\"cmd\":\"getStoreInfoTime\",\"storeId\":\""+storeId+"\"}";
        params.put("json",json);
        OkHttpUtils.post().url(Constant.THE_SERVER_URL).params(params).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ToastUtils.makeText(context,e.getMessage());
            }
            @Override
            public void onResponse(String response, int id) {
                Log.i("getStoreInfoTime", "onResponse: " +response );
                Gson gson = new Gson();
                StoreTimeBean storeTimeBean = gson.fromJson(response,StoreTimeBean.class);
                if (storeTimeBean.getResult().equals("1")){
                    ToastUtils.makeText(context,storeTimeBean.getResultNote());
                    return;
                }
                int storeEndTime = storeTimeBean.getStoreEndTime();

                Date date = new Date();//取时间
                Calendar calendar = new GregorianCalendar();
                calendar.setTime(date);
                date = calendar.getTime(); //这个时间就是日期往后推一天的结果
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                String dateString = formatter.format(date);
                if (dateString.equals(paytime)){
                    int currentTime = calendar.get(Calendar.HOUR_OF_DAY);
                    if (currentTime >= storeEndTime){
                        ToastUtils.makeText(context,"已过截止时间，不能退单了");
                    } else {
                        Intent intent = new Intent(context,WantRefundActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("orderId",orderId);
                        bundle.putString("totalPrice",totalPrice);
                        intent.putExtras(bundle);
                        context.startActivity(intent);
                    }
                }else {
                    ToastUtils.makeText(context,"已过截止时间，不能退单了");
                }
            }
        });
    }
}
