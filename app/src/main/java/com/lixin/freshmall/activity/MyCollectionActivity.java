package com.lixin.freshmall.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;

import com.example.xrecyclerview.XRecyclerView;
import com.google.gson.Gson;
import com.lixin.freshmall.R;
import com.lixin.freshmall.adapter.MyCollectionAdapter;
import com.lixin.freshmall.adapter.MyCollectionDecAdapter;
import com.lixin.freshmall.model.Constant;
import com.lixin.freshmall.model.MyCollection;
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
 * Create time on  2017/5/19
 * My mailbox is 1403241630@qq.com
 */

public class MyCollectionActivity extends BaseActivity implements MyCollectionDecAdapter.ModifyCountInterface {
    private XRecyclerView collection_list;
    private MyCollectionAdapter mAdapter;
    private int nowPage = 1;
    private String uid,townId;
    private String handleType;
    private List<MyCollection.commoditysList> mList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_collection);
        hideBack(2);
        setTitleText("我的收藏");
        uid = SPUtil.getString(context,"uid");
        townId = SPUtil.getString(context,"TownId");
        mList = new ArrayList<>();
        initView();
        getdata();
    }
    private void initView() {
        collection_list = findViewById(R.id.collection_list);
        collection_list.setLayoutManager(new LinearLayoutManager(context));
        mAdapter = new MyCollectionAdapter(context, mList,uid);
        collection_list.setAdapter(mAdapter);
        collection_list.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                nowPage = 1;
                mList.clear();
                getdata();
                mAdapter.notifyDataSetChanged();
                collection_list.refreshComplete();
            }

            @Override
            public void onLoadMore() {
                nowPage++;
                getdata();
                mAdapter.notifyDataSetChanged();
                collection_list.refreshComplete();
            }
        });
    }

    private void getdata() {
        Map<String, String> params = new HashMap<>();
        final String json="{\"cmd\":\"getCollection\",\"uid\":\"" + uid + "\",\"nowPage\":\"" + nowPage + "\",\"townId\":\""+townId+"\"}";
        params.put("json", json);
        Log.i("MyCollectionActivity", "onResponse: " + json);
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
                Log.i("MyCollectionActivity", "onResponse: " + response);
                Gson gson = new Gson();
                dialog1.dismiss();
                MyCollection myCollection = gson.fromJson(response, MyCollection.class);
                if (myCollection.result.equals("1")) {
                    ToastUtils.makeText(context, myCollection.resultNote);
                    return;
                }

                List<MyCollection.commoditysList> securitiesList = myCollection.commoditysList;
                mList.addAll(securitiesList);
                mAdapter.notifyDataSetChanged();
                collection_list.refreshComplete();

                if (myCollection.totalPage < nowPage) {
                    ToastUtils.makeText(context,"没有更多了");
                    collection_list.noMoreLoading();
                    return;
                }
            }
        });
    }

    @Override
    public void childDelete(int position) {
        if (position == 4){
            mList.clear();
            getdata();
            collection_list.refreshDrawableState();
        }
    }
}
