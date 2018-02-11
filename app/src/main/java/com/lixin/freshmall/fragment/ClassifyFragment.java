package com.lixin.freshmall.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
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
import com.lixin.freshmall.model.ClassBean;
import com.lixin.freshmall.activity.MyApplication;
import com.lixin.freshmall.activity.SearchShopActivity;
import com.lixin.freshmall.activity.ShopDecActivity;
import com.lixin.freshmall.adapter.FirstAdapter;
import com.lixin.freshmall.adapter.SecondAdapter;
import com.lixin.freshmall.listenter.RecyclerItemTouchListener;
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
    private int defClass1;
    private ListView firstList;
    private FirstAdapter firstAdapter;
    private SecondAdapter secondAdapter;
    private XRecyclerView secondList;
    private int nowPage = 1;
    private List<HomeBean.classifyBottom> mFirstList = new ArrayList<>();
    private List<ClassBean.Commoditys> mSecondList;
    private String uid,TownId,meunid;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_class,container,false);
        if (Constant.mClassifyBottom != null && !Constant.mClassifyBottom.isEmpty()){
            mFirstList.addAll(Constant.mClassifyBottom);
            meunid = mFirstList.get(MyApplication.secondId).getMeunid();
        }
        mSecondList = new ArrayList<>();
        uid = SPUtil.getString(context,"uid");
        TownId = SPUtil.getString(context,"TownId");
        initView();
        getdata();
        return view;
    }

    private void getdata() {
        Map<String, String> params = new HashMap<>();
        final String json="{\"cmd\":\"getClassifyListInfo\",\"city\":\"" + TownId +"\",\"meunid\":\""
                + meunid + "\",\"nowPage\":\"" + nowPage + "\"}";
        params.put("json", json);
        Log.i("getClassifyListInfo", "getdata: " + json);
        dialog.show();
        OkHttpUtils.post().url(Constant.THE_SERVER_URL).params(params)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ToastUtils.makeText(context, e.getMessage());
                dialog.dismiss();
                secondList.refreshComplete();
            }
            @Override
            public void onResponse(String response, int id) {
                Log.i("getClassifyListInfo", "getdata: " + response);
                Gson gson = new Gson();
                dialog.dismiss();
                ClassBean mClassBean = gson.fromJson(response, ClassBean.class);
                if (mClassBean.getResult().equals("1")) {
                    ToastUtils.makeText(context, mClassBean.getResultNote());
                    return;
                }
                List<ClassBean.Commoditys> mCommoditys = mClassBean.getCommoditys();
                mSecondList.addAll(mCommoditys);
                secondAdapter.notifyDataSetChanged();
                secondList.refreshComplete();
                if (mClassBean.getTotalPage() < nowPage) {
                    ToastUtils.makeText(context, "没有更多了");
                    secondList.noMoreLoading();
                    return;
                }
            }
        });
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden) {
            if (MyApplication.temp == 1){
                if (mFirstList !=null && !mFirstList.isEmpty()) {
                    defClass1 = MyApplication.secondId;
                    firstAdapter.setDefSelect(defClass1);
                    nowPage = 1;
                    mSecondList.clear();
                    secondAdapter.notifyDataSetChanged();
                    meunid = mFirstList.get(defClass1).getMeunid();
                    getdata();
                }
                MyApplication.temp = 0;
            }
            if (mFirstList != null && !mFirstList.isEmpty()){
                return;
            }else {
                if (Constant.mClassifyBottom != null && !Constant.mClassifyBottom.isEmpty()){
                    mFirstList.addAll(Constant.mClassifyBottom);
                    firstAdapter.notifyDataSetChanged();
                    meunid = mFirstList.get(MyApplication.secondId).getMeunid();
                    getdata();
                }
            }
        }
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
        firstList = view.findViewById(R.id.first_list);
        firstList.setVerticalScrollBarEnabled(false);
        firstAdapter = new FirstAdapter(context,mFirstList);
        firstAdapter.setDefSelect(MyApplication.secondId);
        firstAdapter.notifyDataSetChanged();
        firstList.setAdapter(firstAdapter);
        firstList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                firstAdapter.setDefSelect(position);
                MyApplication.secondId = position;
                firstAdapter.notifyDataSetChanged();
                nowPage = 1;
                meunid = mFirstList.get(position).getMeunid();
                mSecondList.clear();
                secondAdapter.notifyDataSetChanged();
                getdata();
                secondList.refreshComplete();
            }
        });
        secondList = view.findViewById(R.id.second_list);
        secondList.setLayoutManager(new LinearLayoutManager(getActivity()));
        secondList.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                nowPage = 1;
                mSecondList.clear();
                secondAdapter.notifyDataSetChanged();
                getdata();
                secondList.refreshComplete();
            }

            @Override
            public void onLoadMore() {
                nowPage++;
                getdata();
                secondAdapter.notifyDataSetChanged();
                secondList.refreshComplete();
            }
        });
        secondAdapter = new SecondAdapter(context,mSecondList);
        secondList.setAdapter(secondAdapter);
        secondList.addOnItemTouchListener(new RecyclerItemTouchListener(secondList) {
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
}
