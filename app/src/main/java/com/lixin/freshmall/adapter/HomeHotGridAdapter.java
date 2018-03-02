package com.lixin.freshmall.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lixin.freshmall.R;
import com.lixin.freshmall.model.HomeBean;
import com.lixin.freshmall.uitls.ImageManagerUtils;

import java.util.List;


/**
 * Created by 小火
 * Create time on  2017/5/22
 * My mailbox is 1403241630@qq.com
 */

public class HomeHotGridAdapter extends BaseAdapter{
    private Context context;
    private List<HomeBean.ThemeList.CommodityList> commodityList;
    public HomeHotGridAdapter(Context context, List<HomeBean.ThemeList.CommodityList> commodityList) {
        this.context = context;
        this.commodityList = commodityList;
    }

    @Override
    public int getCount() {
        return commodityList == null ? 0 : commodityList.size();
    }

    @Override
    public Object getItem(int position) {
        return commodityList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_grid_shop,null);
            viewHolder = new ViewHolder();
            viewHolder.mPicture = convertView.findViewById(R.id.ima_grid_shop_picture);
            viewHolder.mShopName = convertView.findViewById(R.id.text_grid_shop_name);
            viewHolder.mNowPrice = convertView.findViewById(R.id.text_grid_shop_now_price);
            viewHolder.mMarketPrice = convertView.findViewById(R.id.text_grid_shop_market_price);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        HomeBean.ThemeList.CommodityList mList = commodityList.get(position);
        viewHolder.mShopName.setText(mList.getCommodityTitle());
        viewHolder.mNowPrice.setText(mList.getCommodityNewPrice() + "元/" + mList.getCommodityUnit());
        viewHolder.mMarketPrice.setText("市场价:" + mList.getCommodityOriginalPrice() + "元/" + mList.getCommodityNewPrice());
        String img = mList.getCommodityIcon();
        if (TextUtils.isEmpty(img)){
            viewHolder.mPicture.setImageResource(R.drawable.image_fail_empty);
        }else {
            ImageManagerUtils.imageLoader.displayImage(img, viewHolder.mPicture, ImageManagerUtils.options3);
        }
        viewHolder.mMarketPrice.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG|Paint.ANTI_ALIAS_FLAG);
        return convertView;
    }
    class ViewHolder{
        ImageView mPicture;
        TextView mShopName,mNowPrice,mMarketPrice;

    }
}
