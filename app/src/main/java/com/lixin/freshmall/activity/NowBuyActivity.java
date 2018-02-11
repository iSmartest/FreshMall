package com.lixin.freshmall.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lixin.freshmall.R;
import com.lixin.freshmall.adapter.NowBuyAdapter;
import com.lixin.freshmall.dialog.LogOutDialog;
import com.lixin.freshmall.model.Constant;
import com.lixin.freshmall.model.GenerateOrderBean;
import com.lixin.freshmall.model.StoreTimeBean;
import com.lixin.freshmall.model.UserInfo;
import com.lixin.freshmall.okhttp.OkHttpUtils;
import com.lixin.freshmall.okhttp.budiler.StringCallback;
import com.lixin.freshmall.popupwindow.GetGoodsTimePopupWindow;
import com.lixin.freshmall.popupwindow.SendGoodsTimePopupWindow;
import com.lixin.freshmall.uitls.DoubleCalculationUtil;
import com.lixin.freshmall.uitls.SPUtil;
import com.lixin.freshmall.uitls.ToastUtils;
import com.pingplusplus.android.Pingpp;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by 小火
 * Create time on  2017/12/4
 * My mailbox is 1403241630@qq.com
 */

public class NowBuyActivity extends BaseActivity {

    private View headView,cursor_send,cursor_get;
    private CheckBox mCheckBox;
    private EditText tvMessage;
    private LogOutDialog dialog;
    private ListView now_buy_list;
    private NowBuyAdapter mAdapter;
    private boolean isCheckBox = false;
    private int storeEndTime,currentTime;
    private LinearLayout mLinearBalance, mLinearChooseTime,mLyDistributionFee;
    private ArrayList<? extends GenerateOrderBean.commoditys> mList;
    private TextView totalPrice, tvChooseTime, tvChooseData, tvShoppingBag, tvCoupon, mBalance, mSure, mTvOk;
    private Double mTotalPrice,mTempTotalPrice, money, allPrice, Surplus, totalMoney, reducePrice = 0.00, shoppingBagPrice = 0.00, temp = 0.00,free = 0.00, fullFree = 0.00;
    private String orderId, mMoney, storeName, storeAddress, storePhone,storeId,addressId,address,userName,phone,dateString,channel, charge, issend,getTime = "",sendTime = "",couponId = "", shoppingBag = "";
    private TextView mStoreName,mStorePhone,mStoreAddress, mAddressTitle,mReservationData,mReservationTime,mGetGoods,mSendGoods,mDistributionFee;
    private boolean mChooseMode = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_now_buy);
        hideBack(2);
        setTitleText("立即购买");
        Intent intent = getIntent();
        mList = intent.getParcelableArrayListExtra("OrderShop");
        orderId = intent.getStringExtra("orderId");
        mMoney = intent.getStringExtra("mMoney");
        mTotalPrice = intent.getDoubleExtra("totalPrice", 1);
        mTempTotalPrice = mTotalPrice;
        money = Double.parseDouble(mMoney);
        storeName = SPUtil.getString(context, "storeName");
        storeAddress = SPUtil.getString(context, "storeAddress");
        storePhone = SPUtil.getString(context, "storePhone");
        storeId = SPUtil.getString(context, "storeId");
        initView();
        getdata();
        showType(true);
        initData();
    }
    private void getdata() {
        Map<String, String> params = new HashMap<>();
        final String json="{\"cmd\":\"getStoreInfoTime\",\"storeId\":\"" + storeId +"\"}";
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
                Log.i("MyWalletActivity", "onResponse: " + response);
                Gson gson = new Gson();
                dialog1.dismiss();
                StoreTimeBean storeTimeBean = gson.fromJson(response, StoreTimeBean.class);
                if (storeTimeBean.getResult().equals("1")) {
                    ToastUtils.makeText(context, storeTimeBean.getResultNote());
                    return;
                }
                issend = storeTimeBean.getIssend();
                fullFree = storeTimeBean.getSendAllMoney();
                if (mTotalPrice >= fullFree){
                    free = 0.0;
                }else {
                    free = storeTimeBean.getSendMoney();
                }
            }
        });
    }



    private void initView() {
        now_buy_list = findViewById(R.id.now_buy_list);
        totalPrice = findViewById(R.id.text_now_buy_total_price);
        mSure = findViewById(R.id.text_now_buy_sure);
        mSure.setOnClickListener(this);
        headView = LayoutInflater.from(NowBuyActivity.this).inflate(R.layout.now_buy_head, null);
        headView.findViewById(R.id.linear_get_goods).setOnClickListener(this);
        headView.findViewById(R.id.linear_send_goods).setOnClickListener(this);
        headView.findViewById(R.id.iv_add_information).setOnClickListener(this);
        mLyDistributionFee = headView.findViewById(R.id.linear_distribution_fee);
        mAddressTitle = headView.findViewById(R.id.text_address_title);
        mReservationData = headView.findViewById(R.id.text_date_reservation);
        mReservationTime = headView.findViewById(R.id.text_time_reservation);
        mGetGoods = headView.findViewById(R.id.text_get_goods);
        mSendGoods = headView.findViewById(R.id.text_send_goods);
        mDistributionFee = headView.findViewById(R.id.text_distribution_fee);
        cursor_send = headView.findViewById(R.id.cursor_send);
        cursor_get = headView.findViewById(R.id.cursor_get);

        mStoreName = ((TextView) headView.findViewById(R.id.text_buy_store_name));
        mStorePhone = ((TextView) headView.findViewById(R.id.text_store_phone));
        mStoreAddress = ((TextView) headView.findViewById(R.id.add_user_address));
        tvChooseTime = headView.findViewById(R.id.text_choose_time);
        tvChooseTime.setOnClickListener(this);
        tvChooseData = headView.findViewById(R.id.text_choose_data);
        tvShoppingBag = headView.findViewById(R.id.text_choose_shopping_bag);
        tvShoppingBag.setOnClickListener(this);
        tvCoupon = headView.findViewById(R.id.text_choose_coupon);
        tvCoupon.setOnClickListener(this);
        mLinearBalance = headView.findViewById(R.id.linear_now_buy_balance);
        mLinearChooseTime = headView.findViewById(R.id.linear_choose_time);
        mBalance = headView.findViewById(R.id.text_buy_foot_balance);
        mCheckBox = headView.findViewById(R.id.ck_now_buy_foot_chose);
        tvMessage = headView.findViewById(R.id.a_now_foot_leaving_a_message);
        if (headView != null) {
            now_buy_list.addHeaderView(headView);
        }
        mAdapter = new NowBuyAdapter(context, mList);
        now_buy_list.setAdapter(mAdapter);
    }

    private void showType(boolean isShow){
        if (isShow){
            mAddressTitle.setText("取货地址");
            mStoreName.setText(storeName);
            mStorePhone.setText(storePhone);
            mStoreAddress.setText(storeAddress);
            tvChooseTime.setText(getTime);
            mReservationData.setText("预约取货日期");
            mReservationTime.setText("预约取货时间");
            mGetGoods.setTextColor(context.getResources().getColorStateList(R.color.text_color_green));
            mSendGoods.setTextColor(context.getResources().getColorStateList(R.color.text_color_main));
            cursor_send.setVisibility(View.INVISIBLE);
            cursor_get.setVisibility(View.VISIBLE);
            mLyDistributionFee.setVisibility(View.GONE);
            mTotalPrice = mTempTotalPrice;
            totalPrice.setText("" + mTotalPrice);
        }else {
            mAddressTitle.setText("送货地址");
            mStoreName.setText(" ");
            mStorePhone.setText(" ");
            mStoreAddress.setText(" ");
            tvChooseTime.setText(sendTime);
            mReservationData.setText("预约送货日期");
            mReservationTime.setText("预约送货时间");
            mSendGoods.setTextColor(context.getResources().getColorStateList(R.color.text_color_green));
            mGetGoods.setTextColor(context.getResources().getColorStateList(R.color.text_color_main));
            cursor_get.setVisibility(View.INVISIBLE);
            cursor_send.setVisibility(View.VISIBLE);
            mLyDistributionFee.setVisibility(View.VISIBLE);
            if (fullFree <= mTotalPrice){
                mDistributionFee.setText("免配送费");
            }else {
                mDistributionFee.setText(free + "(满" + fullFree + "免配送费)");
            }
            mTotalPrice = mTotalPrice + free;
            totalPrice.setText("" + mTotalPrice);
        }
    }
    private void initData() {
        Date date = new Date();//取时间
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(calendar.DATE, 1);//把日期往后增加一天.整数往后推,负数往前移动
        date = calendar.getTime(); //这个时间就是日期往后推一天的结果
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy" + "年" + "MM" + "月" + "dd" + "日");
        dateString = formatter.format(date);
        tvChooseData.setText(dateString + "(明天)");
        if (money > 0) {
            mLinearBalance.setVisibility(View.VISIBLE);
            mBalance.setText(mMoney);
        }
        mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Surplus = DoubleCalculationUtil.add(mTotalPrice, shoppingBagPrice);
                if (isChecked) {
                    isCheckBox = true;
                    if (DoubleCalculationUtil.add(money, reducePrice) >= Surplus) {
                        mBalance.setText(DoubleCalculationUtil.sub(DoubleCalculationUtil.add(money, reducePrice), Surplus) + "");
                        totalPrice.setText("0.0");
                    } else {
                        mBalance.setText("0.0");
                        totalPrice.setText("" + DoubleCalculationUtil.sub(Surplus, DoubleCalculationUtil.add(money, reducePrice)));
                    }
                } else {
                    isCheckBox = false;
                    mBalance.setText(money + "");
                    totalPrice.setText(DoubleCalculationUtil.sub(DoubleCalculationUtil.add(mTotalPrice, shoppingBagPrice), reducePrice) + "");
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_add_information:
                if (mChooseMode){
                    if (dialog == null)
                        dialog = new LogOutDialog(NowBuyActivity.this, storePhone, "取消", "呼叫", new LogOutDialog.OnSureBtnClickListener() {
                            @Override
                            public void sure() {
                                dialog.dismiss();
                                Intent intent2 = new Intent();
                                intent2.setAction(Intent.ACTION_CALL);
                                intent2.setData(Uri.parse("tel:" + storePhone));
                                startActivity(intent2);
                            }
                        });
                    dialog.show();
                }else {
                    Bundle bundle = new Bundle();
                    bundle.putInt("type",1);
                   MyApplication.openActivityForResult(NowBuyActivity.this,MyAddressListActivity.class,bundle,1002);
                }
                break;
            case R.id.text_now_buy_sure:
                Submit(mChooseMode);
                break;
            case R.id.text_choose_time:
                if (mChooseMode){
                    GetGoodsTimePopupWindow mGetGoodsTimePopupWindow = new GetGoodsTimePopupWindow(context, mLinearChooseTime,storeId);
                    mGetGoodsTimePopupWindow.setSelectedTextListener(new GetGoodsTimePopupWindow.SelectedTextListener() {
                        @Override
                        public void sure(String name) {
                            tvChooseTime.setText(name);
                            getTime = name;
                        }
                    });
                }else {
                    SendGoodsTimePopupWindow mGetGoodsTimePopupWindow = new SendGoodsTimePopupWindow(context, mLinearChooseTime,storeId);
                    mGetGoodsTimePopupWindow.setSelectedTextListener(new SendGoodsTimePopupWindow.SelectedTextListener() {
                        @Override
                        public void sure(String name) {
                            tvChooseTime.setText(name);
                            sendTime = name;
                        }
                    });
                }
                break;
            case R.id.text_choose_shopping_bag:
                MyApplication.openActivityForResult(NowBuyActivity.this, ShoppingBagActivity.class, 1002);
                break;
            case R.id.text_choose_coupon:
                Bundle bundle = new Bundle();
                bundle.putDouble("mTotalPrice", mTotalPrice);
                bundle.putInt("flag", 0);
                MyApplication.openActivityForResult(NowBuyActivity.this, MyCouponActivity.class, bundle, 1002);
                break;
            case R.id.linear_get_goods:
                mChooseMode = true;
                showType(true);
                break;
            case R.id.linear_send_goods:
                if (issend.equals("0")){
                    mChooseMode = false;
                    showType(false);
                }else {
                    ToastUtils.makeText(context,"该门店暂不支持送货上门服务");
                }
                break;
        }

    }

    private void Submit(boolean isChoseMode) {
        String takeGoodMessage = tvMessage.getText().toString().trim();
        totalMoney = DoubleCalculationUtil.sub(mMoney, mBalance.getText().toString().trim());
        String oderPayPrice = totalPrice.getText().toString().trim();
        String mTime = tvChooseTime.getText().toString().trim();
        String mPickUpGoodsTime = dateString + mTime;
        if (isChoseMode){
            if (TextUtils.isEmpty(getTime)) {
                ToastUtils.makeText(NowBuyActivity.this, "请预约取货时间");
            } else {
                storeEndTime = SPUtil.getInt(context, "storeEndTime",0);
                Calendar calendar = Calendar.getInstance();
                currentTime = calendar.get(Calendar.HOUR_OF_DAY);
                if (currentTime > storeEndTime){
                    ToastUtils.makeText(context,"现在下单已经来不及了，明天再下单吧！");
                    return;
                }
                getSubmitOrder(0,mPickUpGoodsTime, takeGoodMessage, totalMoney, oderPayPrice);
            }
        }else {
            if (TextUtils.isEmpty(mStoreAddress.getText().toString().trim())){
                ToastUtils.makeText(context,"送货地址不能为空");
                return;
            }
            if (TextUtils.isEmpty(sendTime)) {
                ToastUtils.makeText(NowBuyActivity.this, "请预约收货时间");
            } else {
                storeEndTime = SPUtil.getInt(context, "storeEndTime",0);
                Calendar calendar = Calendar.getInstance();
                currentTime = calendar.get(Calendar.HOUR_OF_DAY);
                if (currentTime > storeEndTime){
                    ToastUtils.makeText(context,"现在下单已经来不及了，明天再下单吧！");
                    return;
                }
                getSubmitOrder(1,mPickUpGoodsTime, takeGoodMessage, totalMoney, oderPayPrice);
            }
        }
    }

    private void getSubmitOrder(int type ,String time, String takeGoodMessage, final double totalMoney, final String oderPayPrice) {
        Map<String, String> params = new HashMap<>();
        final String json = "{\"cmd\":\"commitCommodity\",\"orderId\":\"" + orderId + "\",\"type\":\""+type+"\",\"sendMoney\":\""+free+"\",\"takeGoodDate\":\""
                + time + "\",\"takeGoodMessage\":\"" + takeGoodMessage + "\",\"oderTotalPrice\":\"" + (mTotalPrice+shoppingBagPrice) + "\"" +
                ",\"totalMoney\":\"" + totalMoney + "\",\"securitiesid\":\"" + couponId + "\",\"oderPayPrice\":\"" + oderPayPrice + "\"" +
                ",\"getGoodsStore\":\"" + storeName + "\",\"getGoodsAddress\":\"" + storeAddress + "\"," +
                "\"getGoodsPhone\":\"" + storePhone + "\",\"shoppingBag\":\"" + shoppingBag + "\",\"getGoodsId\":\"" + storeId + "\",\"addressId\":\""+addressId+"\"" +
                ",\"address\":\""+address+"\",\"userName\":\""+userName+"\",\"phone\":\""+phone+"\"}";
        params.put("json", json);
        Log.i("NowBuyActivity", "onResponse: " + json);
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
                Log.i("NowBuyActivity", "onResponse: " + response);
                Gson gson = new Gson();
                dialog1.dismiss();
                UserInfo baseBean = gson.fromJson(response, UserInfo.class);
                if (baseBean.getResult().equals("1")) {
                    ToastUtils.makeText(context, baseBean.getResultNote());
                    return;
                }
                if (Double.parseDouble(oderPayPrice) == 0) {
                    if (isCheckBox) {//如果使用余额，通知我的页面
                        Intent intent = new Intent();
                        intent.setAction("com.freshmall.mine.changed");
                        getApplicationContext().sendBroadcast(intent);
                    }
                    Intent intent = new Intent();
                    intent.setAction("com.freshmall.code.changed");
                    getApplicationContext().sendBroadcast(intent);
                    ToastUtils.makeText(NowBuyActivity.this, "商品购买成功！");
                    finish();
                } else {
                    ToastUtils.makeText(NowBuyActivity.this, "订单提交成功！");
                    popuWindowShow mPopuWindowShow = new popuWindowShow(NowBuyActivity.this, Double.parseDouble(totalPrice.getText().toString().trim()));
                    mPopuWindowShow.showAtLocation(mSure, Gravity.BOTTOM, 0, 0);
                }
            }
        });
    }

    public class popuWindowShow extends PopupWindow {
        private View view;
        private TextView mTotalMoney;
        private ImageView mClose;
        private RadioGroup radio_group;
        private String type = "3";

        public popuWindowShow(final Activity context, final Double mtotalMoney) {
            super(context);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.order_dec_popupwindow, null);
            mTvOk = view.findViewById(R.id.text_order_ok_pay);
            mTotalMoney = view.findViewById(R.id.tv_shop_total_money);
            mTotalMoney.setText("￥" + mtotalMoney);
            radio_group = view.findViewById(R.id.radio_group);
            mClose = view.findViewById(R.id.text_order_dec_close);
            mTvOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (type.equals("3")) {
                        ToastUtils.makeText(context, "请选择支付方式");
                        return;
                    }
                    charge(mtotalMoney, "溜达兔", channel, orderId);
                }
            });
            radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    if (R.id.rabtn_check_weixin == checkedId) {
                        channel = "wx";
                        type = "1";
                    } else if (R.id.rabtn_check_zhifubao == checkedId) {
                        channel = "alipay";
                        type = "2";
                    }
                }
            });

            this.setContentView(view);
            this.setWidth(ViewPager.LayoutParams.MATCH_PARENT);
            this.setHeight(ViewPager.LayoutParams.WRAP_CONTENT);
            this.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
            this.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            this.setOutsideTouchable(true);
            this.setFocusable(true);
            this.setBackgroundDrawable(new BitmapDrawable());
            setBackgroundAlpha(0.5f);
            this.setOnDismissListener(new poponDismissListener());
            this.setAnimationStyle(R.style.AnimBottom);
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    int height = view.findViewById(R.id.pop_pay_dec_layout)
                            .getTop();
                    int y = (int) event.getY();
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        if (y < height) {
                            dismiss();
                        }
                    }
                    return true;
                }
            });
            mClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
        }
    }

    class poponDismissListener implements PopupWindow.OnDismissListener {

        @Override
        public void onDismiss() {
            setBackgroundAlpha(1f);
        }
    }

    public void setBackgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = ((Activity) context).getWindow()
                .getAttributes();
        lp.alpha = bgAlpha;
        ((Activity) context).getWindow().setAttributes(lp);
    }

    //获取charge 用于第三方支付
    private void charge(Double mtotalMoney, String body, final String channel, String orderId) {
        int amount = (int) (mtotalMoney * 100);
        Map<String, String> params = new HashMap<>();
        final String json = "{\"cmd\":\"getChargeCommodity\",\"amount\":\"" + amount + "\",\"orderId\":\""
                + orderId + "\",\"channel\":\"" + channel + "\",\"body\":\"" + body + "\"}";
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
                dialog1.dismiss();
                String result = "1";//返回码
                String resultNote = "";//错误信息
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.has("result") && !object.isNull("result")) {
                        result = object.getString("result");
                    }

                    if (object.has("resultNote") && !object.isNull("resultNote")) {
                        resultNote = object.getString("resultNote");
                    }

                    if (object.has("charge") && !object.isNull("charge")) {
                        charge = object.getString("charge");
                    }
                    Log.i("charge", "onResponse: " + charge);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (result.equals("0")) {
                    new PaymentTask().execute(new PaymentRequest(channel, 1));
                } else {
                    Toast.makeText(NowBuyActivity.this, resultNote, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    class PaymentRequest {
        String channel;
        double amount;

        public PaymentRequest(String channel, double amount) {
            this.channel = channel;
            this.amount = amount;
        }
    }

    class PaymentTask extends AsyncTask<PaymentRequest, Void, String> {
        @Override
        protected void onPreExecute() {
            //按键点击之后的禁用，防止重复点击
            mTvOk.setOnClickListener(null);
        }

        @Override
        protected String doInBackground(PaymentRequest... pr) {
            String data = null;
            data = charge;
            return data;
        }

        //获得服务端的charge，调用ping++ sdk。
        @Override
        protected void onPostExecute(String data) {
            if (null == data) {
                return;
            }
            Pingpp.createPayment(NowBuyActivity.this, data);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1002) {
            if (resultCode == 2001) {
                Bundle bundle = data.getExtras();
                allPrice = bundle.getDouble("allPrice");
                reducePrice = bundle.getDouble("reducePrice");
                couponId = bundle.getString("couponId");
                tvCoupon.setText("满" + allPrice + "元减" + reducePrice);
                if (isCheckBox) {
                    if (DoubleCalculationUtil.add(money, reducePrice) > DoubleCalculationUtil.add(mTotalPrice, shoppingBagPrice)) {
                        temp = DoubleCalculationUtil.sub(DoubleCalculationUtil.add(money, reducePrice), DoubleCalculationUtil.add(mTotalPrice, shoppingBagPrice));
                        totalPrice.setText("0.0");
                        mBalance.setText(temp + "");
                    } else {
                        temp = DoubleCalculationUtil.sub(DoubleCalculationUtil.add(mTotalPrice, shoppingBagPrice), DoubleCalculationUtil.add(money, reducePrice));
                        totalPrice.setText(temp + "");
                        mBalance.setText("0.0");
                    }
                } else {
                    temp = DoubleCalculationUtil.sub(DoubleCalculationUtil.add(mTotalPrice, shoppingBagPrice), reducePrice);
                    totalPrice.setText(temp + "");
                    mBalance.setText(money + "");
                }
            } else if (resultCode == 2002) {
                shoppingBag = data.getStringExtra("shoppingBag");
                String shoppingBagMoney = data.getStringExtra("shoppingBagPrice");
                shoppingBagPrice = Double.parseDouble(shoppingBagMoney);
                tvShoppingBag.setText(shoppingBag + "(￥" + shoppingBagMoney + ")");
                if (isCheckBox) {
                    if (DoubleCalculationUtil.add(money, reducePrice) > DoubleCalculationUtil.add(mTotalPrice, shoppingBagPrice)) {
                        temp = DoubleCalculationUtil.sub(DoubleCalculationUtil.add(money, reducePrice), DoubleCalculationUtil.add(mTotalPrice, shoppingBagPrice));
                        totalPrice.setText("0.0");
                        mBalance.setText(temp + "");
                    } else {
                        temp = DoubleCalculationUtil.sub(DoubleCalculationUtil.add(mTotalPrice, shoppingBagPrice), DoubleCalculationUtil.add(money, reducePrice));
                        totalPrice.setText(temp + "");
                        mBalance.setText("0.0");
                    }
                } else {
                    temp = DoubleCalculationUtil.sub(DoubleCalculationUtil.add(mTotalPrice, shoppingBagPrice), reducePrice);
                    totalPrice.setText(temp + "");
                    mBalance.setText(money + "");
                }
            }else if (resultCode == 1003){
                addressId = data.getStringExtra("addressId");
                address = data.getStringExtra("address");
                userName = data.getStringExtra("userName");
                phone = data.getStringExtra("phone");
                mStoreName.setText(userName);
                mStorePhone.setText(phone);
                mStoreAddress.setText(address);
            }
        }

        if (requestCode == Pingpp.REQUEST_CODE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getExtras().getString("pay_result");
                String errorMsg = data.getExtras().getString("error_msg"); // 错误信息
                String extraMsg = data.getExtras().getString("extra_msg"); // 错误信息
                switch (result) {
                    case "success":
                        //如果使用余额，通知我的页面
                        if (isCheckBox) {
                            Intent intent = new Intent();
                            intent.setAction("com.freshmall.mine.changed");
                            getApplicationContext().sendBroadcast(intent);
                        }
                        Intent intent = new Intent();
                        intent.setAction("com.freshmall.code.changed");
                        getApplicationContext().sendBroadcast(intent);
                        ToastUtils.makeText(this, "支付成功");
                        finish();
                        break;
                    case "fail":
                        ToastUtils.makeText(this, "支付失败");
                        break;
                    case "cancel":
                        ToastUtils.makeText(this, "取消支付");
                        break;
                    case "invalid":
                        ToastUtils.makeText(this, "支付插件未安装");
                        break;
                    default:
                        break;
                }
            }
        }
    }
}
