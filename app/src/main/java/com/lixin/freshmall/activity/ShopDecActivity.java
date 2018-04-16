package com.lixin.freshmall.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hhl.library.FlowTagLayout;
import com.hhl.library.OnTagSelectListener;
import com.lixin.freshmall.R;
import com.lixin.freshmall.adapter.FlowTagAdapter;
import com.lixin.freshmall.dialog.ProgressDialog;
import com.lixin.freshmall.dialog.SeePictureDialog;
import com.lixin.freshmall.dialog.StatementSettlementDialog;
import com.lixin.freshmall.fragment.IntroduceFragment;
import com.lixin.freshmall.fragment.SpecFragment;
import com.lixin.freshmall.model.Constant;
import com.lixin.freshmall.model.GenerateOrderBean;
import com.lixin.freshmall.model.MaxBuyNum;
import com.lixin.freshmall.model.ShopDecBean;
import com.lixin.freshmall.model.SkuBean;
import com.lixin.freshmall.model.UserInfo;
import com.lixin.freshmall.okhttp.OkHttpUtils;
import com.lixin.freshmall.okhttp.budiler.StringCallback;
import com.lixin.freshmall.uitls.GlideImageLoader;
import com.lixin.freshmall.uitls.ImageManagerUtils;
import com.lixin.freshmall.uitls.SPUtil;
import com.lixin.freshmall.uitls.ToastUtils;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerClickListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by 小火
 * Create time on  2017/12/5
 * My mailbox is 1403241630@qq.com
 */

public class ShopDecActivity extends FragmentActivity implements View.OnClickListener, OnBannerClickListener {
    private View[] views;
    private Dialog dialog;
    private Banner banner;
    private Context context;
    private double tempPrice;
    private List<String> imag;
    private TextView[] mTextView;
    private ImageView IvCollection;
    private double totalPrice = 0.00;
    private int temp, collectType, flag;
    private SpecFragment mSpecFragment;
    private LinearLayout[] mLinearLayout;
    private SeePictureDialog mSeePictureDialog;
    private IntroduceFragment mIntroduceFragment;
    private List<GenerateOrderBean.commoditys> list;
    private Fragment currentFragment = new Fragment();
    private List<ShopDecBean.commoditySelectParameters> mList;
    private StatementSettlementDialog mStatementSettlementDialog;
    private List<String> rotateCommodityList = new ArrayList<>();
    private String rotateid, rotateIcon, mtitle, newPrice, townId, mShopUnit, url, uid, orderId, mMoney, commodityPrice, maxBuyNum, mInventory = "0";
    private TextView mShopName, mSalesVolume, mNowPrice, mMarketValue, TextCollection, mBuyNow, mAddShoppingCart, mLimitedNum, mStatementSettlement;
    private boolean isLimitBuy = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_dec);
        Intent intent = getIntent();
        context = this;
        dialog = ProgressDialog.createLoadingDialog(context, "加载中.....");
        mList = new ArrayList<>();
        rotateid = intent.getStringExtra("rotateid");
        rotateIcon = intent.getStringExtra("rotateIcon");
        uid = SPUtil.getString(ShopDecActivity.this, "uid");
        townId = SPUtil.getString(ShopDecActivity.this, "TownId");
        initView();
        getdata();
    }

    @Override
    protected void onResume() {
        super.onResume();
        uid = SPUtil.getString(ShopDecActivity.this, "uid");
    }

    private void initView() {
        banner = findViewById(R.id.img_shop_gallery);
        banner.setOnBannerClickListener(this);
        findViewById(R.id.Iv_shop_dec_back).setOnClickListener(this);
        findViewById(R.id.linear_shop_dec_collection).setOnClickListener(this);
        mShopName = findViewById(R.id.text_shop_dec_name);
        mLimitedNum = findViewById(R.id.text_shop_dec_limited_num);
        mSalesVolume = findViewById(R.id.text_shop_dec_sales_volume);
        mNowPrice = findViewById(R.id.text_shop_dec_now_price);
        mMarketValue = findViewById(R.id.text_shop_dec_market_value);
        TextCollection = findViewById(R.id.text_shop_dec_collection);
        IvCollection = findViewById(R.id.iv_shop_dec_collection);
        findViewById(R.id.linear_shop_dec_select_attribute).setOnClickListener(this);
        mStatementSettlement = findViewById(R.id.text_shop_dec_statement_settlement);
        mStatementSettlement.setOnClickListener(this);
        findViewById(R.id.linear_shop_dec_evaluate).setOnClickListener(this);
        mAddShoppingCart = findViewById(R.id.text_shop_dec_add_shopping_cart);
        mAddShoppingCart.setOnClickListener(this);
        mBuyNow = findViewById(R.id.text_shop_dec_buy_now);
        mBuyNow.setOnClickListener(this);
        mTextView = new TextView[2];
        mTextView[0] = findViewById(R.id.text_shop_dec_introduce);
        mTextView[1] = findViewById(R.id.text_shop_dec_spec);
        views = new View[2];
        views[0] = findViewById(R.id.v_cursor0);
        views[1] = findViewById(R.id.v_cursor1);
        mLinearLayout = new LinearLayout[2];
        mLinearLayout[0] = findViewById(R.id.linear_shop_dec_introduce);
        mLinearLayout[1] = findViewById(R.id.linear_shop_dec_spec);
        for (int i = 0; i < mLinearLayout.length; i++) {
            mLinearLayout[i].setId(i);
            mLinearLayout[i].setOnClickListener(this);
        }

    }

    private void setCurrent(int position, Fragment fragment) {
        if (currentFragment != fragment) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.hide(currentFragment);
            currentFragment = fragment;
            if (!fragment.isAdded()) {
                transaction.add(R.id.shop_dec_layout_content, fragment).show(fragment).commit();
            } else {
                transaction.show(fragment).commit();
            }
        }
        mLinearLayout[position].setSelected(true);
        Resources resource = context.getResources();
        ColorStateList csl1 = resource.getColorStateList(R.color.text_color_main);
        ColorStateList csl2 = resource.getColorStateList(R.color.text_color_green);
        for (int i = 0; i < mLinearLayout.length; i++) {
            if (i != position) {
                mLinearLayout[i].setSelected(false);
                mTextView[i].setTextColor(csl1);
                views[i].setVisibility(View.INVISIBLE);
            } else {
                mTextView[i].setTextColor(csl2);
                views[i].setVisibility(View.VISIBLE);
            }
        }
    }

    private void getdata() {
        Map<String, String> params = new HashMap<>();
        final String json = "{\"cmd\":\"getCommoditysDetilInfo\",\"commodityid\":\"" + rotateid + "\",\"uid\":\"" + uid + "\"}";
        params.put("json", json);
        dialog.show();
        OkHttpUtils.post().url(Constant.THE_SERVER_URL).params(params)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ToastUtils.makeText(context, "网络异常");
                dialog.dismiss();
            }

            @Override
            public void onResponse(String response, int id) {
                Log.i("ShopDecActivity", "response: " + response);
                Gson gson = new Gson();
                dialog.dismiss();
                ShopDecBean shopDecBean = gson.fromJson(response, ShopDecBean.class);
                if (shopDecBean.getResult().equals("1")) {
                    ToastUtils.makeText(ShopDecActivity.this, shopDecBean.getResultNote());
                }
                List<String> rotateCommodityPics = shopDecBean.rotateCommodityPics;
                if (rotateCommodityPics != null && !rotateCommodityPics.isEmpty() && rotateCommodityPics.size() != 0) {
                    rotateCommodityList.addAll(rotateCommodityPics);
                    initTopViewData(rotateCommodityPics);
                }
                mShopName.setText(shopDecBean.getCommodityTitle());
                mtitle = shopDecBean.getCommodityTitle();
                url = shopDecBean.getCommodityWebLink();
                mShopUnit = shopDecBean.getCommodityUnit();
                mSalesVolume.setText("销量：" + shopDecBean.getCommoditysellerNum());
                mNowPrice.setText(shopDecBean.getCommodityNewPrice() + "元/" + shopDecBean.getCommodityUnit());
                newPrice = shopDecBean.getCommodityNewPrice();
                mMarketValue.setText("市场价:" + shopDecBean.getCommodityOriginalPrice() + "元/" + shopDecBean.getCommodityUnit());
                if (shopDecBean.getCommodityUnit().equals("斤")) {
                    mStatementSettlement.setVisibility(View.VISIBLE);
                } else {
                    mStatementSettlement.setVisibility(View.GONE);
                }
                if (shopDecBean.getCommodityMaxLimitationNum().length() == 0) {
                    mLimitedNum.setVisibility(View.INVISIBLE);
                    isLimitBuy = false;
                } else {
                    mLimitedNum.setVisibility(View.VISIBLE);
                    mLimitedNum.setText("每个用户限量购买" + shopDecBean.getCommodityMaxLimitationNum() + "个");
                    isLimitBuy = true;
                }
                if (TextUtils.isEmpty(shopDecBean.getCommodityMaxBuyNum())) {
                    maxBuyNum = "";
                } else {
                    maxBuyNum = shopDecBean.getCommodityMaxBuyNum();
                }
                List<ShopDecBean.commoditySelectParameters> commoditySelectParameters = shopDecBean.commoditySelectParameters;
                if (!commoditySelectParameters.isEmpty()) {
                    mList.addAll(commoditySelectParameters);
                }
                if (shopDecBean.getCommodityIscollotion() == 0) {
                    IvCollection.setImageResource(R.drawable.shop_collection);
                    TextCollection.setText("取消收藏");
                    temp = 0;
                } else if (shopDecBean.getCommodityIscollotion() == 1) {
                    IvCollection.setImageResource(R.drawable.shop_not_collection);
                    TextCollection.setText("收藏");
                    temp = 1;
                }
                mIntroduceFragment = IntroduceFragment.newInstance(url);
                mSpecFragment = SpecFragment.newInstance(rotateid);
                setCurrent(0, mIntroduceFragment);
            }
        });
    }

    private void initTopViewData(final List<String> rotateCommodityPics) {
        imag = new ArrayList<>();
        for (int i = 0; i < rotateCommodityPics.size(); i++) {
            imag.add(rotateCommodityPics.get(i));
        }
        banner.setImages(imag)
                .setBannerStyle(BannerConfig.NUM_INDICATOR)
                .setImageLoader(new GlideImageLoader())
                .start();
    }

    @Override
    public void OnBannerClick(int position) {
        if (rotateCommodityList != null && !rotateCommodityList.isEmpty()) {
            mSeePictureDialog = new SeePictureDialog(context, rotateCommodityList);
            mSeePictureDialog.show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Iv_shop_dec_back:
                finish();
                break;
            case R.id.text_shop_dec_statement_settlement:
                mStatementSettlementDialog = new StatementSettlementDialog(context);
                mStatementSettlementDialog.show();
                break;
            case 0:
                setCurrent(0, mIntroduceFragment);
                break;
            case 1:
                setCurrent(1, mSpecFragment);
                break;
            case R.id.linear_shop_dec_collection:
                if (TextUtils.isEmpty(uid)) {
                    MyApplication.openActivity(context,LoginActivity.class);
                } else {
                    if (temp == 0) {
                        collectType = 1;
                        getCollection(collectType);
                    } else if (temp == 1) {
                        collectType = 0;
                        getCollection(collectType);
                    }
                }
                break;
            case R.id.text_shop_dec_buy_now:
                if (TextUtils.isEmpty(uid)) {
                    MyApplication.openActivity(context,LoginActivity.class);
                } else {
                    getMaxBuyNum(0);
                }
                break;
            case R.id.text_shop_dec_add_shopping_cart:
                if (TextUtils.isEmpty(uid)) {
                    MyApplication.openActivity(context,LoginActivity.class);
                } else {
                    getMaxBuyNum(1);
                }
                break;
            case R.id.linear_shop_dec_select_attribute:
                if (TextUtils.isEmpty(uid)) {
                    MyApplication.openActivity(context,LoginActivity.class);
                } else {
                    if (mList.isEmpty()) {
                        ToastUtils.makeText(context, "暂无可选口味");
                    } else {
                        flag = 2;
                        CommodityAttribute mCommodityAttribute = new CommodityAttribute(ShopDecActivity.this);
                        mCommodityAttribute.showAtLocation(mBuyNow, Gravity.BOTTOM, 0, 0);
                    }
                }
                break;
            case R.id.linear_shop_dec_evaluate:
                Bundle bundle = new Bundle();
                bundle.putString("rotateid", rotateid);
                MyApplication.openActivity(context, ShopEvaluateActivity.class, bundle);
                break;
        }
    }

    private void getMaxBuyNum(final int position) {
        final Map<String, String> params = new HashMap<>();
        final String json = "{\"cmd\":\"getCommoditysNum\",\"commodityid\":\"" + rotateid + "\",\"uid\":\"" + uid + "\"}";
        params.put("json", json);
        OkHttpUtils.post().url(Constant.THE_SERVER_URL).params(params).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ToastUtils.makeText(context, e.getMessage());
            }

            @Override
            public void onResponse(String response, int id) {
                Log.i("dsd", "onResponse: " + response);
                Gson gson = new Gson();
                MaxBuyNum maxBuyNumBean = gson.fromJson(response, MaxBuyNum.class);
                if (maxBuyNumBean.getResult().equals("1")) {
                    ToastUtils.makeText(context, maxBuyNumBean.getResultNote());
                    return;
                }
                if (TextUtils.isEmpty(maxBuyNumBean.getCommodityMaxBuyNum())) {
                    maxBuyNum = "";
                } else {
                    maxBuyNum = maxBuyNumBean.getCommodityMaxBuyNum();
                }
                if (maxBuyNum.equals("0")) {
                    ToastUtils.makeText(context, "该商品已达今日购买上限");
                    return;
                }
                if (position == 1) {
                    if (mList.isEmpty()) {
                        getAddShoppingCart(1, "", "");
                    } else {
                        flag = 1;
                        CommodityAttribute mCommodityAttribute1 = new CommodityAttribute(ShopDecActivity.this);
                        mCommodityAttribute1.showAtLocation(mAddShoppingCart, Gravity.BOTTOM, 0, 0);
                    }
                } else if (position == 0) {
                    list = new ArrayList<>();
                    if (mList.isEmpty()) {
                        GenerateOrderBean.commoditys comm = new GenerateOrderBean.commoditys(rotateid,
                                mtitle, rotateIcon, "", "", newPrice, "1", mShopUnit);
                        list.add(comm);
                        getOrderData(list);
                        totalPrice = Double.valueOf(newPrice);
                    } else {
                        flag = 0;
                        CommodityAttribute mCommodityAttribute = new CommodityAttribute(ShopDecActivity.this);
                        mCommodityAttribute.showAtLocation(mBuyNow, Gravity.BOTTOM, 0, 0);
                    }
                }
            }
        });
    }

    private void getCollection(final int collectType) {
        Map<String, String> params = new HashMap<>();
        final String json = "{\"cmd\":\"collectCommoditys\",\"commodityid\":\"" + rotateid + "\",\"uid\":\"" + uid + "\",\"collectType\":" +
                "\"" + collectType + "\",\"townId\":\"" + townId + "\"}";
        params.put("json", json);
        dialog.show();
        OkHttpUtils.post().url(Constant.THE_SERVER_URL).params(params)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ToastUtils.makeText(context, "网络异常");
                dialog.dismiss();
            }

            @Override
            public void onResponse(String response, int id) {
                Log.i("ShopDecActivity", "response: " + response);
                Gson gson = new Gson();
                dialog.dismiss();
                UserInfo baseBean = gson.fromJson(response, UserInfo.class);
                if (baseBean.getResult().equals("1")) {
                    ToastUtils.makeText(ShopDecActivity.this, baseBean.getResultNote());
                }
                if (collectType == 0) {
                    ToastUtils.makeText(ShopDecActivity.this, "收藏成功！");
                    IvCollection.setImageResource(R.drawable.shop_collection);
                    TextCollection.setText("取消收藏");
                    temp = 0;
                } else if (collectType == 1) {
                    ToastUtils.makeText(ShopDecActivity.this, "已取消收藏!");
                    IvCollection.setImageResource(R.drawable.shop_not_collection);
                    TextCollection.setText("收藏");
                    temp = 1;
                }
            }
        });
    }

    private void getOrderData(final List<GenerateOrderBean.commoditys> list) {
        Map<String, String> params = new HashMap<>();
        GenerateOrderBean generateOrderBean = new GenerateOrderBean("buyCommodity", uid, townId, list);
        String json = new Gson().toJson(generateOrderBean);
        params.put("json", json);
        Log.i("ShopDecActivity", "getdata: " + json);
        dialog.show();
        OkHttpUtils.post().url(Constant.THE_SERVER_URL).params(params)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ToastUtils.makeText(context, e.getMessage());
                dialog.dismiss();
            }

            @Override
            public void onResponse(String response, int id) {
                Log.i("ShopDecActivity", "onResponse: " + response);
                Gson gson = new Gson();
                dialog.dismiss();
                UserInfo baseBean = gson.fromJson(response, UserInfo.class);
                if (baseBean.getResult().equals("1")) {
                    ToastUtils.makeText(context, baseBean.getResultNote());
                    return;
                }
                orderId = baseBean.getOrderId();
                mMoney = baseBean.getTotalMoney();
                SPUtil.putString(ShopDecActivity.this, "mATime", baseBean.getSendAtmTime());
                SPUtil.putString(ShopDecActivity.this, "mPTime", baseBean.getSendPtmTime());
                Intent intent = new Intent(ShopDecActivity.this, NowBuyActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("orderId", orderId);
                bundle.putString("mMoney", mMoney);
                bundle.putDouble("totalPrice", totalPrice);
                bundle.putSerializable("OrderShop", (Serializable) list);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    private void getAddShoppingCart(int commodityShooCarNum, String commodityFirstParam, String commoditySecondParam) {
        Map<String, String> params = new HashMap<>();
        final String json = "{\"cmd\":\"addShopCar\",\"commodityid\":\"" + rotateid + "\",\"uid\":\"" + uid + "\"" +
                ",\"commodityShooCarNum\":\"" + commodityShooCarNum + "\",\"commodityFirstParam\":\"" + commodityFirstParam + "\"" +
                ",\"commoditySecondParam\":\"" + commoditySecondParam + "\",\"townId\":\"" + townId + "\"}";
        params.put("json", json);
        Log.i("ShopDecActivity", "response: " + json);
        dialog.show();
        OkHttpUtils.post().url(Constant.THE_SERVER_URL).params(params).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ToastUtils.makeText(context, "网络异常");
                dialog.dismiss();
            }

            @Override
            public void onResponse(String response, int id) {
                Log.i("ShopDecActivity", "response: " + response);
                Gson gson = new Gson();
                dialog.dismiss();
                UserInfo baseBean = gson.fromJson(response, UserInfo.class);
                if (baseBean.getResult().equals("1")) {
                    ToastUtils.makeText(ShopDecActivity.this, baseBean.getResultNote());
                }
                ToastUtils.makeText(ShopDecActivity.this, "加入购物车成功");
                MyApplication.shopPay = 1;
            }
        });
    }

    /**
     * 商品属性PopupWind
     */
    public class CommodityAttribute extends PopupWindow {
        private View view;
        private TextView mNum, mShopStock, mShopPrice, mBack, mTitle, mTitle01;
        private ImageView mShopPicture;
        private FlowTagLayout mFlavor, mFlavor01;
        private LinearLayout linear_sku, linear_now_pay_or_cart, linear_ok;
        private String[] title;
        private String[] title01;
        private String commodityFirstParam;
        private String commoditySecondParam = "";

        public CommodityAttribute(final Activity context) {
            super(context);
            if (!mList.isEmpty()) {
                if (mList.size() > 1) {
                    title = new String[mList.get(0).getParameters().size()];
                    title01 = new String[mList.get(1).getParameters().size()];
                } else {
                    title = new String[mList.get(0).getParameters().size()];
                }
            }
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.sku_popupwindow, null);
            linear_sku = view.findViewById(R.id.linear_sku_second_params);
            linear_ok = view.findViewById(R.id.linear_ok);
            linear_now_pay_or_cart = view.findViewById(R.id.linear_now_pay_or_cart);
            //加 减 数量
            mNum = view.findViewById(R.id.tv_shop_num);
            //确定
            view.findViewById(R.id.popupwind_ok).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    list = new ArrayList<>();
                    int commodityShooCarNum = Integer.parseInt(mNum.getText().toString().trim());
                    String num = String.valueOf(commodityShooCarNum);
                    if (commodityShooCarNum > Integer.parseInt(mInventory)) {
                        ToastUtils.makeText(context, "该商品库存不足");
                        return;
                    }
                    if (flag == 0) {
                        GenerateOrderBean.commoditys comm = new GenerateOrderBean.commoditys(rotateid,
                                mtitle, rotateIcon
                                , commodityFirstParam, commoditySecondParam, commodityPrice, num, mShopUnit);
                        list.add(comm);
                        getOrderData(list);
                        totalPrice = tempPrice * Integer.parseInt(mNum.getText().toString().trim());
                    } else {
                        getAddShoppingCart(commodityShooCarNum, commodityFirstParam, commoditySecondParam);
                    }
                    dismiss();
                }
            });
            view.findViewById(R.id.text_sku_now_pay).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    list = new ArrayList<>();
                    int commodityShooCarNum = Integer.parseInt(mNum.getText().toString().trim());
                    String num = String.valueOf(commodityShooCarNum);
                    if (commodityShooCarNum > Integer.parseInt(mInventory)) {
                        ToastUtils.makeText(context, "该商品库存不足");
                        return;
                    }
                    GenerateOrderBean.commoditys comm = new GenerateOrderBean.commoditys(rotateid,
                            mtitle, rotateIcon
                            , commodityFirstParam, commoditySecondParam, commodityPrice, num, mShopUnit);
                    list.add(comm);
                    getOrderData(list);
                    totalPrice = tempPrice * Integer.parseInt(mNum.getText().toString().trim());
                    dismiss();
                }
            });
            view.findViewById(R.id.text_sku_add_shopping_cart).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int commodityShooCarNum = Integer.parseInt(mNum.getText().toString().trim());
                    if (commodityShooCarNum > Integer.parseInt(mInventory)) {
                        ToastUtils.makeText(context, "该商品库存不足");
                        return;
                    }
                    getAddShoppingCart(commodityShooCarNum, commodityFirstParam, commoditySecondParam);
                    dismiss();
                }
            });
            if (flag == 2) {
                linear_now_pay_or_cart.setVisibility(View.VISIBLE);
                linear_ok.setVisibility(View.GONE);
            } else {
                linear_ok.setVisibility(View.VISIBLE);
                linear_now_pay_or_cart.setVisibility(View.GONE);
            }
            view.findViewById(R.id.iv_shop_add).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int temp = Integer.parseInt(mNum.getText().toString().trim());
                    if (TextUtils.isEmpty(maxBuyNum)) {
                        if (Integer.parseInt(mInventory) > temp) {
                            temp++;
                        } else {
                            ToastUtils.makeText(ShopDecActivity.this, "库存不足");
                        }
                    } else {
                        if (Integer.parseInt(maxBuyNum) > temp) {
                            if (Integer.parseInt(mInventory) > temp) {
                                temp++;
                            } else {
                                ToastUtils.makeText(ShopDecActivity.this, "库存不足");
                            }
                        } else {
                            ToastUtils.makeText(ShopDecActivity.this, "您的可购买数量已达上限");
                        }
                    }
                    mNum.setText("" + temp);
                }
            });
            view.findViewById(R.id.iv_shop_reduce).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int temp = Integer.parseInt(mNum.getText().toString().trim());
                    temp--;
                    if (temp <= 1) {
                        mNum.setText("1");
                    } else {
                        mNum.setText("" + temp);
                    }
                }
            });
            // 属性名称 属性
            mTitle = view.findViewById(R.id.text_item_sku_first_title);
            mTitle01 = view.findViewById(R.id.text_item_sku_second_title);
            mFlavor = view.findViewById(R.id.tf_first_flavor);
            mFlavor01 = view.findViewById(R.id.tf_second_flavor);
            //商品详情，图片 价格 库存
            mShopPicture = view.findViewById(R.id.iv_shop_picture1);
            mShopPrice = view.findViewById(R.id.text_sku_shop_price);
            mShopStock = view.findViewById(R.id.text_sku_shop_stock);
            mBack = view.findViewById(R.id.text_sku_back);
            FlowTagAdapter<String> mFristFlavorAdapter = new FlowTagAdapter<>(context);
            FlowTagAdapter<String> mSecondFlavorAdapter = new FlowTagAdapter<>(context);
            mFlavor.setTagCheckedMode(FlowTagLayout.FLOW_TAG_CHECKED_SINGLE);
            mFlavor.setAdapter(mFristFlavorAdapter);
            mFlavor01.setTagCheckedMode(FlowTagLayout.FLOW_TAG_CHECKED_SINGLE);
            mFlavor01.setAdapter(mSecondFlavorAdapter);
            if (mList.size() == 2) {
                for (int i = 0; i < mList.get(0).getParameters().size(); i++) {
                    title[i] = mList.get(0).getParameters().get(i);
                }
                for (int i = 0; i < mList.get(1).getParameters().size(); i++) {
                    title01[i] = mList.get(1).getParameters().get(i);
                }
                commodityFirstParam = title[0];
                commoditySecondParam = title01[0];
                getSukdata();
                linear_sku.setVisibility(View.VISIBLE);
                mTitle.setText(mList.get(0).getParameterTitle());
                mTitle01.setText(mList.get(1).getParameterTitle());
                mFristFlavorAdapter.onlyAddAll(mList.get(0).getParameters());
                mSecondFlavorAdapter.onlyAddAll(mList.get(1).getParameters());
            } else if (mList.size() == 1) {
                linear_sku.setVisibility(View.GONE);
                mTitle.setText(mList.get(0).getParameterTitle());
                mFristFlavorAdapter.onlyAddAll(mList.get(0).getParameters());
                for (int i = 0; i < mList.get(0).getParameters().size(); i++) {
                    title[i] = mList.get(0).getParameters().get(i);
                }
                commodityFirstParam = title[0];
                getSukdata();
            }

            mFlavor.setOnTagSelectListener(new OnTagSelectListener() {
                @Override
                public void onItemSelect(FlowTagLayout parent, List<Integer> selectedList) {
                    if (selectedList != null && selectedList.size() > 0) {
                        StringBuilder sb = new StringBuilder();
                        for (int i : selectedList) {
                            sb.append(parent.getAdapter().getItem(i));
                        }
                        commodityFirstParam = sb.toString();
                        getSukdata();
                    } else {
                        ToastUtils.makeText(context, "请选择属性");
                    }
                }
            });
            mFlavor01.setOnTagSelectListener(new OnTagSelectListener() {
                @Override
                public void onItemSelect(FlowTagLayout parent, List<Integer> selectedList) {
                    if (selectedList != null && selectedList.size() > 0) {
                        StringBuilder sb = new StringBuilder();
                        for (int i : selectedList) {
                            sb.append(parent.getAdapter().getItem(i));
                        }
                        commoditySecondParam = sb.toString();
                        getSukdata();
                    } else {
                        ToastUtils.makeText(context, "请选择属性");
                    }
                }
            });

            mBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });

            // 设置SelectPicPopupWindow的View
            this.setContentView(view);
            // 设置SelectPicPopupWindow弹出窗体的宽
            this.setWidth(ViewPager.LayoutParams.MATCH_PARENT);
            // 设置SelectPicPopupWindow弹出窗体的高
            this.setHeight(ViewPager.LayoutParams.WRAP_CONTENT);
            // 在PopupWindow里面就加上下面两句代码，让键盘弹出时，不会挡住pop窗口。
            this.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
            this.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            // 设置popupWindow以外可以触摸
            this.setOutsideTouchable(true);
            // 以下两个设置点击空白处时，隐藏掉pop窗口
            this.setFocusable(true);
            this.setBackgroundDrawable(new BitmapDrawable());
            // 设置popupWindow以外为半透明0-1 0为全黑,1为全白
            backgroundAlpha(0.3f);
            // 添加pop窗口关闭事件
            this.setOnDismissListener(new poponDismissListener());
            // 设置动画--这里按需求设置成系统输入法动画
            this.setAnimationStyle(R.style.AnimBottom);
            // mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    int height = view.findViewById(R.id.linear_sku)
                            .getTop();
                    int y = (int) event.getY();
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        if (y < height) {
                            dismiss();
                        }
                    }
                    return true;
                }
            });
        }

        private void getSukdata() {
            Map<String, String> params = new HashMap<>();
            final String json1 = "{\"cmd\":\"selectCommodityParameters\",\"commodityid\":\"" + rotateid + "\",\"commodityFirstParam\":\"" + commodityFirstParam + "\"," +
                    "\"commoditySecondParam\":\"" + commoditySecondParam + "\"}";
            final String json2 = "{\"cmd\":\"selectCommodityParameters\",\"commodityid\":\"" + rotateid + "\",\"commodityFirstParam\":\"" + commodityFirstParam + "\"," +
                    "\"commoditySecondParam\":\"" + "" + "\"}";
            if (TextUtils.isEmpty(commoditySecondParam)) {
                params.put("json", json2);
                Log.i("sku", "getSukdata: " + json2);
            } else {
                params.put("json", json1);
                Log.i("sku", "getSukdata: " + json1);
            }
            dialog.show();
            OkHttpUtils.post().url(Constant.THE_SERVER_URL).params(params)
                    .build().execute(new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int id) {
                    ToastUtils.makeText(context, "网络异常");
                    dialog.dismiss();
                }

                @Override
                public void onResponse(String response, int id) {
                    Log.i("sku", "response: " + response);
                    Gson gson = new Gson();
                    dialog.dismiss();
                    SkuBean skuBean = gson.fromJson(response, SkuBean.class);
                    if (skuBean.getResult().equals("1")) {
                        ToastUtils.makeText(ShopDecActivity.this, skuBean.getResultNote());
                    }
                    mShopPrice.setText("￥" + skuBean.getCommodityNewPrice());
                    commodityPrice = skuBean.getCommodityNewPrice();
                    if (!TextUtils.isEmpty(skuBean.getCommodityNewPrice()))
                        tempPrice = Double.parseDouble(skuBean.getCommodityNewPrice());
                    if (TextUtils.isEmpty(skuBean.getCommodityInventory())) {
                        mInventory = "0";
                    } else {
                        mInventory = skuBean.getCommodityInventory();
                    }
                    mShopStock.setText("库存" + mInventory + "件");
                    if (TextUtils.isEmpty(skuBean.getCommodityIcon())) {
                        ImageManagerUtils.imageLoader.displayImage(rotateIcon, mShopPicture, ImageManagerUtils.options3);
                    } else {
                        ImageManagerUtils.imageLoader.displayImage(skuBean.getCommodityIcon(), mShopPicture, ImageManagerUtils.options3);
                    }
                }
            });
        }
    }

    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = this.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        this.getWindow().setAttributes(lp);
    }

    class poponDismissListener implements PopupWindow.OnDismissListener {

        @Override
        public void onDismiss() {
            backgroundAlpha(1f);
        }
    }
}
