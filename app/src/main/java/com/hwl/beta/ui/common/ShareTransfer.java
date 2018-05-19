package com.hwl.beta.ui.common;

import com.hwl.beta.HWLApp;
import com.hwl.beta.utils.AppUtils;

import cn.sharesdk.onekeyshare.OnekeyShare;

public class ShareTransfer {

    public static final String APP_DOWNLOAD_URL = "http://192.168.1.4:8032/home/shareapp";

    public static void shareApp() {
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(AppUtils.getAppName() + "模式开启");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("快来和附近的人来聊天吧!");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        oks.setImageUrl("http://192.168.1.4:8033/content/default.png");
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl(APP_DOWNLOAD_URL);
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl(APP_DOWNLOAD_URL);

        // 启动分享GUI
        oks.show(HWLApp.getContext());
    }

}
