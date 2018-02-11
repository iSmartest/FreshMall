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
import com.lixin.freshmall.model.GenerateOrderBean;
import com.lixin.freshmall.uitls.ImageManagerUtils;

import java.util.ArrayList;

/**
 * Created by 小火
 * Create time on  2017/5/19
 * My mailbox is 1403241630@qq.com
 */

public class NowBuyAdapter extends BaseAdapter{
    private Context context;
    private ArrayList<? extends GenerateOrderBean.commoditys> mList;
    public NowBuyAdapter(Context context, ArrayList<? extends GenerateOrderBean.commoditys> mList) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_shop_dec,null);
            viewHolder = new ViewHolder();
            viewHolder.mShopPicture = convertView.findViewById(R.id.iv_now_buy_shop_picture);
            viewHolder.mShopName = convertView.findViewById(R.id.text_now_buy_shop_name);
            viewHolder.mShopFlavor = convertView.findViewById(R.id.text_now_buy_shop_flavor);
            viewHolder.mShopUnitPrice = convertView.findViewById(R.id.text_now_buy_shop_unit_price);
            viewHolder.mShopNum = convertView.findViewById(R.id.text_now_buy_shop_num);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        GenerateOrderBean.commoditys orderShopList = mList.get(position);
        viewHolder.mShopName.setText(orderShopList.getCommodityTitle() + "/" + orderShopList.getCommodityUnit());
        viewHolder.mShopFlavor.setText(orderShopList.getCommodityFirstParam() + "  " + orderShopList.getCommoditySecondParam());
        viewHolder.mShopUnitPrice.setText("￥" + orderShopList.getCommodityNewPrice());
        viewHolder.mShopNum.setText("x" + orderShopList.getCommodityShooCarNum());
        String img = orderShopList.getCommodityIcon();
        if (TextUtils.isEmpty(img)){
            viewHolder.mShopPicture.setImageResource(R.drawable.image_fail_empty);
        }else {
            ImageManagerUtils.imageLoader.displayImage(img,viewHolder.mShopPicture,ImageManagerUtils.options3);
        }
        return convertView;
    }
    class ViewHolder{
        ImageView mShopPicture;
        TextView mShopName,mShopFlavor,mShopUnitPrice,mShopNum;
    }
}
