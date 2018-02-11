package com.lixin.freshmall.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lixin.freshmall.R;
import com.lixin.freshmall.model.ShopDecBean;

import java.util.List;

/**
 * Created by 小火
 * Create time on  2017/12/9
 * My mailbox is 1403241630@qq.com
 */

public class SpecAdapter extends BaseAdapter{
    private Context context;
    private List<ShopDecBean.CommoditySpec> mList;
    public SpecAdapter(Context context, List<ShopDecBean.CommoditySpec> mList) {
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
        SelectCityViewHolder viewHolder;
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_spec,null);
            viewHolder = new SelectCityViewHolder();
            viewHolder.mTitle = convertView.findViewById(R.id.text_spec_title);
            viewHolder.mParameter = convertView.findViewById(R.id.text_spec_parameter);
            convertView.setTag(viewHolder);
        }else viewHolder = (SelectCityViewHolder) convertView.getTag();
        ShopDecBean.CommoditySpec commoditySpec = mList.get(position);
        viewHolder.mTitle.setText(commoditySpec.getSpecTitle());
        viewHolder.mParameter.setText(commoditySpec.getSpecParameter());
        return convertView;
    }

    class SelectCityViewHolder{
        TextView mTitle,mParameter;
    }
}
