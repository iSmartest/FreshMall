package com.lixin.freshmall.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lixin.freshmall.R;
import com.lixin.freshmall.dialog.LogOutDialog;
import com.lixin.freshmall.model.StoreBean;

import java.util.List;

/**
 * Created by 小火
 * Create time on  2017/12/9
 * My mailbox is 1403241630@qq.com
 */

public class SelectStoreAdapter extends BaseAdapter {
    private Context context;
    private List<StoreBean.StoreList> mList;
    private LogOutDialog mLogOutDialog;
    public SelectStoreAdapter(Context context, List<StoreBean.StoreList> mList) {
        this.context = context;
        this.mList = mList;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SelectStoreViewHolder viewHolder;
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_select_store,null);
            viewHolder = new SelectStoreViewHolder();
            viewHolder.mStoreName = convertView.findViewById(R.id.text_select_store_name);
            viewHolder.mStoreAderss = convertView.findViewById(R.id.text_select_store_address);
            viewHolder.mStoreDistance = convertView.findViewById(R.id.text_select_store_distance);
            viewHolder.mStoreTime = convertView.findViewById(R.id.text_select_store_time);
            viewHolder.mStorePhone = convertView.findViewById(R.id.iv_store_phone);
            convertView.setTag(viewHolder);
        }else viewHolder = (SelectStoreViewHolder) convertView.getTag();
        final StoreBean.StoreList storeList = mList.get(position);
        viewHolder.mStoreName.setText(storeList.getStoreName());
        viewHolder.mStoreAderss.setText(storeList.getStoreAderss());
        viewHolder.mStoreDistance.setText(storeList.getStoreDistance());
        viewHolder.mStoreTime.setText(storeList.getStoreTime());
        viewHolder.mStorePhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLogOutDialog = new LogOutDialog(context, storeList.getStorePhone(),"取消","呼叫",new LogOutDialog.OnSureBtnClickListener() {
                    @Override
                    public void sure() {
                        mLogOutDialog.dismiss();
                        Intent intent2 = new Intent();
                        intent2.setAction(Intent.ACTION_CALL);
                        intent2.setData(Uri.parse("tel:" + storeList.getStorePhone()));
                        context.startActivity(intent2);
                    }
                });
                mLogOutDialog.show();
            }
        });
        return convertView;
    }

    class SelectStoreViewHolder{
        TextView mStoreName,mStoreAderss,mStoreDistance,mStoreTime;
        ImageView mStorePhone;
    }
}
