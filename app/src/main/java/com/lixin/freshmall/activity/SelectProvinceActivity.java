package com.lixin.freshmall.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.lixin.freshmall.R;
import com.lixin.freshmall.adapter.SelectProvinceAdapter;
import com.lixin.freshmall.model.CityInfoBean;
import com.lixin.freshmall.model.Constant;
import com.lixin.freshmall.okhttp.OkHttpUtils;
import com.lixin.freshmall.okhttp.budiler.StringCallback;
import com.lixin.freshmall.uitls.ToastUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by 小火
 * Create time on  2017/12/9
 * My mailbox is 1403241630@qq.com
 */

public class SelectProvinceActivity extends BaseActivity {
    private ListView select_list;
    private List<CityInfoBean.ProvinceList> mList;
    private SelectProvinceAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_province_ciry_area_store);
        mList = new ArrayList<>();
        hideBack(3);
        setTitleText("门店选择");
        initView();
        getdata();
    }

    private void initView() {
        select_list = findViewById(R.id.select_list);
        mAdapter = new SelectProvinceAdapter(context,mList);
        select_list.setAdapter(mAdapter);
        select_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("city",(Serializable) mList.get(position).getCityList());
                MyApplication.openActivity(context,SelectCityActivity.class,bundle);
            }
        });
    }

    private void getdata() {
        Map<String,String> params = new HashMap<>();
        final String json = "{\"cmd\":\"getCityInfo\"}";
        params.put("json",json);
        dialog1.show();
        OkHttpUtils.post().url(Constant.THE_SERVER_URL).params(params).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                dialog1.dismiss();
                ToastUtils.makeText(context,e.getMessage());
            }

            @Override
            public void onResponse(String response, int id) {
                Gson gson = new Gson();
                dialog1.dismiss();
                CityInfoBean cityInfoBean = gson.fromJson(response,CityInfoBean.class);
                if (cityInfoBean.getResult().equals("1")){
                    ToastUtils.makeText(context,cityInfoBean.getResultNote());
                    return;
                }
                List<CityInfoBean.ProvinceList> provinceLists = cityInfoBean.getProvinceList();
                mList.addAll(provinceLists);
                mAdapter.notifyDataSetChanged();
            }
        });
    }

}
