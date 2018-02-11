package com.lixin.freshmall.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lixin.freshmall.R;
import com.lixin.freshmall.dialog.ErrorDialog;
import com.lixin.freshmall.model.Constant;
import com.lixin.freshmall.model.UpdateBean;
import com.lixin.freshmall.okhttp.OkHttpUtils;
import com.lixin.freshmall.okhttp.budiler.StringCallback;
import com.lixin.freshmall.uitls.GlobalMethod;
import com.lixin.freshmall.uitls.ToastUtils;
import com.lixin.freshmall.uitls.UpdateManager;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by 小火
 * Create time on  2017/11/9
 * My mailbox is 1403241630@qq.com
 */

public class UpdateActivity extends BaseActivity{
    private TextView mIntroduce,tvRight, tvVersion,tvVersionName;
    private String updataAddress,versionName,descc;
    private int versionCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        hideBack(2);
        setTitleText("版本更新");
        initView();
        getdata();
    }



    private void initView() {
        findViewById(R.id.a_about_lay_rate).setOnClickListener(this);
        findViewById(R.id.a_about_check_version).setOnClickListener(this);
        mIntroduce = findViewById(R.id.text_lay_introduce_dec);
        tvRight = findViewById(R.id.a_about_tv_right);
        tvVersion = findViewById(R.id.a_about_tv_version);
        tvVersionName = findViewById(R.id.a_about_version_name);

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        tvRight.setText(String.format(getString(R.string.company_copyright), year));
        tvVersion.setText(String.format(getString(R.string.version_format), GlobalMethod.getVersionName(UpdateActivity.this)));
    }

    private void getdata() {
        Map<String,String> params = new HashMap<>();
        final String json = "{\"cmd\":\"getversion\"}";
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
                Log.i("response", "onResponse: " + response);
                Gson gson = new Gson();
                dialog1.dismiss();
                UpdateBean mUpdateBean = gson.fromJson(response,UpdateBean.class);
                if (mUpdateBean.getResult().equals("1")){
                    ToastUtils.makeText(context,mUpdateBean.getResultNote());
                    return;
                }
                versionCode = mUpdateBean.getVersionNumber();
                updataAddress = mUpdateBean.getUpdataAddress();
                versionName = mUpdateBean.getVersionName();
                descc = mUpdateBean.getDescc();
                mIntroduce.setText(descc);
                if (versionCode > GlobalMethod.getVersionCode(context)) {
                    tvVersionName.setText("最新版" + versionName);
                } else {
                    tvVersionName.setText("已是最新版");
                }
            }
        });
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.a_about_lay_rate:
                rate();
                break;
            case R.id.a_about_check_version:
                if (versionCode > GlobalMethod.getVersionCode(context)) {
                    UpdateManager mUpdateManager = new UpdateManager(context,updataAddress,versionName);
                    mUpdateManager.checkUpdateInfo();
                } else {
                    ToastUtils.makeText(context, "当前已是最新版本");
                }
                break;
            default:
                super.onClick(v);
                break;

        }
    }

    /**
     * 打分
     */
    private void rate() {
        try {
            Uri uri = Uri.parse("market://details?id=" + getPackageName());
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } catch (Exception e) {
            new ErrorDialog(UpdateActivity.this, null).show();
        }
    }
}
