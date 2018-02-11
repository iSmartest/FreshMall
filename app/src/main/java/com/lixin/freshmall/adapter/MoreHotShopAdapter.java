package com.lixin.freshmall.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lixin.freshmall.R;
import com.lixin.freshmall.model.MoreShopBean;
import com.lixin.freshmall.uitls.ImageManagerUtils;

import java.util.List;


/**
 * Created by 小火
 * Create time on  2017/5/19
 * My mailbox is 1403241630@qq.com
 */

public class MoreHotShopAdapter extends RecyclerView.Adapter<MoreHotShopAdapter.MoreShopViewHolder>{
    private Context context;
    private List<MoreShopBean.moreCommoditys> mList;
    public MoreHotShopAdapter(Context context, List<MoreShopBean.moreCommoditys> mList) {
        this.context = context;
        this.mList = mList;
    }

    @Override
    public MoreShopViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_grid_shop,parent,false);
        MoreShopViewHolder viewHolder = new MoreShopViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MoreShopViewHolder viewHolder, int position) {
        MoreShopBean.moreCommoditys moreCommoditys = mList.get(position);
        viewHolder.mShopName.setText(moreCommoditys.getCommodityTitle());
        viewHolder.mNowPrice.setText(moreCommoditys.getCommodityNewPrice() + "元/" + moreCommoditys.getCommodityUnit());
        viewHolder.mMarketPrice.setText("市场价:" + moreCommoditys.getCommodityOriginalPrice() + "元/" + moreCommoditys.getCommodityUnit());
        viewHolder.mMarketPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG|Paint.ANTI_ALIAS_FLAG);
        String img = moreCommoditys.getCommodityIcon();
        if (TextUtils.isEmpty(img)){
            viewHolder.mPicture.setImageResource(R.drawable.image_fail_empty);
        }else {
            ImageManagerUtils.imageLoader.displayImage(img,viewHolder.mPicture,ImageManagerUtils.options3);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    class MoreShopViewHolder extends RecyclerView.ViewHolder{
        ImageView mPicture;
        TextView mShopName,mNowPrice,mMarketPrice;

        public MoreShopViewHolder(View itemView) {
            super(itemView);
            mPicture = itemView.findViewById(R.id.ima_grid_shop_picture);
            mShopName = itemView.findViewById(R.id.text_grid_shop_name);
            mNowPrice = itemView.findViewById(R.id.text_grid_shop_now_price);
            mMarketPrice = itemView.findViewById(R.id.text_grid_shop_market_price);
        }
    }
}
