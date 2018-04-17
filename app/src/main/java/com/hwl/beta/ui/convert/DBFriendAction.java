package com.hwl.beta.ui.convert;

import com.hwl.beta.db.entity.Friend;
import com.hwl.beta.net.user.NetUserFriendInfo;

/**
 * Created by Administrator on 2018/4/1.
 */

public class DBFriendAction {
    public static Friend convertToFriendInfo(NetUserFriendInfo netFriendInfo) {
        Friend friend = new Friend();
        friend.setId(netFriendInfo.getId());
        friend.setSymbol(netFriendInfo.getSymbol());
        friend.setName(netFriendInfo.getName());
//        friend.setRemark(netFriendInfo.getNameRemark());
        friend.setHeadImage(netFriendInfo.getHeadImage());
        friend.setCountry(netFriendInfo.getCountry());
        friend.setProvince(netFriendInfo.getProvince());
        return friend;
    }
}
