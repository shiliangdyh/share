package com.mirstone.sharelib;

import android.app.Activity;
import android.content.Context;

import com.mirstone.ShareContent;
import com.mirstone.sharelib.listener.CallBack;
import com.mirstone.sharelib.listener.ShareResult;
import com.sina.weibo.sdk.share.WbShareCallback;
import com.sina.weibo.sdk.share.WbShareHandler;
import com.tencent.tauth.IUiListener;

import java.lang.ref.WeakReference;

/**
 * Created by lenovo on 2018/1/31/0031.
 */

public class ShareManager {
    private static final String TAG = "ShareManager";
    private static ShareManager INSTANCE;

    IPlatform platform;
    ShareContent shareContent;
    WeakReference<CallBack<ShareResult>> refCallBack;

    private WeakReference<Activity> refActivity;
    private WeakReference<IUiListener> refQqListener;
    private WeakReference<WbShareCallback> refWbShareCallback;

    private ShareManager() {
    }

    public static ShareManager getInstance(){
        if (INSTANCE == null){
            synchronized (ShareManager.class){
                if (INSTANCE == null){
                    INSTANCE = new ShareManager();
                }
            }
        }
        return INSTANCE;
    }

    static void shareToWeChat(Activity activity){
        ShareManager instance = ShareManager.getInstance();
        instance.refActivity = new WeakReference<Activity>(activity);
        instance.platform.share(activity);
    }

    static WbShareHandler shareToWeiBo(Activity activity, WbShareCallback wbShareCallback){
        ShareManager instance = ShareManager.getInstance();
        instance.refActivity = new WeakReference<Activity>(activity);
        instance.refWbShareCallback = new WeakReference<WbShareCallback>(wbShareCallback);
        return instance.platform.share(activity);
    }

    static void shareToQQ(Activity activity, IUiListener qqListener){
        ShareManager instance = ShareManager.getInstance();
        instance.refActivity = new WeakReference<Activity>(activity);
        instance.refQqListener = new WeakReference<IUiListener>(qqListener);
        instance.platform.share(activity);
    }

    public IUiListener getQQListener(){
        if (refQqListener != null) {
            return refQqListener.get();
        }
        return null;
    }


    public static void destroy(){
        if (INSTANCE != null) {
            INSTANCE = null;
        }
    }

    public CallBack<ShareResult> getCallBack() {
        if (refCallBack != null){
            return refCallBack.get();
        }
        return null;
    }

    public WbShareCallback getWbShareCallback() {
        if (refWbShareCallback != null){
            return refWbShareCallback.get();
        }
        return null;
    }

    String getPlatform(Context context){
        String[] array = context.getResources().getStringArray(R.array.share_platform);
        return array[shareContent.getPlatform()];
    }

    public ShareContent getShareContent() {
        return shareContent;
    }
}
