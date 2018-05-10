package com.hwl.beta.db.manage;

import android.content.Context;

import com.hwl.beta.db.BaseDao;
import com.hwl.beta.db.dao.NearCircleCommentDao;
import com.hwl.beta.db.dao.NearCircleDao;
import com.hwl.beta.db.dao.NearCircleImageDao;
import com.hwl.beta.db.dao.NearCircleLikeDao;
import com.hwl.beta.db.entity.NearCircle;
import com.hwl.beta.db.entity.NearCircleComment;
import com.hwl.beta.db.entity.NearCircleImage;
import com.hwl.beta.db.entity.NearCircleLike;
import com.hwl.beta.db.ext.NearCircleExt;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2018/3/10.
 */

public class NearCircleManager extends BaseDao<NearCircle> {
    public NearCircleManager(Context context) {
        super(context);
    }

    public long save(NearCircle model) {
        if (model == null) return 0;
        return daoSession.getNearCircleDao().insertOrReplace(model);
    }

    public NearCircle getNearCircle(long nearCircleId) {
        if (nearCircleId <= 0) return null;
        return daoSession.getNearCircleDao().load(nearCircleId);
    }

    public long save(NearCircle model, List<NearCircleImage> images) {
        if (model == null) return 0;
        long id = daoSession.getNearCircleDao().insertOrReplace(model);
        if (id > 0 && images != null && images.size() > 0) {
            for (int i = 0; i < images.size(); i++) {
                images.get(i).setNearCircleId(id);
            }
            daoSession.getNearCircleImageDao().saveInTx(images);
        }
        return id;
    }

    public List<NearCircleImage> getImages(long nearCircleId) {
        if (nearCircleId <= 0) return null;
        return daoSession.getNearCircleImageDao().queryBuilder()
                .where(NearCircleImageDao.Properties.NearCircleId.eq(nearCircleId))
                .list();
    }

    public void saveImages(long nearCircleId, List<NearCircleImage> images) {
        if (nearCircleId > 0 && images != null && images.size() > 0) {
            daoSession.getNearCircleImageDao().saveInTx(images);
        }
    }

    public void deleteImages(long nearCircleId) {
        if (nearCircleId > 0) {
            String deleteSql = "delete from " + NearCircleImageDao.TABLENAME + " where " + NearCircleImageDao.Properties.NearCircleId.columnName + "=" + nearCircleId;
            daoSession.getDatabase().execSQL(deleteSql);
        }
    }

    public void deleteComment(long nearCircleId, long userId, int commentId) {
        if (nearCircleId > 0) {
            String deleteSql = "delete from " + NearCircleCommentDao.TABLENAME + " where " +
                    NearCircleCommentDao.Properties.NearCircleId.columnName + "=" + nearCircleId + " and " +
                    NearCircleCommentDao.Properties.CommentId.columnName + "=" + commentId + " and " +
                    NearCircleCommentDao.Properties.CommentUserId.columnName + " = " + userId;
            daoSession.getDatabase().execSQL(deleteSql);
        }
    }

    public void deleteComments(long nearCircleId) {
        if (nearCircleId > 0) {
            String deleteSql = "delete from " + NearCircleCommentDao.TABLENAME + " where " + NearCircleCommentDao.Properties.NearCircleId.columnName + "=" + nearCircleId;
            daoSession.getDatabase().execSQL(deleteSql);
        }
    }

    public NearCircleComment getComment(long nearCircleId, long userId, int commentId) {
        if (nearCircleId <= 0) return null;
        return daoSession.getNearCircleCommentDao().queryBuilder()
                .where(NearCircleCommentDao.Properties.NearCircleId.eq(nearCircleId))
                .where(NearCircleCommentDao.Properties.CommentUserId.eq(userId))
                .where(NearCircleCommentDao.Properties.CommentId.eq(commentId))
                .unique();
    }

    public List<NearCircleComment> getComments(long nearCircleId) {
        if (nearCircleId <= 0) return null;
        return daoSession.getNearCircleCommentDao().queryBuilder()
                .where(NearCircleCommentDao.Properties.NearCircleId.eq(nearCircleId))
                .list();
    }

    public void saveComment(long nearCircleId, NearCircleComment comment) {
        if (nearCircleId > 0 && comment != null) {
            daoSession.getNearCircleCommentDao().save(comment);
        }
    }

    public void saveComments(long nearCircleId, List<NearCircleComment> comments) {
        if (nearCircleId > 0 && comments != null && comments.size() > 0) {
            daoSession.getNearCircleCommentDao().saveInTx(comments);
        }
    }

    public NearCircleLike getLike(long nearCircleId, long userId) {
        if (nearCircleId <= 0) return null;
        return daoSession.getNearCircleLikeDao().queryBuilder()
                .where(NearCircleLikeDao.Properties.NearCircleId.eq(nearCircleId))
                .where(NearCircleLikeDao.Properties.LikeUserId.eq(userId))
                .unique();
    }

    public List<NearCircleLike> getLikes(long nearCircleId) {
        if (nearCircleId <= 0) return null;
        return daoSession.getNearCircleLikeDao().queryBuilder()
                .where(NearCircleLikeDao.Properties.NearCircleId.eq(nearCircleId))
                .list();
    }

    public void deleteLikes(long nearCircleId) {
        if (nearCircleId > 0) {
            String deleteSql = "delete from " + NearCircleLikeDao.TABLENAME + " where " + NearCircleLikeDao.Properties.NearCircleId.columnName + " = " + nearCircleId;
            daoSession.getDatabase().execSQL(deleteSql);
        }
    }

    public void deleteLike(long nearCircleId, long userId) {
        if (nearCircleId > 0) {
            String deleteSql = "delete from " + NearCircleLikeDao.TABLENAME + " where " +
                    NearCircleLikeDao.Properties.NearCircleId.columnName + " = " + nearCircleId + " and " +
                    NearCircleLikeDao.Properties.LikeUserId.columnName + " = " + userId;
            daoSession.getDatabase().execSQL(deleteSql);
        }
    }

    public void saveLikes(long nearCircleId, List<NearCircleLike> likes) {
        if (nearCircleId > 0 && likes != null && likes.size() > 0) {
            daoSession.getNearCircleLikeDao().saveInTx(likes);
        }
    }

    public void saveLike(long nearCircleId, NearCircleLike likeInfo) {
        if (nearCircleId > 0 && likeInfo != null) {
            daoSession.getNearCircleLikeDao().save(likeInfo);
        }
    }

    public List<NearCircleExt> getNearCircles(int pageCount) {
        List<NearCircle> infos = daoSession.getNearCircleDao().queryBuilder()
                .orderDesc(NearCircleDao.Properties.NearCircleId)
                .limit(pageCount)
                .list();
        if (infos == null || infos.size() <= 0) return null;
        List<NearCircleExt> exts = new ArrayList<>(infos.size());
        NearCircleExt ext = null;
        for (int i = 0; i < infos.size(); i++) {
            ext = new NearCircleExt();
            ext.setInfo(infos.get(i));
            ext.setImages(getImages(infos.get(i).getNearCircleId()));
            ext.setComments(getComments(infos.get(i).getNearCircleId()));
            ext.setLikes(getLikes(infos.get(i).getNearCircleId()));
            exts.add(ext);
        }
        return exts;
    }

    public NearCircleExt get(long nearCircleId) {
        if (nearCircleId <= 0) return null;
        NearCircle model = daoSession.getNearCircleDao().load(nearCircleId);
        if (model == null) return null;
        NearCircleExt info = new NearCircleExt(
                model,
                getImages(model.getNearCircleId()),
                getComments(model.getNearCircleId()),
                getLikes(model.getNearCircleId())
        );
        return info;
    }
}
