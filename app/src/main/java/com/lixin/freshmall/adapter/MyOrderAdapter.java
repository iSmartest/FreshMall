package com.lixin.freshmall.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lixin.freshmall.R;
import com.lixin.freshmall.activity.MyApplication;
import com.lixin.freshmall.activity.NowBuyActivity;
import com.lixin.freshmall.activity.OrderDecActivity;
import com.lixin.freshmall.activity.RefundDecActivity;
import com.lixin.freshmall.activity.WantRefundActivity;
import com.lixin.freshmall.dialog.LogOutDialog;
import com.lixin.freshmall.model.Constant;
import com.lixin.freshmall.model.GenerateOrderBean;
import com.lixin.freshmall.model.MyOrderBean;
import com.lixin.freshmall.model.MyWelletBean;
import com.lixin.freshmall.model.StoreTimeBean;
import com.lixin.freshmall.model.UserInfo;
import com.lixin.freshmall.okhttp.OkHttpUtils;
import com.lixin.freshmall.okhttp.budiler.StringCallback;
import com.lixin.freshmall.uitls.SPUtil;
import com.lixin.freshmall.uitls.ToastUtils;
import com.lixin.freshmall.uitls.Utility;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by 小火
 * Create time on  2017/10/30
 * My mailbox is 1403241630@qq.com
 */

public class MyOrderAdapter extends RecyclerView.Adapter<MyOrderAdapter.MyOrderViewHolder>{
    private Context context;
    private List<MyOrderBean.Orders> mList;
    private List<GenerateOrderBean.commoditys> list;
    private String uid;
    private String orderState;
    private String mMoney;
    private int nowPage = 1;
    private LogOutDialog mLogOutDialog;
    public MyOrderAdapter(Context context, List<MyOrderBean.Orders> mList, String uid, String orderState) {
        this.context = context;
        this.mList = mList;
        this.uid = uid;
        this.orderState = orderState;

    }

    @Override
    public MyOrderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_my_order_list,parent,false);
        MyOrderViewHolder viewHolder = new MyOrderViewHolder(view);
        return viewHolder;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public void onBindViewHolder(final MyOrderViewHolder viewHolder, final int position) {
        final MyOrderBean.Orders ordersList = mList.get(position);
        viewHolder.order_number.setText("订单号："+ordersList.getOrderId());
        OrderlistAdapter commodityAdapter = new OrderlistAdapter(ordersList.getOrderCommodity(),context);
        viewHolder.commodity_lv.setAdapter(commodityAdapter);
        Utility.setListViewHeightBasedOnChildren(viewHolder.commodity_lv);
        switch (orderState){
            case "1":
                viewHolder.order_state.setText("待付款");
                viewHolder.bt_delete.setVisibility(View.VISIBLE);
                viewHolder.bt_pay.setText("去付款");
                viewHolder.bt_delete.setText("取消订单");
                viewHolder.bt_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        mLogOutDialog = new LogOutDialog(context, "确定取消订单吗？","取消","确定",new LogOutDialog.OnSureBtnClickListener() {
                            @Override
                            public void sure() {
                                mLogOutDialog.dismiss();
                                int type = 0;
                                mList.remove(position);
                                getCancelOrder(ordersList.getOrderId(),type,position);
                            }
                        });
                        mLogOutDialog.show();
                    }
                });
                viewHolder.bt_pay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getdata(position);
                    }
                });
                break;
            case "2":
                viewHolder.order_state.setText("待取货");
                viewHolder.bt_delete.setText("我要退款");
                viewHolder.bt_pay.setVisibility(View.GONE);
                viewHolder.bt_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        initData(ordersList.getOrderId(),ordersList.getOderTotalPrice(),ordersList.getPaytime());
                    }
                });
                viewHolder.commodity_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Bundle bundle = new Bundle();
                        bundle.putString("orderId",ordersList.getOrderId());
                        bundle.putString("orderState",orderState);
                        bundle.putString("payTime",ordersList.getPaytime());
                        MyApplication.openActivity(context,OrderDecActivity.class,bundle);
                    }
                });
                break;
            case "3":
                viewHolder.order_state.setText("已完成");
                viewHolder.bt_pay.setVisibility(View.GONE);
                viewHolder.bt_delete.setText("删除订单");
                viewHolder.bt_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                            mLogOutDialog = new LogOutDialog(context, "确定删除订单吗？","取消","确定",new LogOutDialog.OnSureBtnClickListener() {
                                @Override
                                public void sure() {
                                    mLogOutDialog.dismiss();
                                    int type = 1;
                                    mList.remove(position);
                                    getCancelOrder(ordersList.getOrderId(),type,position);
                                }
                            });
                        mLogOutDialog.show();
                    }
                });
                viewHolder.commodity_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Bundle bundle = new Bundle();
                        bundle.putString("orderId",ordersList.getOrderId());
                        bundle.putString("orderState",orderState);
                        bundle.putString("payTime",ordersList.getPaytime());
                        MyApplication.openActivity(context,OrderDecActivity.class,bundle);
                    }
                });
                break;
            case "4":
                switch (ordersList.getOrderState()){
                    case "4":
                        viewHolder.order_state.setText("退款中");
                        break;
                    case "5":
                        viewHolder.order_state.setText("已退款");
                        break;
                    case "6":
                        viewHolder.order_state.setText("已拒绝");
                        break;
                }
                viewHolder.bt_delete.setVisibility(View.GONE);
                viewHolder.bt_pay.setText("查看退款详情");
                viewHolder.bt_pay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context,RefundDecActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("orderId",ordersList.getOrderId());
                        intent.putExtras(bundle);
                        context.startActivity(intent);
                    }
                });
                break;
            case "5":
                viewHolder.order_state.setText("待收货");
                viewHolder.bt_delete.setText("我要退款");
                viewHolder.bt_pay.setVisibility(View.GONE);
                viewHolder.bt_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        initData(ordersList.getOrderId(),ordersList.getOderTotalPrice(),ordersList.getPaytime());
                    }
                });
                viewHolder.commodity_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Bundle bundle = new Bundle();
                        bundle.putString("orderId",ordersList.getOrderId());
                        bundle.putString("orderState",orderState);
                        bundle.putString("payTime",ordersList.getPaytime());
                        MyApplication.openActivity(context,OrderDecActivity.class,bundle);
                    }
                });
                break;
        }
    }

    private void getdata(final int position) {
        Map<String, String> params = new HashMap<>();
        final String json="{\"cmd\":\"getWalletInfo\",\"nowPage\":\"" + nowPage +"\",\"uid\":\""
                + uid + "\"}";
        params.put("json", json);
        OkHttpUtils.post().url(Constant.THE_SERVER_URL).params(params)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ToastUtils.makeText(context, e.getMessage());
            }
            @Override
            public void onResponse(String response, int id) {
                Log.i("MyWalletActivity", "onResponse: " + response);
                Gson gson = new Gson();
                MyWelletBean myWelletBean = gson.fromJson(response, MyWelletBean.class);
                if (myWelletBean.result.equals("1")) {
                    ToastUtils.makeText(context, myWelletBean.resultNote);
                    return;
                }
                Double temp = 0.00;
                for (int i = 0; i < mList.get(position).getOrderCommodity().size() ; i++) {
                    temp =  temp + Double.parseDouble(mList.get(position).getOrderCommodity().get(i).getCommodityNewPrice()) * Double.parseDouble(mList.get(position).getOrderCommodity().get(i).getCommodityBuyNum()) ;
                }
                list = new ArrayList<>();
                for (int i = 0; i < mList.get(position).getOrderCommodity().size(); i++) {
                    MyOrderBean.Orders.orderCommodity mlist = mList.get(position).getOrderCommodity().get(i);
                    GenerateOrderBean.commoditys comm = new GenerateOrderBean.commoditys(mlist.getCommodityid(),
                            mlist.getCommodityTitle(),mlist.getCommodityIcon()
                            ,mlist.getCommodityFirstParam(),mlist.getCommoditySecondParam(),mlist.getCommodityNewPrice(),mlist.getCommodityBuyNum(),mlist.getCommodityUnit());
                    list.add(comm);
                }
                mMoney = myWelletBean.getTotalMoney();
                Intent intent = new Intent(context,NowBuyActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("orderId",mList.get(position).getOrderId());
                bundle.putString("mMoney",mMoney);
                bundle.putDouble("totalPrice",temp);
                bundle.putSerializable("OrderShop", (Serializable) list);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
    }

    private void getCancelOrder(String orderId, final int type, final int position) {
        Map<String, String> params = new HashMap<>();
        final String json="{\"cmd\":\"deleteOrder\",\"orderId\":\"" + orderId +"\",\"uid\":\""
                + uid + "\",\"type\":\"" + type + "\"}";
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
                if (type == 0){
                    ToastUtils.makeText(context,"订单取消成功！");
                    notifyDataSetChanged();
                }else if (type == 1){
                    ToastUtils.makeText(context,"订单删除成功！");
                    notifyDataSetChanged();
                }
            }
        });
    }
    class MyOrderViewHolder extends RecyclerView.ViewHolder {
        LinearLayout ll_order_list;
        TextView order_number;
        TextView order_state;
        ListView commodity_lv;
        TextView bt_pay;
        TextView bt_delete;

        public MyOrderViewHolder(View itemView) {
            super(itemView);
            ll_order_list = itemView.findViewById(R.id.ll_order_list);
            order_number = itemView.findViewById(R.id.order_number);
            order_state = itemView.findViewById(R.id.order_state);
            commodity_lv = itemView.findViewById(R.id.commodity_lv);
            bt_pay = itemView.findViewById(R.id.bt_pay);
            bt_delete = itemView.findViewById(R.id.bt_delete);

        }
    }

    private void initData(final String orderId, final String totalPrice,final String paytime) {
        String storeId = SPUtil.getString(context,"storeId");
        Map<String,String> params = new HashMap<>();
        String json = "{\"cmd\":\"getStoreInfoTime\",\"storeId\":\""+storeId+"\"}";
        params.put("json",json);
        OkHttpUtils.post().url(Constant.THE_SERVER_URL).params(params).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ToastUtils.makeText(context,e.getMessage());
            }
            @Override
            public void onResponse(String response, int id) {
                Log.i("getStoreInfoTime", "onResponse: " +response );
                Gson gson = new Gson();
                StoreTimeBean storeTimeBean = gson.fromJson(response,StoreTimeBean.class);
                if (storeTimeBean.getResult().equals("1")){
                    ToastUtils.makeText(context,storeTimeBean.getResultNote());
                    return;
                }
                int storeEndTime = storeTimeBean.getStoreEndTime();

                Date date = new Date();//取时间
                Calendar calendar = new GregorianCalendar();
                calendar.setTime(date);
                date = calendar.getTime(); //这个时间就是日期往后推一天的结果
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                String dateString = formatter.format(date);
                if (dateString.equals(paytime)){
                    int currentTime = calendar.get(Calendar.HOUR_OF_DAY);
                    if (currentTime >= storeEndTime){
                        ToastUtils.makeText(context,"已过截止时间，不能退单了");
                    } else {
                        Intent intent = new Intent(context,WantRefundActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("orderId",orderId);
                        bundle.putString("totalPrice",totalPrice);
                        intent.putExtras(bundle);
                        context.startActivity(intent);
                    }
                }else {
                    ToastUtils.makeText(context,"已过截止时间，不能退单了");
                }
            }
        });
    }
}
