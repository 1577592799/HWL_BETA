package com.hwl.beta.net.user.body;


import com.hwl.beta.net.user.NetGroupUserInfo;

import java.util.List;

/**
 * Created by Administrator on 2018/1/20.
 */

public class SetUserPosResponse {

    private int Status;
    private int UserPosId;
    private String UserGroupGuid;
    private List<NetGroupUserInfo> GroupUserInfos;

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public int getUserPosId() {
        return UserPosId;
    }

    public void setUserPosId(int userPosId) {
        UserPosId = userPosId;
    }

    public String getUserGroupGuid() {
        return UserGroupGuid;
    }

    public void setUserGroupGuid(String userGroupGuid) {
        UserGroupGuid = userGroupGuid;
    }

    public List<NetGroupUserInfo> getGroupUserInfos() {
        return GroupUserInfos;
    }

    public void setGroupUserInfos(List<NetGroupUserInfo> groupUserInfos) {
        GroupUserInfos = groupUserInfos;
    }
}