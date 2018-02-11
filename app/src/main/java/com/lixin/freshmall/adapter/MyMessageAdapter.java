package com.lixin.freshmall.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lixin.freshmall.R;
import com.lixin.freshmall.model.MyMessageBean;

import java.util.List;

/**
 * Created by 小火
 * Create time on  2017/12/11
 * My mailbox is 1403241630@qq.com
 */

public class MyMessageAdapter extends RecyclerView.Adapter<MyMessageAdapter.MyMessageViewHolder> {
    private Context context;
    private List<MyMessageBean.messageList> mList;

    public MyMessageAdapter(Context context, List<MyMessageBean.messageList> mList) {
        this.context = context;
        this.mList = mList;
    }
    @Override
    public MyMessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view =  LayoutInflater.from(context).inflate(R.layout.item_my_message,parent,false);
        MyMessageViewHolder viewHolder = new MyMessageViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyMessageViewHolder viewHolder, int position) {
        MyMessageBean.messageList messageList = mList.get(position);
        viewHolder.mTitle.setText(messageList.getMessageTitle());
        viewHolder.mTime.setText(messageList.getMessgaeTime());
        viewHolder.mContext.setText(messageList.getMessageContent());
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public class MyMessageViewHolder extends RecyclerView.ViewHolder{
        TextView mTitle,mTime,mContext;
        public MyMessageViewHolder(View itemView) {
            super(itemView);
            mTitle = itemView.findViewById(R.id.my_message_title);
            mTime = itemView.findViewById(R.id.my_message_time);
            mContext = itemView.findViewById(R.id.my_message_context);
        }
    }
}
