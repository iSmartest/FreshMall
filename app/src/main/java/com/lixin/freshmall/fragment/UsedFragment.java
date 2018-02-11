package com.lixin.freshmall.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.gson.Gson;
import com.lixin.freshmall.R;
import com.lixin.freshmall.adapter.UsedAdapter;
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

public class UsedFragment extends BaseFragment{
    private View view;
    private UsedAdapter usedAdapter;
    private ListView used_list;
    private List<CouponBean.securitiesList> mList;
    private String securitiesType = "1";
    private String uid;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_used,null);
        mList = new ArrayList<>();
        uid = SPUtil.getString(getActivity(),"uid");
        initView();
        getdata();
        return view;
    }
    private void initView() {
        used_list = view.findViewById(R.id.used_list);
        usedAdapter = new UsedAdapter();
        used_list.setAdapter(usedAdapter);
    }

    private void getdata() {
        Map<String, String> params = new HashMap<>();
        final String json="{\"cmd\":\"getCouponInfo\",\"securitiesType\":\"" + securitiesType +"\",\"uid\":\""
                + uid + "\"}";
        params.put("json", json);
        Log.i("UsedFragment", "onResponse: " + json);
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
                Log.i("UsedFragment", "onResponse: " + response);
                Gson gson = new Gson();
                dialog.dismiss();
                CouponBean couponBean = gson.fromJson(response, CouponBean.class);
                if (couponBean.result.equals("1")) {
                    ToastUtils.makeText(context, couponBean.resultNote);
                    return;
                }
                List<CouponBean.securitiesList> securitiesList = couponBean.securitiesList;
                mList.addAll(securitiesList);
                usedAdapter.setShopCart(getActivity(),mList,uid);
                used_list.setAdapter(usedAdapter);
            }
        });
    }
}
