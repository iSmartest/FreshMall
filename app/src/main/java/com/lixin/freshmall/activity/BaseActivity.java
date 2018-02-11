package com.lixin.freshmall.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lixin.freshmall.R;
import com.lixin.freshmall.dialog.ProgressDialog;
import com.lixin.freshmall.uitls.AppManager;
import com.lixin.freshmall.uitls.StatusBarUtil;

/**
 * Created by 小火
 * Create time on  2017/5/15
 * My mailbox is 1403241630@qq.com
 */

public class BaseActivity extends FragmentActivity implements View.OnClickListener {
    protected Context context;
    protected Dialog dialog1;
    private ImageView Iv_base_back,ivSearch;
    private LinearLayout lay_bg;
    private TextView mCityLocation,mStoreName;
    private EditText editKey;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        context = this;
        dialog1 = ProgressDialog.createLoadingDialog(context, "加载中.....");
        StatusBarUtil.transparentStatusBar(BaseActivity.this);
        AppManager.addActivity(this);
    }
    public void hideBack(int position) {
        RelativeLayout top_iag = findViewById(R.id.top_iag);
        StatusBarUtil.setHeightAndPadding(context,top_iag);
        //左
        LinearLayout mLeft = findViewById(R.id.ly_base_left);
        Iv_base_back =  findViewById(R.id.Iv_base_back);
        LinearLayout mLocation = findViewById(R.id.ly_base_location);
        mCityLocation = findViewById(R.id.text_city_location);
        mStoreName = findViewById(R.id.text_store_name);
        mLocation.setOnClickListener(this);
        //中
        LinearLayout mCenter = findViewById(R.id.ly_base_center);
        LinearLayout mBaseSearch = findViewById(R.id.base_search);
        editKey = findViewById(R.id.a_key_edt_search);
        findViewById(R.id.im_search).setOnClickListener(this);

        TextView mTitleText = findViewById(R.id.tv_base_titleText);
        //右
        LinearLayout mRight = findViewById(R.id.ly_base_right);
        ImageView mMessage = findViewById(R.id.Iv_base_message);

        TextView mRightText = findViewById(R.id.tv_base_rightText);
        mRightText.setOnClickListener(this);
        Iv_base_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        switch (position){
            case 1://返回键、标题、右文字
                mLeft.setVisibility(View.VISIBLE);
                Iv_base_back.setVisibility(View.VISIBLE);
                mLocation.setVisibility(View.GONE);
                mCenter.setVisibility(View.VISIBLE);
                mTitleText.setVisibility(View.VISIBLE);
                mBaseSearch.setVisibility(View.GONE);
                mRight.setVisibility(View.VISIBLE);
                mMessage.setVisibility(View.GONE);
                mRightText.setVisibility(View.VISIBLE);
                break;
            case 2://返回键、标题
                mLeft.setVisibility(View.VISIBLE);
                Iv_base_back.setVisibility(View.VISIBLE);
                mLocation.setVisibility(View.GONE);
                mCenter.setVisibility(View.VISIBLE);
                mTitleText.setVisibility(View.VISIBLE);
                mBaseSearch.setVisibility(View.GONE);
                mRight.setVisibility(View.GONE);
                break;
            case 3://标题
                mLeft.setVisibility(View.GONE);
                mCenter.setVisibility(View.VISIBLE);
                mTitleText.setVisibility(View.VISIBLE);
                mBaseSearch.setVisibility(View.GONE);
                mRight.setVisibility(View.GONE);
                break;
            case 4://搜索
                mLeft.setVisibility(View.VISIBLE);
                Iv_base_back.setVisibility(View.VISIBLE);
                mLocation.setVisibility(View.GONE);
                mCenter.setVisibility(View.VISIBLE);
                mTitleText.setVisibility(View.GONE);
                mBaseSearch.setVisibility(View.VISIBLE);
                mRight.setVisibility(View.GONE);
                break;
            case 5://消息
                mLeft.setVisibility(View.GONE);
                mCenter.setVisibility(View.GONE);
                mRight.setVisibility(View.VISIBLE);
                mMessage.setVisibility(View.VISIBLE);
                mRightText.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void onClick(View v) {

    }

    public void setTitleText(String string) {
        TextView titleTv = findViewById(R.id.tv_base_titleText);
        Resources resource = context.getResources();
        ColorStateList csl1 = resource.getColorStateList(R.color.app_main_default);
        titleTv.setText(string);
        titleTv.setTextColor(csl1);
    }
    public void setRightText(String string) {
        TextView rightTv = findViewById(R.id.tv_base_rightText);
        Resources resource = context.getResources();
        ColorStateList csl1 = resource.getColorStateList(R.color.app_main_background);
        rightTv.setText(string);
        rightTv.setTextColor(csl1);
        rightTv.setOnClickListener(this);
    }
}
