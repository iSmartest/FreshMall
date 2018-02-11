package com.lixin.freshmall.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.lixin.freshmall.R;
import com.lixin.freshmall.adapter.SelectCityAdapter;
import com.lixin.freshmall.model.CityInfoBean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by 小火
 * Create time on  2017/12/9
 * My mailbox is 1403241630@qq.com
 */

public class SelectCityActivity extends BaseActivity {
    private ListView select_list;
    private ArrayList<? extends CityInfoBean.ProvinceList.CityList> mList;
    private SelectCityAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_province_ciry_area_store);
        mList = getIntent().getParcelableArrayListExtra("city");
        hideBack(2);
        setTitleText("门店选择");
        initView();
    }

    private void initView() {
        select_list = findViewById(R.id.select_list);
        mAdapter = new SelectCityAdapter(context,mList);
        select_list.setAdapter(mAdapter);
        select_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("area",(Serializable) mList.get(position).getTownList());
                MyApplication.openActivity(context,SelectAreaActivity.class,bundle);
            }
        });
    }
}