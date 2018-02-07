package com.mirstone.sharelib;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.mirstone.commonutil.LogUtil;
import com.mirstone.sharelib.listener.CallBack;
import com.mirstone.sharelib.listener.ShareResult;
import com.mirstone.sharelib.platform.Platform;
import com.sina.weibo.sdk.share.WbShareCallback;
import com.sina.weibo.sdk.share.WbShareHandler;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

/**
 * Created by lenovo on 2018/2/5/0005.
 */

public class ShareActivity extends Activity {
    private static final String TAG = "ShareActivity";
    private boolean firstResume;

    private WbShareHandler wbShareHandler;
    private IUiListener qqListener = new IUiListener() {
        @Override
        public void onComplete(Object o) {
            finish();
            ShareManager instance = ShareManager.getInstance();
            CallBack<ShareResult> callBack = instance.getCallBack();
            if (callBack != null) {
                ShareResult shareResult = new ShareResult();
                shareResult.setPlatform(instance.getPlatform(ShareActivity.this));
                callBack.success(shareResult);
            }
        }

        @Override
        public void onError(UiError uiError) {
            finish();
            ShareManager instance = ShareManager.getInstance();
            CallBack<ShareResult> callBack = instance.getCallBack();
            if (callBack != null) {
                callBack.failed(uiError.errorMessage);
            }
        }

        @Override
        public void onCancel() {
            finish();
            ShareManager instance = ShareManager.getInstance();
            CallBack<ShareResult> callBack = instance.getCallBack();
            if (callBack != null) {
                callBack.cancel();
            }
        }
    };

    private WbShareCallback wbShareCallback = new WbShareCallback() {
        @Override
        public void onWbShareSuccess() {
            finish();
            ShareManager instance = ShareManager.getInstance();
            CallBack<ShareResult> callBack = instance.getCallBack();
            if (callBack != null) {
                ShareResult shareResult = new ShareResult();
                shareResult.setPlatform(instance.getPlatform(ShareActivity.this));
                callBack.success(shareResult);
            }
        }

        @Override
        public void onWbShareCancel() {
            finish();
            ShareManager instance = ShareManager.getInstance();
            CallBack<ShareResult> callBack = instance.getCallBack();
            if (callBack != null) {
                callBack.cancel();
            }
        }

        @Override
        public void onWbShareFail() {
            finish();
            ShareManager instance = ShareManager.getInstance();
            CallBack<ShareResult> callBack = instance.getCallBack();
            if (callBack != null) {
                callBack.failed("");
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int platform = ShareManager.getInstance().shareContent.getPlatform();
        if (platform == Platform.WEIBO){
            wbShareHandler = ShareManager.shareToWeiBo(this, wbShareCallback);
        }else if (platform == Platform.QQ || platform == Platform.QZONE){
            ShareManager.shareToQQ(this, qqListener);
        }else if (platform == Platform.WECHAT || platform == Platform.WECHAT_MOMENTS){
            ShareManager.shareToWeChat(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (firstResume){
            finish();
        }
        firstResume = true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogUtil.d(TAG, "onActivityResult: sssss" + resultCode);

        Tencent.onActivityResultData(requestCode, resultCode, data, qqListener);
        finish();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (wbShareHandler != null) {
            wbShareHandler.doResultIntent(intent, wbShareCallback);
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0,0);
    }
}
