package com.lixin.freshmall.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baidu.mapapi.search.core.PoiInfo;
import com.lixin.freshmall.R;

import java.util.List;

/**
 * Created by 小火
 * Create time on  2018/01/23
 * My mailbox is 1403241630@qq.com
 */
public class AddressAdapter extends android.widget.BaseAdapter {

    private Context context;
    private List<PoiInfo> poiData;

    public AddressAdapter(Context context, List<PoiInfo> poiData) {
        this.context = context;
        this.poiData = poiData;
    }

    @Override
    public int getCount() {
        return poiData.size();
    }

    @Override
    public Object getItem(int position) {
        return poiData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        final ViewHolder holder;
        PoiInfo poiInfo = poiData.get(position);
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.address_item, null);
            holder = new ViewHolder();
            holder.poiName = (TextView) convertView.findViewById(R.id.poiName);
            holder.poiAddress = (TextView) convertView.findViewById(R.id.poiAddress);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.poiName.setText(poiInfo.name);
        holder.poiAddress.setText(poiInfo.address);
        return convertView;
    }

    static class ViewHolder {
        TextView poiName;
        TextView poiAddress;
    }

}
