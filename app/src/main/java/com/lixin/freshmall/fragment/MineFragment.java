package com.lixin.freshmall.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lixin.freshmall.R;
import com.lixin.freshmall.activity.InvitingUsersActivity;
import com.lixin.freshmall.activity.LoginActivity;
import com.lixin.freshmall.activity.MyApplication;
import com.lixin.freshmall.activity.MyBalanceActivity;
import com.lixin.freshmall.activity.MyCollectionActivity;
import com.lixin.freshmall.activity.MyCouponActivity;
import com.lixin.freshmall.activity.MyEvaluateActivity;
import com.lixin.freshmall.activity.MyIntegralActivity;
import com.lixin.freshmall.activity.MyLifeActivity;
import com.lixin.freshmall.activity.MyMassageActivity;
import com.lixin.freshmall.activity.MyOrderActivity;
import com.lixin.freshmall.activity.MyPersonalInformationActivity;
import com.lixin.freshmall.activity.MyRedPackagesActivity;
import com.lixin.freshmall.activity.MySettingActivity;
import com.lixin.freshmall.dialog.LogOutDialog;
import com.lixin.freshmall.model.Constant;
import com.lixin.freshmall.model.UserInfo;
import com.lixin.freshmall.okhttp.OkHttpUtils;
import com.lixin.freshmall.okhttp.budiler.StringCallback;
import com.lixin.freshmall.uitls.ImageManagerUtils;
import com.lixin.freshmall.uitls.SPUtil;
import com.lixin.freshmall.uitls.ToastUtils;
import com.lixin.freshmall.view.RoundedImageView;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by 小火
 * Create time on  2017/11/30
 * My mailbox is 1403241630@qq.com
 */

public class MineFragment extends BaseFragment implements View.OnClickListener {
    private View view;
    private RoundedImageView mUserIcon;
    private TextView mUserName, tvBalance, tvIntegral, tvCouponNum;
    private String uid, userIcon, userName,userSex,invitation,telephone,aboutUs,userAgreement,mBalance;
    private LogOutDialog mLogOutDialog;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        //注册广播
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.freshmall.mine.changed");
        getActivity().registerReceiver(mAllBroad, intentFilter);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_mine, null);
        uid = SPUtil.getString(context, "uid");
        initView();
        getdata();
        return view;

    }

    private void initView() {
        mUserIcon = view.findViewById(R.id.mine_user_icon);
        mUserName = view.findViewById(R.id.head_text);
        if (TextUtils.isEmpty(uid)) {
            mUserName.setText("登录/注册");
        }
        view.findViewById(R.id.mine_user_icon).setOnClickListener(this);
        tvBalance = view.findViewById(R.id.tv_mine_balance);
        tvIntegral = view.findViewById(R.id.tv_mine_integral);
        tvCouponNum = view.findViewById(R.id.tv_mine_coupon_num);
        view.findViewById(R.id.iv_mine_massage).setOnClickListener(this);
        view.findViewById(R.id.linear_mine_balance).setOnClickListener(this);
        view.findViewById(R.id.linear_mine_integral).setOnClickListener(this);
        view.findViewById(R.id.linear_mine_coupon).setOnClickListener(this);
        view.findViewById(R.id.text_mine_wait_pay).setOnClickListener(this);
        view.findViewById(R.id.text_main_wait_goods).setOnClickListener(this);
        view.findViewById(R.id.text_main_send_goods).setOnClickListener(this);
        view.findViewById(R.id.text_mine_completed).setOnClickListener(this);
        view.findViewById(R.id.text_mine_evaluate).setOnClickListener(this);
        view.findViewById(R.id.text_mine_refund).setOnClickListener(this);
        view.findViewById(R.id.linear_mine_life).setOnClickListener(this);
        view.findViewById(R.id.linear_mine_red_packages).setOnClickListener(this);
        view.findViewById(R.id.linear_mine_collection).setOnClickListener(this);
        view.findViewById(R.id.linear_mine_share).setOnClickListener(this);
        view.findViewById(R.id.linear_mine_customer_service).setOnClickListener(this);
        view.findViewById(R.id.linear_mine_setting).setOnClickListener(this);

    }
    @Override
    public void onResume() {
        super.onResume();
        userIcon = SPUtil.getString(getActivity(), "userIcon");
        userName = SPUtil.getString(getActivity(), "nickName");
        uid = SPUtil.getString(getActivity(), "uid");
        if (!TextUtils.isEmpty(userIcon)) {
            ImageManagerUtils.imageLoader.displayImage(userIcon, mUserIcon, ImageManagerUtils.options3);
        }
        if (!TextUtils.isEmpty(userName)) {
            mUserName.setText(userName);
        } else {
            if (TextUtils.isEmpty(uid)) {
                mUserName.setText("登录/注册");
            } else {
                mUserName.setText("昵称");
            }
        }
    }
    private void getdata() {
        Map<String, String> params = new HashMap<>();
        String json = "{\"cmd\":\"getUserDeatilInfo\",\"uid\":\"" + uid + "\"}";
        params.put("json", json);
        Log.i("getUserDeatilInfo", "json: " + json);
        dialog.show();
        OkHttpUtils.post().url(Constant.THE_SERVER_URL).params(params).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                dialog.dismiss();
                ToastUtils.makeText(context, e.getMessage());
            }
            @Override
            public void onResponse(String response, int id) {
                Log.i("getUserDeatilInfo", "response: " + response);
                Gson gson = new Gson();
                dialog.dismiss();
                UserInfo mUserInfo = gson.fromJson(response, UserInfo.class);
                if (mUserInfo.getResult().equals("1")) {
                    ToastUtils.makeText(context, mUserInfo.getResultNote());
                    return;
                }
                userIcon = mUserInfo.getUserIcon();
                userName = mUserInfo.getUserName();
                userSex = mUserInfo.getUserSex();
                invitation = mUserInfo.getInvitation();
                telephone = mUserInfo.getCustomerService();
                aboutUs = mUserInfo.getAboutUs();
                userAgreement = mUserInfo.getUserAgreement();
                mBalance = mUserInfo.getBalance();
                if (TextUtils.isEmpty(mBalance)){
                    tvBalance.setText("￥0.00元");
                }else {
                    tvBalance.setText("￥" + mBalance + "元");
                }
                if (TextUtils.isEmpty(mUserInfo.getIntegral())){
                    tvIntegral.setText("0分");
                }else {
                    tvIntegral.setText(mUserInfo.getIntegral() + "分");
                }
                if (TextUtils.isEmpty(mUserInfo.getCouponNum())){
                    tvCouponNum.setText("0张");
                }else {
                    tvCouponNum.setText(mUserInfo.getCouponNum() + "张");
                }
                if (TextUtils.isEmpty(userIcon))
                    mUserIcon.setImageResource(R.drawable.my_head_portrait);
                else
                    ImageManagerUtils.imageLoader.displayImage(userIcon, mUserIcon, ImageManagerUtils.options3);
                if (TextUtils.isEmpty(userName)){
                    if (TextUtils.isEmpty(uid)) {
                        mUserName.setText("登录/注册");
                    } else {
                        mUserName.setText("昵称");
                    }
                } else mUserName.setText(userName);
                SPUtil.putString(getActivity(), "userIcon", userIcon);
                SPUtil.putString(getActivity(), "nickName", userName);
                SPUtil.putString(getActivity(), "userSex", userSex);
                if (TextUtils.isEmpty(mUserInfo.getUserSex())){
                    return;
                }
                if (mUserInfo.getUserSex().equals("0")) {
                    SPUtil.putString(getActivity(), "sex", "男");
                } else if (mUserInfo.getUserSex().equals("1")) {
                    SPUtil.putString(getActivity(), "sex", "女");
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mine_user_icon:
                if (TextUtils.isEmpty(uid)) {
                    MyApplication.openActivity(getActivity(), LoginActivity.class);
                } else {
                    MyApplication.openActivityForResult(getActivity(), MyPersonalInformationActivity.class,1027);
                }
                break;
            case R.id.iv_mine_massage:
                if (TextUtils.isEmpty(uid)) {
                    MyApplication.openActivity(getActivity(), LoginActivity.class);
                } else {
                    MyApplication.openActivity(getActivity(), MyMassageActivity.class);
                }
                break;
            case R.id.linear_mine_balance:
                if (TextUtils.isEmpty(uid)) {
                    MyApplication.openActivity(getActivity(), LoginActivity.class);
                } else {
                    Bundle bundleBalance = new Bundle();
                    bundleBalance.putString("mBalance",mBalance);
                    MyApplication.openActivity(getActivity(), MyBalanceActivity.class,bundleBalance);
                }
                break;
            case R.id.linear_mine_integral:
                if (TextUtils.isEmpty(uid)) {
                    MyApplication.openActivity(getActivity(), LoginActivity.class);
                } else {
                    MyApplication.openActivity(getActivity(), MyIntegralActivity.class);
                }
                break;
            case R.id.linear_mine_coupon:
                if (TextUtils.isEmpty(uid)) {
                    MyApplication.openActivity(getActivity(), LoginActivity.class);
                } else {
                    MyApplication.openActivity(getActivity(), MyCouponActivity.class);
                }
                break;
            case R.id.text_mine_wait_pay:
                if (TextUtils.isEmpty(uid)) {
                    MyApplication.openActivity(getActivity(), LoginActivity.class);
                } else {
                    Bundle payBundle = new Bundle();
                    payBundle.putString("currentItem", "0");
                    MyApplication.openActivity(getActivity(), MyOrderActivity.class, payBundle);
                }
                break;
            case R.id.text_main_wait_goods:
                if (TextUtils.isEmpty(uid)) {
                    MyApplication.openActivity(getActivity(), LoginActivity.class);
                } else {
                    Bundle goodsBundle = new Bundle();
                    goodsBundle.putString("currentItem", "1");
                    MyApplication.openActivity(getActivity(), MyOrderActivity.class, goodsBundle);
                }
                break;
            case R.id.text_main_send_goods:
                if (TextUtils.isEmpty(uid)) {
                    MyApplication.openActivity(getActivity(), LoginActivity.class);
                } else {
                    Bundle goodsBundle = new Bundle();
                    goodsBundle.putString("currentItem", "2");
                    MyApplication.openActivity(getActivity(), MyOrderActivity.class, goodsBundle);
                }
                break;
            case R.id.text_mine_completed:
                if (TextUtils.isEmpty(uid)) {
                    MyApplication.openActivity(getActivity(), LoginActivity.class);
                } else {
                    Bundle completedBundle = new Bundle();
                    completedBundle.putString("currentItem", "3");
                    MyApplication.openActivity(getActivity(), MyOrderActivity.class, completedBundle);
                }
                break;
            case R.id.text_mine_evaluate:
                if (TextUtils.isEmpty(uid)) {
                    MyApplication.openActivity(getActivity(), LoginActivity.class);
                } else {
                    MyApplication.openActivity(getActivity(), MyEvaluateActivity.class);
                }
                break;
            case R.id.text_mine_refund:
                if (TextUtils.isEmpty(uid)) {
                    MyApplication.openActivity(getActivity(), LoginActivity.class);
                } else {
                    Bundle refundBundle = new Bundle();
                    refundBundle.putString("currentItem", "4");
                    MyApplication.openActivity(getActivity(), MyOrderActivity.class, refundBundle);
                }
                break;
            case R.id.linear_mine_life:
                MyApplication.openActivity(getActivity(), MyLifeActivity.class);
                break;
            case R.id.linear_mine_red_packages:
                if (TextUtils.isEmpty(uid)) {
                    MyApplication.openActivity(getActivity(), LoginActivity.class);
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putString("invitation",invitation);
                    MyApplication.openActivity(getActivity(), MyRedPackagesActivity.class,bundle);
                }
                break;
            case R.id.linear_mine_collection:
                if (TextUtils.isEmpty(uid)) {
                    MyApplication.openActivity(getActivity(), LoginActivity.class);
                } else {
                    MyApplication.openActivity(getActivity(), MyCollectionActivity.class);
                }
                break;
            case R.id.linear_mine_share:
                if (TextUtils.isEmpty(SPUtil.getString(context,"uid"))) {
                    MyApplication.openActivity(getActivity(), LoginActivity.class);
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putString("invitation",invitation);
                    MyApplication.openActivity(context, InvitingUsersActivity.class,bundle);
                }
                break;
            case R.id.linear_mine_customer_service:
                if (mLogOutDialog == null)
                    mLogOutDialog = new LogOutDialog(getActivity(), telephone,"取消","呼叫",new LogOutDialog.OnSureBtnClickListener() {
                        @Override
                        public void sure() {
                            mLogOutDialog.dismiss();
                            Intent intent2 = new Intent();
                            intent2.setAction(Intent.ACTION_CALL);
                            intent2.setData(Uri.parse("tel:" + telephone));
                            startActivity(intent2);
                        }
                    });
                mLogOutDialog.show();
                break;
            case R.id.linear_mine_setting:
                Bundle bundle = new Bundle();
                bundle.putString("aboutUs",aboutUs);
                bundle.putString("userAgreement",userAgreement);
                MyApplication.openActivity(getActivity(), MySettingActivity.class,bundle);
                break;
        }
    }

    private BroadcastReceiver mAllBroad = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {
            //接到广播通知后刷新数据源
            uid = SPUtil.getString(context,"uid");
            getdata();
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(mAllBroad);
    }
}
