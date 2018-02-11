package com.lixin.freshmall.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lixin.freshmall.R;
import com.lixin.freshmall.activity.KnowLedgeWebActivity;
import com.lixin.freshmall.activity.LoginActivity;
import com.lixin.freshmall.activity.MoreShopActivity;
import com.lixin.freshmall.activity.MyApplication;
import com.lixin.freshmall.activity.MyMassageActivity;
import com.lixin.freshmall.activity.SearchShopActivity;
import com.lixin.freshmall.activity.SelectProvinceActivity;
import com.lixin.freshmall.activity.ShopDecActivity;
import com.lixin.freshmall.adapter.HomeAdapter;
import com.lixin.freshmall.adapter.MyGridViewAdpter;
import com.lixin.freshmall.adapter.MyViewPagerAdapter;
import com.lixin.freshmall.adapter.NewShopAdapter;
import com.lixin.freshmall.model.Constant;
import com.lixin.freshmall.model.HomeBean;
import com.lixin.freshmall.okhttp.OkHttpUtils;
import com.lixin.freshmall.okhttp.budiler.StringCallback;
import com.lixin.freshmall.uitls.GlideImageLoader;
import com.lixin.freshmall.uitls.ImageManagerUtils;
import com.lixin.freshmall.uitls.SPUtil;
import com.lixin.freshmall.uitls.StatusBarUtil;
import com.lixin.freshmall.uitls.ToastUtils;
import com.lixin.freshmall.uitls.Utils;
import com.lixin.freshmall.view.ImageSlideshow;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerClickListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by 小火
 * Create time on  2017/11/30
 * My mailbox is 1403241630@qq.com
 */

public class HomeFragment extends BaseFragment implements View.OnClickListener, View.OnTouchListener, OnBannerClickListener {
    private GridView newGrid;
    private ViewPager viewPager;
    private LinearLayout group;
    private EditText shopSearch;
    private ListView home_list;
    private Banner mSlideshow01;
    private HomeAdapter homeAdapter;
    private ImageView mPicture01;
    private ImageSlideshow mSlideshow02;
    private View view, headView, footView;
    private NewShopAdapter mNewShopAdapter;
    private ImageView mPicture02, mPicture03;
    private TextView mTitle01, mNowPrice01, mOldPrice01, mTitle02, mNowPrice02, mOldPrice02, mTitle03, mNowPrice03, mOldPrice03, name;
    private List<HomeBean.rotateTopCommoditys> rotateTopList;
    private List<HomeBean.rotateAdvertisement> rotateAdList;
    private List<HomeBean.classifyBottom> classifyBottomList;
    private List<HomeBean.promoteCommoditys> promoteCommoditysList;
    private List<HomeBean.newsCommoditys> newsCommoditysList;
    private List<String> imag;
    private CallBackValue callBackValue;
    private int totalPage; //总的页数
    private int mPageSize = 8; //每页显示的最大的数量
    private ImageView[] ivPoints;//小圆点图片的集合
    private List<View> viewPagerList;//GridView作为一个View对象添加到ViewPager集合中
    private int dotSize = 20;
    private int dotSpace = 30;
    private String TownId ,Town,StoreName;

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callBackValue = (HomeFragment.CallBackValue) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frgment_home, container, false);
        TownId = SPUtil.getString(context,"TownId");
        Town = SPUtil.getString(context,"Town");
        StoreName = SPUtil.getString(context,"storeName");
        rotateTopList = new ArrayList<>();
        rotateAdList = new ArrayList<>();
        classifyBottomList = new ArrayList<>();
        promoteCommoditysList = new ArrayList<>();
        newsCommoditysList = new ArrayList<>();
        initView();
        getdata();
        return view;

    }

    private void initView() {
        RelativeLayout mToolbar = view.findViewById(R.id.rl_home_toolbar);
        StatusBarUtil.setHeightAndPadding(getActivity(), mToolbar);
        ((TextView)view.findViewById(R.id.text_home_city_location)).setText(Town);
        ((TextView)view.findViewById(R.id.text_home_store_name)).setText(StoreName);
        view.findViewById(R.id.ly_home_left).setOnClickListener(this);
        view.findViewById(R.id.im_home_search).setOnClickListener(this);
        view.findViewById(R.id.Iv_home_message).setOnClickListener(this);
        shopSearch = view.findViewById(R.id.a_key_edt_search);
        shopSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (TextUtils.isEmpty(shopSearch.getText().toString().trim())) {
                        ToastUtils.makeText(getActivity(), "请输入商品名称");
                    } else {
                        Intent intent6 = new Intent(getActivity(), SearchShopActivity.class);
                        intent6.putExtra("searchKey", shopSearch.getText().toString().trim());
                        startActivity(intent6);
                    }
                    return true;
                }
                return false;
            }
        });

        home_list = view.findViewById(R.id.home_list);
        headView = LayoutInflater.from(getActivity()).inflate(R.layout.home_list_head, null);
        mSlideshow01 = headView.findViewById(R.id.img_home_gallery);
        mSlideshow01.setOnBannerClickListener(this);
        mSlideshow02 = headView.findViewById(R.id.img_store_gallery1);
        viewPager = headView.findViewById(R.id.home_viewpager);
        group = headView.findViewById(R.id.home_points);
        headView.findViewById(R.id.linear_shop_promotion).setOnClickListener(this);

        headView.findViewById(R.id.linear_promotion_01).setOnClickListener(this);
        mPicture01 = headView.findViewById(R.id.iv_shop_picture01);
        mTitle01 = headView.findViewById(R.id.text_shop_title01);
        mNowPrice01 = headView.findViewById(R.id.text_shop_now_price01);
        mOldPrice01 = headView.findViewById(R.id.text_shop_old_price01);
        mOldPrice01.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);

        headView.findViewById(R.id.linear_promotion_02).setOnClickListener(this);
        mPicture02 = headView.findViewById(R.id.iv_shop_picture02);
        mTitle02 = headView.findViewById(R.id.text_shop_title02);
        mNowPrice02 = headView.findViewById(R.id.text_shop_now_price02);
        mOldPrice02 = headView.findViewById(R.id.text_shop_old_price02);
        mOldPrice02.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);

        headView.findViewById(R.id.linear_promotion_03).setOnClickListener(this);
        mPicture03 = headView.findViewById(R.id.iv_shop_picture03);
        mTitle03 = headView.findViewById(R.id.text_shop_title03);
        mNowPrice03 = headView.findViewById(R.id.text_shop_now_price03);
        mOldPrice03 = headView.findViewById(R.id.text_shop_old_price03);
        mOldPrice03.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);

        headView.findViewById(R.id.linear_shop_hot).setOnClickListener(this);

        footView = LayoutInflater.from(getActivity()).inflate(R.layout.home_list_foot, null);
        footView.findViewById(R.id.linear_shop_new).setOnClickListener(this);

        newGrid = footView.findViewById(R.id.grid_new_shop);
        mNewShopAdapter = new NewShopAdapter();
        newGrid.setAdapter(mNewShopAdapter);
        newGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), ShopDecActivity.class);
                intent.putExtra("rotateid", newsCommoditysList.get(position).getCommodityid());
                intent.putExtra("rotateIcon", newsCommoditysList.get(position).getCommodityIcon());
                startActivity(intent);
            }
        });
        home_list.addHeaderView(headView);
        home_list.addFooterView(footView);
        homeAdapter = new HomeAdapter();
        home_list.setAdapter(homeAdapter);
    }

    private void getdata() {
        Map<String, String> params = new HashMap<>();
        final String json = "{\"cmd\":\"getMianCommoditysInfo\",\"city\":\""+TownId+"\"}";
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
                Log.i("HomeFragment", "response: " + response);
                Gson gson = new Gson();
                dialog.dismiss();
                HomeBean homeBean = gson.fromJson(response, HomeBean.class);
                if (homeBean.getResult().equals("1")) {
                    ToastUtils.makeText(getActivity(), homeBean.getResultNote());
                }

                List<HomeBean.rotateTopCommoditys> rotateTopCommoditys = homeBean.rotateTopCommoditys;//顶部轮播图集合
                rotateTopList.addAll(rotateTopCommoditys);
                initTopViewData(rotateTopList);
                List<HomeBean.rotateAdvertisement> rotateAdvertisement = homeBean.rotateAdvertisement;//中部轮播图
                rotateAdList.addAll(rotateAdvertisement);
                initAdViewData(rotateAdList);
                List<HomeBean.classifyBottom> classifyBottom = homeBean.classifyBottom;//类别
                classifyBottomList.addAll(classifyBottom);
                Constant.mClassifyBottom = classifyBottomList;
                initData(classifyBottom);
                List<HomeBean.hotCommoditys> hotCommoditys = homeBean.hotCommoditys;
                homeAdapter.setHome(getActivity(), hotCommoditys);
                home_list.setAdapter(homeAdapter);
                List<HomeBean.newsCommoditys> newsCommoditys = homeBean.newsCommoditys;
                newsCommoditysList.addAll(newsCommoditys);
                mNewShopAdapter.setNewShop(getActivity(), newsCommoditysList);
                newGrid.setAdapter(mNewShopAdapter);
                List<HomeBean.promoteCommoditys> promoteCommoditys = homeBean.promoteCommoditys;
                promoteCommoditysList.addAll(promoteCommoditys);
                if(promoteCommoditys != null && !promoteCommoditys.isEmpty() && promoteCommoditys.size() >= 3) {
                    mTitle01.setText(promoteCommoditys.get(0).getCommodityTitle());
                    mTitle02.setText(promoteCommoditys.get(1).getCommodityTitle());
                    mTitle03.setText(promoteCommoditys.get(2).getCommodityTitle());

                    mNowPrice01.setText(promoteCommoditys.get(0).getCommodityNewPrice() + "元/" + promoteCommoditys.get(0).getCommodityUnit());
                    mNowPrice02.setText(promoteCommoditys.get(1).getCommodityNewPrice() + "元/" + promoteCommoditys.get(1).getCommodityUnit());
                    mNowPrice03.setText(promoteCommoditys.get(2).getCommodityNewPrice() + "元/" + promoteCommoditys.get(2).getCommodityUnit());

                    mOldPrice01.setText("市场价:" + promoteCommoditys.get(0).getCommodityOriginalPrice() + "元/" + promoteCommoditys.get(0).getCommodityUnit());
                    mOldPrice02.setText("市场价:" + promoteCommoditys.get(1).getCommodityOriginalPrice() + "元/" + promoteCommoditys.get(1).getCommodityUnit());
                    mOldPrice03.setText("市场价:" + promoteCommoditys.get(2).getCommodityOriginalPrice() + "元/" + promoteCommoditys.get(2).getCommodityUnit());
                    String img1 = promoteCommoditys.get(0).getCommodityIcon();
                    String img2 = promoteCommoditys.get(1).getCommodityIcon();
                    String img3 = promoteCommoditys.get(2).getCommodityIcon();
                    if (TextUtils.isEmpty(img1)) {
                        mPicture01.setImageResource(R.drawable.image_fail_empty);
                    } else {
                        ImageManagerUtils.imageLoader.displayImage(img1, mPicture01, ImageManagerUtils.options3);
                    }
                    if (TextUtils.isEmpty(img2)) {
                        mPicture02.setImageResource(R.drawable.image_fail_empty);
                    } else {
                        ImageManagerUtils.imageLoader.displayImage(img2, mPicture02, ImageManagerUtils.options3);
                    }
                    if (TextUtils.isEmpty(img3)) {
                        mPicture03.setImageResource(R.drawable.image_fail_empty);
                    } else {
                        ImageManagerUtils.imageLoader.displayImage(img3, mPicture03, ImageManagerUtils.options3);
                    }
                }
            }
        });
    }

    //顶部轮播图
    private void initTopViewData(List<HomeBean.rotateTopCommoditys> rotateTopList) {
        imag = new ArrayList<>();
        for (int i = 0; i < rotateTopList.size(); i++) {
            imag.add(rotateTopList.get(i).getRotateIcon());
        }
        mSlideshow01.setImages(imag)
                .setBannerStyle(BannerConfig.CIRCLE_INDICATOR)
                .setImageLoader(new GlideImageLoader())
                .start();
    }

    private void initAdViewData(final List<HomeBean.rotateAdvertisement> rotateAdList) {
        for (int i = 0; i < rotateAdList.size(); i++) {
            mSlideshow02.addImageTitle(rotateAdList.get(i).getAdvertisementIcon());
        }
        mSlideshow02.setDotSpace(12);
        mSlideshow02.setDotSize(12);
        mSlideshow02.setDelay(10000);
        mSlideshow02.setOnItemClickListener(new ImageSlideshow.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(context, ShopDecActivity.class);
                intent.putExtra("rotateid", rotateAdList.get(position).getAdvertisementid());
                intent.putExtra("rotateIcon", rotateAdList.get(position).getAdvertisementid());
                startActivity(intent);
            }
        });
        mSlideshow02.commit();
    }

    // 类别展示
    private void initData(List<HomeBean.classifyBottom> classifyBottomList) {
        //总的页数向上取整
        totalPage = (int) Math.ceil(classifyBottomList.size() * 1.0 / mPageSize);
        viewPagerList = new ArrayList<>();
        for (int i = 0; i < totalPage; i++) {
            //每个页面都是inflate出一个新实例
            final View view = LayoutInflater.from(context).inflate(R.layout.item_gridview, null);
            final GridView gridView = view.findViewById(R.id.gridView);
            gridView.setAdapter(new MyGridViewAdpter(getActivity(), classifyBottomList, i, mPageSize));
            //添加item点击监听
            final int finalI = i;
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                    final int pos = position + finalI * mPageSize;//假设mPageSie
                    callBackValue.SendMessageValue("6", pos);
                }
            });
            //每一个GridView作为一个View对象添加到ViewPager集合中
            viewPagerList.add(view);
        }
        //设置ViewPager适配器
        viewPager.setAdapter(new MyViewPagerAdapter(viewPagerList));
        //添加小圆点
        ivPoints = new ImageView[totalPage];
        if (totalPage > 1) {
            for (int i = 0; i < totalPage; i++) {
                //循坏加入点点图片组
                ivPoints[i] = new ImageView(context);
                if (i == 0) {
                    ivPoints[i].setImageResource(R.drawable.dot_selected);
                } else {
                    ivPoints[i].setImageResource(R.drawable.dot_unselected);
                }
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(dotSize, dotSize);
                layoutParams.leftMargin = dotSpace / 2;
                layoutParams.rightMargin = dotSpace / 2;
                layoutParams.topMargin = dotSpace / 2;
                layoutParams.bottomMargin = dotSpace / 2;
                group.addView(ivPoints[i], layoutParams);
            }
        } else {
            return;
        }

        //设置ViewPager的滑动监听，主要是设置点点的背景颜色的改变
        viewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < totalPage; i++) {
                    if (i == position) {
                        ivPoints[i].setImageResource(R.drawable.dot_selected);
                    } else {
                        ivPoints[i].setImageResource(R.drawable.dot_unselected);
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ly_home_left:
                MyApplication.openActivity(context, SelectProvinceActivity.class);
                break;
            case R.id.Iv_home_message:
                if (TextUtils.isEmpty(SPUtil.getString(context,"uid"))) {
                    MyApplication.openActivity(getActivity(), LoginActivity.class);
                } else {
                    MyApplication.openActivity(getActivity(), MyMassageActivity.class);
                }
                break;
            case R.id.im_home_search:
                if (TextUtils.isEmpty(shopSearch.getText().toString().trim())) {
                    ToastUtils.makeText(getActivity(), "请输入商品名称");
                } else {
                    Intent intent6 = new Intent(getActivity(), SearchShopActivity.class);
                    intent6.putExtra("searchKey", shopSearch.getText().toString().trim());
                    startActivity(intent6);
                }
                break;
            case R.id.linear_shop_promotion:
                Intent intent0 = new Intent(getActivity(), MoreShopActivity.class);
                intent0.putExtra("flag", "0");
                startActivity(intent0);
                break;
            case R.id.linear_shop_hot:
                Intent intent1 = new Intent(getActivity(), MoreShopActivity.class);
                intent1.putExtra("flag", "1");
                startActivity(intent1);
                break;
            case R.id.linear_shop_new:
                Intent intent2 = new Intent(getActivity(), MoreShopActivity.class);
                intent2.putExtra("flag", "2");
                startActivity(intent2);
                break;
            case R.id.linear_promotion_01:
                if(promoteCommoditysList != null && !promoteCommoditysList.isEmpty() && promoteCommoditysList.size() >= 3) {
                    Intent intent3 = new Intent(getActivity(), ShopDecActivity.class);
                    intent3.putExtra("rotateid", promoteCommoditysList.get(0).getCommodityid());
                    intent3.putExtra("rotateIcon", promoteCommoditysList.get(0).getCommodityIcon());
                    startActivity(intent3);
                }else {
                    ToastUtils.makeText(context,"暂无商品信息");
                }
                break;
            case R.id.linear_promotion_02:
                if(promoteCommoditysList != null && !promoteCommoditysList.isEmpty() && promoteCommoditysList.size() >= 3) {
                    Intent intent4 = new Intent(getActivity(), ShopDecActivity.class);
                    intent4.putExtra("rotateid", promoteCommoditysList.get(1).getCommodityid());
                    intent4.putExtra("rotateIcon", promoteCommoditysList.get(1).getCommodityIcon());
                    startActivity(intent4);
                }else {
                    ToastUtils.makeText(context,"暂无商品信息");
                }
                break;
            case R.id.linear_promotion_03:
                if(promoteCommoditysList != null && !promoteCommoditysList.isEmpty() && promoteCommoditysList.size() >= 3) {
                    Intent intent5 = new Intent(getActivity(), ShopDecActivity.class);
                    intent5.putExtra("rotateid", promoteCommoditysList.get(2).getCommodityid());
                    intent5.putExtra("rotateIcon", promoteCommoditysList.get(2).getCommodityIcon());
                    startActivity(intent5);
                }else {
                    ToastUtils.makeText(context,"暂无商品信息");
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void OnBannerClick(int position) {
        int type = rotateTopList.get(position-1).getRotateType();
        if (type == 0) {
            Intent intent = new Intent(context, KnowLedgeWebActivity.class);
            intent.putExtra(KnowLedgeWebActivity.TITLE,"活动详情");
            intent.putExtra(KnowLedgeWebActivity.URL, rotateTopList.get(position-1).getRotateid());
            startActivity(intent);
        } else if (type == 1) {
            Intent intent = new Intent(context, ShopDecActivity.class);
            intent.putExtra("rotateid", rotateTopList.get(position-1).getRotateid());
            intent.putExtra("rotateIcon", rotateTopList.get(position-1).getRotateIcon());
            startActivity(intent);
        }
    }

    //写一个回调接口
    public interface CallBackValue {
        void SendMessageValue(String strValue, int position);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {

        if (hidden) {
        } else {
            if (rotateTopList.isEmpty()) {
                if (Utils.isNetworkAvailable(getActivity())) {
                    getdata();
                } else {
                    ToastUtils.makeText(getActivity(), "呀！网络跑丢了！");
                }
            }
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return true;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // 拦截触摸事件，防止泄露下去
        view.setOnTouchListener(this);
    }
}
