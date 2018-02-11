package com.lixin.freshmall.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.lixin.freshmall.R;
import com.lixin.freshmall.adapter.ShoppingBagAdapter;
import com.lixin.freshmall.model.Constant;
import com.lixin.freshmall.model.ShoppingBag;
import com.lixin.freshmall.okhttp.OkHttpUtils;
import com.lixin.freshmall.okhttp.budiler.StringCallback;
import com.lixin.freshmall.uitls.ToastUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by 小火
 * Create time on  2017/12/13
 * My mailbox is 1403241630@qq.com
 */

public class ShoppingBagActivity extends BaseActivity {
    private ListView bag_list;
    private ShoppingBagAdapter mAdapter;
    private List<ShoppingBag.ShoppingBagList> mList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_bag);
        hideBack(2);
        setTitleText("购物袋");
        mList = new ArrayList<>();
        initView();
        getdata();
    }

    private void initView() {
        bag_list = findViewById(R.id.bag_list);
        mAdapter = new ShoppingBagAdapter(context,mList);
        bag_list.setAdapter(mAdapter);
        bag_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent();
                intent.putExtra("shoppingBag", mList.get(position).getShoppingBag());
                intent.putExtra("shoppingBagPrice", mList.get(position).getShoppingBagMoney());
                setResult(2002,intent);
                finish();
            }
        });
    }

    private void getdata() {
        Map<String,String> praams = new HashMap<>();
        String json = "{\"cmd\":\"getShoppingBag\"}";
        praams.put("json",json);
        dialog1.show();
        OkHttpUtils.post().url(Constant.THE_SERVER_URL).params(praams).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                dialog1.dismiss();
                ToastUtils.makeText(context,e.getMessage());
            }

            @Override
            public void onResponse(String response, int id) {
                Log.i("getShoppingBag", "onResponse: " + response);
                Gson gson = new Gson();
                dialog1.dismiss();
                ShoppingBag shoppingBag = gson.fromJson(response,ShoppingBag.class);
                if (shoppingBag.getResult().equals("1")){
                    ToastUtils.makeText(context,shoppingBag.getResultNote());
                    return;
                }
                List<ShoppingBag.ShoppingBagList> shoppingBagLists = shoppingBag.getShoppingBagList();
                mList.addAll(shoppingBagLists);
                mAdapter.notifyDataSetChanged();
            }
        });
    }
}
