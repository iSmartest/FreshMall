package com.lixin.freshmall.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lixin.freshmall.R;
import com.lixin.freshmall.model.Constant;
import com.lixin.freshmall.model.UserInfo;
import com.lixin.freshmall.okhttp.OkHttpUtils;
import com.lixin.freshmall.okhttp.budiler.StringCallback;
import com.lixin.freshmall.uitls.ImageManagerUtils;
import com.lixin.freshmall.uitls.ImageUtil;
import com.lixin.freshmall.uitls.PhotoUtil;
import com.lixin.freshmall.uitls.SPUtil;
import com.lixin.freshmall.uitls.ToastUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;

/**
 * Created by 小火
 * Create time on  2017/11/30
 * My mailbox is 1403241630@qq.com
 */

public class MyPersonalInformationActivity extends BaseActivity {
    private String userIcon, nickName;
    private CircleImageView mIcon;
    private TextView tvNickName, tvSex;
    private AlertDialog builder; //底部弹出菜单
    private static final int CODE_GALLERY_REQUEST = 0xa0;
    private static final int CODE_CAMERA_REQUEST = 0xa1;
    private static final int CODE_RESULT_REQUEST = 0xa2;
    private static final int CAMERA_PERMISSIONS_REQUEST_CODE = 0x03;
    private static final int STORAGE_PERMISSIONS_REQUEST_CODE = 0x04;
    private File fileUri = new File(Environment.getExternalStorageDirectory().getPath() + "/photo.jpg");
    private File fileCropUri = new File(Environment.getExternalStorageDirectory().getPath() + "/crop_photo.jpg");
    private Uri imageUri,cropImageUri;
    private String uid, userSex,mPicture = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_information);
        userIcon = SPUtil.getString(context, "userIcon");
        nickName = SPUtil.getString(context, "nickName");
        userSex = SPUtil.getString(context, "userSex");
        uid = SPUtil.getString(context, "uid");
        hideBack(2);
        setTitleText("个人信息");
        initView();
    }

    private void initView() {
        mIcon = findViewById(R.id.a_my_info_iv_header);
        mIcon.setOnClickListener(this);
        if (TextUtils.isEmpty(userIcon)) {
            mIcon.setImageResource(R.drawable.my_head_portrait);
        } else {
            ImageManagerUtils.imageLoader.displayImage(userIcon, mIcon, ImageManagerUtils.options);
        }
        findViewById(R.id.rl_personal_information_nick_name).setOnClickListener(this);
        findViewById(R.id.rl_personal_information_sex).setOnClickListener(this);
        findViewById(R.id.rl_personal_information_address_management).setOnClickListener(this);
        tvSex = findViewById(R.id.text_personal_information_sex);
        tvNickName = findViewById(R.id.text_personal_information_nick_name);
        if (TextUtils.isEmpty(nickName)) {
            tvNickName.setText("昵称");
        } else {
            tvNickName.setText(nickName);
        }
        if (TextUtils.isEmpty(userSex)) {
            tvSex.setText("请选择");
        } else {
            if (userSex.equals("0")) {
                tvSex.setText("男");
            } else {
                tvSex.setText("女");
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.a_my_info_iv_header:
                showChoosePicDialog();
                break;
            case R.id.tv_album://相册
                autoObtainStoragePermission();
                builder.dismiss();
                break;
            case R.id.tv_photograph://拍照
                autoObtainCameraPermission();
                builder.dismiss();
                break;
            case R.id.tv_cancel://取消
                builder.dismiss();
                break;
            case R.id.rl_personal_information_nick_name:
                Bundle bundle = new Bundle();
                bundle.putString("userSex", userSex);
                MyApplication.openActivityForResult(MyPersonalInformationActivity.this, ModifyNameActivity.class, bundle, 3001);
                break;
            case R.id.rl_personal_information_sex:
                new AlertDialog.Builder(MyPersonalInformationActivity.this).setMessage("性别")
                        .setPositiveButton("男", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                tvSex.setText("男");
                                userSex = "0";
                            }
                        }).setNegativeButton("女", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        tvSex.setText("女");
                        userSex = "1";
                    }
                }).show();
                break;
            case R.id.rl_personal_information_address_management:
                Bundle bundle1 = new Bundle();
                bundle1.putString("type","1");
                MyApplication.openActivity(MyPersonalInformationActivity.this,MyAddressListActivity.class,bundle1);
                break;
            default:
                break;
        }
    }

    private void showChoosePicDialog() {
        builder = new AlertDialog.Builder(context, R.style.Dialog).create(); // 先得到构造器
        builder.show();
        LayoutInflater factory = LayoutInflater.from(context);
        View view = factory.inflate(R.layout.dialog_photo_upload, null);
        builder.getWindow().setContentView(view);
        view.findViewById(R.id.tv_album).setOnClickListener(this);
        view.findViewById(R.id.tv_photograph).setOnClickListener(this);
        view.findViewById(R.id.tv_cancel).setOnClickListener(this);
        Window dialogWindow = builder.getWindow();
        dialogWindow.setWindowAnimations(R.style.Dialog);
        dialogWindow.setGravity(Gravity.BOTTOM);//显示在底部
        WindowManager m = getWindowManager();
        Display display = m.getDefaultDisplay();
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        Point size = new Point();
        display.getSize(size);
        p.width = size.x;
        dialogWindow.setAttributes(p);
    }

    /**
     * 自动获取相机权限
     */
    private void autoObtainCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                ToastUtils.makeText(this, "您已经拒绝过一次");

            }
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, CAMERA_PERMISSIONS_REQUEST_CODE);
        } else {//有权限直接调用系统相机拍照
            if (hasSdcard()) {
                imageUri = Uri.fromFile(fileUri);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                    imageUri = FileProvider.getUriForFile(MyPersonalInformationActivity.this, "com.lixin.freshmall.fileprovider", fileUri);//通过FileProvider创建一个content类型的Uri
                PhotoUtil.takePicture(this, imageUri, CODE_CAMERA_REQUEST);
            } else {
                ToastUtils.makeText(this, "设备没有SD卡！");
            }
        }
    }

    /**
     * 自动获取sdk权限
     */

    private void autoObtainStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSIONS_REQUEST_CODE);
        } else {
            PhotoUtil.openPic(this, CODE_GALLERY_REQUEST);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CAMERA_PERMISSIONS_REQUEST_CODE: {//调用系统相机申请拍照权限回调
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (hasSdcard()) {
                        imageUri = Uri.fromFile(fileUri);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                            imageUri = FileProvider.getUriForFile(MyPersonalInformationActivity.this, "com.lixin.freshmall.fileprovider", fileUri);//通过FileProvider创建一个content类型的Uri
                        PhotoUtil.takePicture(this, imageUri, CODE_CAMERA_REQUEST);
                    } else {
                        ToastUtils.makeText(this, "设备没有SD卡！");
                    }
                } else {
                    ToastUtils.makeText(this, "请允许打开相机！！");
                }
                break;
            }
            case STORAGE_PERMISSIONS_REQUEST_CODE://调用系统相册申请Sdcard权限回调
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    PhotoUtil.openPic(this, CODE_GALLERY_REQUEST);
                } else {
                    ToastUtils.makeText(this, "请允许打操作SDCard！！");
                }
                break;
        }
    }

    private static final int output_X = 480;
    private static final int output_Y = 480;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CODE_CAMERA_REQUEST://拍照完成回调
                    cropImageUri = Uri.fromFile(fileCropUri);
                    PhotoUtil.cropImageUri(this, imageUri, cropImageUri, 1, 1, output_X, output_Y, CODE_RESULT_REQUEST);
                    break;
                case CODE_GALLERY_REQUEST://访问相册完成回调
                    if (hasSdcard()) {
                        cropImageUri = Uri.fromFile(fileCropUri);
                        Uri newUri = Uri.parse(PhotoUtil.getPath(this, data.getData()));
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                            newUri = FileProvider.getUriForFile(this, "com.lixin.freshmall.fileprovider", new File(newUri.getPath()));
                        PhotoUtil.cropImageUri(this, newUri, cropImageUri, 1, 1, output_X, output_Y, CODE_RESULT_REQUEST);
                    } else {
                        ToastUtils.makeText(this, "设备没有SD卡！");
                    }
                    break;
                case CODE_RESULT_REQUEST:
                    Bitmap bitmap = PhotoUtil.getBitmapFromUri(cropImageUri, this);
                    if (bitmap != null) {
                        showImages(bitmap);
                    }
                    break;
            }
        }
        if (requestCode == 3001 && resultCode == 3002) {
            String result = data.getStringExtra("result");
            tvNickName.setText(result);
        }
    }

    private void showImages(Bitmap bitmap) {
        mIcon.setImageBitmap(bitmap);
        mPicture = ImageUtil.bitmaptoString(bitmap);
        String userName = tvNickName.getText().toString().trim();
        setdata(userName);
    }

    /**
     * 检查设备是否存在SDCard的工具方法
     */
    private static boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED);
    }

    private void setdata(final String userName) {
        Map<String, String> params = new HashMap<>();
        String json = "{\"cmd\":\"commitUserInfoDeatil\",\"uid\":\"" + uid + "\"" +
                ",\"userIcon\":\"" + mPicture + "\",\"userName\":\"" + userName + "\",\"userSex\":\"" + userSex + "\"}";
        params.put("json", json);
        Log.i("json", "updateUserMessage: " + json);
        dialog1.show();
        OkHttpUtils.post().url(Constant.THE_SERVER_URL).params(params).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                dialog1.dismiss();
                ToastUtils.makeText(context, e.getMessage());
            }

            @Override
            public void onResponse(String response, int id) {
                Log.i("response", "updateUserMessage: " + response);
                Gson gson = new Gson();
                dialog1.dismiss();
                UserInfo mUserInfo = gson.fromJson(response, UserInfo.class);
                if (mUserInfo.getResult().equals("1")) {
                    ToastUtils.makeText(context, mUserInfo.getResultNote());
                    return;
                }
                ToastUtils.makeText(context, "修改成功！");
                SPUtil.putString(context, "nickName", userName);
                SPUtil.putString(context, "userIcon", mUserInfo.getUserIconUrl());
            }
        });
    }
}
