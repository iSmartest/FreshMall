package com.lixin.freshmall.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.xrecyclerview.XRecyclerView;
import com.google.gson.Gson;
import com.lixin.freshmall.R;
import com.lixin.freshmall.adapter.ClassListAdapter;
import com.lixin.freshmall.listenter.RecyclerItemTouchListener;
import com.lixin.freshmall.model.ClassListBean;
import com.lixin.freshmall.model.Constant;
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
 * Create time on  2017/6/1
 * My mailbox is 1403241630@qq.com
 */

public class SearchShopActivity extends BaseActivity{
    private String searchKey;
    private EditText mSearch;
    private int nowPage = 1;
    private int totalPage = 1;
    private XRecyclerView search_list;
    private List<ClassListBean.commoditys> mList;
    private ClassListAdapter classListAdapter;
    private LinearLayout mEmpty;
    private String TownId ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_shop);
        Intent intent = getIntent();
        searchKey = intent.getStringExtra("searchKey");
        TownId = SPUtil.getString(context,"TownId");
        mList = new ArrayList<>();
        hideBack(4);
        getdata();
        initView();
    }
    private void initView() {
        mSearch = findViewById(R.id.a_key_edt_search);
        mSearch.setText(searchKey);
        mEmpty = findViewById(R.id.linear_empty);
        mSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH){
                    if (TextUtils.isEmpty(mSearch.getText().toString().trim())){
                        ToastUtils.makeText(SearchShopActivity.this,"请输入商品名称");
                    }else {
                        searchKey = mSearch.getText().toString().trim();
                        mList.clear();
                        getdata();
                    }
                    return true;
                }
                return false;
            }
        });
        search_list = findViewById(R.id.search_list);
        search_list.setLayoutManager(new LinearLayoutManager(this));
        classListAdapter = new ClassListAdapter(context,mList);
        search_list.setAdapter(classListAdapter);
        search_list.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                nowPage = 1;
                mList.clear();
                getdata();
                classListAdapter.notifyDataSetChanged();
                search_list.refreshComplete();
            }

            @Override
            public void onLoadMore() {
                nowPage++;
                if (nowPage < totalPage){
                    getdata();
                    classListAdapter.notifyDataSetChanged();
                    search_list.refreshComplete();
                }else {
                    ToastUtils.makeText(context, "没有更多了");
                    search_list.noMoreLoading();
                    return;
                }

            }
        });

        search_list.addOnItemTouchListener(new RecyclerItemTouchListener(search_list) {
            @Override
            public void onItemClick(RecyclerView.ViewHolder vh) {
                int position = vh.getAdapterPosition() - 1;
                if (position < 0 | position >= mList.size()){
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putString("rotateid",mList.get(position).commodityid);
                bundle.putString("rotateIcon",mList.get(position).commodityIcon);
                MyApplication.openActivity(context,ShopDecActivity.class,bundle);
            }
        });
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.im_search:
                searchKey = mSearch.getText().toString().trim();
                if (TextUtils.isEmpty(searchKey)){
                    ToastUtils.makeText(SearchShopActivity.this,"请输入商品名称");
                }else {
                    mList.clear();
                    nowPage = 1;
                    getdata();
                }
                break;
        }
    }
    private void getdata() {
        Map<String, String> params = new HashMap<>();
        final String json="{\"cmd\":\"getSerachCommodityListInfo\",\"nowPage\":\"" + nowPage +"\",\"searchKey\":\""
                + searchKey + "\",\"city\":\""+TownId+"\"}";
        params.put("json", json);
        Log.i("SearchShopActivity", "getdata: " + json);
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
                Log.i("SearchShopActivity", "onResponse: " + response);
                Gson gson = new Gson();
                dialog1.dismiss();
                ClassListBean classListBean = gson.fromJson(response, ClassListBean.class);
                if (classListBean.result.equals("1")) {
                    ToastUtils.makeText(context, classListBean.resultNote);
                    return;
                }
                List<ClassListBean.commoditys> commodityslist = classListBean.getCommoditys();
                totalPage = classListBean.totalPage;
                if (commodityslist != null && !commodityslist.isEmpty()){
                    mEmpty.setVisibility(View.GONE);
                    search_list.setVisibility(View.VISIBLE);
                    mList.addAll(commodityslist);
                    classListAdapter.notifyDataSetChanged();
                }else {
                    mEmpty.setVisibility(View.VISIBLE);
                    search_list.setVisibility(View.GONE);
                }
                if (totalPage == 1){
                    search_list.noMoreLoading();

                }
            }
        });
    }
}
