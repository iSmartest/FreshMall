package com.lixin.freshmall.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;

import com.example.xrecyclerview.XRecyclerView;
import com.google.gson.Gson;
import com.lixin.freshmall.R;
import com.lixin.freshmall.adapter.ShopEvaluateAdapter;
import com.lixin.freshmall.model.Constant;
import com.lixin.freshmall.model.EvaluateBean;
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
 * Create time on  2017/12/6
 * My mailbox is 1403241630@qq.com
 */

public class ShopEvaluateActivity extends BaseActivity {
    private XRecyclerView evaluate_list;
    private String rotateid;
    private ShopEvaluateAdapter mAdapter;
    private int nowPage = 1;
    private List<EvaluateBean.CommentList> mList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_evaluate);
        rotateid = getIntent().getStringExtra("rotateid");
        mList = new ArrayList<>();
        hideBack(2);
        setTitleText("商品评价");
        initView();
        getdata();
    }

    private void initView() {
        evaluate_list = findViewById(R.id.evaluate_list);
        evaluate_list.setLayoutManager(new LinearLayoutManager(ShopEvaluateActivity.this));
        evaluate_list.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                nowPage = 1;
                mList.clear();
                getdata();
                mAdapter.notifyDataSetChanged();
                evaluate_list.refreshComplete();
            }

            @Override
            public void onLoadMore() {
                nowPage++;
                getdata();
                mAdapter.notifyDataSetChanged();
                evaluate_list.refreshComplete();
            }
        });
        mAdapter = new ShopEvaluateAdapter(context, mList);
        evaluate_list.setAdapter(mAdapter);
    }


    private void getdata() {
        Map<String, String> params = new HashMap<>();
        final String json="{\"cmd\":\"getCommodityComment\",\"commodityid\":\"" + rotateid +"\",\"nowPage\":\"" + nowPage + "\"}";
        params.put("json", json);
        Log.i("WaitPaymentFragment", "onResponse: " + json);
        dialog1.show();
        OkHttpUtils.post().url(Constant.THE_SERVER_URL).params(params)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ToastUtils.makeText(context, e.getMessage());
                dialog1.dismiss();
                evaluate_list.refreshComplete();
            }
            @Override
            public void onResponse(String response, int id) {
                Log.i("WaitPaymentFragment", "onResponse: " + response);
                Gson gson = new Gson();
                dialog1.dismiss();
                EvaluateBean myEvaluateBean = gson.fromJson(response, EvaluateBean.class);
                if (myEvaluateBean.getResult().equals("1")) {
                    ToastUtils.makeText(context, myEvaluateBean.getResultNote());
                    return;
                }
                List<EvaluateBean.CommentList> commentList = myEvaluateBean.getCommentList();
                mList.addAll(commentList);
                evaluate_list.refreshComplete();
                if (myEvaluateBean.getTotalPage() < nowPage) {
                    ToastUtils.makeText(context, "没有更多了");
                    evaluate_list.noMoreLoading();
                    return;
                }
            }
        });
    }
}
