package com.lixin.freshmall.fragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.lixin.freshmall.dialog.ProgressDialog;


/**
 * Created by 小火
 * Create time on  2017/6/23
 * My mailbox is 1403241630@qq.com
 */

public class BaseFragment extends Fragment {
    protected Context context;
    protected Dialog dialog;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        dialog = ProgressDialog.createLoadingDialog(context, "加载中.....");
    }
}
