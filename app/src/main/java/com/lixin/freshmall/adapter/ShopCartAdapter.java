package com.lixin.freshmall.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lixin.freshmall.R;
import com.lixin.freshmall.dialog.LogOutDialog;
import com.lixin.freshmall.model.Constant;
import com.lixin.freshmall.model.ShopCartBean;
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
 * Create time on  2017/11/30
 * My mailbox is 1403241630@qq.com
 */

public class ShopCartAdapter extends RecyclerView.Adapter<ShopCartAdapter.ShopCartViewHolder>{
    private Context context;
    private List<ShopCartBean.Shop> mList;
    private CheckInterface checkInterface;
    private ModifyCountInterface modifyCountInterface;
    private String uid;
    public ShopCartAdapter(Context context, List<ShopCartBean.Shop> mList,String uid) {
        this.context = context;
        this.mList = mList;
        this.uid = uid;
    }
    /**
     * 单选接口
     *
     * @param checkInterface
     */
    public void setCheckInterface(CheckInterface checkInterface) {
        this.checkInterface = checkInterface;
    }

    /**
     * 改变商品数量接口
     *
     * @param modifyCountInterface
     */
    public void setModifyCountInterface(ModifyCountInterface modifyCountInterface) {
        this.modifyCountInterface = modifyCountInterface;
    }

    @Override
    public ShopCartViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_shop_cart,parent,false);
        ShopCartViewHolder viewHolder = new ShopCartViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ShopCartViewHolder viewHolder, final int position) {
        final ShopCartBean.Shop shopList = mList.get(position);
        viewHolder.mName.setText(shopList.getCommodityTitle());
        viewHolder.mFirstParam.setText(shopList.getCommodityFirstParam());
        viewHolder.mSecondParam.setText(shopList.getCommoditySecondParam());
        viewHolder.mNum.setText(shopList.getCommodityShooCarNum());
        viewHolder.mPrice.setText("￥" + shopList.getCommodityNewPrice() + "元");
        viewHolder.mChose.setChecked(shopList.isChoosed());
        String img = shopList.getCommodityIcon();
        if (TextUtils.isEmpty(img)) {
            viewHolder.mPicture.setImageResource(R.drawable.image_fail_empty);
        } else {
            ImageManagerUtils.imageLoader.displayImage(img, viewHolder.mPicture, ImageManagerUtils.options3);
        }
        viewHolder.mDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogOutDialog dialog = new LogOutDialog(context, R.string.delete_item, new LogOutDialog.OnSureBtnClickListener() {
                    @Override
                    public void sure() {
                        getRemove(shopList.getCommodityid(), shopList.getCommodityFirstParam(), shopList.getCommoditySecondParam(), position);
                    }
                });
                dialog.show();
            }
        });
        viewHolder.mAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modifyCountInterface.doIncrease(position, viewHolder.mNum, viewHolder.mChose.isChecked());//暴露增加接口
            }
        });
        viewHolder.mSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modifyCountInterface.doDecrease(position, viewHolder.mNum, viewHolder.mChose.isChecked());//暴露删减接口
            }
        });
        //单选框按钮
        viewHolder.mChose.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        shopList.setChoosed(((CheckBox) v).isChecked());
                        checkInterface.checkGroup(position, ((CheckBox) v).isChecked());//向外暴露接口
                    }
                }
        );

        viewHolder.mPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(context,ShopDecActivity.class);
//                intent.putExtra("rotateid",shopList.getCommodityid());
//                intent.putExtra("rotateIcon",shopList.getCommodityIcon());
//                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    class ShopCartViewHolder extends RecyclerView.ViewHolder{
        TextView mName, mFirstParam, mSecondParam, mNum, mPrice;
        ImageView mDelete, mPicture;
        CheckBox mChose;
        LinearLayout mAdd,mSub;
        public ShopCartViewHolder(View itemView) {
            super(itemView);
            mName = itemView.findViewById(R.id.tv_shop_cart_commodity_name);
            mFirstParam = itemView.findViewById(R.id.tv_shop_cart_first_param);
            mSecondParam = itemView.findViewById(R.id.tv_shop_cart_second_param);
            mNum = itemView.findViewById(R.id.tv_comm_num);
            mPrice = itemView.findViewById(R.id.tv_shop_cart_price);
            mSub = itemView.findViewById(R.id.iv_shop_cart_sub);
            mAdd = itemView.findViewById(R.id.iv_shop_cart_add);
            mDelete = itemView.findViewById(R.id.iv_shop_cart_delete);
            mPicture = itemView.findViewById(R.id.iv_shop_cart_show_pic);
            mChose = itemView.findViewById(R.id.ck_shop_cart_chose);
        }
    }

    private void getRemove(String commodityid, String commodityFirstParam, String commoditySecondParam, final int position) {
        Log.i("asdsad", "initView: " + "10");
        Map<String, String> params = new HashMap<>();
        final String json = "{\"cmd\":\"deleCommodityFromCar\",\"commodityid\":\"" + commodityid + "\",\"uid\":\"" + uid + "\"," +
                "\"commodityFirstParam\":\"" + commodityFirstParam + "\",\"commoditySecondParam\":\"" + commoditySecondParam + "\"}";
        params.put("json", json);
        Log.i("delete", "onResponse: " + json);
        OkHttpUtils.post().url(Constant.THE_SERVER_URL).params(params)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ToastUtils.makeText(context, "网络异常");
            }

            @Override
            public void onResponse(String response, int id) {
                Log.i("delete", "onResponse: " + response);
                Gson gson = new Gson();
                UserInfo baseBean = gson.fromJson(response, UserInfo.class);
                if (baseBean.getResult().equals("1")) {
                    ToastUtils.makeText(context, baseBean.getResultNote());
                }
                Log.i("asdsad", "initView: " + "11");
                modifyCountInterface.childDelete(position);//删除 从item中移除
            }
        });

    }

    /**
     * 复选框接口
     */
    public interface CheckInterface {
        /**
         * 组选框状态改变触发的事件
         *
         * @param position  元素位置
         * @param isChecked 元素选中与否
         */
        void checkGroup(int position, boolean isChecked);
    }


    /**
     * 改变数量的接口
     */
    public interface ModifyCountInterface {
        /**
         * 增加操作
         *
         * @param position      组元素位置
         * @param showCountView 用于展示变化后数量的View
         * @param isChecked     子元素选中与否
         */
        void doIncrease(int position, View showCountView, boolean isChecked);

        /**
         * 删减操作
         *
         * @param position      组元素位置
         * @param showCountView 用于展示变化后数量的View
         * @param isChecked     子元素选中与否
         */
        void doDecrease(int position, View showCountView, boolean isChecked);

        /**
         * 删除子item
         *
         * @param position
         */
        void childDelete(int position);
    }
}
