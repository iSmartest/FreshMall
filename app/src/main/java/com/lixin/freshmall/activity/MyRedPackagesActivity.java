package com.lixin.freshmall.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import com.example.xrecyclerview.XRecyclerView;
import com.google.gson.Gson;
import com.lixin.freshmall.R;
import com.lixin.freshmall.adapter.MyRedPackagesAdapter;
import com.lixin.freshmall.model.Constant;
import com.lixin.freshmall.model.RedPackagesBean;
import com.lixin.freshmall.okhttp.OkHttpUtils;
import com.lixin.freshmall.okhttp.budiler.StringCallback;
import com.lixin.freshmall.uitls.ImageManagerUtils;
import com.lixin.freshmall.uitls.SPUtil;
import com.lixin.freshmall.uitls.ToastUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;

/**
 * Created by 小火
 * Create time on  2017/12/7
 * My mailbox is 1403241630@qq.com
 */

public class MyRedPackagesActivity extends BaseActivity {
    private String uid,userIcon, nickName,invitation;
    private CircleImageView header;
    private XRecyclerView red_list;
    private List<RedPackagesBean.RedPacketList> mList;
    private MyRedPackagesAdapter mAdapter;
    private TextView mMyMoney;
    private int nowpage = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_red_packages);
        uid = SPUtil.getString(context, "uid");
        userIcon = SPUtil.getString(context, "userIcon");
        nickName = SPUtil.getString(context, "nickName");
        invitation = getIntent().getStringExtra("invitation");
        mList = new ArrayList<>();
        hideBack(2);
        setTitleText("红包");
        initView();
        getRedPackages();
    }

    private void initView() {
        header = findViewById(R.id.a_my_header);
        if (TextUtils.isEmpty(userIcon)){
            header.setImageResource(R.drawable.image_fail_empty);
        }else {
            ImageManagerUtils.imageLoader.displayImage(userIcon,header,ImageManagerUtils.options3);
        }
        ((TextView)findViewById(R.id.text_my_name)).setText(nickName + "共收到");
        mMyMoney = findViewById(R.id.text_my_money_num);
        red_list = findViewById(R.id.red_list);
        red_list.setLayoutManager(new LinearLayoutManager(this));
        red_list.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                nowpage = 1;
                mList.clear();
                getRedPackages();
                mAdapter.notifyDataSetChanged();
                red_list.refreshComplete();
            }

            @Override
            public void onLoadMore() {
                nowpage++;
                getRedPackages();
                mAdapter.notifyDataSetChanged();
                red_list.refreshComplete();
            }
        });
        mAdapter = new MyRedPackagesAdapter(context,mList);
        red_list.setAdapter(mAdapter);
    }

    private void getRedPackages() {
        Map<String, String> params = new HashMap<>();
        final String json="{\"cmd\":\"getRedPacket\",\"uid\":\"" + uid + "\",\"nowpage\":\""+nowpage+"\"}";
        params.put("json", json);
        dialog1.show();
        OkHttpUtils.post().url(Constant.THE_SERVER_URL).params(params)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ToastUtils.makeText(context, e.getMessage());
                dialog1.dismiss();
                red_list.refreshComplete();
            }
            @Override
            public void onResponse(String response, int id) {
                Log.i("WaitPaymentFragment", "onResponse: " + response);
                Gson gson = new Gson();
                dialog1.dismiss();
                RedPackagesBean mRedPackagesBean = gson.fromJson(response, RedPackagesBean.class);
                if (mRedPackagesBean.getResult().equals("1")) {
                    ToastUtils.makeText(context, mRedPackagesBean.getResultNote());
                    return;
                }
                mMyMoney.setText(mRedPackagesBean.getRedPacketTotalMoney());
                List<RedPackagesBean.RedPacketList> commentList = mRedPackagesBean.getRedPacketList();
                mList.addAll(commentList);
                mAdapter.notifyDataSetChanged();
                red_list.refreshComplete();
            }
        });
    }
}
