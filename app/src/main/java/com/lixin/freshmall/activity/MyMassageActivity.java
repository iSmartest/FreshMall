package com.lixin.freshmall.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.example.xrecyclerview.XRecyclerView;
import com.google.gson.Gson;
import com.lixin.freshmall.R;
import com.lixin.freshmall.adapter.MyMessageAdapter;
import com.lixin.freshmall.model.Constant;
import com.lixin.freshmall.model.MyMessageBean;
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
 * Create time on  2017/11/30
 * My mailbox is 1403241630@qq.com
 */

public class MyMassageActivity extends BaseActivity{
    private XRecyclerView message_list;
    private String uid;
    private List<MyMessageBean.messageList> mList;
    private MyMessageAdapter mAdapter;
    private int nowPage = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_message);
        uid = SPUtil.getString(context,"uid");
        mList = new ArrayList<>();
        hideBack(2);
        setTitleText("消息");
        initView();
        getdata();
    }
    private void initView() {
        message_list = findViewById(R.id.message_list);
        message_list.setLayoutManager(new LinearLayoutManager(this));
        message_list.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                nowPage = 1;
                mList.clear();
                getdata();
                mAdapter.notifyDataSetChanged();
                message_list.refreshComplete();
            }

            @Override
            public void onLoadMore() {
               message_list.noMoreLoading();
            }
        });
        mAdapter = new MyMessageAdapter(context,mList);
        message_list.setAdapter(mAdapter);
    }

    private void getdata() {
        Map<String, String> params = new HashMap<>();
        final String json="{\"cmd\":\"getMineMessageInfo\",\"uid\":\"" + uid + "\"}";
        params.put("json", json);
        dialog1.show();
        OkHttpUtils.post().url(Constant.THE_SERVER_URL).params(params)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ToastUtils.makeText(context, e.getMessage());
                message_list.refreshComplete();
                dialog1.dismiss();
            }
            @Override
            public void onResponse(String response, int id) {
                Gson gson = new Gson();
                dialog1.dismiss();
                message_list.refreshComplete();
                MyMessageBean myMessageBean = gson.fromJson(response, MyMessageBean.class);
                if (myMessageBean.result.equals("1")) {
                    ToastUtils.makeText(context, myMessageBean.resultNote);
                    return;
                }
                List<MyMessageBean.messageList> messageList = myMessageBean.messageList;
                mList.addAll(messageList);
                mAdapter.notifyDataSetChanged();
                message_list.refreshComplete();
            }
        });
    }
}
