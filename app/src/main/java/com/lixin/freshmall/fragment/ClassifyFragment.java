package com.lixin.freshmall.fragment;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.xrecyclerview.XRecyclerView;
import com.google.gson.Gson;
import com.lixin.freshmall.R;
import com.lixin.freshmall.activity.MyApplication;
import com.lixin.freshmall.activity.SearchShopActivity;
import com.lixin.freshmall.activity.ShopDecActivity;
import com.lixin.freshmall.adapter.FirstAdapter;
import com.lixin.freshmall.adapter.SecondAdapter;
import com.lixin.freshmall.dialog.ProgressDialog;
import com.lixin.freshmall.listenter.RecyclerItemTouchListener;
import com.lixin.freshmall.model.ClassBean;
import com.lixin.freshmall.model.Constant;
import com.lixin.freshmall.model.HomeBean;
import com.lixin.freshmall.okhttp.OkHttpUtils;
import com.lixin.freshmall.okhttp.budiler.StringCallback;
import com.lixin.freshmall.uitls.SPUtil;
import com.lixin.freshmall.uitls.StatusBarUtil;
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

public class ClassifyFragment extends BaseFragment {
    private View view;
    private EditText keySearch;
    private ImageView mSearch;
    private ListView first_list;
    private FirstAdapter firstAdapter;
    private SecondAdapter secondAdapter;
    private XRecyclerView second_list;
    private int nowPage = 1,totalPage = 1;
    private int defaultItem = 0;
    private List<HomeBean.classifyBottom> mFirstList = new ArrayList<>();
    private List<ClassBean.Commoditys> mSecondList;
    private String TownId,meunid;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.lixin.freshmall.classify.change");
        getActivity().registerReceiver(mAllBroad, intentFilter);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_class,container,false);
        if (Constant.mClassifyBottom != null && !Constant.mClassifyBottom.isEmpty() && Constant.mClassifyBottom.size()>0){
            mFirstList.addAll(Constant.mClassifyBottom);
            meunid = mFirstList.get(MyApplication.defaultItem).getMeunid();
        }
        mSecondList = new ArrayList<>();
        TownId = SPUtil.getString(context,"TownId");
        initView();
        getdata(true);
        return view;
    }

    private void getdata(boolean isShowDialog) {
        final Dialog progressDialog = ProgressDialog.createLoadingDialog(context, "加载中.....");
        Map<String, String> params = new HashMap<>();
        final String json="{\"cmd\":\"getClassifyListInfo\",\"city\":\"" + TownId +"\",\"meunid\":\""
                + meunid + "\",\"nowPage\":\"" + nowPage + "\"}";
        params.put("json", json);
        if (isShowDialog){
            progressDialog.show();
        }
        OkHttpUtils.post().url(Constant.THE_SERVER_URL).params(params).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ToastUtils.makeText(context, e.getMessage());
                progressDialog.dismiss();
                second_list.refreshComplete();
            }
            @Override
            public void onResponse(String response, int id) {
                Gson gson = new Gson();
                progressDialog.dismiss();
                ClassBean mClassBean = gson.fromJson(response, ClassBean.class);
                if (mClassBean.getResult().equals("1")) {
                    ToastUtils.makeText(context, mClassBean.getResultNote());
                    return;
                }
                totalPage = mClassBean.getTotalPage();
                List<ClassBean.Commoditys> mCommoditys = mClassBean.getCommoditys();
                if (mCommoditys != null && !mCommoditys.isEmpty() && mCommoditys.size() > 0){
                    mSecondList.addAll(mCommoditys);
                    secondAdapter.notifyDataSetChanged();
                    second_list.refreshComplete();
                }
            }
        });
    }

    private void initView() {
        RelativeLayout mToolbar = view.findViewById(R.id.rl_class_toolbar);
        StatusBarUtil.setHeightAndPadding(getActivity(), mToolbar);
        keySearch = view.findViewById(R.id.class_a_key_edt_search);
        keySearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH){
                    if (TextUtils.isEmpty(keySearch.getText().toString().trim())) {
                        ToastUtils.makeText(getActivity(), "请输入关键词");
                    } else {
                        Intent intent = new Intent(getActivity(), SearchShopActivity.class);
                        intent.putExtra("searchKey", keySearch.getText().toString().trim());
                        startActivity(intent);
                    }
                    return true;
                }
                return false;
            }
        });
        mSearch = view.findViewById(R.id.im_class_search);
        mSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(keySearch.getText().toString().trim())) {
                    ToastUtils.makeText(getActivity(), "请输入关键词");
                } else {
                    Intent intent = new Intent(getActivity(), SearchShopActivity.class);
                    intent.putExtra("searchKey", keySearch.getText().toString().trim());
                    startActivity(intent);
                }
            }
        });
        first_list = view.findViewById(R.id.first_list);
        first_list.setVerticalScrollBarEnabled(false);
        firstAdapter = new FirstAdapter(context,mFirstList);
        firstAdapter.setDefSelect(MyApplication.defaultItem);
        firstAdapter.notifyDataSetChanged();
        first_list.setAdapter(firstAdapter);
        first_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                firstAdapter.setDefSelect(position);
                meunid = mFirstList.get(position).getMeunid();
                firstAdapter.notifyDataSetChanged();
                nowPage = 1;
                mSecondList.clear();
                secondAdapter.notifyDataSetChanged();
                getdata(true);
            }
        });
        second_list = view.findViewById(R.id.second_list);
        second_list.setLayoutManager(new LinearLayoutManager(getActivity()));
        secondAdapter = new SecondAdapter(context,mSecondList);
        second_list.setAdapter(secondAdapter);
        second_list.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                nowPage = 1;
                mSecondList.clear();
                secondAdapter.notifyDataSetChanged();
                getdata(false);
            }

            @Override
            public void onLoadMore() {
                nowPage++;
                if (totalPage < nowPage) {
                    ToastUtils.makeText(context, "没有更多了");
                    second_list.noMoreLoading();
                    return;
                }
                getdata(false);
            }
        });
        second_list.addOnItemTouchListener(new RecyclerItemTouchListener(second_list) {
            @Override
            public void onItemClick(RecyclerView.ViewHolder vh) {
                int position = vh.getAdapterPosition() - 1;
                if (position < 0 | position >= mSecondList.size()){
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putString("rotateid",mSecondList.get(position).getCommodityid());
                bundle.putString("rotateIcon",mSecondList.get(position).getCommodityIcon());
                MyApplication.openActivity(context,ShopDecActivity.class,bundle);
            }
        });
    }

    private BroadcastReceiver mAllBroad = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {
            defaultItem = intent.getIntExtra("position",0);
            firstAdapter.setDefSelect(defaultItem);
            meunid = mFirstList.get(defaultItem).getMeunid();
            firstAdapter.notifyDataSetChanged();
            nowPage = 1;
            mSecondList.clear();
            secondAdapter.notifyDataSetChanged();
            getdata(true);
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(mAllBroad);
    }
}
