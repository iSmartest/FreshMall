package com.lixin.freshmall.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lixin.freshmall.R;
import com.lixin.freshmall.model.ShoppingBag;

import java.util.List;

/**
 * Created by 小火
 * Create time on  2017/12/13
 * My mailbox is 1403241630@qq.com
 */

public class ShoppingBagAdapter extends BaseAdapter {
    private Context context;
    private List<ShoppingBag.ShoppingBagList> mList;
    public ShoppingBagAdapter(Context context, List<ShoppingBag.ShoppingBagList> mList) {
        this.context = context;
        this.mList = mList;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ShoppingBagViewHolder viewHolder;
        if (convertView == null ){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_shopping_bag,null);
            viewHolder = new ShoppingBagViewHolder();
            viewHolder.mShoppingBagName = convertView.findViewById(R.id.text_shopping_title);
            viewHolder.mShoppingBagPrice = convertView.findViewById(R.id.text_shopping_price);
            convertView.setTag(viewHolder);
        }else viewHolder = (ShoppingBagViewHolder) convertView.getTag();
        ShoppingBag.ShoppingBagList shoppingBagList = mList.get(position);
        viewHolder.mShoppingBagName.setText(shoppingBagList.getShoppingBag());
        viewHolder.mShoppingBagPrice.setText("￥" + shoppingBagList.getShoppingBagMoney() );
        return convertView;
    }
    class ShoppingBagViewHolder{
        TextView mShoppingBagName,mShoppingBagPrice;
    }
}
