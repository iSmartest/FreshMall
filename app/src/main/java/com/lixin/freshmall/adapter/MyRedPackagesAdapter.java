package com.lixin.freshmall.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lixin.freshmall.R;
import com.lixin.freshmall.model.RedPackagesBean;

import java.util.List;

/**
 * Created by 小火
 * Create time on  2017/12/7
 * My mailbox is 1403241630@qq.com
 */

public class MyRedPackagesAdapter extends RecyclerView.Adapter<MyRedPackagesAdapter.MyRedPackagesViewHolder> {
    private Context context;
    private List<RedPackagesBean.RedPacketList> mList;
    public MyRedPackagesAdapter(Context context, List<RedPackagesBean.RedPacketList> mList) {
        this.context = context;
        this.mList = mList;
    }

    @Override
    public MyRedPackagesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_red_packages,parent,false);
        MyRedPackagesViewHolder viewHolder = new MyRedPackagesViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyRedPackagesViewHolder viewHolder, int position) {
        RedPackagesBean.RedPacketList redPacketList = mList.get(position);
        viewHolder.Name.setText(redPacketList.getFriendsName());
        viewHolder.Money.setText("￥"+redPacketList.getMoney());
        viewHolder.Time.setText(redPacketList.getOpenRedPacketTime());
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    class MyRedPackagesViewHolder extends RecyclerView.ViewHolder{
        TextView Name,Time,Money;

        public MyRedPackagesViewHolder(View itemView) {
            super(itemView);
            Name = itemView.findViewById(R.id.text_red_name);
            Time = itemView.findViewById(R.id.text_red_time);
            Money = itemView.findViewById(R.id.text_red_money);
        }
    }
}
