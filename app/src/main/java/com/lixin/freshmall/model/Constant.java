package com.lixin.freshmall.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 小火
 * Create time on  2017/10/25
 * My mailbox is 1403241630@qq.com
 */

public class Constant {
    //服务器地址
    public static String THE_SERVER_URL = "http://47.100.98.32/freshshopservice/service.action";//正式
//    public static String THE_SERVER_URL = "http://192.168.3.65:8081/samecitylifeservice/service.action?";//正式
//    public static String THE_SERVER_URL = "http://192.168.3.13:8080/freshshopservice/service.action";//本地
    /**联网类型*/
    public static final class NetState {
        public static final int WIFI = 10030;
        public static final int Mobile = 10031;
        public static final int NOWAY = 10032;
    }
    public static final String FIRST_COME = "first_come";

    /**
     * Jpush返回参数
     */
    public static final String JPUSH_EXTRA = "jpush";
    public static String EXTRA = "extra";
    public static List<HomeBean.classifyBottom> mClassifyBottom;
    public static List<Receiver> mReceiver = new ArrayList<>();
    //充值协议
    public static String PROTOCOL = "http://47.100.98.32/freshshop/aboutus/disPlayDetail?id=3";
    //注册协议
    public static String REGISTER = "http://47.100.98.32/freshshop/aboutus/disPlayDetail?id=2";
}
