package com.lixin.freshmall.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lixin.freshmall.R;
import com.lixin.freshmall.activity.MoreShopActivity;
import com.lixin.freshmall.activity.ShopDecActivity;
import com.lixin.freshmall.model.HomeBean;
import com.lixin.freshmall.uitls.ImageManagerUtils;
import com.lixin.freshmall.view.MyGridView;

import java.util.List;


/**
 * Created by 小火
 * Create time on  2017/5/16
 * My mailbox is 1403241630@qq.com
 */

public class HomeAdapter extends BaseAdapter {
    private Context context;
    private List<HomeBean.ThemeList> mList;
    public HomeAdapter(Context context, List<HomeBean.ThemeList> mList) {
        this.context = context;
        this.mList = mList;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_home,null);
            viewHolder = new ViewHolder();
            viewHolder.mThemeLogo = convertView.findViewById(R.id.iv_home_theme_logo);
            viewHolder.mThemeTitle = convertView.findViewById(R.id.iv_home_theme_title);
            viewHolder.mHomeMore = convertView.findViewById(R.id.text_home_more);
            viewHolder.mThemePicture = convertView.findViewById(R.id.iv_home_theme_picture);
            viewHolder.hotGrid = convertView.findViewById(R.id.grid_home_commodity);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final HomeBean.ThemeList themeList = mList.get(position);
        String imgLogo = themeList.getThemeLogo();
        if (TextUtils.isEmpty(imgLogo)){
            viewHolder.mThemeLogo.setImageResource(R.drawable.image_fail_empty);
        }else {
            ImageManagerUtils.imageLoader.displayImage(imgLogo,viewHolder.mThemeLogo,ImageManagerUtils.options3);
        }
        viewHolder.mThemeTitle.setText(themeList.getThemeTitle());
        String imgPicture = themeList.getThemePicture();
        if (TextUtils.isEmpty(imgPicture)){
            viewHolder.mThemePicture.setImageResource(R.drawable.image_fail_empty);
        }else {
            ImageManagerUtils.imageLoader.displayImage(imgPicture,viewHolder.mThemePicture,ImageManagerUtils.options3);
        }
        HomeHotGridAdapter homeHotGridAdapter = new HomeHotGridAdapter(context,themeList.getCommodityList());
        viewHolder.hotGrid.setAdapter(homeHotGridAdapter);
        viewHolder.hotGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(context,ShopDecActivity.class);
                intent.putExtra("rotateid",themeList.getCommodityList().get(position).getCommodityid());
                intent.putExtra("rotateIcon",themeList.getCommodityList().get(position).getCommodityIcon());
                context.startActivity(intent);
            }
        });
        viewHolder.mHomeMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(context, MoreShopActivity.class);
                intent1.putExtra("themeId", themeList.getThemeId());
                intent1.putExtra("themeTitle", themeList.getThemeTitle());
                context.startActivity(intent1);
            }
        });
        viewHolder.mThemePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(context, MoreShopActivity.class);
                intent1.putExtra("themeId", themeList.getThemeId());
                intent1.putExtra("themeTitle", themeList.getThemeTitle());
                context.startActivity(intent1);
            }
        });
        return convertView;
    }
    class ViewHolder {
        ImageView mThemeLogo,mThemePicture;
        TextView mThemeTitle,mHomeMore;
        MyGridView hotGrid;
    }
}
