package com.lixin.freshmall.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Window;
import android.view.WindowManager;

import com.lixin.freshmall.R;
import com.lixin.freshmall.model.Constant;
import com.lixin.freshmall.uitls.SPUtil;

/**
 * Created by 小火
 * Create time on  2017/12/9
 * My mailbox is 1403241630@qq.com
 */

public class StartActivity extends Activity {
    private boolean isFirst;
    private Context context;
    private static final int SHOW_TIME_MIN = 1000;// 最小显示时间
    private long mStartTime;// 开始时间
    private boolean IsFirst;//第一次进入应用
    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1:
                    long loadingTime = System.currentTimeMillis() - mStartTime;// 计算一下总共花费的时间
                    if (loadingTime < SHOW_TIME_MIN) {
                        // 如果比最小显示时间还短，就延时进入MainActivity，否则直接进入
                        mHandler.postDelayed(goToMainActivity, SHOW_TIME_MIN
                                - loadingTime);
                    } else {
                        mHandler.post(goToMainActivity);
                    }
                    break;
                default:
                    break;
            }
        }
    };

    //进入下一个Activity
    Runnable goToMainActivity = new Runnable() {
        @Override
        public void run() {
            IsFirst = SPUtil.getBoolean(context, Constant.FIRST_COME, true);
            if (IsFirst) {
                startActivity(new Intent(StartActivity.this,
                        SelectProvinceActivity.class));
                SPUtil.putBoolean(context, Constant.FIRST_COME, false);
                finish();
            } else {
                if (!TextUtils.isEmpty(SPUtil.getString(context,"storeName"))){
                    MyApplication.openActivity(context,MainActivity.class);
                    finish();
                }else {
                    startActivity(new Intent(StartActivity.this,
                            SelectProvinceActivity.class));
                    finish();
                }
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_start);
        context = this;
        mStartTime = System.currentTimeMillis();//记录开始时间1
        mHandler.sendEmptyMessage(1);
    }

    private void saveTag() {
        SPUtil.putBoolean(context, Constant.FIRST_COME, false);
    }

    @Override
    public void finish() {
        super.finish();
        saveTag();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mHandler.removeCallbacks(goToMainActivity);//移除回调
    }
}

