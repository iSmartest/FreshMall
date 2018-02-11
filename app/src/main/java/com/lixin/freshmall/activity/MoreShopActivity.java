package com.lixin.freshmall.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.xrecyclerview.XRecyclerView;
import com.google.gson.Gson;
import com.lixin.freshmall.R;
import com.lixin.freshmall.adapter.MoreHotShopAdapter;
import com.lixin.freshmall.adapter.MoreNewShopAdapter;
import com.lixin.freshmall.listenter.RecyclerItemTouchListener;
import com.lixin.freshmall.model.Constant;
import com.lixin.freshmall.model.MoreShopBean;
import com.lixin.freshmall.okhttp.OkHttpUtils;
import com.lixin.freshmall.okhttp.budiler.StringCallback;
import com.lixin.freshmall.uitls.DividerGridItemDecoration;
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

public class MoreShopActivity extends BaseActivity {
    private int flag;
    private XRecyclerView more_shop_grid;
    private MoreHotShopAdapter mAdapter;
    private MoreNewShopAdapter moreNewShopAdapter;
    private int nowPage = 1;
    private List<MoreShopBean.moreCommoditys> mList = new ArrayList<>();
    private String TownId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_shop);
        Intent intent = getIntent();
        flag = Integer.parseInt(intent.getStringExtra("flag"));
        TownId = SPUtil.getString(context, "TownId");
        hideBack(2);
        if (flag == 0) {
            setTitleText("促销专区");
        } else if (flag == 1) {
            setTitleText("热销专区");
        } else if (flag == 2) {
            setTitleText("新品专区");
        }
        initView();
        getdata(true);
    }

    private void initView() {
        more_shop_grid = findViewById(R.id.more_shop_grid);
        if (flag == 1) {
            more_shop_grid.setLayoutManager(new GridLayoutManager(context, 3));
            more_shop_grid.addItemDecoration(new DividerGridItemDecoration(context,3));
            mAdapter = new MoreHotShopAdapter(context, mList);
            more_shop_grid.setAdapter(mAdapter);
        } else {
            more_shop_grid.setLayoutManager(new GridLayoutManager(context, 2));
            more_shop_grid.addItemDecoration(new DividerGridItemDecoration(context,2));
            moreNewShopAdapter = new MoreNewShopAdapter(context, mList);
            more_shop_grid.setAdapter(moreNewShopAdapter);
        }
        more_shop_grid.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                nowPage = 1;
                mList.clear();
                if (flag == 1){
                    mAdapter.notifyDataSetChanged();
                }else {
                    moreNewShopAdapter.notifyDataSetChanged();
                }
                getdata(false);
            }

            @Override
            public void onLoadMore() {
                nowPage++;
                getdata(false);
            }
        });
        more_shop_grid.addOnItemTouchListener(new RecyclerItemTouchListener(more_shop_grid) {
            @Override
            public void onItemClick(RecyclerView.ViewHolder vh) {
                int position = vh.getAdapterPosition() - 1;
                if (position < 0 | position >= mList.size()) {
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putString("rotateid", mList.get(position).commodityid);
                bundle.putString("rotateIcon", mList.get(position).commodityIcon);
                MyApplication.openActivity(context, ShopDecActivity.class, bundle);
            }
        });
    }

    private void getdata(boolean isShowLoadDialog) {
        Map<String,String> params = new HashMap<>();
        final String json = "{\"cmd\":\"getMoreCommoditys\",\"nowPage\":\"" + nowPage + "\",\"moreType\":\"" + flag + "\",\"city\":\"" + TownId + "\"}";
        params.put("json", json);
        if (isShowLoadDialog){
            dialog1.show();
        }
        OkHttpUtils.post().url(Constant.THE_SERVER_URL).params(params).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ToastUtils.makeText(context, e.getMessage());
                if (dialog1 != null){
                    dialog1.dismiss();
                }
            }

            @Override
            public void onResponse(String response, int id) {
                Gson gson = new Gson();
                if (dialog1 != null){
                    dialog1.dismiss();
                }
                MoreShopBean moreShopBean = gson.fromJson(response, MoreShopBean.class);
                if (moreShopBean.result.equals("1")) {
                    ToastUtils.makeText(context, moreShopBean.resultNote);
                    return;
                }
                List<MoreShopBean.moreCommoditys> commodityslist = moreShopBean.moreCommoditys;
                mList.addAll(commodityslist);
                if (flag == 1){
                    mAdapter.notifyDataSetChanged();
                }else {
                    moreNewShopAdapter.notifyDataSetChanged();
                }
                more_shop_grid.refreshComplete();

                if (moreShopBean.getTotalPage() < nowPage) {
                    ToastUtils.makeText(context, "没有更多了");
                    more_shop_grid.noMoreLoading();
                }
            }
        });
    }
}
