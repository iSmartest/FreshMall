package com.lixin.freshmall.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.lixin.freshmall.beecloude.BCPay;
import com.lixin.freshmall.beecloude.BeeCloud;
import com.lixin.freshmall.beecloude.async.BCCallback;
import com.lixin.freshmall.beecloude.async.BCResult;
import com.lixin.freshmall.beecloude.entity.BCPayResult;
import com.lixin.freshmall.beecloude.entity.BCReqParams;
import com.lixin.freshmall.dialog.LogOutDialog;
import com.lixin.freshmall.dialog.ProgressDialog;
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
    private View headView, cursor_send, cursor_get;
    private CheckBox mCheckBox;
    private EditText tvMessage;
    private LogOutDialog dialog;
    private ListView now_buy_list;
    private NowBuyAdapter mAdapter;
    private boolean isCheckBox = false;
    private int storeEndTime, currentTime;
    private LinearLayout mLinearBalance, mLinearChooseTime, mLyDistributionFee;
    private ArrayList<? extends GenerateOrderBean.commoditys> mList;
    private TextView totalPrice, tvChooseTime, tvChooseData, tvShoppingBag, tvCoupon, mBalance, mSure, mTvOk;
    private Double mTotalPrice, mTempTotalPrice, money, allPrice, Surplus, totalMoney, reducePrice = 0.00, shoppingBagPrice = 0.00, temp = 0.00, free = 0.00, fullFree = 0.00;
    private String orderId, mMoney, storeName, storeAddress, storePhone, storeId, addressId, address, userName, phone, dateString, channel, issend, getTime = "", sendTime = "", couponId = "", shoppingBag = "";
    private TextView mStoreName, mStorePhone, mStoreAddress, mAddressTitle, mReservationData, mReservationTime, mGetGoods, mSendGoods, mDistributionFee;
    private boolean mChooseMode = true;
    private Dialog progressDlg;

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
        BeeCloud.setAppIdAndSecret("636883c7-e0ff-403a-b81d-8cf8d8cf6d88",
                "9d70a721-e0ff-4362-9bd7-a4010215c01c");
        String initInfo = BCPay.initWechatPay(context, "wxf2e7a3ba30d356a2");
        if (initInfo != null) {
            Toast.makeText(this, "微信初始化失败：" + initInfo, Toast.LENGTH_LONG).show();
        }
        initView();
        getdata();
        showType(true);
        initData();
    }

    private void getdata() {
        Map<String, String> params = new HashMap<>();
        final String json = "{\"cmd\":\"getStoreInfoTime\",\"storeId\":\"" + storeId + "\"}";
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
                if (mTotalPrice >= fullFree) {
                    free = 0.0;
                } else {
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

    private void showType(boolean isShow) {
        if (isShow) {
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
        } else {
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
            if (fullFree <= mTotalPrice) {
                mDistributionFee.setText("免配送费");
            } else {
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
                if (mChooseMode) {
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
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putInt("type", 1);
                    MyApplication.openActivityForResult(NowBuyActivity.this, MyAddressListActivity.class, bundle, 1002);
                }
                break;
            case R.id.text_now_buy_sure:
                Submit(mChooseMode);
                break;
            case R.id.text_choose_time:
                if (mChooseMode) {
                    GetGoodsTimePopupWindow mGetGoodsTimePopupWindow = new GetGoodsTimePopupWindow(context, mLinearChooseTime, storeId);
                    mGetGoodsTimePopupWindow.setSelectedTextListener(new GetGoodsTimePopupWindow.SelectedTextListener() {
                        @Override
                        public void sure(String name) {
                            tvChooseTime.setText(name);
                            getTime = name;
                        }
                    });
                } else {
                    SendGoodsTimePopupWindow mGetGoodsTimePopupWindow = new SendGoodsTimePopupWindow(context, mLinearChooseTime, storeId);
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
                if (issend.equals("0")) {
                    mChooseMode = false;
                    showType(false);
                } else {
                    ToastUtils.makeText(context, "该门店暂不支持送货上门服务");
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
        if (isChoseMode) {
            if (TextUtils.isEmpty(getTime)) {
                ToastUtils.makeText(NowBuyActivity.this, "请预约取货时间");
            } else {
                storeEndTime = SPUtil.getInt(context, "storeEndTime", 0);
                Calendar calendar = Calendar.getInstance();
                currentTime = calendar.get(Calendar.HOUR_OF_DAY);
                if (currentTime > storeEndTime) {
                    ToastUtils.makeText(context, "现在下单已经来不及了，明天再下单吧！");
                    return;
                }
                getSubmitOrder(0, mPickUpGoodsTime, takeGoodMessage, totalMoney, oderPayPrice);
            }
        } else {
            if (TextUtils.isEmpty(mStoreAddress.getText().toString().trim())) {
                ToastUtils.makeText(context, "送货地址不能为空");
                return;
            }
            if (TextUtils.isEmpty(sendTime)) {
                ToastUtils.makeText(NowBuyActivity.this, "请预约收货时间");
            } else {
                storeEndTime = SPUtil.getInt(context, "storeEndTime", 0);
                Calendar calendar = Calendar.getInstance();
                currentTime = calendar.get(Calendar.HOUR_OF_DAY);
                if (currentTime > storeEndTime) {
                    ToastUtils.makeText(context, "现在下单已经来不及了，明天再下单吧！");
                    return;
                }
                getSubmitOrder(1, mPickUpGoodsTime, takeGoodMessage, totalMoney, oderPayPrice);
            }
        }
    }

    private void getSubmitOrder(int type, String time, String takeGoodMessage, final double totalMoney, final String oderPayPrice) {
        Map<String, String> params = new HashMap<>();
        final String json = "{\"cmd\":\"commitCommodity\",\"orderId\":\"" + orderId + "\",\"type\":\"" + type + "\",\"sendMoney\":\"" + free + "\",\"takeGoodDate\":\""
                + time + "\",\"takeGoodMessage\":\"" + takeGoodMessage + "\",\"oderTotalPrice\":\"" + (mTotalPrice + shoppingBagPrice) + "\"" +
                ",\"totalMoney\":\"" + totalMoney + "\",\"securitiesid\":\"" + couponId + "\",\"oderPayPrice\":\"" + oderPayPrice + "\"" +
                ",\"getGoodsStore\":\"" + storeName + "\",\"getGoodsAddress\":\"" + storeAddress + "\"," +
                "\"getGoodsPhone\":\"" + storePhone + "\",\"shoppingBag\":\"" + shoppingBag + "\",\"getGoodsId\":\"" + storeId + "\",\"addressId\":\"" + addressId + "\"" +
                ",\"address\":\"" + address + "\",\"userName\":\"" + userName + "\",\"phone\":\"" + phone + "\"}";
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
                    ThreePayment(mtotalMoney, "溜达兔", type, orderId);
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

    private void ThreePayment(Double mtotalMoney, String boby, String type, String orderId) {
//        int amount = (int) (mtotalMoney * 100);
        int amount = 1;
        progressDlg = ProgressDialog.createLoadingDialog(context, "处理中，请稍后...");
        if (type.equals("1")) {
            progressDlg.show();
            Map<String, String> mapOptional = new HashMap<String, String>();
            mapOptional.put("testkey1", "测试value值1");
            if (BCPay.isWXAppInstalledAndSupported() && BCPay.isWXPaySupported()) {
                BCPay.PayParams payParams = new BCPay.PayParams();
                payParams.channelType = BCReqParams.BCChannelTypes.WX_APP;
                payParams.billTitle = boby;   //订单标题
                payParams.billTotalFee = amount;    //订单金额(分)
                payParams.billNum = orderId;  //订单流水号
//              payParams.couponId = "bbbf835d-f6b0-484f-bb6e-8e6082d4a35f";    // 优惠券ID
                payParams.optional = mapOptional;            //扩展参数(可以null)
                BCPay.getInstance(context).reqPaymentAsync(payParams, bcCallback);
            } else {
                ToastUtils.makeText(context, "您尚未安装微信或者安装的微信版本不支持");
                dialog1.dismiss();
            }
        } else {
            dialog1.show();
            Map<String, String> mapOptional = new HashMap<>();
            BCPay.PayParams aliParam = new BCPay.PayParams();
            aliParam.channelType = BCReqParams.BCChannelTypes.ALI_APP;
            aliParam.billTitle = boby;
            aliParam.billTotalFee = amount;
            aliParam.billNum = orderId;
            aliParam.optional = mapOptional;
            BCPay.getInstance(context).reqPaymentAsync(aliParam, bcCallback);
        }
    }

    BCCallback bcCallback = new BCCallback() {
        @Override
        public void done(final BCResult bcResult) {
            final BCPayResult bcPayResult = (BCPayResult) bcResult;
            dialog1.dismiss();
            String result = bcPayResult.getResult();
            Message msg = mHandler.obtainMessage();
            if (result.equals(BCPayResult.RESULT_SUCCESS)) {
                msg.what = 1;
            } else if (result.equals(BCPayResult.RESULT_CANCEL)) {
                msg.what = 2;
            } else if (result.equals(BCPayResult.RESULT_FAIL)) {
                Log.i("fail", "支付失败, 原因: " + bcPayResult.getErrCode() +
                        " # " + bcPayResult.getErrMsg() +
                        " # " + bcPayResult.getDetailInfo());
                if (bcPayResult.getErrMsg().equals("PAY_FACTOR_NOT_SET") && bcPayResult.getDetailInfo().startsWith("支付宝参数")) {
                    Log.i("fail", "支付失败, 原因: 由于支付宝政策原因，故不再提供支付宝支付的测试功能，给您带来的不便，敬请谅解");
                }
                msg.what = 3;
            } else if (result.equals(BCPayResult.RESULT_UNKNOWN)) {
                msg.what = 4;
            } else {
                msg.what = 5;
            }

            mHandler.sendMessage(msg);

        }
    };

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    if (isCheckBox) {
                        Intent intent = new Intent();
                        intent.setAction("com.freshmall.mine.changed");
                        getApplicationContext().sendBroadcast(intent);
                    }
                    Intent intent = new Intent();
                    intent.setAction("com.freshmall.code.changed");
                    getApplicationContext().sendBroadcast(intent);
//                  LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                    ToastUtils.makeText(context, "订单支付成功");
                    finish();
                    break;
                case 2:
                    ToastUtils.makeText(context, "用户取消支付");
                    break;
                case 3:
                    ToastUtils.makeText(context, "订单支付失败");
                    break;
                case 4:
                    ToastUtils.makeText(context, "订单状态未知");
                    break;
                case 5:
                    ToastUtils.makeText(context, "invalid return");
                    break;
            }
            return true;
        }
    });

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
            } else if (resultCode == 1003) {
                addressId = data.getStringExtra("addressId");
                address = data.getStringExtra("address");
                userName = data.getStringExtra("userName");
                phone = data.getStringExtra("phone");
                mStoreName.setText(userName);
                mStorePhone.setText(phone);
                mStoreAddress.setText(address);
            }
        }
    }
}
