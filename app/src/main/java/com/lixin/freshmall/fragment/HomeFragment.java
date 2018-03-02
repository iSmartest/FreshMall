package com.lixin.freshmall.fragment;

import android.app.Activity;
import android.content.Intent;
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
import com.lixin.freshmall.model.Constant;
import com.lixin.freshmall.model.HomeBean;
import com.lixin.freshmall.okhttp.OkHttpUtils;
import com.lixin.freshmall.okhttp.budiler.StringCallback;
import com.lixin.freshmall.uitls.GlideImageLoader;
import com.lixin.freshmall.uitls.SPUtil;
import com.lixin.freshmall.uitls.StatusBarUtil;
import com.lixin.freshmall.uitls.ToastUtils;
import com.lixin.freshmall.uitls.Utils;
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
    private ViewPager viewPager;
    private LinearLayout group;
    private EditText shopSearch;
    private ListView home_list;
    private HomeAdapter mAdapter;
    private Banner mSlideshow01;
    private View view, headView;
    private List<HomeBean.rotateTopCommoditys> rotateTopList;
    private List<HomeBean.ThemeList> mList;
    private List<HomeBean.classifyBottom> classifyBottomList;
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
        mList = new ArrayList<>();
        classifyBottomList = new ArrayList<>();
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
        viewPager = headView.findViewById(R.id.home_viewpager);
        group = headView.findViewById(R.id.home_points);
        home_list.addHeaderView(headView);
        mAdapter = new HomeAdapter(context,mList);
        home_list.setAdapter(mAdapter);
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
                List<HomeBean.rotateTopCommoditys> rotateTopCommoditys = homeBean.getRotateTopCommoditys();//顶部轮播图集合
                rotateTopList.addAll(rotateTopCommoditys);
                initTopViewData(rotateTopList);
                List<HomeBean.classifyBottom> classifyBottom = homeBean.getClassifyBottom();//类别
                classifyBottomList.addAll(classifyBottom);
                Constant.mClassifyBottom = classifyBottomList;
                initData(classifyBottom);
                List<HomeBean.ThemeList> themeLists = homeBean.getThemeList();
                if (themeLists != null && !themeLists.isEmpty() && themeLists.size() > 0){
                    mList.addAll(themeLists);
                    mAdapter.notifyDataSetChanged();
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

    // 类别展示
    private void initData(List<HomeBean.classifyBottom> classifyBottomList) {
        totalPage = (int) Math.ceil(classifyBottomList.size() * 1.0 / mPageSize);
        viewPagerList = new ArrayList<>();
        for (int i = 0; i < totalPage; i++) {
            final View view = LayoutInflater.from(context).inflate(R.layout.item_gridview, null);
            final GridView gridView = view.findViewById(R.id.gridView);
            gridView.setAdapter(new MyGridViewAdpter(getActivity(), classifyBottomList, i, mPageSize));
            final int finalI = i;
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                    final int pos = position + finalI * mPageSize;//假设mPageSie
                    callBackValue.SendMessageValue("6", pos);
                }
            });
            viewPagerList.add(view);
        }
        viewPager.setAdapter(new MyViewPagerAdapter(viewPagerList));
        ivPoints = new ImageView[totalPage];
        if (totalPage > 1) {
            for (int i = 0; i < totalPage; i++) {
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

            case R.id.linear_shop_hot:
                Intent intent1 = new Intent(getActivity(), MoreShopActivity.class);
                intent1.putExtra("flag", "1");
                startActivity(intent1);
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
