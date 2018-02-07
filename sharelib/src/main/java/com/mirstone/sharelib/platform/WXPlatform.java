package com.mirstone.sharelib.platform;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.widget.Toast;

import com.mirstone.ShareContent;
import com.mirstone.sharelib.utils.BitmapUtil;
import com.mirstone.sharelib.IPlatform;
import com.mirstone.sharelib.R;
import com.mirstone.sharelib.ShareManager;
import com.mirstone.sharelib.ShareType;
import com.mirstone.sharelib.listener.CallBack;
import com.mirstone.sharelib.listener.ShareResult;
import com.sina.weibo.sdk.share.WbShareHandler;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXTextObject;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * Created by lenovo on 2018/1/31/0031.
 */

public class WXPlatform implements IPlatform {

    public WXPlatform() {
    }

    /**
     * 分享到微信 纯文本时text不能为空，分享图片时图片不能为空，分享图文时，title和content不能全为空，url不能为空。
     * @param activity
     */
    @Override
    public WbShareHandler share(Activity activity) {
        if (activity == null) return null;
        ShareContent shareContent = ShareManager.getInstance().getShareContent();
        IWXAPI wxapi = WXAPIFactory.createWXAPI(activity, "wxda926b4f6d2fbe40", false);
        CallBack<ShareResult> callBack = ShareManager.getInstance().getCallBack();
        if (!wxapi.isWXAppInstalled()) {
            Toast.makeText(activity, activity.getString(R.string.share_wx_not_installed), Toast.LENGTH_SHORT).show();
            if (callBack != null) {
                callBack.failed("weixin not installed");
            }
            return null;
        }
        WXMediaMessage mediaMessage = getMediaMessage(activity, shareContent, callBack);
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf(System.currentTimeMillis());
        req.message = mediaMessage;
        wxapi.sendReq(req);
        return null;
    }

    /**
     * 根据分享的类型获取WXMediaMessage
     * @param context
     * @param shareContent
     * @return
     */
    private WXMediaMessage getMediaMessage(Context context, ShareContent shareContent, CallBack<ShareResult> callBack) {
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeFile(shareContent.getImagePath());
        } catch (Exception e) {
            //
        }
        if (bitmap == null) {
            bitmap = BitmapFactory.decodeResource(context.getResources(), shareContent.getResId());
        }

        WXMediaMessage mediaMessage = new WXMediaMessage();
        int type = shareContent.getShareType();
        switch (type) {
            case ShareType.TEXT:
                WXTextObject textObject = new WXTextObject();
                textObject.text = shareContent.getContent();
                if (TextUtils.isEmpty(shareContent.getContent())) {
                    if (callBack != null) {
                        callBack.failed("content is null");
                    }
                    return null;
                }
                mediaMessage.mediaObject = textObject;
                break;
            case ShareType.IMAGE: {
                if (bitmap == null) {
                    if (callBack != null) {
                        callBack.failed("bitmap is null");
                    }
                    return null;
                }
                mediaMessage.mediaObject = new WXImageObject(bitmap);
                Bitmap thumbBitmap = BitmapUtil.scaledBitmap(bitmap, 150, true);
                mediaMessage.thumbData = BitmapUtil.bitmap2bytes(thumbBitmap, true);
            }
                break;
            case ShareType.TEXT_IMAGE: {
                if (TextUtils.isEmpty(shareContent.getUrl())) {
                    if (callBack != null) {
                        callBack.failed("url is null");
                    }
                    return null;
                }
                if (TextUtils.isEmpty(shareContent.getTitle()) && TextUtils.isEmpty(shareContent.getContent())) {
                    if (callBack != null) {
                        callBack.failed("title and content is null");
                    }
                    return null;
                }
                WXWebpageObject webpageObject = new WXWebpageObject();
                webpageObject.webpageUrl = shareContent.getUrl();
                mediaMessage.mediaObject = webpageObject;
                mediaMessage.title = shareContent.getTitle();
                mediaMessage.description = shareContent.getContent();
                Bitmap thumbBitmap = BitmapUtil.scaledBitmap(bitmap, 150, true);
                mediaMessage.thumbData = BitmapUtil.bitmap2bytes(thumbBitmap, true);
            }
                break;
            default:
                throw new RuntimeException("");
        }
        mediaMessage.description = shareContent.getContent();
        return mediaMessage;
    }
}
