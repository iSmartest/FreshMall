package com.lixin.freshmall.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.gson.Gson;
import com.lixin.freshmall.R;
import com.lixin.freshmall.adapter.NotUsedAdapter;
import com.lixin.freshmall.model.Constant;
import com.lixin.freshmall.model.CouponBean;
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
 * Create time on  2017/5/18
 * My mailbox is 1403241630@qq.com
 */

public class NotUsedFragment extends BaseFragment implements NotUsedAdapter.onUseOnClickListener {
    private View view;
    private NotUsedAdapter notUsedAdapter;
    private ListView not_used_list;
    private List<CouponBean.securitiesList> mList;
    private String securitiesType = "0";
    private String uid;
    private Double totalPrice = 0.00;
    private Double reducePrice = 0.00;
    private Double allPrice = 0.00;
    private int flag;
    private static final String PRICE_MARK = "price_mark";// 设置标记
    private static final String FLAG_MARK = "flag_mark";// 设置标记
    private CallBackValue callBackValue;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        //当前fragment从activity重写了回调接口  得到接口的实例化对象
        callBackValue =(NotUsedFragment.CallBackValue) getActivity();
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_not_used,null);
        mList = new ArrayList<>();
        uid = SPUtil.getString(getActivity(),"uid");
        Bundle bundle = this.getArguments();
        if (bundle != null)
        {
            totalPrice = bundle.getDouble(PRICE_MARK);
            flag = bundle.getInt(FLAG_MARK);
        }
        initView();
        getdata();
        return view;
    }

    private void initView() {
        not_used_list = view.findViewById(R.id.not_used_list);
        notUsedAdapter = new NotUsedAdapter();
        notUsedAdapter.setUseOnClickListener(this);
        not_used_list.setAdapter(notUsedAdapter);

    }

    private void getdata() {
        Map<String, String> params = new HashMap<>();
        final String json="{\"cmd\":\"getCouponInfo\",\"securitiesType\":\"" + securitiesType +"\",\"uid\":\""
                + uid + "\"}";
        params.put("json", json);
        Log.i("NotUsedFragment", "onResponse: " + json);
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
                Log.i("NotUsedFragment", "onResponse: " + response);
                Gson gson = new Gson();
                dialog.dismiss();
                CouponBean couponBean = gson.fromJson(response, CouponBean.class);
                if (couponBean.result.equals("1")) {
                    ToastUtils.makeText(context, couponBean.resultNote);
                    return;
                }
                List<CouponBean.securitiesList> securitiesList = couponBean.securitiesList;
                mList.addAll(securitiesList);
                notUsedAdapter.setShopCart(getActivity(),mList,uid);
                not_used_list.setAdapter(notUsedAdapter);
            }
        });
    }

    public static NotUsedFragment newMyFragment(double mTotalPrice,int flag) {
        //将fragment绑定参数
        Bundle bundle = new Bundle();
        bundle.putDouble(PRICE_MARK, mTotalPrice);
        bundle.putInt(FLAG_MARK,flag);
        NotUsedFragment fragment = new NotUsedFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void setUseOnClickListener(int position) {
        allPrice = Double.parseDouble(mList.get(position).getSecuritiesAllPrice());
        reducePrice = Double.parseDouble(mList.get(position).getSecuritiesReducePrice());
        if (flag == 0){
            if (totalPrice >= allPrice){
                callBackValue.SendMessageValue(allPrice,reducePrice,mList.get(position).getSecuritiesid());
            }else {
                ToastUtils.makeText(getActivity(),"只有满" + allPrice + "才可以使用！");
            }
        }
    }

    //写一个回调接口
    public interface CallBackValue{
        void SendMessageValue(Double value1, Double value2, String value3);
    }
}
