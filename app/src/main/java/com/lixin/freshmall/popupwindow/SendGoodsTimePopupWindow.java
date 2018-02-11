package com.lixin.freshmall.popupwindow;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.google.gson.Gson;
import com.lixin.freshmall.R;
import com.lixin.freshmall.adapter.SendGoodsTimeAdapter;
import com.lixin.freshmall.model.Constant;
import com.lixin.freshmall.model.StoreTimeBean;
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
 * Create time on  2017/10/31
 * My mailbox is 1403241630@qq.com
 */

public class SendGoodsTimePopupWindow extends PopupWindow{
    private Context context;
    private String storeId;
    private ListView class_list;
    private View view;
    private List<StoreTimeBean.SendTimeList> mList;
    private SendGoodsTimeAdapter mAdapter;
    private SelectedTextListener mSelectedTextListener;
    private LinearLayout linear_01;
    public SendGoodsTimePopupWindow(final Context context, View parent, String storeId) {
        super(context);
        this.context = context;
        this.storeId = storeId;
        view = View.inflate(context, R.layout.class_popu, null);
        mList = new ArrayList<>();
        initView(parent);
        initData();
    }

    private void initView(View parent) {
        class_list = view.findViewById(R.id.class_list);
        linear_01 = view.findViewById(R.id.linear_01);
        //设置SelectPicPopupWindow的View
        this.setContentView(view);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setBackgroundDrawable(new BitmapDrawable());
        this.setOutsideTouchable(true);
        this.setFocusable(true);
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        this.setBackgroundDrawable(dw);
        this.showAsDropDown(parent, 0, 2);
        mAdapter = new SendGoodsTimeAdapter(context,mList);
        class_list.setAdapter(mAdapter);
        class_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mSelectedTextListener.sure(mList.get(position).getStime());
                dismiss();
            }
        });
        linear_01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
    public void setSelectedTextListener(SelectedTextListener mSelectedTextListener){
        this.mSelectedTextListener = mSelectedTextListener;
    }

    private void initData() {
        Map<String,String> params = new HashMap<>();
        String json = "{\"cmd\":\"getStoreInfoTime\",\"storeId\":\""+storeId+"\"}";
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
                SPUtil.putInt(context,"storeEndTime",storeTimeBean.getStoreEndTime());
                List<StoreTimeBean.SendTimeList> timeLists = storeTimeBean.getSendTimeList();
                mList.addAll(timeLists);
                mAdapter.notifyDataSetChanged();
            }
        });
    }
    public interface SelectedTextListener{
        void sure(String name);
    }
}
