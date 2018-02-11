package com.lixin.freshmall.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.xrecyclerview.XRecyclerView;
import com.google.gson.Gson;
import com.lixin.freshmall.R;
import com.lixin.freshmall.adapter.LifeAdapter;
import com.lixin.freshmall.listenter.RecyclerItemTouchListener;
import com.lixin.freshmall.model.Constant;
import com.lixin.freshmall.model.LifeBean;
import com.lixin.freshmall.okhttp.OkHttpUtils;
import com.lixin.freshmall.okhttp.budiler.StringCallback;
import com.lixin.freshmall.uitls.ToastUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by 小火
 * Create time on  2017/12/1
 * My mailbox is 1403241630@qq.com
 */

public class MyLifeActivity extends BaseActivity {
    private XRecyclerView life_list;
    private LifeAdapter mAdapter;
    private List<LifeBean.LifeList> mList;
    private int nowPage = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_life);
        mList = new ArrayList<>();
        hideBack(2);
        setTitleText("美味生活");
        initView();
        getLifeData();
    }

    private void initView() {
        life_list = findViewById(R.id.life_list);
        life_list.setLayoutManager(new LinearLayoutManager(context));
        mAdapter = new LifeAdapter(context, mList);
        life_list.setAdapter(mAdapter);
        life_list.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                nowPage = 1;
                mList.clear();
                getLifeData();
                mAdapter.notifyDataSetChanged();
                life_list.refreshComplete();
            }
            @Override
            public void onLoadMore() {
                nowPage++;
                getLifeData();
                mAdapter.notifyDataSetChanged();
                life_list.refreshComplete();
            }
        });

        life_list.addOnItemTouchListener(new RecyclerItemTouchListener(life_list) {
            @Override
            public void onItemClick(RecyclerView.ViewHolder vh) {
                int position = vh.getAdapterPosition() - 1;
                if (position < 0 | position >= mList.size()){
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putString(KnowLedgeWebActivity.TITLE,mList.get(position).getLifeTitle());
                bundle.putString(KnowLedgeWebActivity.URL,mList.get(position).getLifeUrl());
                MyApplication.openActivity(context,KnowLedgeWebActivity.class,bundle);
            }
        });
    }

    private void getLifeData() {
        Map<String, String> params = new HashMap<>();
        final String json="{\"cmd\":\"deliciousLife\",\"nowPage\":\"" + nowPage +"\"}";
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
                Log.i("ShopCartFragment", "onResponse: " + response);
                Gson gson = new Gson();
                dialog1.dismiss();

                LifeBean mLifeBean = gson.fromJson(response,LifeBean.class);
                if (mLifeBean.getResult().equals("1")){
                    ToastUtils.makeText(context,mLifeBean.getResultNote());
                    return;
                }
                List<LifeBean.LifeList> mLifeList = mLifeBean.getLifeList();
                mList.addAll(mLifeList);
                mAdapter.notifyDataSetChanged();
                life_list.refreshComplete();
                if (mLifeBean.getTotalPage() < nowPage){
                    ToastUtils.makeText(context,"没有更多了");
                    life_list.noMoreLoading();
                }
            }
        });
    }
}
