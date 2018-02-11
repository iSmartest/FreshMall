package com.lixin.freshmall.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;

import com.example.xrecyclerview.XRecyclerView;
import com.google.gson.Gson;
import com.lixin.freshmall.R;
import com.lixin.freshmall.adapter.IntagralDecAdapter;
import com.lixin.freshmall.model.Constant;
import com.lixin.freshmall.model.MyIntegralBean;
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
 * Create time on  2017/12/11
 * My mailbox is 1403241630@qq.com
 */

public class MyIntegralDecActivity extends BaseActivity {
    private List<MyIntegralBean.MoneyList> mList;
    private XRecyclerView money_dec_list;
    private IntagralDecAdapter mAdapter;
    private int nowPage = 1;
    private String uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moeny_dec);
        hideBack(2);
        setTitleText("积分明细");
        uid = SPUtil.getString(context,"uid");
        mList = new ArrayList<>();
        initView();
        getMyWellet();
    }
    private void initView() {
        money_dec_list = findViewById(R.id.money_dec_list);
        money_dec_list.setLayoutManager(new LinearLayoutManager(context));
        mAdapter = new IntagralDecAdapter(context, mList);
        money_dec_list.setAdapter(mAdapter);
        money_dec_list.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                nowPage = 1;
                mList.clear();
                getMyWellet();
                mAdapter.notifyDataSetChanged();
                money_dec_list.refreshComplete();
            }
            @Override
            public void onLoadMore() {
                nowPage++;
                getMyWellet();
                mAdapter.notifyDataSetChanged();
                money_dec_list.refreshComplete();
            }
        });

    }
    private void getMyWellet() {
        Map<String, String> params = new HashMap<>();
        final String json="{\"cmd\":\"getIntegralInfo\",\"nowPage\":\"" + nowPage +"\",\"uid\":\""
                + uid + "\"}";
        params.put("json", json);
        dialog1.show();
        OkHttpUtils.post().url(Constant.THE_SERVER_URL).params(params)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ToastUtils.makeText(context, e.getMessage());
                dialog1.dismiss();
            }
            @Override
            public void onResponse(String response, int id) {
                Log.i("MyIntegralActivity", "onResponse: " + response);
                Gson gson = new Gson();
                dialog1.dismiss();
                MyIntegralBean myIntegralBean = gson.fromJson(response, MyIntegralBean.class);
                if (myIntegralBean.getResult().equals("1")) {
                    ToastUtils.makeText(context, myIntegralBean.getResultNote());
                    return;
                }
                List<MyIntegralBean.MoneyList> commodityslist = myIntegralBean.getMoneyList();
                mList.addAll(commodityslist);
                mAdapter.notifyDataSetChanged();
                money_dec_list.refreshComplete();
                if (myIntegralBean.getTotalPage() < nowPage){
                    ToastUtils.makeText(context,"没有更多了");
                    money_dec_list.noMoreLoading();
                }
            }
        });
    }
}
