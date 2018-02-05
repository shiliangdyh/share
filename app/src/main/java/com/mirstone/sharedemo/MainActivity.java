package com.mirstone.sharedemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.mirstone.ShareContent;
import com.mirstone.sharelib.ShareSdk;
import com.mirstone.sharelib.ShareType;
import com.mirstone.sharelib.listener.CallBack;
import com.mirstone.sharelib.listener.ShareResult;
import com.mirstone.sharelib.platform.Platform;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;


public class MainActivity extends AppCompatActivity implements CallBack<ShareResult> {

    private IWXAPI wxapi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        wxapi = WXAPIFactory.createWXAPI(this, "wx9bcc43603d5aba17", false);
        wxapi.registerApp("wx9bcc43603d5aba17");
    }

    public void onClick(View view) {
        ShareContent shareContent = new ShareContent();
        shareContent.setPlatform(Platform.WECHAT);

        switch (view.getId()) {
            case R.id.text:
                shareContent.setShareType(ShareType.TEXT);
                shareContent.setContent("this is test");
                break;
            case R.id.image:
                shareContent.setShareType(ShareType.IMAGE);
                shareContent.setResId(R.mipmap.ic_launcher);
                break;
            case R.id.text_image:
                shareContent.setShareType(ShareType.TEXT_IMAGE);
                shareContent.setTitle("aa");
                shareContent.setContent("");
//                shareContent.setResId(R.mipmap.ic_launcher);
                shareContent.setUrl("https://www.baidu.com");
                break;
        }



        ShareSdk.share2wx(this, shareContent, this);
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
        Toast.makeText(this, "分享失败", Toast.LENGTH_SHORT).show();
    }
}
