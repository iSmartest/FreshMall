package com.lixin.freshmall.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lixin.freshmall.R;
import com.lixin.freshmall.model.CityInfoBean;

import java.util.ArrayList;

/**
 * Created by 小火
 * Create time on  2017/12/9
 * My mailbox is 1403241630@qq.com
 */

public class SelectAreaAdapter extends BaseAdapter{
    private Context context;
    private ArrayList<? extends CityInfoBean.ProvinceList.CityList.TownList> mList;
    public SelectAreaAdapter(Context context, ArrayList<? extends CityInfoBean.ProvinceList.CityList.TownList> mList) {
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
        SelectAreaViewHolder viewHolder;
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_province_city_area,null);
            viewHolder = new SelectAreaViewHolder();
            viewHolder.mProvince = convertView.findViewById(R.id.text_province_city_area);
            convertView.setTag(viewHolder);
        }else viewHolder = (SelectAreaViewHolder) convertView.getTag();
        CityInfoBean.ProvinceList.CityList.TownList townList = mList.get(position);
        viewHolder.mProvince.setText(townList.getTown());
        return convertView;
    }

    class SelectAreaViewHolder{
        TextView mProvince;
    }
}
