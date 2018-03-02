package com.lixin.freshmall.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.TextView;

import com.lixin.freshmall.R;
import com.lixin.freshmall.adapter.MyPagerAdapter;
import com.lixin.freshmall.fragment.EvaluatedFragment;
import com.lixin.freshmall.fragment.WaitEvaluateFragment;
import com.lixin.freshmall.view.LazyViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 小火
 * Create time on  2017/12/4
 * My mailbox is 1403241630@qq.com
 */

public class MyEvaluateActivity extends BaseActivity {
    private int selectedColor, unSelectedColor;//是否选择显示的颜色
    private View[] mView;
    private TextView[] mTextView;
    private LazyViewPager viewPager;
    private List<Fragment> fragments;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluate);
        hideBack(2);
        setTitleText("我的评价");
        initView();
    }

    private void initView() {
        selectedColor = getResources().getColor(R.color.text_color_green);
        unSelectedColor = getResources().getColor(R.color.text_color_main);
        mTextView = new TextView[2];
        mView = new View[2];
        mView[0] = findViewById(R.id.evaluate_cursor0);
        mView[1] = findViewById(R.id.evaluate_cursor1);
        mTextView[0] = findViewById(R.id.tv_my_wait_evaluate);
        mTextView[1] = findViewById(R.id.tv_my_evaluate);
        viewPager = findViewById(R.id.evaluate_vPager);
        fragments = new ArrayList<>();
        fragments.add(new WaitEvaluateFragment());
        fragments.add(new EvaluatedFragment());
        viewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager(),
                fragments));
        viewPager.setCurrentItem(0);
        for (int i = 0; i < mTextView.length; i++) {
            mTextView[i].setId(i);
            mTextView[i].setOnClickListener(new MyOnClickListener(i));
        }
        viewPager.setOnPageChangeListener(new MyOnPageChangeListener());
        setSelect(0);
        }

    private class MyOnClickListener implements View.OnClickListener {
        private int index = 0;

        public MyOnClickListener(int i) {
            index = i;
        }

        public void onClick(View v) {
            switch (index) {
                case 0:
                    setSelect(0);
                    break;
                case 1:
                    setSelect(1);
                    break;
            }
            viewPager.setCurrentItem(index);
        }
    }

    public class MyOnPageChangeListener implements LazyViewPager.OnPageChangeListener {
        public void onPageScrollStateChanged(int index) {
        }
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }
        public void onPageSelected(int index) {
            switch (index) {
                case 0:
                    setSelect(0);
                    break;
                case 1:
                    setSelect(1);
                    break;
            }
        }
    }

    public void setSelect(int position){
        selectedColor = getResources().getColor(R.color.text_color_green);
        unSelectedColor = getResources().getColor(R.color.text_color_main);
        for (int i = 0; i < mTextView.length; i++) {
            if (i != position) {
                mTextView[i].setTextColor(unSelectedColor);
                mView[i].setVisibility(View.INVISIBLE);
            }else {
                mTextView[i].setTextColor(selectedColor);
                mView[i].setVisibility(View.VISIBLE);
            }
        }
    }
}
