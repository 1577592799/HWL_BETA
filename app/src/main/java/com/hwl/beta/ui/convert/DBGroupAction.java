package com.hwl.beta.ui.convert;

import com.hwl.beta.db.entity.Friend;
import com.hwl.beta.db.entity.GroupInfo;
import com.hwl.beta.db.entity.GroupUserInfo;
import com.hwl.beta.mq.bean.MQGroupUserInfo;
import com.hwl.beta.net.user.NetGroupUserInfo;
import com.hwl.beta.sp.UserPosSP;
import com.hwl.beta.utils.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2018/3/31.
 */

public class DBGroupAction {

    public static GroupInfo convertToGroupInfo(String groupGuid, int groupUserCount, List<String> groupUserImages) {
        return convertToGroupInfo(groupGuid, UserPosSP.getNearDesc(), groupUserCount, groupUserImages, new Date());
    }

    public static GroupInfo convertToGroupInfo(String groupGuid, String groupName, int groupUserCount, List<String> groupUserImages, Date buildTime) {
        GroupInfo groupInfo = new GroupInfo();
        groupInfo.setGroupGuid(groupGuid);
        groupInfo.setGroupName(groupName);
        groupInfo.setBuildTime(buildTime);
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

    public static List<GroupUserInfo> convertToGroupUserInfos(String groupGuid, List<Friend> friends) {
        if (StringUtils.isBlank(groupGuid)) return null;
        if (friends == null || friends.size() <= 0) return null;

        List<GroupUserInfo> groupUserInfos = new ArrayList<>(friends.size());
        for (int i = 0; i < friends.size(); i++) {
            GroupUserInfo userInfo = new GroupUserInfo();
            userInfo.setUserId(friends.get(i).getId());
            userInfo.setUserName(friends.get(i).getName());
            userInfo.setUserHeadImage(friends.get(i).getHeadImage());
            userInfo.setGroupGuid(groupGuid);
            userInfo.setAddTime(new Date());
            groupUserInfos.add(userInfo);
        }

        return groupUserInfos;
    }

    public static List<GroupUserInfo> convertToGroupUserInfos2(String groupGuid, List<MQGroupUserInfo> userInfos) {
        if (StringUtils.isBlank(groupGuid)) return null;
        if (userInfos == null || userInfos.size() <= 0) return null;

        List<GroupUserInfo> groupUserInfos = new ArrayList<>(userInfos.size());
        for (int i = 0; i < userInfos.size(); i++) {
            GroupUserInfo userInfo = new GroupUserInfo();
            userInfo.setUserId(userInfos.get(i).getUserId());
            userInfo.setUserName(userInfos.get(i).getUserName());
            userInfo.setUserHeadImage(userInfos.get(i).getUserImage());
            userInfo.setGroupGuid(groupGuid);
            userInfo.setAddTime(new Date());
            groupUserInfos.add(userInfo);
        }

        return groupUserInfos;
    }
}
