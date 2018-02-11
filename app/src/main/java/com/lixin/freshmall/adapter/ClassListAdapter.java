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
import com.lixin.freshmall.model.ClassListBean;
import com.lixin.freshmall.uitls.ImageManagerUtils;

import java.util.List;


/**
 * Created by 小火
 * Create time on  2017/5/16
 * My mailbox is 1403241630@qq.com
 */

public class ClassListAdapter extends RecyclerView.Adapter<ClassListAdapter.ClassListViewHolder>{
    private Context context;
    private List<ClassListBean.commoditys> mList;

    public ClassListAdapter(Context context, List<ClassListBean.commoditys> mList) {
        this.context = context;
        this.mList = mList;
    }

    @Override
    public ClassListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_class,parent,false);
        ClassListViewHolder viewHolder = new ClassListViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ClassListViewHolder viewHolder, int position) {
        ClassListBean.commoditys commoditys = mList.get(position);
        viewHolder.title.setText(commoditys.commodityTitle + commoditys.getCommodityUnit());
        viewHolder.nowPrice.setText("￥" + commoditys.commodityNewPrice);
        viewHolder.originalPrice.setText("市场价:￥" + commoditys.commodityOriginalPrice);
        viewHolder.soldNum.setText("已售" + commoditys.commoditySeller + "件");
        viewHolder.originalPrice.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG|Paint.ANTI_ALIAS_FLAG);//设置中划线并加清晰
        String img = commoditys.commodityIcon;
        if (TextUtils.isEmpty(img)){
            viewHolder.shopPicture.setImageResource(R.drawable.image_fail_empty);
        }else {
            ImageManagerUtils.imageLoader.displayImage(img,viewHolder.shopPicture,ImageManagerUtils.options3);
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

    public class ClassListViewHolder extends RecyclerView.ViewHolder {
        ImageView shopPicture;
        TextView title,nowPrice,originalPrice,soldNum,dec;
        public ClassListViewHolder(View itemView) {
            super(itemView);
            shopPicture = itemView.findViewById(R.id.iv_shop_picture);
            title = itemView.findViewById(R.id.text_shop_name);
            nowPrice = itemView.findViewById(R.id.text_shop_price);
            originalPrice = itemView.findViewById(R.id.text_shop_original_price);
            soldNum = itemView.findViewById(R.id.text_have_sold_num);
            dec = itemView.findViewById(R.id.text_shop_dec);
        }
    }
}
