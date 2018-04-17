package com.hwl.beta.db.manage;

import android.content.Context;

import com.hwl.beta.db.BaseDao;
import com.hwl.beta.db.dao.GroupInfoDao;
import com.hwl.beta.db.entity.GroupInfo;
import com.hwl.beta.utils.StringUtils;

import io.reactivex.Observable;

/**
 * Created by Administrator on 2018/2/10.
 */

public class GroupInfoManager extends BaseDao<GroupInfo> {
    public GroupInfoManager(Context context) {
        super(context);
    }

    public void add(GroupInfo groupInfo) {
        if(groupInfo==null||StringUtils.isBlank(groupInfo.getGroupGuid()))
            return;
        daoSession.getGroupInfoDao().insertOrReplace(groupInfo);
    }

    public GroupInfo get(String groupGuid) {
        if (StringUtils.isBlank(groupGuid)) return null;

        return daoSession.getGroupInfoDao().queryBuilder().where(GroupInfoDao.Properties.GroupGuid.eq(groupGuid)).unique();
    }

    public boolean updateUserCount(String groupGuid, int count) {
        if (StringUtils.isBlank(groupGuid)) return false;

        GroupInfo group = get(groupGuid);
        if (group != null) {
            group.setGroupUserCount(count);
            add(group);
            return true;
        }
        return false;
    }
}
