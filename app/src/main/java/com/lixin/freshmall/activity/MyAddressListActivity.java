package com.lixin.freshmall.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.lixin.freshmall.R;
import com.lixin.freshmall.adapter.MyAddressAdapter;
import com.lixin.freshmall.model.Constant;
import com.lixin.freshmall.model.MyAddressBean;
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
 * Create time on  2018/01/22
 * My mailbox is 1403241630@qq.com
 */

public class MyAddressListActivity extends BaseActivity{
    private ListView address_list;
    private List<MyAddressBean.AddressList> mList;
    private String uid,storeId;
    private MyAddressAdapter mAdapter;
    private int type = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_address_list);
        hideBack(1);
        setTitleText("地址管理");
        setRightText("添加");
        uid = SPUtil.getString(context,"uid");
        storeId = SPUtil.getString(context,"storeId");
        type = getIntent().getIntExtra("type",0);
        mList = new ArrayList<>();
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mList.clear();
        getdata();
    }

    private void initView() {
        address_list = findViewById(R.id.address_list);
        mAdapter = new MyAddressAdapter(context,mList);
        address_list.setAdapter(mAdapter);
        address_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (type == 1){
                    if (mList.get(position).getInGround().equals("0")){
                        Intent intent = new Intent();
                        intent.putExtra("userName",mList.get(position).getUserName());
                        intent.putExtra("phone",mList.get(position).getPhone());
                        intent.putExtra("address",mList.get(position).getAddress());
                        intent.putExtra("addressId",mList.get(position).getAddressId());
                        setResult(1003,intent);
                        finish();
                    }else {
                        ToastUtils.makeText(context,"该地址不在本门店配送范围");
                    }

                }
            }
        });
    }
    private void getdata() {
        Map<String, String> params = new HashMap<>();
        final String json="{\"cmd\":\"getMyAddress\",\"uid\":\"" + uid + "\",\"storeId\":\""+storeId+"\"}";
        params.put("json", json);
        Log.i("dzgl-----", "getdata: " + json);
        dialog1.show();
        OkHttpUtils.post().url(Constant.THE_SERVER_URL).params(params).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ToastUtils.makeText(context, e.getMessage());
                dialog1.dismiss();
            }
            @Override
            public void onResponse(String response, int id) {
                Gson gson = new Gson();
                dialog1.dismiss();
                MyAddressBean myAddressBean = gson.fromJson(response, MyAddressBean.class);
                if (myAddressBean.getResult().equals("1")) {
                    ToastUtils.makeText(context, myAddressBean.getResultNote());
                    return;
                }
                List<MyAddressBean.AddressList> addressList = myAddressBean.getAddressList();
                mList.addAll(addressList);
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_base_rightText:
                MyApplication.openActivity(MyAddressListActivity.this,AddAddressActivity.class);
                break;
        }
    }
}
