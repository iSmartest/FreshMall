package com.lixin.freshmall.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lixin.freshmall.R;
import com.lixin.freshmall.model.ClassBean;
import com.lixin.freshmall.uitls.ImageManagerUtils;

import java.util.List;

/**
 * Created by 小火
 * Create time on  2017/12/7
 * My mailbox is 1403241630@qq.com
 */

public class SecondAdapter extends RecyclerView.Adapter<SecondAdapter.SecondViewHolder> {
    private Context context;
    private List<ClassBean.Commoditys> mSecondList;
    public SecondAdapter(Context context, List<ClassBean.Commoditys> mSecondList) {
        this.context = context;
        this.mSecondList = mSecondList;
    }

    @Override
    public SecondViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_second,parent,false);
        SecondViewHolder viewHolder = new SecondViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(SecondViewHolder viewHolder, int position) {
        ClassBean.Commoditys mList = mSecondList.get(position);
        viewHolder.Name.setText(mList.getCommodityTitle());
        viewHolder.Dec.setText(mList.getCommodityDescription());
        viewHolder.NewPrice.setText(mList.getCommodityNewPrice() + "元/" + mList.getCommodityUnit());
        viewHolder.OriginalPrice.setText("市场价："+mList.getCommodityOriginalPrice());
        viewHolder.Seller.setText("销量：" + mList.getCommoditySeller());
        if (TextUtils.isEmpty(mList.getCommodityLimitNum())){
            viewHolder.Limit.setVisibility(View.GONE);
        }else {
            viewHolder.Limit.setVisibility(View.VISIBLE);
            viewHolder.Limit.setText("限购"+ mList.getCommodityLimitNum() + "份");
        }

        String img = mList.getCommodityIcon();
        if (TextUtils.isEmpty(img)){
            viewHolder.Icon.setImageResource(R.drawable.image_fail_empty);
        }else {
            ImageManagerUtils.imageLoader.displayImage(img,viewHolder.Icon,ImageManagerUtils.options3);
        }
    }

    @Override
    public int getItemCount() {
        return mSecondList == null ? 0 : mSecondList.size();
    }

    public class SecondViewHolder extends RecyclerView.ViewHolder{
        ImageView Icon;
        TextView Name,Dec,NewPrice,OriginalPrice,Seller,Limit;
        public SecondViewHolder(View itemView) {
            super(itemView);
            Icon = itemView.findViewById(R.id.iv_class_shop_icon);
            Name = itemView.findViewById(R.id.text_class_shop_name);
            Dec = itemView.findViewById(R.id.text_class_dec);
            NewPrice = itemView.findViewById(R.id.text_class_new_price);
            OriginalPrice = itemView.findViewById(R.id.text_class_original_price);
            Seller = itemView.findViewById(R.id.text_class_seller);
            Limit = itemView.findViewById(R.id.text_class_limit);
        }
    }
}
