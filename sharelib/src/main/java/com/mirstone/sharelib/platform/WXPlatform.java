package com.mirstone.sharelib.platform;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import com.mirstone.ShareContent;
import com.mirstone.sharelib.IPlatform;
import com.mirstone.sharelib.ShareType;
import com.mirstone.sharelib.listener.CallBack;
import com.mirstone.sharelib.listener.ShareResult;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXTextObject;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.io.ByteArrayOutputStream;

/**
 * Created by lenovo on 2018/1/31/0031.
 */

public class WXPlatform implements IPlatform {

    /**
     * 分享到微信 纯文本时text不能为空，分享图片时图片不能为空，分享图文时，title和content不能全为空，url不能为空。
     * @param context
     * @param shareContent
     */
    @Override
    public void share(Context context, ShareContent shareContent, CallBack<ShareResult> callBack) {
        IWXAPI wxapi = WXAPIFactory.createWXAPI(context, "wx9bcc43603d5aba17", false);
        WXMediaMessage mediaMessage = getMediaMessage(context, shareContent, callBack);
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf(System.currentTimeMillis());
        req.message = mediaMessage;
        wxapi.sendReq(req);
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
                        callBack.failed("context is null");
                    }
                    return null;
                }
                mediaMessage.mediaObject = textObject;
                break;
            case ShareType.IMAGE:
                if (bitmap == null) {
                    if (callBack != null) {
                        callBack.failed("bitmap is null");
                    }
                    return null;
                }
                mediaMessage.mediaObject = new WXImageObject(bitmap);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                mediaMessage.thumbData = baos.toByteArray();
                break;
            case ShareType.TEXT_IMAGE:
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
                break;
            default:
                throw new RuntimeException("");
        }
        mediaMessage.description = shareContent.getContent();
        return mediaMessage;
    }
}
