package com.lixin.freshmall.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.google.gson.Gson;
import com.lixin.freshmall.R;
import com.lixin.freshmall.adapter.SelectStoreAdapter;
import com.lixin.freshmall.listenter.MyLocationListener;
import com.lixin.freshmall.model.Constant;
import com.lixin.freshmall.model.StoreBean;
import com.lixin.freshmall.okhttp.OkHttpUtils;
import com.lixin.freshmall.okhttp.budiler.StringCallback;
import com.lixin.freshmall.uitls.AppManager;
import com.lixin.freshmall.uitls.PermissionUtil;
import com.lixin.freshmall.uitls.SPUtil;
import com.lixin.freshmall.uitls.ToastUtils;

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

public class SelectStoreActivity extends BaseActivity implements MyLocationListener.onLocationListener{
    private String TownId;
    private double longitude,latitude;
    public LocationClient mLocationClient = null;
    public MyLocationListener myListener = new MyLocationListener();
    private static final String[] PERMISSIONS_CONTACT = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE};
    private static final int REQUEST_CONTACTS = 1000;
    private ListView select_list;
    private SelectStoreAdapter mAdapter;
    private List<StoreBean.StoreList> mList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_province_ciry_area_store);
        hideBack(2);
        setTitleText("门店选择");
        mList = new ArrayList<>();
        TownId = getIntent().getStringExtra("TownId");
        myListener.setLocationListener(this);
        mLocationClient = new LocationClient(MyApplication.getContext());
        mLocationClient.registerLocationListener(myListener);//
        initLocation();
        initView();
        if (Build.VERSION.SDK_INT >= 23) {
            showSetPermission();
        } else {
            mLocationClient.start();
        }
    }

    private void initView() {
        select_list = findViewById(R.id.select_list);
        mAdapter = new SelectStoreAdapter(context,mList);
        select_list.setAdapter(mAdapter);
        select_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SPUtil.putString(context,"storeName",mList.get(position).getStoreName());
                SPUtil.putString(context,"storeAddress",mList.get(position).getStoreAderss());
                SPUtil.putString(context,"storePhone",mList.get(position).getStorePhone());
                SPUtil.putString(context,"storeId",mList.get(position).getStoreId());
                SPUtil.putString(context,"TownId",TownId);
                SPUtil.putString(context,"issend",mList.get(position).getIssend());
                AppManager.finishAllActivity();
                Intent intent= new Intent(SelectStoreActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span = 1000;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        mLocationClient.setLocOption(option);
    }

    private void showSetPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            requestSetPermissions(select_list);
        } else {
            mLocationClient.start();
        }
    }

    private void requestSetPermissions(View view) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)
                || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_PHONE_STATE)) {
            Snackbar.make(view, "permission_contacts_rationale", Snackbar.LENGTH_INDEFINITE)
                    .setAction("ok", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ActivityCompat.requestPermissions(SelectStoreActivity.this, PERMISSIONS_CONTACT, REQUEST_CONTACTS);
                        }
                    })
                    .show();
        } else {
            ActivityCompat.requestPermissions(SelectStoreActivity.this, PERMISSIONS_CONTACT, REQUEST_CONTACTS);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CONTACTS) {
            if (PermissionUtil.verifyPermissions(grantResults)) {
                mLocationClient.start();
            } else {
                ToastUtils.makeText(context, "获取位置权限失败，请手动开启");
            }
        }
    }

    @Override
    public void onLocation(String city, String rode, double latitude, double longitude, String district) {
        if (city == null) {
            ToastUtils.makeText(context,"当前位置定位失败！");
        } else {
            this.longitude = longitude;
            this.latitude = latitude;
            getdata();
        }
        SPUtil.putString(context,"city",city);
        SPUtil.putString(context,"latitude",latitude+"");
        SPUtil.putString(context,"longitude",longitude+"");
        mLocationClient.stop();
    }

    private void getdata() {
        Map<String,String> params = new HashMap<>();
        final String json = "{\"cmd\":\"getStoreInfo\",\"longitude\":\""+longitude+"\"" +
                ",\"latitude\":\""+latitude+"\",\"townId\":\""+TownId+"\"}";
        params.put("json",json);
        OkHttpUtils.post().url(Constant.THE_SERVER_URL).params(params).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                dialog1.dismiss();
                ToastUtils.makeText(context,e.getMessage());
            }
            @Override
            public void onResponse(String response, int id) {
                Gson gson = new Gson();
                StoreBean storeBean = gson.fromJson(response,StoreBean.class);
                if (storeBean.getResult().equals("1")){
                    ToastUtils.makeText(context,storeBean.getResultNote());
                    return;
                }
                List<StoreBean.StoreList> storeLists = storeBean.getStoreList();
                mList.addAll(storeLists);
                mAdapter.notifyDataSetChanged();
            }
        });
    }
}
