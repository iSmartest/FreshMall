package com.lixin.freshmall.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import com.lixin.freshmall.R;
import com.lixin.freshmall.dialog.LogOutDialog;
import com.lixin.freshmall.uitls.DataCleanManager;
import com.lixin.freshmall.uitls.SPUtil;
import com.lixin.freshmall.uitls.ToastUtils;

/**
 * Created by 小火
 * Create time on  2017/10/30
 * My mailbox is 1403241630@qq.com
 */

public class MySettingActivity extends BaseActivity{
    private TextView mCacheSize,mLogOut;
    private String aboutUs,userAgreement;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        aboutUs = getIntent().getStringExtra("aboutUs");
        userAgreement = getIntent().getStringExtra("userAgreement");
        hideBack(2);
        setTitleText("设置");
        initView();
    }

    private void initView() {
        findViewById(R.id.linear_my_setting_about_us).setOnClickListener(this);
        findViewById(R.id.linear_my_setting_feedback).setOnClickListener(this);
        findViewById(R.id.linear_my_setting_agreement).setOnClickListener(this);
        findViewById(R.id.linear_my_setting_update).setOnClickListener(this);
        findViewById(R.id.linear_my_setting_clear_cache).setOnClickListener(this);
        mCacheSize = findViewById(R.id.text_my_setting_clear_cache_size);
        try {
            mCacheSize.setText(DataCleanManager.getTotalCacheSize(this));
        } catch (Exception e) {
            e.printStackTrace();
        }
        mLogOut = findViewById(R.id.text_my_setting_log_out);
        mLogOut.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.linear_my_setting_feedback:
                MyApplication.openActivity(context,SettingFeedbackActivity.class);
                break;
            case R.id.linear_my_setting_about_us:
                Bundle bundle = new Bundle();
                bundle.putString("about",aboutUs);
                bundle.putString("title","关于我们");
                MyApplication.openActivity(context,SettingAboutUsActivity.class,bundle);
                break;
            case R.id.linear_my_setting_clear_cache:
                new Thread(new clearCache()).start();
                break;
            case R.id.text_my_setting_log_out:
                LogOutDialog dialog = new LogOutDialog(MySettingActivity.this,R.string.log_out, new LogOutDialog.OnSureBtnClickListener() {
                    @Override
                    public void sure() {
                        SPUtil.putString(context,"uid","");//用户ID
                        SPUtil.putString(context,"phoneNum","");//手机号码
                        ToastUtils.makeText(context,"已安全退出账号");
                        MyApplication.openActivity(context,LoginActivity.class);
                        finish();
                    }
                });
                dialog.show();
                break;
            case R.id.linear_my_setting_agreement:
                Bundle bundle1 = new Bundle();
                bundle1.putString("about",userAgreement);
                bundle1.putString("title","用户协议");
                MyApplication.openActivity(context,SettingAboutUsActivity.class,bundle1);
                break;
            case R.id.linear_my_setting_update:
                MyApplication.openActivity(context,UpdateActivity.class);
                break;
            default:
                break;
        }
    }
    class clearCache implements Runnable {
        @Override
        public void run() {
            try {
                DataCleanManager.clearAllCache(MySettingActivity.this);
                Thread.sleep(3000);
                if (DataCleanManager.getTotalCacheSize(MySettingActivity.this).startsWith("0")) {
                    handler.sendEmptyMessage(0);
                }
            } catch (Exception e) {
                return;
            }
        }
    }
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    ToastUtils.makeText(MySettingActivity.this,"清理完成");
                    try {
                        mCacheSize.setText(DataCleanManager.getTotalCacheSize(MySettingActivity.this));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
            }
        }
    };
}
