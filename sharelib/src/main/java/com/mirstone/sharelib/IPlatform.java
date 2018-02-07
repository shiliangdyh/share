package com.mirstone.sharelib;

import android.app.Activity;

import com.sina.weibo.sdk.share.WbShareHandler;

/**
 * Created by lenovo on 2018/1/31/0031.
 */

public interface IPlatform {

     WbShareHandler share(Activity activity);
}
