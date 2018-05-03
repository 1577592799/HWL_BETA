package com.hwl.beta.db.manage;

import android.content.Context;

import com.hwl.beta.db.BaseDao;
import com.hwl.beta.db.dao.ChatUserMessageDao;
import com.hwl.beta.db.entity.ChatUserMessage;
import com.hwl.beta.db.entity.ChatUserSetting;

import java.util.List;

/**
 * Created by Administrator on 2018/2/8.
 */

public class ChatUserMessageManager extends BaseDao<ChatUserMessage> {
    public ChatUserMessageManager(Context context) {
        super(context);
    }

    public long save(ChatUserMessage request) {
        if (request == null) return 0;

        return daoSession.getChatUserMessageDao().insertOrReplace(request);
    }

    public boolean updateLocalPath(long msgId, String localPath) {
        ChatUserMessage message = daoSession.getChatUserMessageDao().loadByRowId(msgId);
        if (message == null) return false;
        message.setLocalUrl(localPath);
        save(message);
        return true;
    }

    public void updateStatus(long msgId, int status) {
        if (msgId <= 0) return;
        ChatUserMessage message = daoSession.getChatUserMessageDao().loadByRowId(msgId);
        if (message == null) return;
        message.setSendStatus(status);
        save(message);
    }

    public List<ChatUserMessage> getAll() {
        return daoSession.getChatUserMessageDao().loadAll();
    }

    public List<ChatUserMessage> getFromUserMessages(long toUserId, long fromUserId) {
        return daoSession.getChatUserMessageDao().queryBuilder()
                .whereOr(ChatUserMessageDao.Properties.FromUserId.eq(fromUserId), ChatUserMessageDao.Properties.FromUserId.eq(toUserId))
                .whereOr(ChatUserMessageDao.Properties.ToUserId.eq(fromUserId), ChatUserMessageDao.Properties.ToUserId.eq(toUserId))
                .list();
    }

    public boolean deleteMessage(long msgId) {
        if (msgId <= 0) return false;
        daoSession.getChatUserMessageDao().deleteByKeyInTx(msgId);
        return true;
    }

    public boolean deleteMessage(ChatUserMessage message) {
        if (message == null || message.getMsgId() <= 0) return false;
        daoSession.getChatUserMessageDao().delete(message);
        return true;
    }

    public boolean deleteUserMessages(long myUserId, long friendUserId) {
        if (myUserId <= 0 || friendUserId <= 0 || myUserId == friendUserId) return false;
        List<ChatUserMessage> messages = getFromUserMessages(myUserId, friendUserId);
        if (messages == null || messages.size() <= 0) return true;
        daoSession.getChatUserMessageDao().deleteInTx(messages);
        return true;
    }

    public ChatUserSetting getChatUserSetting(long userId) {
        if (userId <= 0) return null;
        return daoSession.getChatUserSettingDao().load(userId);
    }

    public void setChatUserSetting(ChatUserSetting userSetting) {
        if (userSetting == null) return;
        daoSession.getChatUserSettingDao().insertOrReplace(userSetting);
    }
}
