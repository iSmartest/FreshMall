package com.lixin.freshmall.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lixin.freshmall.R;
import com.lixin.freshmall.model.CouponBean;

import java.util.List;


/**
 * Created by 小火
 * Create time on  2017/5/31
 * My mailbox is 1403241630@qq.com
 */

public class ExpiredAdapter extends BaseAdapter{
    private Context context;
    private List<CouponBean.securitiesList> mList;
    public void setExpired(Context context, List<CouponBean.securitiesList> mList, String uid) {
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
        final ViewHolder viewHolder;
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_expried_coupon,null);
            viewHolder = new ViewHolder();
            viewHolder.mReduction = convertView.findViewById(R.id.text_item_expired_coupon_securities_reduce_price);
            viewHolder.mAllPrice = convertView.findViewById(R.id.text_item_expried_coupon_securities_all_price);
            viewHolder.mTime = convertView.findViewById(R.id.text_item_not_expried_securities_time);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        CouponBean.securitiesList securitiesList = mList.get(position);
        viewHolder.mReduction.setText(securitiesList.getSecuritiesReducePrice());
        viewHolder.mAllPrice.setText("满" + securitiesList.getSecuritiesAllPrice() + "使用");
        viewHolder.mTime.setText(securitiesList.getSecuritiesTimeZone());
        return convertView;
    }
    class ViewHolder{
        TextView mReduction,mAllPrice,mTime;
    }
}
