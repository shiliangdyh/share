package com.mirstone.sharelib;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.mirstone.sharelib.listener.CallBack;
import com.mirstone.sharelib.listener.ShareResult;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * Created by lenovo on 2018/1/31/0031.
 */

public class BaseWXEntryActivity extends Activity implements IWXAPIEventHandler {

    private IWXAPI wxapi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        wxapi = WXAPIFactory.createWXAPI(this, "wx9bcc43603d5aba17", false);
        wxapi.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        wxapi.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp baseResp) {
        int type = baseResp.getType();
        ShareSdk shareSdk = ShareSdk.getInstance();
        CallBack<ShareResult> callBack = shareSdk.getCallBack();
        if (type == 2){
            switch (baseResp.errCode) {
                case BaseResp.ErrCode.ERR_OK:
                    if (callBack != null) {
                        ShareResult shareResult = new ShareResult();
                        String platform = shareSdk.getPlatform(BaseWXEntryActivity.this);
                        shareResult.setPlatform(platform);
                        callBack.success(shareResult);
                    }
                    break;
                case BaseResp.ErrCode.ERR_USER_CANCEL:
                    if (callBack != null) {
                        callBack.cancel();
                    }
                    break;
                case BaseResp.ErrCode.ERR_AUTH_DENIED:
                case BaseResp.ErrCode.ERR_UNSUPPORT:
                default:
                    if (callBack != null) {
                        SendAuth.Resp resp = (SendAuth.Resp) baseResp;
                        callBack.failed(resp.code);
                    }
                    break;
            }
        }
        ShareSdk.destroy();
        finish();
    }
}
