package com.mirstone.sharelib.platform;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import com.mirstone.ShareContent;
import com.mirstone.sharelib.IPlatform;
import com.mirstone.sharelib.ShareManager;
import com.mirstone.sharelib.utils.BitmapUtil;
import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.share.WbShareCallback;
import com.sina.weibo.sdk.share.WbShareHandler;

/**
 * Created by lenovo on 2018/2/5/0005.
 */

public class WeiboPlatform implements IPlatform {
    private static final String TAG = "WeiboPlatform";

    @Override
    public WbShareHandler share(Activity activity) {
        if (activity == null) return null;
        ShareContent shareContent = ShareManager.getInstance().getShareContent();
        WbShareCallback wbShareCallback = ShareManager.getInstance().getWbShareCallback();
//        if (!isWBClientValid(activity)){
//            Toast.makeText(activity, activity.getString(R.string.share_wb_not_installed), Toast.LENGTH_SHORT).show();
//            if (wbShareCallback != null) {
//                wbShareCallback.onWbShareFail();
//            }
//            return null;
//        }
        WbShareHandler wbShareHandler = new WbShareHandler(activity);
        wbShareHandler.registerApp();
        WeiboMultiMessage weiboMultiMessage = new WeiboMultiMessage();
        TextObject textObject = new TextObject();
        textObject.text = shareContent.getContent();
        weiboMultiMessage.textObject = textObject;
        ImageObject imageObject = new ImageObject();
        String imagePath = shareContent.getImagePath();
        if (TextUtils.isEmpty(imagePath)){
            Bitmap bitmap = BitmapFactory.decodeResource(activity.getResources(), shareContent.getResId());
            imagePath = BitmapUtil.save(activity, bitmap);
        }
        imageObject.imagePath = imagePath;
        weiboMultiMessage.imageObject = imageObject;
        wbShareHandler.shareMessage(weiboMultiMessage, false);
        return wbShareHandler;
    }

    private boolean isWBClientValid(Context context){
        PackageManager packageManager = context.getPackageManager();
        String versionName = null;
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo("com.sina.weibo", 0);
            versionName = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            versionName = null;
        }
        return !TextUtils.isEmpty(versionName);
    }
}
