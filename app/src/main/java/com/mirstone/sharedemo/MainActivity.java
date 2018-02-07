package com.mirstone.sharedemo;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.mirstone.ShareContent;
import com.mirstone.sharelib.ShareSdk;
import com.mirstone.sharelib.ShareType;
import com.mirstone.sharelib.listener.CallBack;
import com.mirstone.sharelib.listener.ShareResult;
import com.mirstone.sharelib.platform.Platform;
import com.sina.weibo.sdk.WbSdk;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;


public class MainActivity extends AppCompatActivity implements CallBack<ShareResult> {

    private IWXAPI wxapi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        wxapi = WXAPIFactory.createWXAPI(this, "wxda926b4f6d2fbe40", false);
        wxapi.registerApp("wxda926b4f6d2fbe40");
        WbSdk.install(this, new AuthInfo(this, "1146552421", "https://api.weibo.com/oauth2/default.html", null));
    }

    /**
     * View转Bitmap
     *
     * @return Bitmap
     */
    public static Bitmap convertViewToBitmap(View view , Bitmap.Config config) {
        if (view != null) {
            Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), config);
            Canvas canvas = new Canvas(bitmap);
            canvas.drawColor(Color.TRANSPARENT);
            view.draw(canvas);

            return bitmap;
        }
        return null;
    }

    public void onClick(View view) {
        ShareContent shareContent = new ShareContent();
        View rootView = findViewById(R.id.container);
        Bitmap bitmap = convertViewToBitmap(rootView, Bitmap.Config.RGB_565);
        String imagePath = Util.save(this, bitmap);
        Log.d("tag", imagePath);
        switch (view.getId()) {
            case R.id.wx_text:
                shareContent.setPlatform(Platform.WECHAT);
                shareContent.setShareType(ShareType.TEXT);
                shareContent.setContent("this is test");
//                ShareManager.shareToWeChat(this, shareContent, this);
                break;
            case R.id.wx_image:
                shareContent.setPlatform(Platform.WECHAT);
                shareContent.setShareType(ShareType.IMAGE);
//                shareContent.setResId(R.mipmap.ic_launcher);
                shareContent.setImagePath(imagePath);
//                ShareManager.shareToWeChat(this, shareContent, this);
                break;
            case R.id.wx_text_image:
                shareContent.setPlatform(Platform.WECHAT);
                shareContent.setShareType(ShareType.TEXT_IMAGE);
                shareContent.setTitle("aa");
                shareContent.setContent("aaaaaaaaaaaaaaaaaaaaaaaaa");
                shareContent.setResId(R.mipmap.ic_launcher);
                shareContent.setUrl("https://www.baidu.com");
//                ShareManager.shareToWeChat(this, shareContent, this);
                break;
            case R.id.weibo:
                shareContent.setPlatform(Platform.WEIBO);
                shareContent.setContent("#贵州茅台#咋收款单解放东路");
                shareContent.setImagePath(imagePath);
//                ShareManager.shareToWeiBo(this, shareContent, this);
                break;
            case R.id.qq_image:
                shareContent.setPlatform(Platform.QQ);
                shareContent.setShareType(ShareType.IMAGE);
                shareContent.setTitle("aa");
                shareContent.setImagePath(imagePath);
                shareContent.setContent("aaaaaaaaaaaaaaaaaaaaaaaaa");
                shareContent.setResId(R.mipmap.ic_launcher);
                shareContent.setUrl("https://www.baidu.com");
//                ShareManager.shareToQQ(this, shareContent, this);
                break;
            case R.id.qq_text_image:
                shareContent.setPlatform(Platform.QQ);
                shareContent.setShareType(ShareType.TEXT_IMAGE);
                shareContent.setTitle("aa");
//                shareContent.setImagePath(imagePath);
                shareContent.setContent("aaaaaaaaaaaaaaaaaaaaaaaaa");
                shareContent.setResId(R.mipmap.ic_launcher);
                shareContent.setUrl("https://www.baidu.com");
//                ShareManager.shareToQQ(this, shareContent, this);
                break;
            case R.id.qzone:
                shareContent.setPlatform(Platform.QZONE);
                shareContent.setShareType(ShareType.TEXT_IMAGE);
                shareContent.setTitle("aa");
                shareContent.setImagePath(imagePath);
                shareContent.setContent("aaaaaaaaaaaaaaaaaaaaaaaaa");
                shareContent.setResId(R.mipmap.ic_launcher);
                shareContent.setUrl("https://www.baidu.com");
//                ShareManager.shareToQQ(this, shareContent, this);
                break;
        }
        ShareSdk.share(this, shareContent, this);


    }

    @Override
    public void success(ShareResult shareResult) {
        Toast.makeText(this, "分享到" + shareResult.getPlatform() + "成功", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void cancel() {
        Toast.makeText(this, "分享取消", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void failed(String error) {
        Log.d("TAG", error);
        Toast.makeText(this, "分享失败", Toast.LENGTH_SHORT).show();
    }
}
