package com.lixin.freshmall.activity;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.lixin.freshmall.uitls.CrashHandler;
import com.lixin.freshmall.uitls.ImageManagerUtils;
import com.lixin.freshmall.uitls.UILImageLoader;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.view.CropImageView;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by 小火
 * Create time on  2017/10/24
 * My mailbox is 1403241630@qq.com
 */

public class MyApplication extends Application {
    public static Context CONTEXT;
    private static MyApplication myApplication;
    public static int secondId = 0;
    public static int temp = 0;
    public static int shopCart = 0;
    public static int shopPay = 0;
    public static int evaluate = 0;
    @Override
    public void onCreate() {
        super.onCreate();
        CONTEXT = getApplicationContext();
        initImageLoader(this);
        UMShareAPI.get(this);
        JPushInterface.setDebugMode(true);//如果时正式版就改成false
        JPushInterface.init(this);
        PlatformConfig.setWeixin("wxf2e7a3ba30d356a2","e3ef710774f9306eac83324a8a82e18c");
        PlatformConfig.setQQZone("1106503568","IfX37v9duWew5JBQ");
        myApplication = this;
        initImagePicker();
        //崩溃错误日志写入本地文档
        CrashHandler catchExcep = new CrashHandler(this);
        Thread.setDefaultUncaughtExceptionHandler(catchExcep);
    }

    public static Context getContext(){
        return CONTEXT;
    }
    public static MyApplication getApplication() {
        return myApplication;
    }

    /**
     * 通过类名启动Activity
     *
     * @param targetClass
     */
    public static void openActivity(Context context, Class<?> targetClass) {
        openActivity(context, targetClass, null);
    }

    /**
     * 通过类名启动Activity，并且含有Bundle数据
     *
     * @param targetClass
     * @param extras
     */
    public static void openActivity(Context context, Class<?> targetClass,
                                    Bundle extras) {
        Intent intent = new Intent(context, targetClass);
        if (extras != null) {
            intent.putExtras(extras);
        }
        context.startActivity(intent);
    }

    public static void openActivityForResult(Activity activity,
                                             Class<?> targetClass, Bundle extras, int requestCode) {
        Intent intent = new Intent(activity, targetClass);
        if (extras != null) {
            intent.putExtras(extras);
        }
        activity.startActivityForResult(intent, requestCode);
    }

    public static void openActivityForResult(Activity activity,
                                             Class<?> targetClass, int requestCode) {
        openActivityForResult(activity, targetClass, null, requestCode);
    }

    /**
     * 通过Action启动Activity
     *
     * @param action
     */
    public static void openActivity(Context context, String action) {
        openActivity(context, action, null);
    }

    /**
     * 通过Action启动Activity，并且含有Bundle数据
     *
     * @param action
     * @param extras
     */
    public static void openActivity(Context context, String action,
                                    Bundle extras) {
        Intent intent = new Intent(action);
        if (extras != null) {
            intent.putExtras(extras);
        }
        context.startActivity(intent);
    }
    public void exit() {
        //使用状态统计-结束
        ImageManagerUtils.imageLoader.destroy();
        android.os.Process.killProcess(android.os.Process.myPid());
        ActivityManager activityMgr = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        activityMgr.killBackgroundProcesses(getPackageName());
        System.exit(0);
    }

    public static void initImageLoader(Context context) {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .threadPriority(Thread.NORM_PRIORITY - 2).denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(new Md5FileNameGenerator()).tasksProcessingOrder(QueueProcessingType.LIFO)
                .build();
        ImageLoader.getInstance().init(config);

        ImageManagerUtils.init();
    }

    private void initImagePicker() {
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new UILImageLoader());   //设置图片加载器
        imagePicker.setShowCamera(true);  //显示拍照按钮
        imagePicker.setCrop(true);        //允许裁剪（单选才有效）
        imagePicker.setSaveRectangle(true); //是否按矩形区域保存
        imagePicker.setSelectLimit(10);    //选中数量限制
        imagePicker.setStyle(CropImageView.Style.RECTANGLE);  //裁剪框的形状
        imagePicker.setFocusWidth(800);   //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        imagePicker.setFocusHeight(800);  //裁剪框的高度。单位像素（圆形自动取宽高最小值）
        imagePicker.setOutPutX(1000);//保存文件的宽度。单位像素
        imagePicker.setOutPutY(1000);//保存文件的高度。单位像素
    }
}
