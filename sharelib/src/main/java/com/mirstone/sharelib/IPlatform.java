package com.mirstone.sharelib;

import android.content.Context;

import com.mirstone.ShareContent;
import com.mirstone.sharelib.listener.CallBack;
import com.mirstone.sharelib.listener.ShareResult;

/**
 * Created by lenovo on 2018/1/31/0031.
 */

public interface IPlatform {

     void share(Context context, ShareContent shareContent, CallBack<ShareResult> callBack);
}
