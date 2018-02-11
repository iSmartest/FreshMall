package com.lixin.freshmall.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.xrecyclerview.XRecyclerView;
import com.google.gson.Gson;
import com.lixin.freshmall.R;
import com.lixin.freshmall.adapter.MyEvaluateAdapter;
import com.lixin.freshmall.model.Constant;
import com.lixin.freshmall.model.MyEvaluateBean;
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
 * Create time on  2017/10/30
 * My mailbox is 1403241630@qq.com
 */

public class EvaluatedFragment extends BaseFragment{
    private View view;
    private XRecyclerView order_list;
    private MyEvaluateAdapter mAdapter;
    private int nowPage = 1;
    private String uid;
    private String commentState = "1";
    private List<MyEvaluateBean.OrderCommodity> mList;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_order_content,null);
        uid = SPUtil.getString(getActivity(),"uid");
        mList = new ArrayList<>();
        initView();
        getdata(true);
        return view;
    }

    private void initView() {
        order_list = view.findViewById(R.id.order_list);
        order_list.setLayoutManager(new LinearLayoutManager(getActivity()));
        order_list.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                nowPage = 1;
                mList.clear();
                getdata(false);
                mAdapter.notifyDataSetChanged();
                order_list.refreshComplete();
            }

            @Override
            public void onLoadMore() {
                nowPage++;
                getdata(false);
                mAdapter.notifyDataSetChanged();
                order_list.refreshComplete();
            }
        });
        mAdapter = new MyEvaluateAdapter(context, mList,uid,commentState);
        order_list.setAdapter(mAdapter);
    }

    private void getdata(boolean isShowLoadDialog) {
        Map<String, String> params = new HashMap<>();
        final String json="{\"cmd\":\"getMyCommodityComment\",\"commentState\":\"" + commentState +"\",\"uid\":\""
                + uid + "\",\"nowPage\":\"" + nowPage + "\"}";
        params.put("json", json);
        Log.i("WaitPaymentFragment", "onResponse: " + json);
        if (isShowLoadDialog){
            dialog.show();
        }
        OkHttpUtils.post().url(Constant.THE_SERVER_URL).params(params)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ToastUtils.makeText(context, e.getMessage());
                if (dialog != null){
                    dialog.dismiss();
                }
                order_list.refreshComplete();
            }
            @Override
            public void onResponse(String response, int id) {
                Log.i("WaitPaymentFragment", "onResponse: " + response);
                Gson gson = new Gson();
                if (dialog != null){
                    dialog.dismiss();
                }
                MyEvaluateBean myEvaluateBean = gson.fromJson(response, MyEvaluateBean.class);
                if (myEvaluateBean.getResult().equals("1")) {
                    ToastUtils.makeText(context, myEvaluateBean.getResultNote());
                    return;
                }
                List<MyEvaluateBean.OrderCommodity> orderCommodities = myEvaluateBean.getOrderCommodity();
                mList.addAll(orderCommodities);
                order_list.refreshComplete();
                if (myEvaluateBean.getTotalPage() < nowPage) {
                    ToastUtils.makeText(context, "没有更多了");
                    order_list.noMoreLoading();
                    return;
                }
            }
        });
    }
    @Override
    public void onPause(){
        super.onPause();
        nowPage = 1;
    }
}
