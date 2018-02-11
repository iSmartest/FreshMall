package com.lixin.freshmall.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lixin.freshmall.R;
import com.lixin.freshmall.activity.GoEvaluateActivity;
import com.lixin.freshmall.activity.MyApplication;
import com.lixin.freshmall.model.Constant;
import com.lixin.freshmall.model.MyEvaluateBean;
import com.lixin.freshmall.model.UserInfo;
import com.lixin.freshmall.okhttp.OkHttpUtils;
import com.lixin.freshmall.okhttp.budiler.StringCallback;
import com.lixin.freshmall.uitls.ImageManagerUtils;
import com.lixin.freshmall.uitls.ToastUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by 小火
 * Create time on  2017/12/7
 * My mailbox is 1403241630@qq.com
 */

public class MyEvaluateAdapter extends RecyclerView.Adapter<MyEvaluateAdapter.MyEvaluateViewHolder> {
    private Context context;
    private List<MyEvaluateBean.OrderCommodity> mList;
    private String uid, commentState;
    public MyEvaluateAdapter(Context context, List<MyEvaluateBean.OrderCommodity> mList, String uid, String commentState) {
        this.context = context;
        this.mList = mList;
        this.commentState = commentState;
        this.uid = uid;
    }

    @Override
    public MyEvaluateViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_my_evaluate,parent,false);
        MyEvaluateViewHolder viewHolder = new MyEvaluateViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final MyEvaluateViewHolder holder, int position) {
        final MyEvaluateBean.OrderCommodity orderCommodity = mList.get(position);
        String img = orderCommodity.getCommodityIcon();
        if (TextUtils.isEmpty(img)){
            holder.comm_icon.setImageResource(R.drawable.image_fail_empty);
        }else {
            ImageManagerUtils.imageLoader.displayImage(img,holder.comm_icon,ImageManagerUtils.options3);
        }
        holder.comm_title.setText(orderCommodity.getCommodityTitle() + "/" + orderCommodity.getCommodityUnit());
        holder.comm_flavor.setText(orderCommodity.getCommodityFirstParam() + " " + orderCommodity.getCommoditySecondParam());
        holder.comm_number.setText("x"+orderCommodity.getCommodityBuyNum());
        holder.comm_price.setText("￥"+orderCommodity.getCommodityNewPrice());
        if (commentState.equals("0")){
            holder.comm_content.setVisibility(View.GONE);
            holder.tv_pay.setText("去评价");
            holder.tv_pay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putString("commentId",orderCommodity.getCommentId());
                    MyApplication.openActivity(context,GoEvaluateActivity.class,bundle);
                }
            });
        }else if (commentState.equals("1")){
            holder.comm_content.setVisibility(View.VISIBLE);
            holder.comm_content.setText(orderCommodity.getCommentContent());
            holder.tv_pay.setVisibility(View.GONE);
        }
        holder.tv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCancelOrder(orderCommodity.getCommentId(),holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public class MyEvaluateViewHolder extends RecyclerView.ViewHolder{
        ImageView comm_icon;
        TextView comm_title,comm_flavor,comm_price,comm_number,comm_content,tv_delete,tv_pay;
        public MyEvaluateViewHolder(View itemView) {
            super(itemView);
            comm_icon = itemView.findViewById(R.id.comm_icon);
            comm_title = itemView.findViewById(R.id.comm_title);
            comm_flavor = itemView.findViewById(R.id.comm_flavor);
            comm_price = itemView.findViewById(R.id.comm_price);
            comm_number = itemView.findViewById(R.id.comm_number);
            comm_content = itemView.findViewById(R.id.comm_content);
            tv_delete = itemView.findViewById(R.id.tv_delete);
            tv_pay = itemView.findViewById(R.id.tv_pay);
        }
    }

    private void getCancelOrder(String commentId, final int position) {
        Map<String, String> params = new HashMap<>();
        final String json="{\"cmd\":\"deleteCommentedCommodity\",\"commentId\":\"" + commentId +"\",\"uid\":\""
                + uid + "\"}";
        params.put("json", json);
        Log.i("MyAllOrderFragment", "onResponse: " + json);
        OkHttpUtils.post().url(Constant.THE_SERVER_URL).params(params)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ToastUtils.makeText(context, e.getMessage());
            }
            @Override
            public void onResponse(String response, int id) {
                Log.i("MyAllOrderFragment", "onResponse: " + response);
                Gson gson = new Gson();
                UserInfo userInfo = gson.fromJson(response, UserInfo.class);
                if (userInfo.getResult().equals("1")) {
                    ToastUtils.makeText(context, userInfo.getResultNote());
                    return;
                }
                ToastUtils.makeText(context,"订单删除成功！");
                mList.remove(position);
                notifyDataSetChanged();
            }
        });
    }
}
