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
import com.lixin.freshmall.model.LifeBean;
import com.lixin.freshmall.uitls.ImageManagerUtils;

import java.util.List;

/**
 * Created by 小火
 * Create time on  2017/12/1
 * My mailbox is 1403241630@qq.com
 */

public class LifeAdapter extends RecyclerView.Adapter<LifeAdapter.LifeViewHolder> {
    private Context context;
    private List<LifeBean.LifeList> mList;
    public LifeAdapter(Context context, List<LifeBean.LifeList> mList) {
        this.context = context;
        this.mList = mList;
    }

    @Override
    public LifeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_life,parent,false);
        LifeViewHolder viewHolder = new LifeViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(LifeViewHolder viewHolder, int position) {
        LifeBean.LifeList mLifeList = mList.get(position);
        String img = mLifeList.getLifeIcon();
        if (TextUtils.isEmpty(img)){
            viewHolder.mPicture.setImageResource(R.drawable.image_fail_empty);
        }else {
            ImageManagerUtils.imageLoader.displayImage(img,viewHolder.mPicture,ImageManagerUtils.options3);
        }
        viewHolder.mDec.setText(mLifeList.getLifeDec());
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    class LifeViewHolder extends RecyclerView.ViewHolder {
        TextView mDec;
        ImageView mPicture;
        public LifeViewHolder(View itemView) {
            super(itemView);
            mDec = itemView.findViewById(R.id.text_life_dec);
            mPicture = itemView.findViewById(R.id.iv_life_picture);
        }
    }
}

