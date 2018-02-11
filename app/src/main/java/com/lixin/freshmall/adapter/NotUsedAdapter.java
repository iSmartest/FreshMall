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
 * Create time on  2017/5/27
 * My mailbox is 1403241630@qq.com
 */

public class NotUsedAdapter extends BaseAdapter{
    private Context context;
    private List<CouponBean.securitiesList> mList;
    private onUseOnClickListener onClickListener;
    public void setShopCart(Context context, List<CouponBean.securitiesList> mList, String uid) {
        this.context = context;
        this.mList = mList;
    }
    public void setUseOnClickListener(onUseOnClickListener onClickListener){
        this.onClickListener = onClickListener;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_not_used_coupon,null);
            viewHolder = new ViewHolder();
            viewHolder.mReduction = convertView.findViewById(R.id.text_item_not_used_coupon_securities_reduce_price);
            viewHolder.mAllPrice = convertView.findViewById(R.id.text_item_not_used_coupon_securities_all_price);
            viewHolder.mTime = convertView.findViewById(R.id.text_item_not_used_coupon_securities_time);
            viewHolder.mImmediateUse = convertView.findViewById(R.id.text_item_not_used_coupon_immediate_use);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        CouponBean.securitiesList securitiesList = mList.get(position);
        viewHolder.mReduction.setText(securitiesList.getSecuritiesReducePrice());
        viewHolder.mAllPrice.setText("满" + securitiesList.getSecuritiesAllPrice() + "使用");
        viewHolder.mTime.setText(securitiesList.getSecuritiesTimeZone());
        viewHolder.mImmediateUse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListener.setUseOnClickListener(position);
            }
        });
        return convertView;
    }
    class ViewHolder{
        TextView mReduction,mAllPrice,mTime,mImmediateUse;
    }
    public interface onUseOnClickListener{
        void setUseOnClickListener(int position);
    }
}
