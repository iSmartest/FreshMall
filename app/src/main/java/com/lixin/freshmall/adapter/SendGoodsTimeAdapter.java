package com.lixin.freshmall.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lixin.freshmall.R;
import com.lixin.freshmall.model.StoreTimeBean;

import java.util.List;

/**
 * Created by 小火
 * Create time on  2017/10/31
 * My mailbox is 1403241630@qq.com
 */

public class SendGoodsTimeAdapter extends BaseAdapter{
    private Context context;
    private List<StoreTimeBean.SendTimeList> mList;
    private int defItem;//声明选中的项

    public SendGoodsTimeAdapter(Context context, List<StoreTimeBean.SendTimeList> mList) {
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
        ClassViewHolder viewHolder;
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_time,null);
            viewHolder = new ClassViewHolder();
            viewHolder.mTypeName = convertView.findViewById(R.id.tv_item_class_name);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ClassViewHolder) convertView.getTag();
        }
        StoreTimeBean.SendTimeList mTypeList = mList.get(position);
        viewHolder.mTypeName.setText(mTypeList.getStime());
        return convertView;
    }

    class ClassViewHolder{
        TextView mTypeName;
    }
}
