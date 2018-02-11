package com.lixin.freshmall.adapter;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lixin.freshmall.R;
import com.lixin.freshmall.activity.MyApplication;
import com.lixin.freshmall.activity.OrderDecActivity;
import com.lixin.freshmall.model.CodeBean;
import com.lixin.freshmall.uitls.ImageManagerUtils;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by 小火
 * Create time on  2017/12/5
 * My mailbox is 1403241630@qq.com
 */

public class InnerAdapter extends BaseAdapter {
    private Context context;
    private List<CodeBean.CodeList> mList = new ArrayList<>();
    public InnerAdapter(Context context, List<CodeBean.CodeList> mList) {
        this.context = context;
        if (isEmpty()) {
            this.mList = mList;
            notifyDataSetChanged();
        } else {
            this.mList = mList;
        }
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public Object getItem(int position) {
        if(mList==null ||mList.size()==0) return null;
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void remove(int position) {
        if (position > -1 && position < mList.size()) {
            mList.remove(position);
            notifyDataSetChanged();
        }
    }

    public boolean isEmpty() {
        return mList.isEmpty();
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_code, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.mCode = convertView.findViewById(R.id.text_code);
            viewHolder.mUserIcon = convertView.findViewById(R.id.text_code_user_icon);
            viewHolder.mOrderNum = convertView.findViewById(R.id.text_code_order_num);
            viewHolder.mCodeTime = convertView.findViewById(R.id.text_code_time);
            viewHolder.mUserName = convertView.findViewById(R.id.text_code_user_name);
            viewHolder.mOrderDec = convertView.findViewById(R.id.text_see_order_dec);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final CodeBean.CodeList mCodeList = mList.get(position);
        String img = mCodeList.getQRCode();
        String pic = mCodeList.getUserIcon();
        if (TextUtils.isEmpty(img)){
            viewHolder.mCode.setImageResource(R.drawable.image_fail_empty);
        }else {
            ImageManagerUtils.imageLoader.displayImage(img,viewHolder.mCode,ImageManagerUtils.options3);
        }

        if (TextUtils.isEmpty(pic)){
            viewHolder.mUserIcon.setImageResource(R.drawable.image_fail_empty);
        }else {
            ImageManagerUtils.imageLoader.displayImage(pic,viewHolder.mUserIcon,ImageManagerUtils.options3);
        }

        viewHolder.mOrderNum.setText("订单编号：" + mCodeList.getOrderNum());

        viewHolder.mCodeTime.setText("取货时间：" + mCodeList.getGetGoodsTime());

        viewHolder.mUserName.setText(String.format("%s", mCodeList.getUserName()));

        viewHolder.mOrderDec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("orderId",mCodeList.getOrderNum());
                bundle.putString("orderState","2");
                MyApplication.openActivity(context, OrderDecActivity.class,bundle);
            }
        });
        return convertView;
    }

    private static class ViewHolder {
        ImageView mCode;
        CircleImageView mUserIcon;
        TextView mOrderNum,mCodeTime,mUserName,mOrderDec;
    }
}
