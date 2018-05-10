package com.hwl.beta.db.manage;

import android.content.Context;

import com.hwl.beta.db.BaseDao;
import com.hwl.beta.db.DBConstant;
import com.hwl.beta.db.dao.CircleMessageDao;
import com.hwl.beta.db.entity.CircleMessage;
import com.hwl.beta.utils.StringUtils;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

public class CircleMessageManager extends BaseDao<CircleMessage> {
    public CircleMessageManager(Context context) {
        super(context);
    }

    public List<CircleMessage> getAll() {
        return daoSession.getCircleMessageDao()
                .queryBuilder()
                .orderDesc(CircleMessageDao.Properties.Id)
                .list();
    }

    public boolean save(CircleMessage message) {
        if (message == null) return false;
        if (daoSession.getCircleMessageDao().insert(message) > 0) {
            return true;
        }
        return false;
    }

    public boolean deleteMessage(CircleMessage message){
        if(message==null) return false;
        daoSession.getCircleMessageDao().delete(message);
        return true;
    }

    public void deleteAll(){
        daoSession.getCircleMessageDao().deleteAll();
    }

    public boolean updateDelete(long nearCircleId, int type, long fromUserId, int commentId) {
        if (nearCircleId <= 0 || fromUserId <= 0) return false;
        QueryBuilder<CircleMessage> query = daoSession.getCircleMessageDao().queryBuilder()
                .where(CircleMessageDao.Properties.CircleId.eq(nearCircleId))
                .where(CircleMessageDao.Properties.Type.eq(type))
                .where(CircleMessageDao.Properties.Status.notEq(DBConstant.STAUTS_DELETE))
                .where(CircleMessageDao.Properties.UserId.eq(fromUserId));
        if (commentId>0) {
            query = query.where(CircleMessageDao.Properties.CommentId.eq(commentId));
        }
        CircleMessage message = query.limit(1).unique();
        if (message != null) {
            message.setStatus(DBConstant.STAUTS_DELETE);
            daoSession.getCircleMessageDao().update(message);
            return true;
        }
        return false;
    }
}
