package com.lixin.freshmall.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lixin.freshmall.R;
import com.lixin.freshmall.model.HomeBean;

import java.util.List;

/**
 * Created by 小火
 * Create time on  2017/12/7
 * My mailbox is 1403241630@qq.com
 */

public class FirstAdapter extends BaseAdapter {
    private Context context;
    private List<HomeBean.classifyBottom> mFirstList;
    private int defItem;//声明默认选中的项
    public FirstAdapter(Context context, List<HomeBean.classifyBottom> mFirstList) {
        this.context = context;
        this.mFirstList = mFirstList;
    }
    @Override
    public int getCount() {
        return mFirstList == null ? 0 : mFirstList.size();
    }

    @Override
    public Object getItem(int position) {
        return mFirstList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    public void setDefSelect(int position) {
        this.defItem = position;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_text,null);
            viewHolder = new ViewHolder();
            viewHolder.mClass = convertView.findViewById(R.id.text_class_name);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        HomeBean.classifyBottom mList = mFirstList.get(position);
        viewHolder.mClass.setText(mList.getClassifyType());
        disposalView(position,convertView);
        return convertView;
    }

    private void disposalView(int position, View convertView) {
        Resources resource = context.getResources();
        ColorStateList csl1 = resource.getColorStateList(R.color.app_main_default);
        ColorStateList csl2 = resource.getColorStateList(R.color.text_color_green);
        if (position == defItem) {
            convertView.setBackgroundResource(R.color.text_color_green);
            ViewHolder viewHolder = (ViewHolder) convertView.getTag();
            viewHolder.mClass.setTextColor(csl1);
        }else {
            convertView.setBackgroundResource(R.color.app_main_default);
            ViewHolder viewHolder = (ViewHolder) convertView.getTag();
            viewHolder.mClass.setTextColor(csl2);
        }
    }
    class ViewHolder{
        TextView mClass;
    }
}
