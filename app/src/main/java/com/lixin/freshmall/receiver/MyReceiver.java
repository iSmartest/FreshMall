package com.lixin.freshmall.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.lixin.freshmall.activity.MyCouponActivity;
import com.lixin.freshmall.activity.OrderMoneyDecActivity;
import com.lixin.freshmall.activity.RefundDecActivity;
import com.lixin.freshmall.activity.ShopDecActivity;
import com.lixin.freshmall.model.CommonLog;
import com.lixin.freshmall.model.Constant;
import com.lixin.freshmall.uitls.SPUtil;

import org.json.JSONException;
import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by 小火
 * Create time on  2017/6/1
 * My mailbox is 1403241630@qq.com
 */

public class MyReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        Bundle bundle = intent.getExtras();
        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            //send the Registration Id to your server...
            Log.i("MyReceiver", "接收Registration Id : " + regId);
        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            //processCustomMessage(context, bundle);
            Constant.EXTRA = bundle.getString(JPushInterface.EXTRA_EXTRA);
            Log.i("sdfdf", "接收到推送下来的自定义消息: " + Constant.EXTRA);
        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            System.out.println("[MyReceiver] 接收到推送下来的通知");
            int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
            Log.i("MyReceiver", "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);
        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            System.out.println("[MyReceiver] 用户点击打开了通知");
            openAppOrActivity(context, Constant.EXTRA);
            Log.i("sdfdf", "onReceive: " + Constant.EXTRA);
        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
            //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..
            Log.i("MyReceiver", "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
        } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
            boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
            Log.i("MyReceiver", "[MyReceiver]" + intent.getAction() + " connected state change to " + connected);
        } else {
            System.out.println();
            Log.i("MyReceiver", "[MyReceiver] Unhandled intent - " + intent.getAction());
        }
    }

    private void openAppOrActivity(Context context, String  extra) {
        //判断app进程是否处于前台 此时用户处于登陆状态userId!=0
        Log.i("sdfdf", "openAppOrActivity: " + extra);
        if (isLogin(context)) {
            Intent detailIntent = parseJpushBundle(context, extra);
            if (detailIntent == null)
                return;
            detailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(detailIntent);
        } else {
            Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
            if (launchIntent == null)
                return;
            launchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
            launchIntent.putExtra(Constant.JPUSH_EXTRA, extra);
            context.startActivity(launchIntent);
        }
    }
    public static Intent parseJpushBundle(Context context, String extra) {
        Intent detailIntent = null;
        Log.i("sdfdf", "parseJpushBundle: " + extra);
        if (!TextUtils.isEmpty(extra)){
            JSONObject json = null;
            try {
                json = new JSONObject(extra);
                int type = json.getInt("type");
                if (type == 0){
                    detailIntent = new Intent(context, MyCouponActivity.class);
                }else if (type == 1){
                    detailIntent = new Intent(context,ShopDecActivity.class);
                    detailIntent.putExtra("rotateid", json.getString("id"))
                            .putExtra("rotateIcon", "url");

                }else if (type == 2){
                    detailIntent = new Intent(context,RefundDecActivity.class);
                    detailIntent.putExtra("orderId", json.getString("id"));
                }else if (type == 3){
                    detailIntent = new Intent(context,OrderMoneyDecActivity.class);
                    detailIntent.putExtra("orderId", json.getString("id"));
                }
            } catch (JSONException e) {
                CommonLog.e(e);
            }

        }
        return detailIntent;
    }
    public static boolean isLogin(Context context) {
        if (!TextUtils.isEmpty(SPUtil.getString(context,"uid")))
            return true;
        else
            return false;
    }
}