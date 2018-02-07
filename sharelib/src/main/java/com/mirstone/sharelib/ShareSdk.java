package com.mirstone.sharelib;

import android.app.Activity;
import android.content.Intent;

import com.mirstone.ShareContent;
import com.mirstone.sharelib.listener.CallBack;
import com.mirstone.sharelib.listener.ShareResult;
import com.mirstone.sharelib.platform.Platform;
import com.mirstone.sharelib.platform.QQPlatform;
import com.mirstone.sharelib.platform.WXPlatform;
import com.mirstone.sharelib.platform.WeiboPlatform;

import java.lang.ref.WeakReference;

/**
 * Created by lenovo on 2018/2/7/0007.
 */

public class ShareSdk {
    public static void share(Activity activity, ShareContent shareContent, CallBack<ShareResult> callBack){
        if (activity == null || shareContent == null) {
            return;
        }
        ShareManager shareManager = ShareManager.getInstance();
        shareManager.refCallBack = new WeakReference<CallBack<ShareResult>>(callBack);
        shareManager.shareContent = shareContent;
        shareManager.platform = initPlatform(shareContent.getPlatform());
        activity.startActivity(new Intent(activity, ShareActivity.class));
        activity.overridePendingTransition(0,0);
    }

    private static IPlatform initPlatform(int platform) {
        switch (platform) {
            case Platform.WECHAT:
            case Platform.WECHAT_MOMENTS:
                return new WXPlatform();
            case Platform.QQ:
            case Platform.QZONE:
                return new QQPlatform();
            case Platform.WEIBO:
                return new WeiboPlatform();
            default:
                throw new RuntimeException("unknown platform");
        }
    }
}
