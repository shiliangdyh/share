package com.mirstone.sharelib.platform;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.mirstone.ShareContent;
import com.mirstone.sharelib.IPlatform;
import com.mirstone.sharelib.R;
import com.mirstone.sharelib.ShareManager;
import com.mirstone.sharelib.ShareType;
import com.mirstone.sharelib.utils.BitmapUtil;
import com.sina.weibo.sdk.share.WbShareHandler;
import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import java.util.ArrayList;

/**
 * Created by lenovo on 2018/2/5/0005.
 */

public class QQPlatform implements IPlatform {
    private static final String TAG = "QQPlatform";

    @Override
    public WbShareHandler share(Activity activity) {
        if (activity == null) return null;
        ShareContent shareContent = ShareManager.getInstance().getShareContent();
        IUiListener qqListener = ShareManager.getInstance().getQQListener();
        if (!isQQClientValid(activity)){
            String string = activity.getString(R.string.share_qq_not_installed);
            Toast.makeText(activity, string, Toast.LENGTH_SHORT).show();
            if (qqListener != null) {
                qqListener.onError(new UiError(-1, string, string));
            }
            return null;
        }

        String imagePath = shareContent.getImagePath();
        if (TextUtils.isEmpty(imagePath)){
            Bitmap bitmap = BitmapFactory.decodeResource(activity.getResources(), shareContent.getResId());
            imagePath = BitmapUtil.saveImage("temp", bitmap, activity.getCacheDir(), Bitmap.CompressFormat.JPEG);
        }
        ArrayList<String> imageUrls = new ArrayList<>();
        if (!TextUtils.isEmpty(imagePath)){
            imageUrls.add(imagePath);
        }

        Tencent tencent = Tencent.createInstance("100797209", activity);
        Bundle params = new Bundle();
        int platform = shareContent.getPlatform();
        if (platform == Platform.QZONE){
            if (TextUtils.isEmpty(shareContent.getUrl())){
                if (qqListener != null) {
                    qqListener.onError(new UiError(-1, "url is null", "url is null"));
                }
                return null;
            }
            params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
            params.putString(QzoneShare.SHARE_TO_QQ_TITLE, shareContent.getTitle());
            params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, shareContent.getContent());
            params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, shareContent.getUrl());
            params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, imageUrls);
            tencent.shareToQzone(activity, params, qqListener);
        }else if (platform == Platform.QQ){
            int shareType = shareContent.getShareType();
            if (shareType == ShareType.IMAGE){
                if (TextUtils.isEmpty(imagePath)){
                    if (qqListener != null) {
                        qqListener.onError(new UiError(-1, "imagePath is null", "imagePath is null"));
                    }
                    return null;
                }
                params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_IMAGE);
                params.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL, imagePath);
            }else if (shareType == ShareType.TEXT_IMAGE){
                if (TextUtils.isEmpty(shareContent.getUrl())){
                    if (qqListener != null) {
                        qqListener.onError(new UiError(-1, "url is null", "url is null"));
                    }
                    return null;
                }
                if (TextUtils.isEmpty(shareContent.getTitle())){
                    if (qqListener != null) {
                        qqListener.onError(new UiError(-1, "url is null", "url is null"));
                    }
                    return null;
                }
                params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
                params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, shareContent.getUrl());
                params.putString(QQShare.SHARE_TO_QQ_TITLE, shareContent.getTitle());
                params.putString(QQShare.SHARE_TO_QQ_SUMMARY, shareContent.getContent());
                params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, imagePath);
            }
            tencent.shareToQQ(activity, params, qqListener);
        }
        return null;
    }

    private boolean isQQClientValid(Context context) {
        PackageManager packageManager = context.getPackageManager();
        String versionName = null;
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo("com.tencent.mobileqq", 0);
            versionName = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            try {
                PackageInfo packageInfo = packageManager.getPackageInfo("com.tencent.mobileqqi", 0);
                versionName = packageInfo.versionName;
            } catch (Throwable var11) {
                try {
                    PackageInfo packageInfo = packageManager.getPackageInfo("com.tencent.qqlite", 0);
                    versionName = packageInfo.versionName;
                } catch (Throwable var10) {
                    try {
                        PackageInfo packageInfo = packageManager.getPackageInfo("com.tencent.minihd.qq", 0);
                        versionName = packageInfo.versionName;
                    } catch (Throwable var9) {
                        try {
                            PackageInfo packageInfo = packageManager.getPackageInfo("com.tencent.tim", 0);
                            versionName = packageInfo.versionName;
                        } catch (Throwable var8) {
                            versionName = null;
                        }
                    }
                }
            }
        }

        return !TextUtils.isEmpty(versionName);
    }

}
