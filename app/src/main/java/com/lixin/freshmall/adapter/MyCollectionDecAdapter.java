package com.lixin.freshmall.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lixin.freshmall.R;
import com.lixin.freshmall.dialog.LogOutDialog;
import com.lixin.freshmall.model.Constant;
import com.lixin.freshmall.model.MyCollection;
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
 * Create time on  2017/5/31
 * My mailbox is 1403241630@qq.com
 */

public class MyCollectionDecAdapter extends BaseAdapter{
    private Context context;
    private List<MyCollection.commoditysList.commoditys> commoditys;
    private ModifyCountInterface modifyCountInterface;
    private String uid;
    public MyCollectionDecAdapter(List<MyCollection.commoditysList.commoditys> commoditys, Context context, String uid) {
        this.context = context;
        this.commoditys = commoditys;
        this.uid = uid;
    }
    public void setModifyCountInterface(ModifyCountInterface modifyCountInterface) {
        this.modifyCountInterface = modifyCountInterface;
    }
    @Override
    public int getCount() {
        return commoditys == null ? 0 : commoditys.size();
    }

    @Override
    public Object getItem(int position) {
        return commoditys.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_my_collection_share_dec,null);
            viewHolder = new ViewHolder();
            viewHolder.mDelete = convertView.findViewById(R.id.iv_my_collection_share_delete);
            viewHolder.mPicture = convertView.findViewById(R.id.iv_my_collection_share_shop_picture);
            viewHolder.mTitle = convertView.findViewById(R.id.text_my_collection_share_shop_name);
            viewHolder.mNowPrice = convertView.findViewById(R.id.text_my_collection_share_shop_price);
            viewHolder.mOriginalPrice = convertView.findViewById(R.id.text_my_collection_share_shop_original_price);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final MyCollection.commoditysList.commoditys mList = commoditys.get(position);
        viewHolder.mTitle.setText(mList.getCommodityTitle() + "/" + mList.getCommodityUnit());
        viewHolder.mNowPrice.setText("￥"+ mList.getCommodityNewPrice());
        viewHolder.mOriginalPrice.setText("市场价：￥"+ mList.getCommodityOriginalPrice());
        viewHolder.mOriginalPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        String url = mList.getCommodityIcon();
        if (TextUtils.isEmpty(url)){
            viewHolder.mPicture.setImageResource(R.drawable.image_fail_empty);
        }else {
            ImageManagerUtils.imageLoader.displayImage(url,viewHolder.mPicture,ImageManagerUtils.options3);
        }

        viewHolder.mDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogOutDialog dialog = new LogOutDialog(context,R.string.delete_item, new LogOutDialog.OnSureBtnClickListener() {
                    @Override
                    public void sure() {
                        getRemove(mList.getCommodityid());
                    }
                });
                dialog.show();
            }
        });
        return convertView;
    }

    private void getRemove(String commodityid) {
        Map<String, String> params = new HashMap<>();
        final String json="{\"cmd\":\"deleCollection\",\"uid\":\"" + uid + "\",\"commodityid\":\"" + commodityid + "\"}";
        params.put("json", json);
        OkHttpUtils.post().url(Constant.THE_SERVER_URL).params(params)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ToastUtils.makeText(context, e.getMessage());
            }
            @Override
            public void onResponse(String response, int id) {
                Log.i("ShopCartFragment", "onResponse: " + response);
                Gson gson = new Gson();
                UserInfo shopCartBean = gson.fromJson(response, UserInfo.class);
                if (shopCartBean.getResult().equals("1")) {
                    ToastUtils.makeText(context, shopCartBean.getResultNote());
                    return;
                }
               ToastUtils.makeText(context,"删除成功");
               modifyCountInterface.childDelete(4);
            }
        });
    }

    class ViewHolder {
        ImageView mPicture,mDelete;
        TextView mTitle,mNowPrice,mOriginalPrice;
    }
    public interface ModifyCountInterface {
        void childDelete(int position);
    }
}
