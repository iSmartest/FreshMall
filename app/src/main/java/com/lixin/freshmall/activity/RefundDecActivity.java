package com.lixin.freshmall.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lixin.freshmall.R;
import com.lixin.freshmall.model.Constant;
import com.lixin.freshmall.model.RefundBean;
import com.lixin.freshmall.okhttp.OkHttpUtils;
import com.lixin.freshmall.okhttp.budiler.StringCallback;
import com.lixin.freshmall.uitls.ImageManagerUtils;
import com.lixin.freshmall.uitls.SPUtil;
import com.lixin.freshmall.uitls.ToastUtils;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;

/**
 * Created by 小火
 * Create time on  2017/12/4
 * My mailbox is 1403241630@qq.com
 */

public class RefundDecActivity extends BaseActivity {
    private ImageView mSuccessfulPicture,mRefundingPicture,mFailedPicture,mLine;
    private TextView mName1,mName2,mName3,mData1,mData2,mData3,mLoan,mState,mTitle,mMoney,mDetail,mWhere,mStateDec;
    private LinearLayout mLine1,mLine2,mContent1,mContent2;
    private CircleImageView mHead1,mHead2,mHead3;
    private String orderId;
    private String uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activtiy_refund_dec);
        Intent intent = getIntent();
        orderId = intent.getStringExtra("orderId");
        uid = SPUtil.getString(RefundDecActivity.this,"uid");
        hideBack(2);
        setTitleText("退款详情");
        initView();
        getdata();
    }
    private void initView() {
        mSuccessfulPicture = findViewById(R.id.img_refund_successful);
        mRefundingPicture = findViewById(R.id.img_refund_ing);
        mFailedPicture = findViewById(R.id.img_refund_failed);
        mHead1 = findViewById(R.id.ima_refund_head1);
        mHead2 = findViewById(R.id.ima_refund_head2);
        mHead3 = findViewById(R.id.ima_refund_head3);
        mName1 = findViewById(R.id.text_refund_name1);
        mName2 = findViewById(R.id.text_refund_name2);
        mName3 = findViewById(R.id.text_refund_name3);
        mData1 = findViewById(R.id.text_refund_data1);
        mData2 = findViewById(R.id.text_refund_data2);
        mData3 = findViewById(R.id.text_refund_data3);
        mLoan = findViewById(R.id.text_refund_loan);
        mState = findViewById(R.id.text_refund_state);
        mTitle = findViewById(R.id.text_refund_title);
        mMoney = findViewById(R.id.text_refund_money);
        mDetail = findViewById(R.id.text_refund_reason_dec);
        mWhere = findViewById(R.id.text_refund_money_in_where);
        mStateDec = findViewById(R.id.text_refund_state_dec);
        mLine1 = findViewById(R.id.Linear_refund_line1);
        mLine2 = findViewById(R.id.Linear_refund_line2);
        mContent1 = findViewById(R.id.linear_refund_content1);
        mContent2 = findViewById(R.id.linear_refund_content2);
        mLine = findViewById(R.id.ima_refund_line);

    }
    private void getdata() {
        Map<String, String> params = new HashMap<>();
        final String json="{\"cmd\":\"refundInfo\",\"orderId\":\"" + orderId +"\",\"uid\":\""
                + uid +"\"}";
        params.put("json", json);
        dialog1.show();
        OkHttpUtils.post().url(Constant.THE_SERVER_URL).params(params)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ToastUtils.makeText(context, e.getMessage());
                dialog1.dismiss();
            }
            @Override
            public void onResponse(String response, int id) {
                Log.i("RefundDecActivity", "onResponse: " + response);
                Gson gson = new Gson();
                dialog1.dismiss();
                RefundBean refundBean = gson.fromJson(response, RefundBean.class);
                if (refundBean.result.equals("1")) {
                    ToastUtils.makeText(context, refundBean.resultNote);
                    return;
                }
                String imag = refundBean.getUserIcon();
                mLoan.setText("￥" + refundBean.getRefundMoney());
                if (TextUtils.isEmpty(imag)){
                    mHead1.setImageResource(R.drawable.image_fail_empty);
                    mHead2.setImageResource(R.drawable.image_fail_empty);
                    mHead2.setImageResource(R.drawable.image_fail_empty);
                }else {
                    ImageManagerUtils.imageLoader.displayImage(imag,mHead1,ImageManagerUtils.options3);
                    ImageManagerUtils.imageLoader.displayImage(imag,mHead2,ImageManagerUtils.options3);
                    ImageManagerUtils.imageLoader.displayImage(imag,mHead3,ImageManagerUtils.options3);
                }
                mName1.setText(refundBean.getUserNickName());
                mName2.setText(refundBean.getUserNickName());
                mName3.setText(refundBean.getUserNickName());
                mData1.setText(refundBean.getReviewTime());
                mData2.setText(refundBean.getRefundSucessTime());
                mData3.setText(refundBean.getRefundSucessTime());
                mTitle.setText(refundBean.getRefundTitle());
                mMoney.setText(refundBean.getRefundMoney() + "元");
                mDetail.setText(refundBean.getRefundDetail());
                switch (refundBean.getRefundNewState()){
                    case "4":
                        mRefundingPicture.setVisibility(View.VISIBLE);
                        mSuccessfulPicture.setVisibility(View.GONE);
                        mFailedPicture.setVisibility(View.GONE);
                        mState.setText("退款中");
                        break;
                    case "5":
                        mRefundingPicture.setVisibility(View.GONE);
                        mSuccessfulPicture.setVisibility(View.VISIBLE);
                        mFailedPicture.setVisibility(View.GONE);
                        mState.setText("退款成功");
                        mLine1.setVisibility(View.VISIBLE);
                        mLine2.setVisibility(View.VISIBLE);
                        mLine.setVisibility(View.VISIBLE);
                        mContent1.setVisibility(View.VISIBLE);
                        mContent2.setVisibility(View.VISIBLE);
                        mWhere.setText("退款已入账");
                        mStateDec.setText("平台审核通过已完成退款");
                        break;
                    case "6":
                        mRefundingPicture.setVisibility(View.GONE);
                        mSuccessfulPicture.setVisibility(View.GONE);
                        mFailedPicture.setVisibility(View.VISIBLE);
                        mState.setText("退款失败");
                        mLine1.setVisibility(View.VISIBLE);
                        mLine2.setVisibility(View.VISIBLE);
                        mLine.setVisibility(View.VISIBLE);
                        mContent1.setVisibility(View.VISIBLE);
                        mContent2.setVisibility(View.VISIBLE);
                        mWhere.setText("退款申请被驳回");
                        mStateDec.setText("平台未审核通过不能退款");
                        break;
                }
            }
        });
    }
}
