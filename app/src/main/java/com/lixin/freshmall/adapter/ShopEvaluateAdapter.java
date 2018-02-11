package com.lixin.freshmall.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lixin.freshmall.R;
import com.lixin.freshmall.model.EvaluateBean;
import com.lixin.freshmall.uitls.ImageManagerUtils;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by 小火
 * Create time on  2017/12/6
 * My mailbox is 1403241630@qq.com
 */

public class ShopEvaluateAdapter extends RecyclerView.Adapter<ShopEvaluateAdapter.ShopEvaluateViewHolder> {

    private Context context;
    private  List<EvaluateBean.CommentList> mList;

    public ShopEvaluateAdapter(Context context, List<EvaluateBean.CommentList> mList) {
        this.context = context;
        this.mList = mList;
    }

    @Override
    public ShopEvaluateViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_shop_evaluate,parent,false);
        ShopEvaluateViewHolder viewHolder = new ShopEvaluateViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ShopEvaluateViewHolder viewHolder, int position) {
        EvaluateBean.CommentList mCommentList = mList.get(position);
        viewHolder.Name.setText(mCommentList.getCommentNickName());
        viewHolder.Time.setText(mCommentList.getCommentTime());
        viewHolder.Content.setText(mCommentList.getCommentContent());
        String img = mCommentList.getCommentIcon();
        if (TextUtils.isEmpty(img)){
            viewHolder.Icon.setImageResource(R.drawable.my_head_portrait);
        }else {
            ImageManagerUtils.imageLoader.displayImage(img,viewHolder.Icon,ImageManagerUtils.options3);
        }
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public class ShopEvaluateViewHolder extends RecyclerView.ViewHolder{
        CircleImageView Icon;
        TextView Name,Time,Content;
        public ShopEvaluateViewHolder(View itemView) {
            super(itemView);
            Icon = itemView.findViewById(R.id.text_comment_user_icon);
            Name = itemView.findViewById(R.id.text_comment_user_name);
            Time = itemView.findViewById(R.id.text_comment_user_time);
            Content = itemView.findViewById(R.id.text_comment_user_content);
        }
    }
}
