package com.lixin.freshmall.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.lixin.freshmall.R;
import com.lixin.freshmall.adapter.SpecAdapter;
import com.lixin.freshmall.model.Constant;
import com.lixin.freshmall.model.ShopDecBean;
import com.lixin.freshmall.okhttp.OkHttpUtils;
import com.lixin.freshmall.okhttp.budiler.StringCallback;
import com.lixin.freshmall.uitls.SPUtil;
import com.lixin.freshmall.uitls.ToastUtils;
import com.lixin.freshmall.uitls.Utility;
import com.lixin.freshmall.view.MyListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by 小火
 * Create time on  2017/12/7
 * My mailbox is 1403241630@qq.com
 */

public class SpecFragment extends BaseFragment {
    private View view;
    private String rotateid,uid;
    private MyListView spec_list;
    private List<ShopDecBean.CommoditySpec> mList;
    private SpecAdapter mAdapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = LayoutInflater.from(context).inflate(R.layout.fragment_spec,null);
        uid = SPUtil.getString(context,"uid");
        mList = new ArrayList<>();
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            rotateid = bundle.getString("rotateid");
        }
        initView();
        getdata();
        return view;
    }

    private void initView() {
        spec_list = view.findViewById(R.id.spec_list);
        mAdapter = new SpecAdapter(context,mList);
        spec_list.setAdapter(mAdapter);
    }

    private void getdata() {
        Map<String, String> params = new HashMap<>();
        final String json = "{\"cmd\":\"getCommoditysDetilInfo\",\"commodityid\":\"" + rotateid + "\",\"uid\":\"" + uid + "\"}";
        params.put("json", json);
        dialog.show();
        OkHttpUtils.post().url(Constant.THE_SERVER_URL).params(params)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ToastUtils.makeText(context, "网络异常");
                dialog.dismiss();
            }

            @Override
            public void onResponse(String response, int id) {
                Log.i("ShopDecActivity", "response: " + response);
                Gson gson = new Gson();
                dialog.dismiss();
                ShopDecBean shopDecBean = gson.fromJson(response, ShopDecBean.class);
                if (shopDecBean.getResult().equals("1")) {
                    ToastUtils.makeText(context, shopDecBean.getResultNote());
                    return;
                }
                List<ShopDecBean.CommoditySpec> commoditySpecs = shopDecBean.getCommoditySpec();
                mList.addAll(commoditySpecs);
                mAdapter.notifyDataSetChanged();
                Utility.setListViewHeightBasedOnChildren(spec_list);
            }
        });
    }
    public static SpecFragment newInstance(String rotateid) {
        Bundle bundle = new Bundle();
        bundle.putString("rotateid", rotateid);
        SpecFragment fragment = new SpecFragment();
        fragment.setArguments(bundle);
        return fragment;
    }
}
