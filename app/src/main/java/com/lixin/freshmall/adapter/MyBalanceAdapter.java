package com.lixin.freshmall.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lixin.freshmall.R;
import com.lixin.freshmall.model.RechargeBean;

import java.util.List;

/**
 * Created by 小火
 * Create time on  2018/4/8
 * My mailbox is 1403241630@qq.com
 */

public class MyBalanceAdapter extends RecyclerView.Adapter<MyBalanceAdapter.MyBalanceViewHolder>{
    private Context context;
    private List<RechargeBean.ChargeList> mList;
    private int defaultItem;
    public MyBalanceAdapter(Context context, List<RechargeBean.ChargeList> mList) {
        this.context = context;
        this.mList = mList;
    }

    @Override
    public MyBalanceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_balance_recharge,parent,false);
        MyBalanceViewHolder viewHolder = new MyBalanceViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyBalanceViewHolder viewHolder, int position) {
        RechargeBean.ChargeList chargeList = mList.get(position);
        viewHolder.mAmount.setText(chargeList.getMoney() + "元");
        viewHolder.mGiveAmount.setText("送" + chargeList.getSendmoney()+ "元");
        disposalView(position,viewHolder);
    }

    private void disposalView(int position, MyBalanceViewHolder viewHolder) {
        if (defaultItem == position){
            viewHolder.iv_choose.setVisibility(View.VISIBLE);
            viewHolder.rl_recharge.setBackgroundResource(R.drawable.shape_choose_recharge);
        }else {
            viewHolder.iv_choose.setVisibility(View.GONE);
            viewHolder.rl_recharge.setBackgroundResource(R.color.app_main_default);
        }
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public void setDefault(int defaultItem){
        this.defaultItem = defaultItem;
    }

    public class MyBalanceViewHolder extends RecyclerView.ViewHolder{
        RelativeLayout rl_recharge;
        TextView mAmount,mGiveAmount;
        ImageView iv_choose;
        public MyBalanceViewHolder(View itemView) {
            super(itemView);
            rl_recharge = itemView.findViewById(R.id.rl_recharge);
            mAmount = itemView.findViewById(R.id.text_recharge_amount);
            mGiveAmount = itemView.findViewById(R.id.text_give_amount);
            iv_choose = itemView.findViewById(R.id.iv_choose);
        }
    }
}
