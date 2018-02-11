package com.lixin.freshmall.activity;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.widget.TextView;

import com.lixin.freshmall.R;
import com.lixin.freshmall.fragment.CompleteFragment;
import com.lixin.freshmall.fragment.WaitGoodsFragment;
import com.lixin.freshmall.fragment.WaitPayFragment;
import com.lixin.freshmall.fragment.WaitRefundsFragment;
import com.lixin.freshmall.fragment.WaitSendFragment;
import com.lixin.freshmall.view.LazyViewPager;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by 小火
 * Create time on  2017/10/25
 * My mailbox is 1403241630@qq.com
 */

public class MyOrderActivity extends BaseActivity{
    private Resources resource;
    private ColorStateList csl1, csl2;
    private int currentItem = 0;
    private View imageView0, imageView1, imageView2, imageView3,imageView4;
    private LazyViewPager viewPager;
    private TextView allOrder, waitGoods,sendGoods, waitReceipt, complete;// 选项名称
    private List<Fragment> fragments;// Tab页面列表
    private int offset = 0;// 动画图片偏移量
    private int type;//选择的订单状态

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        hideBack(2);
        setTitleText("我的订单");
        currentItem = Integer.valueOf(getIntent().getStringExtra("currentItem"));
        resource = context.getResources();
        initView();
        InitViewPager();
    }

    private void initView() {
        csl1 = resource.getColorStateList(R.color.text_color_green);
        csl2 = resource.getColorStateList(R.color.text_color_main);
    }

    /**
     * 初始化Viewpager页
     */
    private void InitViewPager() {
        imageView0 = findViewById(R.id.cursor_0);
        imageView1 = findViewById(R.id.cursor_1);
        imageView2 = findViewById(R.id.cursor_2);
        imageView3 = findViewById(R.id.cursor_3);
        imageView4 = findViewById(R.id.cursor_4);
        viewPager = findViewById(R.id.vPager);
        fragments = new ArrayList<>();
        fragments.add(new WaitPayFragment());
        fragments.add(new WaitGoodsFragment());
        fragments.add(new WaitSendFragment());
        fragments.add(new CompleteFragment());
        fragments.add(new WaitRefundsFragment());
        viewPager.setAdapter(new myPagerAdapter(getSupportFragmentManager(),
                fragments));
        //页面定位
        viewPager.setCurrentItem(currentItem);
        //初始化头标
        allOrder = findViewById(R.id.text_wait_pay);
        waitGoods = findViewById(R.id.text_wait_goods);
        sendGoods = findViewById(R.id.text_wait_send);
        waitReceipt = findViewById(R.id.text_complete);
        complete = findViewById(R.id.text_wait_refunds);

        allOrder.setOnClickListener(new MyOnClickListener(0));
        waitGoods.setOnClickListener(new MyOnClickListener(1));
        sendGoods.setOnClickListener(new MyOnClickListener(2));
        waitReceipt.setOnClickListener(new MyOnClickListener(3));
        complete.setOnClickListener(new MyOnClickListener(4));
        viewPager.setOnPageChangeListener(new MyOnPageChangeListener());
        if (currentItem == 0) {
            allOrder.setTextColor(csl1);
            waitGoods.setTextColor(csl2);
            sendGoods.setTextColor(csl2);
            waitReceipt.setTextColor(csl2);
            complete.setTextColor(csl2);
            imageView0.setVisibility(View.VISIBLE);
            imageView1.setVisibility(View.INVISIBLE);
            imageView2.setVisibility(View.INVISIBLE);
            imageView3.setVisibility(View.INVISIBLE);
            imageView4.setVisibility(View.INVISIBLE);
        }else if (currentItem == 1) {
            allOrder.setTextColor(csl2);
            waitGoods.setTextColor(csl1);
            sendGoods.setTextColor(csl2);
            waitReceipt.setTextColor(csl2);
            complete.setTextColor(csl2);
            imageView0.setVisibility(View.INVISIBLE);
            imageView1.setVisibility(View.VISIBLE);
            imageView2.setVisibility(View.INVISIBLE);
            imageView3.setVisibility(View.INVISIBLE);
            imageView4.setVisibility(View.INVISIBLE);
        }else if (currentItem == 2){
            allOrder.setTextColor(csl2);
            waitGoods.setTextColor(csl2);
            sendGoods.setTextColor(csl1);
            waitReceipt.setTextColor(csl2);
            complete.setTextColor(csl2);
            imageView0.setVisibility(View.INVISIBLE);
            imageView1.setVisibility(View.INVISIBLE);
            imageView2.setVisibility(View.VISIBLE);
            imageView3.setVisibility(View.INVISIBLE);
            imageView4.setVisibility(View.INVISIBLE);
        }else if (currentItem == 3) {
            allOrder.setTextColor(csl2);
            waitGoods.setTextColor(csl2);
            sendGoods.setTextColor(csl2);
            waitReceipt.setTextColor(csl1);
            complete.setTextColor(csl2);
            imageView0.setVisibility(View.INVISIBLE);
            imageView1.setVisibility(View.INVISIBLE);
            imageView2.setVisibility(View.INVISIBLE);
            imageView3.setVisibility(View.VISIBLE);
            imageView4.setVisibility(View.INVISIBLE);
        }else if (currentItem == 4) {
            allOrder.setTextColor(csl2);
            waitGoods.setTextColor(csl2);
            sendGoods.setTextColor(csl2);
            waitReceipt.setTextColor(csl2);
            complete.setTextColor(csl1);
            imageView0.setVisibility(View.INVISIBLE);
            imageView1.setVisibility(View.INVISIBLE);
            imageView2.setVisibility(View.INVISIBLE);
            imageView3.setVisibility(View.INVISIBLE);
            imageView4.setVisibility(View.VISIBLE);
        }

    }
    /**
     * 初始化动画，这个就是页卡滑动时，下面的横线也滑动的效果，在这里需要计算一些数据
     *
     * 头标点击监听
     */
    private class MyOnClickListener implements View.OnClickListener {
        private int index = 0;

        public MyOnClickListener(int i) {
            index = i;
        }

        public void onClick(View v) {
            switch (index) {
                case 0:
                    allOrder.setTextColor(csl1);
                    waitGoods.setTextColor(csl2);
                    sendGoods.setTextColor(csl2);
                    waitReceipt.setTextColor(csl2);
                    complete.setTextColor(csl2);
                    imageView0.setVisibility(View.VISIBLE);
                    imageView1.setVisibility(View.INVISIBLE);
                    imageView2.setVisibility(View.INVISIBLE);
                    imageView3.setVisibility(View.INVISIBLE);
                    imageView4.setVisibility(View.INVISIBLE);
                    type = 0;
                    break;
                case 1:
                    allOrder.setTextColor(csl2);
                    waitGoods.setTextColor(csl1);
                    sendGoods.setTextColor(csl2);
                    waitReceipt.setTextColor(csl2);
                    complete.setTextColor(csl2);
                    imageView0.setVisibility(View.INVISIBLE);
                    imageView1.setVisibility(View.VISIBLE);
                    imageView2.setVisibility(View.INVISIBLE);
                    imageView3.setVisibility(View.INVISIBLE);
                    imageView4.setVisibility(View.INVISIBLE);
                    type = 1;
                    break;
                case 2:
                    allOrder.setTextColor(csl2);
                    waitGoods.setTextColor(csl2);
                    sendGoods.setTextColor(csl1);
                    waitReceipt.setTextColor(csl2);
                    complete.setTextColor(csl2);
                    imageView0.setVisibility(View.INVISIBLE);
                    imageView1.setVisibility(View.INVISIBLE);
                    imageView2.setVisibility(View.VISIBLE);
                    imageView3.setVisibility(View.INVISIBLE);
                    imageView4.setVisibility(View.INVISIBLE);
                    type = 2;
                    break;
                case 3:
                    allOrder.setTextColor(csl2);
                    waitGoods.setTextColor(csl2);
                    sendGoods.setTextColor(csl2);
                    waitReceipt.setTextColor(csl1);
                    complete.setTextColor(csl2);
                    imageView0.setVisibility(View.INVISIBLE);
                    imageView1.setVisibility(View.INVISIBLE);
                    imageView2.setVisibility(View.INVISIBLE);
                    imageView3.setVisibility(View.VISIBLE);
                    imageView4.setVisibility(View.INVISIBLE);
                    type = 3;
                    break;
                case 4:
                    allOrder.setTextColor(csl2);
                    waitGoods.setTextColor(csl2);
                    sendGoods.setTextColor(csl2);
                    waitReceipt.setTextColor(csl2);
                    complete.setTextColor(csl1);
                    imageView0.setVisibility(View.INVISIBLE);
                    imageView1.setVisibility(View.INVISIBLE);
                    imageView2.setVisibility(View.INVISIBLE);
                    imageView3.setVisibility(View.INVISIBLE);
                    imageView4.setVisibility(View.VISIBLE);
                    break;
            }
            viewPager.setCurrentItem(index);
        }

    }

    /**
     * 为选项卡绑定监听器
     */
    public class MyOnPageChangeListener implements LazyViewPager.OnPageChangeListener {

        public void onPageScrollStateChanged(int index) {
        }

        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        public void onPageSelected(int index) {
//            Animation animation = new TranslateAnimation(one * currIndex, one
//                    * index, 0, 0);// 显然这个比较简洁，只有一行代码。
//            currIndex = index;
//            animation.setFillAfter(true);// True:图片停在动画结束位置
//            animation.setDuration(300);
            switch (index) {
                case 0:
                    allOrder.setTextColor(csl1);
                    waitGoods.setTextColor(csl2);
                    sendGoods.setTextColor(csl2);
                    waitReceipt.setTextColor(csl2);
                    complete.setTextColor(csl2);
                    imageView0.setVisibility(View.VISIBLE);
                    imageView1.setVisibility(View.INVISIBLE);
                    imageView2.setVisibility(View.INVISIBLE);
                    imageView3.setVisibility(View.INVISIBLE);
                    imageView4.setVisibility(View.INVISIBLE);
                    type = 0;
                    break;
                case 1:
                    allOrder.setTextColor(csl2);
                    waitGoods.setTextColor(csl1);
                    sendGoods.setTextColor(csl2);
                    waitReceipt.setTextColor(csl2);
                    complete.setTextColor(csl2);
                    imageView0.setVisibility(View.INVISIBLE);
                    imageView1.setVisibility(View.VISIBLE);
                    imageView2.setVisibility(View.INVISIBLE);
                    imageView3.setVisibility(View.INVISIBLE);
                    imageView4.setVisibility(View.INVISIBLE);
                    type = 1;
                    break;
                case 2:
                    allOrder.setTextColor(csl2);
                    waitGoods.setTextColor(csl2);
                    sendGoods.setTextColor(csl1);
                    waitReceipt.setTextColor(csl2);
                    complete.setTextColor(csl2);
                    imageView0.setVisibility(View.INVISIBLE);
                    imageView1.setVisibility(View.INVISIBLE);
                    imageView2.setVisibility(View.VISIBLE);
                    imageView3.setVisibility(View.INVISIBLE);
                    imageView4.setVisibility(View.INVISIBLE);
                    type = 2;
                    break;
                case 3:
                    allOrder.setTextColor(csl2);
                    waitGoods.setTextColor(csl2);
                    sendGoods.setTextColor(csl2);
                    waitReceipt.setTextColor(csl1);
                    complete.setTextColor(csl2);
                    imageView0.setVisibility(View.INVISIBLE);
                    imageView1.setVisibility(View.INVISIBLE);
                    imageView2.setVisibility(View.INVISIBLE);
                    imageView3.setVisibility(View.VISIBLE);
                    imageView4.setVisibility(View.INVISIBLE);
                    type = 3;
                    break;
                case 4:
                    allOrder.setTextColor(csl2);
                    waitGoods.setTextColor(csl2);
                    sendGoods.setTextColor(csl2);
                    waitReceipt.setTextColor(csl2);
                    complete.setTextColor(csl1);
                    imageView0.setVisibility(View.INVISIBLE);
                    imageView1.setVisibility(View.INVISIBLE);
                    imageView2.setVisibility(View.INVISIBLE);
                    imageView3.setVisibility(View.INVISIBLE);
                    imageView4.setVisibility(View.VISIBLE);
                    break;
            }
        }
    }

    /**
     * 定义适配器
     */
    class myPagerAdapter extends FragmentPagerAdapter {
        private List<Fragment> fragmentList;
        public myPagerAdapter(FragmentManager fm, List<Fragment> fragmentList) {
            super(fm);
            this.fragmentList = fragmentList;
        }

        /**
         * 得到每个页面
         */
        @Override
        public Fragment getItem(int arg0) {
            return (fragmentList == null || fragmentList.size() == 0) ? null
                    : fragmentList.get(arg0);
        }

        /**
         * 每个页面的title
         */
        @Override
        public CharSequence getPageTitle(int position) {
            return null;
        }

        /**
         * 页面的总个数
         */
        @Override
        public int getCount() {
            return fragmentList == null ? 0 : fragmentList.size();
        }
    }

}
