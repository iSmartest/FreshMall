package com.lixin.freshmall.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lixin.freshmall.R;
import com.lixin.freshmall.model.Constant;
import com.lixin.freshmall.model.HomeBean;
import com.lixin.freshmall.model.StoreTimeBean;
import com.lixin.freshmall.okhttp.OkHttpUtils;
import com.lixin.freshmall.okhttp.budiler.StringCallback;
import com.lixin.freshmall.uitls.SPUtil;
import com.lixin.freshmall.uitls.ToastUtils;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by 小火
 * Create time on  2017/01/22
 * My mailbox is 1403241630@qq.com
 */

public class AddAddressActivity extends BaseActivity{
    private String uid,storeId;
    private String userName = "",phone = "",address = "";
    private double longitude,latitude;
    private EditText mName,mPhone,mAddressDec;
    private TextView mAddress;
    private Button mSure;
    private int mStoreDistance;
    private double mStoreLongitude;
    private double mStoreLatitude;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        uid = SPUtil.getString(context,"uid");
        storeId = SPUtil.getString(context,"storeId");
        userName = getIntent().getStringExtra("userName");
        phone = getIntent().getStringExtra("phone");
        address = getIntent().getStringExtra("address");
        hideBack(2);
        setTitleText("添加地址");
        initView();
        getdata();
    }

    private void initView() {
        mName = (EditText) findViewById(R.id.et_user_address_name);
        mPhone = (EditText) findViewById(R.id.et_user_address_phone);
        mAddress = (TextView) findViewById(R.id.et_user_address);
        mAddress.setOnClickListener(this);
        mAddressDec = (EditText) findViewById(R.id.et_user_address_dec);
        mSure = (Button) findViewById(R.id.btn_sure);
        mSure.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
       switch (v.getId()){
           case R.id.et_user_address:
               Bundle bundle = new Bundle();
               bundle.putInt("distance",mStoreDistance);
               bundle.putDouble("longitude",mStoreLongitude);
               bundle.putDouble("latitude",mStoreLatitude);
               MyApplication.openActivityForResult(AddAddressActivity.this,LocationAndPoiSearchActivity.class,bundle,1001);
               break;
           case R.id.btn_sure:
               submit();
               break;
       }
    }

    private void submit() {
        String receiverName = mName.getText().toString().trim();
        String receiverPhone = mPhone.getText().toString().trim();
        String receiverAddress = mAddress.getText().toString().trim();
        String receiverDetailAddress = mAddressDec.getText().toString().trim();

        if (TextUtils.isEmpty(receiverName)){
            ToastUtils.makeText(AddAddressActivity.this,"请输入收货人姓名");
        }else if (TextUtils.isEmpty(receiverPhone)){
            ToastUtils.makeText(AddAddressActivity.this,"请输入电话");
        }else if (TextUtils.isEmpty(receiverAddress)){
            ToastUtils.makeText(AddAddressActivity.this,"请选择区域");
        }else if (TextUtils.isEmpty(receiverDetailAddress)){
            ToastUtils.makeText(AddAddressActivity.this,"请输入详细地址");
        }else
            getReceiverData(receiverName,receiverPhone,receiverAddress + receiverDetailAddress);
    }

    private void getReceiverData(final String receiverName, final String receiverPhone, final String receiverAddress) {
        Map<String, String> params = new HashMap<>();
        final String json = "{\"cmd\":\"addMyAddress\",\"uid\":\"" + uid + "\",\"address\":\"" + receiverAddress + "\"" +
                ",\"userName\":\"" + receiverName + "\",\"phone\":\"" + receiverPhone + "\",\"longitude\":\""+ longitude +"\"" +
                ",\"latitude\":\"" + latitude + "\"}";
        params.put("json", json);
        Log.i("AddAddressActivity", "response: " + json.toString());
        dialog1.show();
        OkHttpUtils.post().url(Constant.THE_SERVER_URL).params(params)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ToastUtils.makeText(context, "网络异常");
                dialog1.dismiss();
            }
            @Override
            public void onResponse(String response, int id) {
                Gson gson = new Gson();
                dialog1.dismiss();
                HomeBean homeBean = gson.fromJson(response, HomeBean.class);
                if (homeBean.getResult().equals("1")) {
                    ToastUtils.makeText(AddAddressActivity.this, homeBean.getResultNote());
                }
                ToastUtils.makeText(AddAddressActivity.this,"添加地址成功！");
                Intent intent = new Intent();
                setResult(1002,intent);
                finish();
            }
        });
    }

    private void getdata() {
        Map<String,String> params = new HashMap<>();
        String json = "{\"cmd\":\"getStoreInfoTime\",\"storeId\":\""+ storeId +"\"}";
        params.put("json",json);
        OkHttpUtils.post().url(Constant.THE_SERVER_URL).params(params).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ToastUtils.makeText(context,e.getMessage());
            }
            @Override
            public void onResponse(String response, int id) {
                Log.i("getStoreInfoTime", "onResponse: " +response );
                Gson gson = new Gson();
                StoreTimeBean storeTimeBean = gson.fromJson(response,StoreTimeBean.class);
                if (storeTimeBean.getResult().equals("1")){
                    ToastUtils.makeText(context,storeTimeBean.getResultNote());
                    return;
                }
                mStoreDistance = storeTimeBean.getDistance();
                mStoreLongitude = storeTimeBean.getLongitude();
                mStoreLatitude = storeTimeBean.getLatitude();
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001){
            if (resultCode == 1007){
                String address = data.getStringExtra("address");
                String name = data.getStringExtra("name");
                longitude = data.getDoubleExtra("longitude",0.0);
                latitude = data.getDoubleExtra("latitude",0.0);
                mAddress.setText(address+"("+name+")");
            }
        }
    }
}
