package com.lixin.freshmall.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lixin.freshmall.R;
import com.lixin.freshmall.adapter.InnerAdapter;
import com.lixin.freshmall.model.CodeBean;
import com.lixin.freshmall.model.Constant;
import com.lixin.freshmall.okhttp.OkHttpUtils;
import com.lixin.freshmall.okhttp.budiler.StringCallback;
import com.lixin.freshmall.uitls.SPUtil;
import com.lixin.freshmall.uitls.StatusBarUtil;
import com.lixin.freshmall.uitls.ToastUtils;
import com.lixin.freshmall.view.SwipeFlingAdapterView;

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

public class CodeFragment extends BaseFragment implements SwipeFlingAdapterView.onFlingListener,
        SwipeFlingAdapterView.OnItemClickListener {
    private View view;
    private int cardWidth;
    private int cardHeight;
    private LinearLayout mYesCode;
    private TextView mPageNum;
    private LinearLayout mNoCode;
    private SwipeFlingAdapterView swipeView;
    private List<CodeBean.CodeList> mList;
    private InnerAdapter adapter;
    private String uid,townId;
    private int totalNum;
    private boolean isGetData = true;
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        //注册广播
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.freshmall.code.changed");
        getActivity().registerReceiver(mAllBroad, intentFilter);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_code,container,false);
        uid = SPUtil.getString(context,"uid");
        townId = SPUtil.getString(context,"TownId");
        mList = new ArrayList<>();
        initView();
        getdata();
        return view;

    }


    private void initView() {
        RelativeLayout mToolbar = view.findViewById(R.id.rl_code_toolbar);
        StatusBarUtil.setHeightAndPadding(getActivity(), mToolbar);
        DisplayMetrics dm = getResources().getDisplayMetrics();
        float density = dm.density;
        cardWidth = (int) (dm.widthPixels - (2 * 18 * density));
        cardHeight = (int) (dm.heightPixels - (338 * density));
        mYesCode = view.findViewById(R.id.linear_yes_code);
        mNoCode = view.findViewById(R.id.text_no_code);
        mPageNum = view.findViewById(R.id.text_page_num);
        swipeView = view.findViewById(R.id.swipe_view);
        if (swipeView != null) {
            swipeView.setIsNeedSwipe(true);
            swipeView.setFlingListener(this);
            swipeView.setOnItemClickListener(this);
            adapter = new InnerAdapter(context,mList);
            swipeView.setAdapter(adapter);
        }
    }

    private void getdata() {
        Map<String,String> params = new HashMap<>();
        final String json = "{\"cmd\":\"getGoodsCode\",\"uid\":\""+uid+"\",\"townId\":\"" + townId + "\"}";
        params.put("json",json);
        dialog.show();
        OkHttpUtils.post().url(Constant.THE_SERVER_URL).params(params).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                dialog.dismiss();
                ToastUtils.makeText(context,e.getMessage());
            }

            @Override
            public void onResponse(String response, int id) {
                Log.i("getGoodsCode", "onResponse: " + response);
                Gson gson = new Gson();
                dialog.dismiss();
                CodeBean mCodeBean = gson.fromJson(response,CodeBean.class);
                if (mCodeBean.getResult().equals("1")){
                    ToastUtils.makeText(context,mCodeBean.getResultNote());
                    return;
                }
                List<CodeBean.CodeList> codeLists = mCodeBean.getCodeList();
                if(codeLists!=null && !codeLists.isEmpty()) {
                    isGetData = false;
                    mNoCode.setVisibility(View.GONE);
                    mYesCode.setVisibility(View.VISIBLE);
                    mList.addAll(codeLists);
                    adapter.notifyDataSetChanged();
                    totalNum = mList.size();
                    mPageNum.setText((totalNum - adapter.getCount()+1)+"/"+totalNum);
                }else {
                    mNoCode.setVisibility(View.VISIBLE);
                    mYesCode.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void onItemClicked(MotionEvent event, View v, Object dataObject) {

    }

    @Override
    public void removeFirstObjectInAdapter() {
        adapter.remove(0);
        mPageNum.setText((totalNum - adapter.getCount()+1)+"/"+totalNum);
    }

    @Override
    public void onLeftCardExit(Object dataObject) {

    }

    @Override
    public void onRightCardExit(Object dataObject) {

    }

    @Override
    public void onAdapterAboutToEmpty(int itemsInAdapter) {
        if (isGetData){
            return;
        }else {
            if (itemsInAdapter == 0) {
                getdata();
            }
        }
    }

    @Override
    public void onScroll(float progress, float scrollXProgress) {

    }

    private BroadcastReceiver mAllBroad = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {
            //接到广播通知后刷新数据源
            mList.clear();
            getdata();
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(mAllBroad);
    }
}
