package com.hwl.beta.ui.convert;

import com.hwl.beta.db.entity.GroupInfo;
import com.hwl.beta.db.entity.GroupUserInfo;
import com.hwl.beta.net.user.NetGroupUserInfo;
import com.hwl.beta.sp.UserPosSP;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2018/3/31.
 */

public class DBGroupAction {

    public static GroupInfo convertToNearGroupInfo(String groupGuid, int groupUserCount,List<String> groupUserImages) {
        GroupInfo groupInfo = new GroupInfo();
        groupInfo.setGroupGuid(groupGuid);
        groupInfo.setGroupName(UserPosSP.getNearDesc());
        groupInfo.setBuildTime(new Date());
        groupInfo.setGroupUserCount(groupUserCount);
        groupInfo.setUserImages(groupUserImages);
        return groupInfo;
    }

    public static List<GroupUserInfo> convertToGroupUserInfos(List<NetGroupUserInfo> userInfos) {
        if (userInfos == null || userInfos.size() <= 0) return null;
        List<GroupUserInfo> groupUserInfos = new ArrayList<>(userInfos.size());
        for (int i = 0; i < userInfos.size(); i++) {
            groupUserInfos.add(convertToGroupUserInfo(userInfos.get(i)));
        }
        return groupUserInfos;
    }


    public static GroupUserInfo convertToGroupUserInfo(NetGroupUserInfo groupUserInfo) {
        GroupUserInfo userInfo = new GroupUserInfo();
        userInfo.setUserId(groupUserInfo.getUserId());
        userInfo.setUserName(groupUserInfo.getUserName());
        userInfo.setUserHeadImage(groupUserInfo.getUserHeadImage());
        userInfo.setGroupGuid(groupUserInfo.getGroupGuid());
        userInfo.setAddTime(new Date());
        return userInfo;
    }
}
