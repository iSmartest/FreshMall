package com.lixin.freshmall.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.lixin.freshmall.R;
import com.lixin.freshmall.uitls.SPUtil;
import com.lixin.freshmall.uitls.ToastUtils;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.shareboard.ShareBoardConfig;

/**
 * Created by 小火
 * Create time on  2017/12/7
 * My mailbox is 1403241630@qq.com
 */

public class InvitingUsersActivity extends BaseActivity {
    private String invitation;
    private String userName;
    private ClipboardManager myClipboard;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inviting_users);
        hideBack(2);
        userName = SPUtil.getString(context, "userName");
        invitation = getIntent().getStringExtra("invitation");
        setTitleText("发展用户");
        initView();
    }

    private void initView() {

        myClipboard = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
        ((TextView) findViewById(R.id.text_my_invitation_code)).setText(invitation);
        findViewById(R.id.linear_copy_invitation).setOnClickListener(this);
        findViewById(R.id.linear_share_invitation).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.linear_copy_invitation:
                ClipData myClip;
                myClip = ClipData.newPlainText("text", invitation);
                myClipboard.setPrimaryClip(myClip);
                ToastUtils.makeText(context,"复制成功");
                break;
            case R.id.linear_share_invitation:
                ShareBoardConfig config = new ShareBoardConfig();
                ShareAction mShareAction = new ShareAction(this);
                config.setMenuItemBackgroundShape(ShareBoardConfig.BG_SHAPE_CIRCULAR);// 圆角背景
                config.setCancelButtonVisibility(false);
                config.setTitleVisibility(true);
                config.setTitleText("— 分享到 —");
                mShareAction.setDisplayList(SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE, SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE)
                        .withTitle("您的好友"+userName+"邀请您加入【溜哒兔】")
                        .withText("您的推荐码是:"+invitation+",赶快下载体验【溜哒兔】APP！")
                        .withMedia(new UMImage(context, R.mipmap.ic_launcher))
                        .withTargetUrl("http://www.zhlzw.com/UploadFiles/Article_UploadFiles/201204/20120412123929822.jpg")
                        .setCallback(umShareListener)
                        .open(config);
                break;
        }
    }

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA platform) {
            Log.d("plat", "platform" + platform);
            ToastUtils.makeText(context, "分享成功啦");
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            ToastUtils.makeText(context, "分享失败啦");
            if (t != null) {
                Log.d("throw", "throw:" + t.getMessage());
            }
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            ToastUtils.makeText(context, "分享取消了");
        }
    };
}
