package com.lixin.freshmall.activity;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lixin.freshmall.R;
import com.lixin.freshmall.fragment.CartFragment;
import com.lixin.freshmall.fragment.ClassifyFragment;
import com.lixin.freshmall.fragment.CodeFragment;
import com.lixin.freshmall.fragment.HomeFragment;
import com.lixin.freshmall.fragment.MineFragment;
import com.lixin.freshmall.uitls.StatusBarUtil;

public class MainActivity extends BaseActivity implements HomeFragment.CallBackValue{
    private LinearLayout[] mLinearLayout;
    private LinearLayout view;
    private TextView[] mTextView;
    private Fragment[] mFragments;
    private Fragment currentFragment = new Fragment();
    private int current = 0;
    private Context context;
    // 定义一个变量，来标识是否退出
    private static boolean isExit = false;

    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isExit = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        StatusBarUtil.transparentStatusBar(MainActivity.this);
        initView();
        initFragment();
        refreshView();
    }

    private void initView() {
        view = findViewById(R.id.linear_main_layout_content);
        mLinearLayout = new LinearLayout[5];
        mLinearLayout[0] = findViewById(R.id.iv_main_home);
        mLinearLayout[1] = findViewById(R.id.iv_main_classify);
        mLinearLayout[2] = findViewById(R.id.iv_main_get_goods_code);
        mLinearLayout[3] = findViewById(R.id.iv_main_shopping_cart);
        mLinearLayout[4] = findViewById(R.id.iv_main_mine);
        mTextView = new TextView[5];
        mTextView[0] = findViewById(R.id.text_main_home);
        mTextView[1] = findViewById(R.id.text_main_classify);
        mTextView[2] = findViewById(R.id.text_main_get_goods_code);
        mTextView[3] = findViewById(R.id.text_main_shopping_cart);
        mTextView[4] = findViewById(R.id.text_main_mine);
    }

    private void initFragment() {
        mFragments = new Fragment[5];
        mFragments[0] = new HomeFragment();
        mFragments[1] = new ClassifyFragment();
        mFragments[2] = new CodeFragment();
        mFragments[3] = new CartFragment();
        mFragments[4] = new MineFragment();
        setCurrent(0);
    }
    private void refreshView() {
        for (int i = 0; i < mLinearLayout.length; i++) {
            mLinearLayout[i].setId(i);
            mLinearLayout[i].setOnClickListener(this);
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case 0:
                setCurrent(0);
                break;
            case 1:
                setCurrent(1);
                break;
            case 2:
                setCurrent(2);
                break;
            case 3:
                setCurrent(3);
                break;
            case 4:
                setCurrent(4);
                break;
            default:
                break;
        }
    }
    private void setCurrent(int position) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (!mFragments[position].isAdded()){
            transaction
                    .hide(currentFragment)
                    .add(R.id.activity_new_main_layout_content,mFragments[position]);
        }else {
            transaction
                    .hide(currentFragment)
                    .show(mFragments[position]);
        }
        currentFragment = mFragments[position];
        transaction.commit();

        mLinearLayout[position].setSelected(true);
        Resources resource = context.getResources();
        ColorStateList csl1 = resource.getColorStateList(R.color.black);
        ColorStateList csl2 = resource.getColorStateList(R.color.text_color_green);
        for (int i = 0; i < mLinearLayout.length; i++) {
            if (i != position) {
                mLinearLayout[i].setSelected(false);
                mTextView[i].setTextColor(csl1);
            }else {
                mTextView[i].setTextColor(csl2);
            }
        }
        current = position;
    }


    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void exit() {
        if (!isExit) {
            isExit = true;
            Toast.makeText(getApplicationContext(), "再按一次退出程序",
                    Toast.LENGTH_SHORT).show();
            mHandler.sendEmptyMessageDelayed(0, 3000);
        } else {
            finish();
            System.exit(0);
        }
    }
    @Override
    public void SendMessageValue(String strValue, int position) {
        if (strValue.equals("6")){
            mLinearLayout[1].setSelected(true);
            for (int i = 0; i < mLinearLayout.length; i++) {
                if (i!= 1) {
                    mLinearLayout[i].setSelected(false);
                }
            }
            MyApplication.temp = 1;
            MyApplication.secondId = position;
            setCurrent(1);
            current = 1;
        }
    }
}


