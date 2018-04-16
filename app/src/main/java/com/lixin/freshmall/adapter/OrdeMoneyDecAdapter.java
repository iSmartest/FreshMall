package com.lixin.freshmall.adapter;

import android.content.Context;
import android.text.TextUtils;
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
 * Create time on  2017/12/18
 * My mailbox is 1403241630@qq.com
 */

public class OrdeMoneyDecAdapter extends BaseAdapter {
    private Context context;
    private List<? extends OrderDec.OrderDetailed> mList;
    public OrdeMoneyDecAdapter(Context context, List<? extends OrderDec.OrderDetailed> mList) {
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
        OrdeMoneyDecViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_order_money_dec,null);
            viewHolder = new OrdeMoneyDecViewHolder();
            viewHolder.commIcon = convertView.findViewById(R.id.iv_order_money_comm_picture);
            viewHolder.commTitle = convertView.findViewById(R.id.text_order_money_comm_title);
            viewHolder.buyParams = convertView.findViewById(R.id.text_order_money_buy_params);
            viewHolder.actualParams = convertView.findViewById(R.id.text_order_money_actual_params);
            viewHolder.buyWeight = convertView.findViewById(R.id.text_order_money_buy_weight);
            viewHolder.buyPrice = convertView.findViewById(R.id.text_order_money_buy_price);
            viewHolder.actualWeight = convertView.findViewById(R.id.text_order_money_actual_weight);
            viewHolder.actualPrice = convertView.findViewById(R.id.text_order_money_actual_price);
            convertView.setTag(viewHolder);
        }else viewHolder = (OrdeMoneyDecViewHolder) convertView.getTag();
        OrderDec.OrderDetailed orderDetailed = mList.get(position);
        if (orderDetailed.getIspiece().equals("2")){
            viewHolder.buyParams.setText("购买重量");
            viewHolder.actualParams.setText("实际重量");
            viewHolder.buyWeight.setText(orderDetailed.getBuyWeight());
            viewHolder.actualWeight.setText(orderDetailed.getActualWeight()+"kg");
        }else {
            viewHolder.buyParams.setText("购买数量");
            viewHolder.actualParams.setText("实际数量");
            viewHolder.buyWeight.setText(orderDetailed.getCommodityBuyNum());
            viewHolder.actualWeight.setText(orderDetailed.getActualBuyNum());
        }
        viewHolder.commTitle.setText(orderDetailed.getCommodityTitle());
        viewHolder.buyPrice.setText(orderDetailed.getBuyPrice() +"元");
        viewHolder.actualPrice.setText(orderDetailed.getActualPrice() +"元");
        String img = orderDetailed.getCommodityIcon();
        if (TextUtils.isEmpty(img)){
            viewHolder.commIcon.setImageResource(R.drawable.image_fail_empty);
        }else {
            ImageManagerUtils.imageLoader.displayImage(img, viewHolder.commIcon,ImageManagerUtils.options3);
        }
        return convertView;
    }

    class OrdeMoneyDecViewHolder {
        ImageView commIcon;
        TextView commTitle,buyWeight,buyPrice,actualWeight,actualPrice,buyParams,actualParams;
    }
}
