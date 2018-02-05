package com.mirstone;

/**
 * Created by lenovo on 2018/1/31/0031.
 */

public class ShareContent {
    private int platform;
    private String title;//标题
    private String content;//正文
    private String url;//链接
    private String imagePath;//本地图片path imagePath和resId都不为空以imagePath为准
    private int resId;//本地图片资源id
    private int shareType = -1;//分享类型，纯文本，图片，图文链接

    public int getPlatform() {
        return platform;
    }

    public void setPlatform(int platform) {
        this.platform = platform;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public int getShareType() {
        return shareType;
    }

    public void setShareType(int shareType) {
        this.shareType = shareType;
    }
}
