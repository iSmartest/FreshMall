package com.lixin.freshmall.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.xrecyclerview.XRecyclerView;
import com.google.gson.Gson;
import com.lixin.freshmall.R;
import com.lixin.freshmall.activity.MyApplication;
import com.lixin.freshmall.activity.NowBuyActivity;
import com.lixin.freshmall.adapter.ShopCartAdapter;
import com.lixin.freshmall.model.Constant;
import com.lixin.freshmall.model.GenerateOrderBean;
import com.lixin.freshmall.model.ShopCartBean;
import com.lixin.freshmall.model.UserInfo;
import com.lixin.freshmall.okhttp.OkHttpUtils;
import com.lixin.freshmall.okhttp.budiler.StringCallback;
import com.lixin.freshmall.uitls.DoubleCalculationUtil;
import com.lixin.freshmall.uitls.SPUtil;
import com.lixin.freshmall.uitls.StatusBarUtil;
import com.lixin.freshmall.uitls.ToastUtils;
import com.lixin.freshmall.uitls.Utils;

import java.io.Serializable;
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

public class CartFragment extends BaseFragment implements View.OnClickListener ,
        ShopCartAdapter.CheckInterface, ShopCartAdapter.ModifyCountInterface ,View.OnTouchListener {
    private View view,headView;
    private TextView mTotalPrice,mTotalNum;
    private CheckBox mAllChoose;
    private XRecyclerView shop_car_list;
    private ShopCartAdapter mAdapter;
    private int nowPage = 1,num = 0;
    private String uid,townId;
    private List<ShopCartBean.Shop> mList;
    private double totalPrice = 0.00;// 购买的商品总价
    private String mMoney;
    private String orderId;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_cart,container,false);
        uid = SPUtil.getString(getActivity(),"uid");
        townId = SPUtil.getString(context,"TownId");
        mList = new ArrayList<>();
        initView();
        requestData();
        return view;
    }
    private void initView() {
        RelativeLayout mToolbar = view.findViewById(R.id.rl_cart_toolbar);
        StatusBarUtil.setHeightAndPadding(getActivity(), mToolbar);
        shop_car_list = view.findViewById(R.id.shop_car_list);
        view.findViewById(R.id.text_shop_car_sure).setOnClickListener(this);
        mTotalPrice = view.findViewById(R.id.text_fragment_shop_total_price);
        mTotalNum = view.findViewById(R.id.text_fragment_cart_total_num);
        headView = LayoutInflater.from(context).inflate(R.layout.header_cart,null);
        mAllChoose = headView.findViewById(R.id.ck_fragment_shop_all_chose);
        mAllChoose.setOnClickListener(this);
        mAllChoose.setChecked(false);
        shop_car_list.setLayoutManager(new LinearLayoutManager(getActivity()));
        if (headView != null){
            shop_car_list.addHeaderView(headView);
        }
        mAdapter = new ShopCartAdapter(context, mList,uid);
        mAdapter.setCheckInterface(this);
        mAdapter.setModifyCountInterface(this);
        shop_car_list.setAdapter(mAdapter);
        shop_car_list.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                nowPage = 1;
                mList.clear();
                requestData();
                mAdapter.notifyDataSetChanged();
                shop_car_list.refreshComplete();
            }

            @Override
            public void onLoadMore() {
                nowPage++;
                requestData();
                mAdapter.notifyDataSetChanged();
                shop_car_list.refreshComplete();
            }
        });
    }

    private void requestData() {
        Map<String, String> params = new HashMap<>();
        final String json="{\"cmd\":\"getShopCarListInfo\",\"nowPage\":\"" + nowPage +"\",\"uid\":\""
                + uid + "\",\"townId\":\"" + townId + "\"}";
        params.put("json", json);
        dialog.show();
        OkHttpUtils.post().url(Constant.THE_SERVER_URL).params(params)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ToastUtils.makeText(context, e.getMessage());
                dialog.dismiss();
            }
            @Override
            public void onResponse(String response, int id) {
                Log.i("ShopCartFragment", "onResponse: " + response);
                Gson gson = new Gson();
                dialog.dismiss();
                ShopCartBean mShopCartBean = gson.fromJson(response,ShopCartBean.class);
                if (mShopCartBean.getResult().equals("1")){
                    ToastUtils.makeText(context,mShopCartBean.getResultNote());
                    return;
                }
                List<ShopCartBean.Shop> shop = mShopCartBean.getShop();
                mList.addAll(shop);
                mAdapter.notifyDataSetChanged();
                shop_car_list.refreshComplete();
                mAllChoose.setChecked(false);
                mTotalPrice.setText(0.00 + "");
                mTotalNum.setText(0 + "");
                if (mShopCartBean.getTotalPage() < nowPage){
                    ToastUtils.makeText(context,"没有更多了");
                    shop_car_list.noMoreLoading();
                }
            }
        });
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.text_shop_car_sure:
                List<GenerateOrderBean.commoditys> list = new ArrayList<>();
                for (int i = 0; i < mList.size() ; i++) {
                    ShopCartBean.Shop shopList = mList.get(i);
                    if (shopList.isChoosed){
                        GenerateOrderBean.commoditys comm = new GenerateOrderBean.commoditys(shopList.getCommodityid(),
                                shopList.getCommodityTitle(),shopList.getCommodityIcon()
                                ,shopList.getCommodityFirstParam(),shopList.getCommoditySecondParam(),shopList.getCommodityNewPrice(),shopList.getCommodityShooCarNum(),shopList.getCommodityUnit());
                        list.add(comm);
                    }
                }
                if (!list.isEmpty()){
                    getOrderData(list);
                }else {
                    ToastUtils.makeText(getActivity(),"未选中任何商品!");
                }
                break;
            case R.id.ck_fragment_shop_all_chose:
                if (mList.size() != 0) {
                    if (mAllChoose.isChecked()) {
                        for (int i = 0; i < mList.size(); i++) {
                            mList.get(i).setChoosed(true);
                        }
                        mAdapter.notifyDataSetChanged();
                    } else {
                        for (int i = 0; i < mList.size(); i++) {
                            mList.get(i).setChoosed(false);
                        }
                        mAdapter.notifyDataSetChanged();
                    }
                }
                statistics();
                break;
        }
    }
    private void getOrderData(final List<GenerateOrderBean.commoditys> list) {
        Map<String, String> params = new HashMap<>();
        GenerateOrderBean generateOrderBean = new GenerateOrderBean("buyCommodity",uid,townId,list);
        String json = new Gson().toJson(generateOrderBean);
        params.put("json", json);
        Log.i("ShopCartFragment", "getOrderData: " + json);
        dialog.show();
        OkHttpUtils.post().url(Constant.THE_SERVER_URL).params(params)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ToastUtils.makeText(context, e.getMessage());
                dialog.dismiss();
            }
            @Override
            public void onResponse(String response, int id) {
                Log.i("ShopCartFragment", "onResponse: " + response);
                Gson gson = new Gson();
                dialog.dismiss();
                UserInfo mUserInfo = gson.fromJson(response, UserInfo.class);
                if (mUserInfo.getResult().equals("1")) {
                    ToastUtils.makeText(context, mUserInfo.getResultNote());
                    return;
                }
                MyApplication.shopPay = 1;
                orderId = mUserInfo.getOrderId();
                mMoney = mUserInfo.getTotalMoney();
                Log.i("ShopCartFragment", "onResponse: " + orderId);
                Intent intent = new Intent(getActivity(),NowBuyActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("orderId",orderId);
                bundle.putString("mMoney",mMoney);
                bundle.putDouble("totalPrice",totalPrice);
                bundle.putSerializable("OrderShop", (Serializable) list);
                intent.putExtras(bundle);
                startActivityForResult(intent,0717);
            }
        });
    }

    /**
     * 遍历list集合
     * @return
     */
    private boolean isAllCheck() {
        for (ShopCartBean.Shop group : mList) {
            if (!group.isChoosed())
                return false;
        }
        return true;
    }

    /**
     * 统计操作
     * 1.先清空全局计数器<br>
     * 2.遍历所有子元素，只要是被选中状态的，就进行相关的计算操作
     * 3.给底部的textView进行数据填充
     */
    private void statistics() {
        totalPrice = 0.00;
        num = 0;
        for (int i = 0; i < mList.size(); i++) {
            ShopCartBean.Shop shop = mList.get(i);
            if (shop.isChoosed()) {
                totalPrice = DoubleCalculationUtil.add(totalPrice, DoubleCalculationUtil.mul(mList.get(i).commodityNewPrice,mList.get(i).getCommodityShooCarNum()));
                num += Integer.parseInt(mList.get(i).getCommodityShooCarNum());
            }
        }
        mTotalPrice.setText(totalPrice + "");
        mTotalNum.setText(num + "");
    }

    @Override
    public void doIncrease(int position, View showCountView, boolean isChecked) {
        ShopCartBean.Shop shopBean = mList.get(position);
        int currentCount = Integer.parseInt(shopBean.getCommodityShooCarNum());
        if (TextUtils.isEmpty(shopBean.getCommodityMaxBuyNum())){
            currentCount++;
        }else {
            if (Integer.parseInt(shopBean.getCommodityMaxBuyNum())> currentCount ){
                currentCount++;
            }else {
                ToastUtils.makeText(getActivity(),"您的可购买数量已达上限");
            }
        }
        String num = String.valueOf(currentCount);
        shopBean.setCommodityShooCarNum(num);
        ((TextView) showCountView).setText(num);
        mAdapter.notifyDataSetChanged();
        statistics();
    }

    @Override
    public void doDecrease(int position, View showCountView, boolean isChecked) {
        ShopCartBean.Shop shopBean = mList.get(position);
        int currentCount = Integer.parseInt(shopBean.getCommodityShooCarNum());
        if (currentCount == 1) {
            return;
        }
        currentCount--;
        String num = String.valueOf(currentCount);
        shopBean.setCommodityShooCarNum(num);
        ((TextView) showCountView).setText(num);
        mAdapter.notifyDataSetChanged();
        statistics();
    }
    @Override
    public void childDelete(int position) {
        mList.remove(position);
        ToastUtils.makeText(context, "商品删除成功！");
        mAdapter.notifyDataSetChanged();
        statistics();
    }

    @Override
    public void checkGroup(int position, boolean isChecked) {
        mList.get(position).setChoosed(isChecked);
        if (isAllCheck())
            mAllChoose.setChecked(true);
        else
            mAllChoose.setChecked(false);
        mAdapter.notifyDataSetChanged();
        statistics();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden){
            if (mList.isEmpty()){
                if (Utils.isNetworkAvailable(getActivity())){
                    mList.clear();
                    requestData();
                    mAdapter.notifyDataSetChanged();
                }else {
                    ToastUtils.makeText(getActivity(),"呀！网络跑丢了！");
                }
            }
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return true;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // 拦截触摸事件，防止泄露下去
        view.setOnTouchListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mList != null && !mList.isEmpty()){
            if (MyApplication.shopPay == 1){
                nowPage = 1;
                mList.clear();
                requestData();
                MyApplication.shopPay = 0;
                mAdapter.notifyDataSetChanged();
            }
        }
    }
}
