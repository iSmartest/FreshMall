package com.lixin.freshmall.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lixin.freshmall.R;
import com.lixin.freshmall.model.MyIntegralBean;

import java.util.List;

/**
 * Created by 小火
 * Create time on  2017/12/1
 * My mailbox is 1403241630@qq.com
 */

public class IntagralDecAdapter extends RecyclerView.Adapter<IntagralDecAdapter. MoneyDecViewHolder>{
    private Context context;
    private List<MyIntegralBean.MoneyList> mList;
    public IntagralDecAdapter(Context context, List<MyIntegralBean.MoneyList> mList) {
        this.context = context;
        this.mList = mList;
    }

    @Override
    public MoneyDecViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_money_dec,parent,false);
        MoneyDecViewHolder viewHolder = new MoneyDecViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MoneyDecViewHolder viewHolder, int position) {
        Resources resource = context.getResources();
        ColorStateList csl1 = resource.getColorStateList(R.color.text_fraction_red);
        ColorStateList csl2 = resource.getColorStateList(R.color.text_fraction_golden);
        MyIntegralBean.MoneyList moneyList = mList.get(position);
        if (!TextUtils.isEmpty(moneyList.getShoppingPayNum())){
            viewHolder.mMoneySoure.setText("购物支付" + moneyList.getShoppingPayNum() + "元");
        }
        if (!TextUtils.isEmpty(moneyList.getIntegralNum())){
            viewHolder.mMoneyNum.setText("+"+moneyList.getIntegralNum());
        }
        viewHolder.mMoneyTime.setText(moneyList.getIntegralTime());
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public class MoneyDecViewHolder extends RecyclerView.ViewHolder {
        TextView mMoneySoure,mMoneyNum,mMoneyTime;
        public MoneyDecViewHolder(View itemView) {
            super(itemView);
            mMoneyNum = itemView.findViewById(R.id.text_item_money_dec_num);
            mMoneySoure = itemView.findViewById(R.id.text_item_money_dec_money_soure);
            mMoneyTime = itemView.findViewById(R.id.text_item_money_dec_time);
        }
    }
}
