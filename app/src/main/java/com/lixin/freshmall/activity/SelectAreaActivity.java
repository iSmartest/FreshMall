package com.lixin.freshmall.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.lixin.freshmall.R;
import com.lixin.freshmall.adapter.SelectAreaAdapter;
import com.lixin.freshmall.model.CityInfoBean;
import com.lixin.freshmall.uitls.SPUtil;

import java.util.ArrayList;

/**
 * Created by 小火
 * Create time on  2017/12/9
 * My mailbox is 1403241630@qq.com
 */

public class SelectAreaActivity extends BaseActivity {
    private ListView select_list;
    private ArrayList<? extends CityInfoBean.ProvinceList.CityList.TownList> mList;
    private SelectAreaAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_province_ciry_area_store);
        mList = getIntent().getParcelableArrayListExtra("area");
        hideBack(2);
        setTitleText("门店选择");
        initView();
    }

    private void initView() {
        select_list = findViewById(R.id.select_list);
        mAdapter = new SelectAreaAdapter(context,mList);
        select_list.setAdapter(mAdapter);
        select_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putString("TownId", mList.get(position).getTownId());
                SPUtil.putString(context,"Town",mList.get(position).getTown());
                MyApplication.openActivity(context,SelectStoreActivity.class,bundle);
            }
        });
    }
}