package com.lixin.freshmall.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.CircleOptions;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.lixin.freshmall.R;
import com.lixin.freshmall.adapter.AddressAdapter;
import com.lixin.freshmall.adapter.SuggestAddressAdapter;
import com.lixin.freshmall.uitls.StatusBarUtil;
import com.lixin.freshmall.uitls.ToastUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 小火
 * Create time on  2017/12/18
 * My mailbox is 1403241630@qq.com
 */
public class LocationAndPoiSearchActivity extends FragmentActivity implements View.OnClickListener{
    private MapView mapView;//MapView地图视图
    private ListView location_List;
    private GeoCoder geoCoder;//地理编码对象
    private BaiduMap baiduMap;//百度地图
    private PoiSearch poiSearch;//PoiSearch对象
    private SuggestionSearch keyWordsPoiSearch;//在线建议查询
    private List<SuggestionResult.SuggestionInfo> keyWordPoiData;//在线建议查询结果集
    private boolean isFocus;//用于判断EditText是否获取了焦点
    private EditText location_name;
    private LinearLayout inputPoiSearchLayout;
    private RelativeLayout layout;
    private ListView inputPoiListView;
    private TextView textView[] = new TextView[4];
    private View view[] = new View[4];
    private int currentPosition;
    private SuggestAddressAdapter suggestAdapter;
    private AddressAdapter mAdapter;
    private List<PoiInfo> poiInfos;
    private List<OverlayOptions> options;
    private LatLng latLng;
    private int distance;
    private double longitude;
    private double latitude;
    private BitmapDescriptor otherMap, bitmap;
    private OverlayOptions ooCircle;
    private OverlayOptions option;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_location_and_poi_search);
        StatusBarUtil.transparentStatusBar(LocationAndPoiSearchActivity.this);
        distance = getIntent().getIntExtra("distance",1000);
        longitude = getIntent().getDoubleExtra("longitude",0.0);
        latitude = getIntent().getDoubleExtra("latitude",0.0);
        poiInfos = new ArrayList<>();
        options = new ArrayList<>();
        keyWordPoiData = new ArrayList<>();
        initView();
        init();
    }

    private void initView() {
        layout = findViewById(R.id.address_title_layout);
        RelativeLayout top_iag = findViewById(R.id.address_title_layout);
        StatusBarUtil.setHeightAndPadding(LocationAndPoiSearchActivity.this,top_iag);
        mapView = (MapView) findViewById(R.id.address_MapView);
        findViewById(R.id.back).setOnClickListener(this);
        location_name = (EditText) findViewById(R.id.location_name);
        inputPoiSearchLayout = (LinearLayout) findViewById(R.id.edit_search_poi);
        inputPoiListView = (ListView) findViewById(R.id.edit_search_poi_list);
        findViewById(R.id.im_search).setOnClickListener(this);
        findViewById(R.id.rl_all_poi).setOnClickListener(this);
        findViewById(R.id.rl_build_poi).setOnClickListener(this);
        findViewById(R.id.rl_plot_poi).setOnClickListener(this);
        findViewById(R.id.rl_school_poi).setOnClickListener(this);
        textView[0] = (TextView) findViewById(R.id.all_poi);
        textView[1] = (TextView) findViewById(R.id.build_poi);
        textView[2] = (TextView) findViewById(R.id.plot_poi);
        textView[3] = (TextView) findViewById(R.id.school_poi);
        view[0] = findViewById(R.id.cursor_0);
        view[1] = findViewById(R.id.cursor_1);
        view[2] = findViewById(R.id.cursor_2);
        view[3] = findViewById(R.id.cursor_3);
        location_List = findViewById(R.id.location_List);
        mAdapter = new AddressAdapter(LocationAndPoiSearchActivity.this, poiInfos);
        location_List.setAdapter(mAdapter);
        location_List.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String address = poiInfos.get(position).address;
                String name = poiInfos.get(position).name;
                double longitude = poiInfos.get(position).location.longitude;
                double latitude = poiInfos.get(position).location.latitude;
                Intent intent=new Intent();
                intent.putExtra("address", address);
                intent.putExtra("name", name);
                intent.putExtra("longitude", longitude);
                intent.putExtra("latitude", latitude);
                setResult(1007,intent);
                finish();
            }
        });
        location_name.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH){
                    if (TextUtils.isEmpty(location_name.getText().toString().trim())){
                        ToastUtils.makeText(LocationAndPoiSearchActivity.this,"请输入搜索内容");
                    }else {
                        changeTextColor(0);
                        nearByKeyPoiSearch(location_name.getText().toString().trim());
                    }
                    return true;
                }
                return false;
            }
        });
    }

    private void init() {
        baiduMap = mapView.getMap();
        geoCoder = GeoCoder.newInstance();
        geoCoder.reverseGeoCode(new ReverseGeoCodeOption().location(new LatLng(latitude,longitude)));
        latLng = new LatLng(latitude,longitude);
        baiduMap.setMyLocationEnabled(true);  //开启定位图层
        baiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(latLng));
        baiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        baiduMap.setBuildingsEnabled(false);
        baiduMap.setMaxAndMinZoomLevel(21, 10);
        bitmap = BitmapDescriptorFactory.fromResource(R.drawable.navigation);
        otherMap = BitmapDescriptorFactory.fromResource(R.drawable.marker);
        ooCircle = new CircleOptions().fillColor(0x000000FF).center(latLng).stroke(new Stroke(5, Color.rgb(0x23, 0x19, 0xDC))).radius(distance);
        option = new MarkerOptions().position(latLng).icon(bitmap);
        MapStatus.Builder builder = new MapStatus.Builder();
        builder.target(new LatLng(latitude,longitude)).zoom(15.0f);
        baiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
        baiduMap.addOverlay(ooCircle);
        baiduMap.addOverlay(option);
        nearByKeyPoiSearch("房子");
    }

    /**
     * 房子Poi数据搜索
     * @注意： 所有的Poi搜索都是异步完成的
     * @这里先nearByAllPoiSearch()方法执行完毕后再执行nearByBuildPoiSearch()..
     * @如果同时开启四个Poi让其分别进行查询,会出现很多bug
     * */
    private void nearByKeyPoiSearch(String key) {
        poiSearch = PoiSearch.newInstance();
        poiInfos.clear();
        options.clear();
        baiduMap.clear();
        baiduMap.addOverlay(ooCircle);
        baiduMap.addOverlay(option);
        poiSearch.setOnGetPoiSearchResultListener(new OnGetPoiSearchResultListener() {
            @Override
            public void onGetPoiResult(PoiResult poiResult) {
                if (poiResult.getAllPoi() != null) {
                    poiInfos.addAll(poiResult.getAllPoi());
                    for (int i = 0; i < poiInfos.size(); i++) {
                        OverlayOptions option1 =  new MarkerOptions()
                                .position(new LatLng(poiInfos.get(i).location.latitude,poiInfos.get(i).location.longitude))
                                .icon(otherMap);
                        options.add(option1);
                    }
                    baiduMap.addOverlays(options);
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {

            }

            @Override
            public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

            }
        });

        /**
         * 设置Poi Option
         * 当前搜索为附近搜索：以圆形的状态进行搜索
         * 还有两种其他的搜素方式：范围搜素和城市内搜索
         * */
        PoiNearbySearchOption nearbySearchOption = new PoiNearbySearchOption();
        nearbySearchOption.location(new LatLng(latitude,longitude)); //设置坐标
        nearbySearchOption.keyword(key);                                       //设置关键字
        nearbySearchOption.radius(distance);                                          //搜索范围的半径
        nearbySearchOption.pageCapacity(30);                                      //设置最多允许加载的poi数量,默认10
        poiSearch.searchNearby(nearbySearchOption);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.rl_all_poi:
                changeTextColor(0);
                nearByKeyPoiSearch("房子");
                break;
            case R.id.rl_build_poi:
                changeTextColor(1);
                nearByKeyPoiSearch("写字楼");
                break;
            case R.id.rl_plot_poi:
                changeTextColor(2);
                nearByKeyPoiSearch("小区");
                break;
            case R.id.rl_school_poi:
                changeTextColor(3);
                nearByKeyPoiSearch("学校");
                break;
            case R.id.im_search:
                changeTextColor(0);
                nearByKeyPoiSearch(location_name.getText().toString().trim());
                break;

        }
    }

    private void changeTextColor(int position) {
        setPageIndicator(position);
        textView[currentPosition].setTextColor(getResources().getColor(R.color.text_color_main));
        textView[position].setTextColor(getResources().getColor(R.color.text_color_green));
        currentPosition = position;
    }

    private void setPageIndicator(int index) {
        view[currentPosition].setVisibility(View.INVISIBLE);
        view[index].setVisibility(View.VISIBLE);
    }

    @Override
    protected void onDestroy() {
        baiduMap.setMyLocationEnabled(false);
        mapView.onDestroy();
        mapView = null;
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }
}
