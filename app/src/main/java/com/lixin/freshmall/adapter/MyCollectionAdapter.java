package com.lixin.freshmall.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.lixin.freshmall.R;
import com.lixin.freshmall.activity.MyApplication;
import com.lixin.freshmall.activity.ShopDecActivity;
import com.lixin.freshmall.model.MyCollection;
import com.lixin.freshmall.uitls.Utility;

import java.util.List;

/**
 * Created by 小火
 * Create time on  2017/5/31
 * My mailbox is 1403241630@qq.com
 */

public class MyCollectionAdapter extends RecyclerView.Adapter<MyCollectionAdapter.MyCollectionViewHolder>{
    private Context context;
    private List<MyCollection.commoditysList> mList;
    private String uid;
    public MyCollectionAdapter(Context context, List<MyCollection.commoditysList> mList, String uid) {
        this.context = context;
        this.mList = mList;
        this.uid = uid;
    }

    @Override
    public MyCollectionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_my_collection_share,parent,false);
        MyCollectionViewHolder viewHolder = new MyCollectionViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyCollectionViewHolder viewHolder, int position) {
        final MyCollection.commoditysList commoditysList = mList.get(position);
        viewHolder.mTime.setText(commoditysList.getSectionTime());
        MyCollectionDecAdapter myCollectionDecAdapter = new MyCollectionDecAdapter(commoditysList.getCommoditys(),context,uid);
        myCollectionDecAdapter.setModifyCountInterface((MyCollectionDecAdapter.ModifyCountInterface)context);
        viewHolder.mListView.setAdapter(myCollectionDecAdapter);
        Utility.setListViewHeightBasedOnChildren(viewHolder.mListView);
        viewHolder.mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putString("rotateid",commoditysList.getCommoditys().get(position).getCommodityid());
                bundle.putString("rotateIcon",commoditysList.getCommoditys().get(position).getCommodityIcon());
                MyApplication.openActivity(context,ShopDecActivity.class,bundle);
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
         return mList == null ? 0 : mList.size();
    }

    class MyCollectionViewHolder extends RecyclerView.ViewHolder {
        ListView mListView;
        TextView mTime;

        public MyCollectionViewHolder(View itemView) {
            super(itemView);
            mTime = itemView.findViewById(R.id.text_my_collection_share_time);
            mListView = itemView.findViewById(R.id.my_collection_share_list);
        }
    }
}
