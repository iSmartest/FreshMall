package com.lixin.freshmall.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xrecyclerview.XRecyclerView;
import com.google.gson.Gson;
import com.lixin.freshmall.R;
import com.lixin.freshmall.adapter.MyBalanceAdapter;
import com.lixin.freshmall.beecloude.BCPay;
import com.lixin.freshmall.beecloude.BeeCloud;
import com.lixin.freshmall.beecloude.async.BCCallback;
import com.lixin.freshmall.beecloude.async.BCResult;
import com.lixin.freshmall.beecloude.entity.BCPayResult;
import com.lixin.freshmall.beecloude.entity.BCReqParams;
import com.lixin.freshmall.dialog.ProgressDialog;
import com.lixin.freshmall.listenter.RecyclerItemTouchListener;
import com.lixin.freshmall.model.Constant;
import com.lixin.freshmall.model.MyWelletBean;
import com.lixin.freshmall.model.RechargeBean;
import com.lixin.freshmall.model.UserInfo;
import com.lixin.freshmall.okhttp.OkHttpUtils;
import com.lixin.freshmall.okhttp.budiler.StringCallback;
import com.lixin.freshmall.uitls.SPUtil;
import com.lixin.freshmall.uitls.ToastUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by 小火
 * Create time on  2017/11/30
 * My mailbox is 1403241630@qq.com
 */

public class MyBalanceActivity extends BaseActivity {
    private XRecyclerView recyclerView;
    private View headView, footView;
    private TextView mWalletNum,mRechargeProtocol,mSure;
    private CheckBox mCheck;
    private int nowPage = 1;
    private String mBalance,uid,channel,money = "",sendmoney = "",orderId;
    private MyBalanceAdapter mAdapter;
    private boolean isChoose = true;
    private List<RechargeBean.ChargeList> mList;
    private Dialog progressDlg;
    private popuWindowShow mPopuWindowShow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance);
        uid = SPUtil.getString(context, "uid");
        mBalance = getIntent().getStringExtra("mBalance");
        hideBack(1);
        setTitleText("我的钱包");
        setRightText("零钱明细");
        mList = new ArrayList<>();
        BeeCloud.setAppIdAndSecret("636883c7-e0ff-403a-b81d-8cf8d8cf6d88",
                "9d70a721-e0ff-4362-9bd7-a4010215c01c");
        String initInfo = BCPay.initWechatPay(context, "wxf2e7a3ba30d356a2");
        if (initInfo != null) {
            Toast.makeText(this, "微信初始化失败：" + initInfo, Toast.LENGTH_LONG).show();
        }
        initView();
        getRechargeList();
    }

    private void initView() {
        recyclerView = findViewById(R.id.recycler_balance);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        headView = LayoutInflater.from(context).inflate(R.layout.head_balance, null);
        footView = LayoutInflater.from(context).inflate(R.layout.foot_balance, null);
        mWalletNum = headView.findViewById(R.id.text_my_wallet_num);
        mWalletNum.setText(mBalance);
        headView.findViewById(R.id.iv_balance_recharge_amount).setOnClickListener(this);
        headView.findViewById(R.id.iv_balance_recharge_bus).setOnClickListener(this);
        headView.findViewById(R.id.iv_balance_recharge_life).setOnClickListener(this);
        mSure = footView.findViewById(R.id.text_now_recharge);
        mSure.setOnClickListener(this);
        mCheck = footView.findViewById(R.id.ck_agreement);
        mCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    isChoose = true;
                } else {
                    isChoose = false;
                }
            }
        });
        mRechargeProtocol = footView.findViewById(R.id.text_recharge_protocol);
        mRechargeProtocol.setText(Html.fromHtml(getResources().getString(R.string.agreement)));
        mRechargeProtocol.setOnClickListener(this);
        recyclerView.addHeaderView(headView);
        recyclerView.addFootView(footView, true);
        mAdapter = new MyBalanceAdapter(context, mList);
        mAdapter.setDefault(0);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setPullRefreshEnabled(false);
        recyclerView.addOnItemTouchListener(new RecyclerItemTouchListener(recyclerView) {
            @Override
            public void onItemClick(RecyclerView.ViewHolder vh) {
                int position = vh.getAdapterPosition()-2;
                if (position < 0 | position >= mList.size()) {
                    return;
                }
                mAdapter.setDefault(position);
                mAdapter.notifyDataSetChanged();
                money = mList.get(position).getMoney();
                sendmoney = mList.get(position).getSendmoney();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_base_rightText:
                MyApplication.openActivity(context, MoneyDecActivity.class);
                break;
            case R.id.iv_balance_recharge_amount:
                break;
            case R.id.iv_balance_recharge_bus:
                ToastUtils.makeText(context, "暂未开通，敬请期待");
                break;
            case R.id.iv_balance_recharge_life:
                ToastUtils.makeText(context, "暂未开通，敬请期待");
                break;
            case R.id.text_now_recharge:
                if (!isChoose) {
                    ToastUtils.makeText(context, "请阅读并同意《溜哒兔充值协议》");
                    return;
                }
                if (TextUtils.isEmpty(money)) {
                    ToastUtils.makeText(context, "请选择要充值的金额！");
                    return;
                }
                getSubmitOrder();
                break;
            case R.id.text_recharge_protocol:
                Bundle bundle1 = new Bundle();
                bundle1.putString("about",Constant.PROTOCOL);
                bundle1.putString("title","充值协议");
                MyApplication.openActivity(context,SettingAboutUsActivity.class,bundle1);
                break;
            default:
                break;
        }
    }

    private void getRechargeList() {
        Map<String, String> params = new HashMap<>();
        final String json = "{\"cmd\":\"rechargeList\"}";
        params.put("json", json);
        dialog1.show();
        OkHttpUtils.post().url(Constant.THE_SERVER_URL).params(params).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ToastUtils.makeText(context, e.getMessage());
                dialog1.dismiss();
            }
            @Override
            public void onResponse(String response, int id) {
                Gson gson = new Gson();
                dialog1.dismiss();
                RechargeBean rechargeBean = gson.fromJson(response, RechargeBean.class);
                if (rechargeBean.getResult().equals("1")) {
                    ToastUtils.makeText(context, rechargeBean.getResultNote());
                    return;
                }
                List<RechargeBean.ChargeList> chargeLists = rechargeBean.getChargeList();
                if (chargeLists != null && !chargeLists.isEmpty() && chargeLists.size() > 0) {
                    mList.addAll(chargeLists);
                    mAdapter.notifyDataSetChanged();
                    money = chargeLists.get(0).getMoney();
                    sendmoney = chargeLists.get(0).getSendmoney();
                }
            }
        });
    }

    private void getSubmitOrder() {//生成订单号
        Map<String, String> params = new HashMap<>();
        final String json = "{\"cmd\":\"rechargeOrdernum\",\"money\":\"" + money + "\",\"uid\":\""
                + uid + "\",\"sendmoney\":\"" + sendmoney + "\"}";
        params.put("json", json);
        dialog1.show();
        OkHttpUtils.post().url(Constant.THE_SERVER_URL).params(params).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ToastUtils.makeText(context, e.getMessage());
                dialog1.dismiss();
            }

            @Override
            public void onResponse(String response, int id) {
                dialog1.dismiss();
                Gson gson = new Gson();
                UserInfo userInfo = gson.fromJson(response, UserInfo.class);
                if (userInfo.getResult().equals("1")) {
                    ToastUtils.makeText(context, userInfo.getResultNote());
                    return;
                }
                orderId = userInfo.getOrderId();
                if (!TextUtils.isEmpty(orderId)) {
                    mPopuWindowShow = new popuWindowShow(MyBalanceActivity.this, Double.parseDouble(money));
                    mPopuWindowShow.showAtLocation(mSure, Gravity.BOTTOM, 0, 0);
                }
            }
        });
    }

    public class popuWindowShow extends PopupWindow {
        private View view;
        private TextView mTotalMoney, mTvOk;
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
                    ThreePayment(mtotalMoney, "溜哒兔", type, orderId);//支付
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
        int amount = (int) (mtotalMoney * 100);
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
                payParams.optional = mapOptional;            //扩展参数(可以null)
                BCPay.getInstance(context).reqPaymentAsync(payParams, bcCallback);
            } else {
                ToastUtils.makeText(context, "您尚未安装微信或者安装的微信版本不支持");
                progressDlg.dismiss();
            }
        } else {
            progressDlg.show();
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
            progressDlg.dismiss();
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
                    ToastUtils.makeText(context, "充值成功");
                    mPopuWindowShow.dismiss();
                    getMyWellet();
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

    private void getMyWellet() {
        Map<String, String> params = new HashMap<>();
        final String json = "{\"cmd\":\"getWalletInfo\",\"nowPage\":\"" + nowPage + "\",\"uid\":\""
                + uid + "\"}";
        params.put("json", json);
        OkHttpUtils.post().url(Constant.THE_SERVER_URL).params(params).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ToastUtils.makeText(context, e.getMessage());
            }
            @Override
            public void onResponse(String response, int id) {
                Gson gson = new Gson();
                MyWelletBean myWelletBean = gson.fromJson(response, MyWelletBean.class);
                if (myWelletBean.result.equals("1")) {
                    ToastUtils.makeText(context, myWelletBean.resultNote);
                    return;
                }
                mWalletNum.setText(myWelletBean.getTotalMoney());
                Intent intent = new Intent();
                intent.setAction("com.freshmall.mine.changed");
                getApplicationContext().sendBroadcast(intent);
            }
        });
    }
}
