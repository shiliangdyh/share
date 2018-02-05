package com.mirstone.sharelib;

import android.content.Context;

import com.mirstone.ShareContent;
import com.mirstone.sharelib.listener.CallBack;
import com.mirstone.sharelib.listener.ShareResult;
import com.mirstone.sharelib.platform.Platform;
import com.mirstone.sharelib.platform.WXPlatform;

import java.lang.ref.WeakReference;

/**
 * Created by lenovo on 2018/1/31/0031.
 */

public class ShareSdk {
    private static ShareSdk INSTANCE;
    private IPlatform platform;
    private ShareContent shareContent;
    private WeakReference<CallBack<ShareResult>> refCallBack;
    private WeakReference<Context> refContext;

    private ShareSdk() {
    }

    public static ShareSdk getInstance(){
        if (INSTANCE == null){
            synchronized (ShareSdk.class){
                if (INSTANCE == null){
                    INSTANCE = new ShareSdk();
                }
            }
        }
        return INSTANCE;
    }

    /**
     *
     * @param context
     * @param shareContent
     * @param callBack
     */
    public static void share2wx(Context context, ShareContent shareContent, CallBack<ShareResult> callBack){
        ShareSdk instance = ShareSdk.getInstance();
        instance.refContext = new WeakReference<Context>(context);
        instance.refCallBack = new WeakReference<CallBack<ShareResult>>(callBack);
        instance.shareContent = shareContent;
        int platform = shareContent.getPlatform();
        if (platform == Platform.WECHAT || platform == Platform.WECHAT_MOMENTS){
            instance.platform = new WXPlatform();
        }
        instance.share();
    }

    private void share() {
        CallBack<ShareResult> callBack = null;
        if (refCallBack != null) {
            callBack = refCallBack.get();
        }
        platform.share(refContext.get(), shareContent, callBack);
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

    public String getPlatform(Context context){
        String[] array = context.getResources().getStringArray(R.array.platform);
        return array[shareContent.getPlatform()];
    }
}
