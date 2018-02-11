package com.lixin.freshmall.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lixin.freshmall.R;
import com.lixin.freshmall.model.OrderDec;
import com.lixin.freshmall.uitls.ImageManagerUtils;

import java.util.List;

/**
 * Created by 小火
 * Create time on  2017/12/13
 * My mailbox is 1403241630@qq.com
 */

public class OrderDecAdapter extends BaseAdapter {
    private Context context;
    private List<OrderDec.Commoditys> mList;
    public OrderDecAdapter(Context context, List<OrderDec.Commoditys> mList) {
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
        final OrderDecViewHolder viewHolder;
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_order_content,null);
            viewHolder = new OrderDecViewHolder();
            viewHolder.comm_title = convertView.findViewById(R.id.comm_title);
            viewHolder.comm_price = convertView.findViewById(R.id.comm_price);
            viewHolder.comm_number = convertView.findViewById(R.id.comm_number);
            viewHolder.comm_flavor = convertView.findViewById(R.id.comm_flavor);
            viewHolder.comm_icon = convertView.findViewById(R.id.comm_icon);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (OrderDecViewHolder) convertView.getTag();
        }
        OrderDec.Commoditys commoditys = mList.get(position);
        viewHolder.comm_title.setText(commoditys.getCommodityTitle() + "/" + commoditys.getCommodityUnit());
        Log.i("OrderlistAdapter", "getView: " + commoditys.getCommodityTitle());
        viewHolder.comm_price.setText("￥" + commoditys.getCommodityNewPrice());
        viewHolder.comm_number.setText("x" + commoditys.getCommodityBuyNum());
        viewHolder.comm_flavor.setText(commoditys.getCommodityFirstParameter() + " " + commoditys.getCommoditySecondParameter());
        String url = commoditys.getCommodityIcon();
        if (TextUtils.isEmpty(url)){
            viewHolder.comm_icon.setImageResource(R.drawable.image_fail_empty);
        }else{
            ImageManagerUtils.imageLoader.displayImage(url,viewHolder.comm_icon,ImageManagerUtils.options3);
        }
        return convertView;
    }
    private class OrderDecViewHolder{
        TextView comm_title;
        TextView comm_price;
        TextView comm_number;
        TextView comm_flavor;
        ImageView comm_icon;
    }
}
