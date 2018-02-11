package com.lixin.freshmall.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lixin.freshmall.R;
import com.lixin.freshmall.model.Constant;
import com.lixin.freshmall.model.HomeBean;
import com.lixin.freshmall.model.MyAddressBean;
import com.lixin.freshmall.okhttp.OkHttpUtils;
import com.lixin.freshmall.okhttp.budiler.StringCallback;
import com.lixin.freshmall.uitls.SPUtil;
import com.lixin.freshmall.uitls.ToastUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by 小火
 * Create time on  2018/01/22
 * My mailbox is 1403241630@qq.com
 */

public class MyAddressAdapter extends BaseAdapter {

    private Context context;
    private List<MyAddressBean.AddressList> mList;

    public MyAddressAdapter(Context context, List<MyAddressBean.AddressList> mList) {
        this.context = context;
        this.mList = mList;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final AddressViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_address, null);
            viewHolder = new AddressViewHolder();
            viewHolder.mAddress = (TextView) convertView.findViewById(R.id.tv_my_address_personal_information);
            viewHolder.mName = (TextView) convertView.findViewById(R.id.text_item_my_address_name);
            viewHolder.mPhone = (TextView) convertView.findViewById(R.id.text_item_my_address_phone);
            viewHolder.mSend = convertView.findViewById(R.id.text_is_send);
            viewHolder.mNoSend = convertView.findViewById(R.id.text_is_no_send);
            viewHolder.mDelete = convertView.findViewById(R.id.iv_delete);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (AddressViewHolder) convertView.getTag();
        }
        final MyAddressBean.AddressList addressList = mList.get(position);
        viewHolder.mName.setText(addressList.getUserName());
        viewHolder.mPhone.setText(addressList.getPhone());
        viewHolder.mAddress.setText(addressList.getAddress());
        if (addressList.getInGround().equals("0")) {
            viewHolder.mSend.setVisibility(View.VISIBLE);
            viewHolder.mNoSend.setVisibility(View.GONE);
        } else {
            viewHolder.mSend.setVisibility(View.GONE);
            viewHolder.mNoSend.setVisibility(View.VISIBLE);
        }
        viewHolder.mDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mList.remove(position);
                deleteAddress(addressList.getAddressId());
            }
        });
        return convertView;
    }

    class AddressViewHolder {
        TextView mAddress, mName, mPhone;
        ImageView mSend,mNoSend, mDelete;
    }

    private void deleteAddress(String addressId) {
        Map<String, String> params = new HashMap<>();
        final String json = "{\"cmd\":\"deleteMyAddress\",\"uid\":\"" + SPUtil.getString(context, "uid") + "\",\"addressId\":\"" + addressId + "\"}";
        params.put("json", json);
        Log.i("sdfg", "onResponse: " + json);
        OkHttpUtils.post().url(Constant.THE_SERVER_URL).params(params)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ToastUtils.makeText(context, "网络异常");
            }

            @Override
            public void onResponse(String response, int id) {
                Log.i("sdfg", "onResponse: " + response);
                Gson gson = new Gson();
                HomeBean homeBean = gson.fromJson(response, HomeBean.class);
                if (homeBean.getResult().equals("1")) {
                    ToastUtils.makeText(context, homeBean.getResultNote());
                    return;
                }
                ToastUtils.makeText(context, "删除成功！");
                notifyDataSetChanged();
            }
        });
    }
}
