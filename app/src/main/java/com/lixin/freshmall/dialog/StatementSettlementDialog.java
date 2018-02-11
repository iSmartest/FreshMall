package com.lixin.freshmall.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.lixin.freshmall.R;

/**
 * Created by 小火
 * Create time on  2018/1/19
 * My mailbox is 1403241630@qq.com
 */

public class StatementSettlementDialog extends Dialog {

    private Context context;
    public StatementSettlementDialog(Context context){
        super(context);
        this.context = context;

        initView();
    }

    private void initView() {
        setContentView(R.layout.dialog_statement_settlement);
        try{
            int dividerID = context.getResources().getIdentifier("android:id/titleDivider", null, null);
            View divider=findViewById(dividerID);
            divider.setBackgroundColor(Color.TRANSPARENT);
        }catch(Exception e){
            //上面的代码，是用来去除Holo主题的蓝色线条
            e.printStackTrace();
        }
    }

    @Override
    public void show() {
        windowDeploy(0, 0);
        super.show();
    }

    public void windowDeploy(int x, int y) {
        Window window = getWindow(); // 得到对话框
        window.setBackgroundDrawableResource(R.drawable.transpant_bg); // 设置对话框背景为透明

        WindowManager.LayoutParams wl = window.getAttributes();
        wl.width = (int) (context.getApplicationContext().getResources()
                .getDisplayMetrics().widthPixels * 1);
        wl.x = x;
        wl.y = y;
        wl.gravity = Gravity.BOTTOM;// 设置重力
        window.setAttributes(wl);

    }


}
