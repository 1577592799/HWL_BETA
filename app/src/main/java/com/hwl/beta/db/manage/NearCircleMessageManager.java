package com.hwl.beta.db.manage;

import android.content.Context;

import com.hwl.beta.db.BaseDao;
import com.hwl.beta.db.DBConstant;
import com.hwl.beta.db.dao.NearCircleMessageDao;
import com.hwl.beta.db.entity.NearCircleMessage;
import com.hwl.beta.utils.StringUtils;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

public class NearCircleMessageManager extends BaseDao<NearCircleMessage> {
    public NearCircleMessageManager(Context context) {
        super(context);
    }

    public List<NearCircleMessage> getAll() {
        return daoSession.getNearCircleMessageDao().loadAll();
    }

    public boolean save(NearCircleMessage message) {
        if (message == null) return false;
        if (daoSession.getNearCircleMessageDao().insert(message) > 0) {
            return true;
        }
        return false;
    }

    public boolean deleteMessage(NearCircleMessage message){
        if(message==null) return false;
        daoSession.getNearCircleMessageDao().delete(message);
        return true;
    }

    public void deleteAll(){
        daoSession.getNearCircleMessageDao().deleteAll();
    }

    public boolean updateDelete(long nearCircleId, int type, long fromUserId, String comment) {
        if (nearCircleId <= 0 || fromUserId <= 0) return false;
        QueryBuilder<NearCircleMessage> query = daoSession.getNearCircleMessageDao().queryBuilder()
                .where(NearCircleMessageDao.Properties.NearCircleId.eq(nearCircleId))
                .where(NearCircleMessageDao.Properties.Type.eq(type))
                .where(NearCircleMessageDao.Properties.Status.notEq(DBConstant.STAUTS_DELETE))
                .where(NearCircleMessageDao.Properties.UserId.eq(fromUserId));
        if (StringUtils.isNotBlank(comment)) {
            query = query.where(NearCircleMessageDao.Properties.Comment.eq(comment));
        }
        NearCircleMessage message = query.limit(1).unique();
        if (message != null) {
            message.setStatus(DBConstant.STAUTS_DELETE);
            daoSession.getNearCircleMessageDao().update(message);
            return true;
        }
        return false;
    }
}
