package com.hwl.beta.db.manage;

import android.content.Context;

import com.hwl.beta.db.BaseDao;
import com.hwl.beta.db.dao.CircleCommentDao;
import com.hwl.beta.db.dao.CircleDao;
import com.hwl.beta.db.dao.CircleImageDao;
import com.hwl.beta.db.dao.CircleLikeDao;
import com.hwl.beta.db.entity.Circle;
import com.hwl.beta.db.entity.CircleComment;
import com.hwl.beta.db.entity.CircleImage;
import com.hwl.beta.db.entity.CircleLike;
import com.hwl.beta.db.ext.CircleExt;

import java.util.ArrayList;
import java.util.List;

public class CircleManager extends BaseDao<Circle> {
    public CircleManager(Context context) {
        super(context);
    }

    public boolean isExists(long circleId) {
        if (circleId <= 0) return false;
        if (daoSession.getCircleDao().load(circleId) != null)
            return true;
        return false;
    }

    public long save(Circle model) {
        if (model == null) return 0;
        return daoSession.getCircleDao().insertOrReplace(model);
    }

    public long save(Circle model, List<CircleImage> images) {
        if (model == null) return 0;
        long id = daoSession.getCircleDao().insertOrReplace(model);
        if (id > 0 && images != null && images.size() > 0) {
            for (int i = 0; i < images.size(); i++) {
                images.get(i).setCircleId(id);
            }
            daoSession.getCircleImageDao().saveInTx(images);
        }
        return id;
    }

    public List<CircleImage> getImages(long CircleId) {
        if (CircleId <= 0) return null;
        return daoSession.getCircleImageDao().queryBuilder()
                .where(CircleImageDao.Properties.CircleId.eq(CircleId))
                .list();
    }

    public void saveImages(long CircleId, List<CircleImage> images) {
        if (CircleId > 0 && images != null && images.size() > 0) {
            daoSession.getCircleImageDao().saveInTx(images);
        }
    }

    public void deleteImages(long CircleId) {
        if (CircleId > 0) {
            String deleteSql = "delete from " + CircleImageDao.TABLENAME + " where " + CircleImageDao.Properties.CircleId.columnName + "=" + CircleId;
            daoSession.getDatabase().execSQL(deleteSql);
        }
    }

    public void deleteComments(long CircleId) {
        if (CircleId > 0) {
            String deleteSql = "delete from " + CircleCommentDao.TABLENAME + " where " + CircleCommentDao.Properties.CircleId.columnName + "=" + CircleId;
            daoSession.getDatabase().execSQL(deleteSql);
        }
    }

    public List<CircleComment> getComments(long CircleId) {
        if (CircleId <= 0) return null;
        return daoSession.getCircleCommentDao().queryBuilder()
                .where(CircleCommentDao.Properties.CircleId.eq(CircleId))
                .list();
    }

    public void saveComments(long CircleId, List<CircleComment> comments) {
        if (CircleId > 0 && comments != null && comments.size() > 0) {
            daoSession.getCircleCommentDao().saveInTx(comments);
        }
    }

    public List<CircleLike> getLikes(long CircleId) {
        if (CircleId <= 0) return null;
        return daoSession.getCircleLikeDao().queryBuilder()
                .where(CircleLikeDao.Properties.CircleId.eq(CircleId))
                .list();
    }

    public void deleteLikes(long CircleId) {
        if (CircleId > 0) {
            String deleteSql = "delete from " + CircleLikeDao.TABLENAME + " where " + CircleLikeDao.Properties.CircleId.columnName + " = " + CircleId;
            daoSession.getDatabase().execSQL(deleteSql);
        }
    }

    public void saveLikes(long CircleId, List<CircleLike> likes) {
        if (CircleId > 0 && likes != null && likes.size() > 0) {
            daoSession.getCircleLikeDao().saveInTx(likes);
        }
    }

    public List<CircleExt> getCircles(int pageCount) {
        List<Circle> infos = daoSession.getCircleDao().queryBuilder()
                .orderDesc(CircleDao.Properties.CircleId)
                .limit(pageCount)
                .list();
        if (infos == null || infos.size() <= 0) return null;
        List<CircleExt> exts = new ArrayList<>(infos.size());
        CircleExt ext;
        for (int i = 0; i < infos.size(); i++) {
            ext = new CircleExt(CircleExt.CircleIndexItem);
            ext.setInfo(infos.get(i));
            ext.setImages(getImages(infos.get(i).getCircleId()));
            ext.setComments(getComments(infos.get(i).getCircleId()));
            ext.setLikes(getLikes(infos.get(i).getCircleId()));
            exts.add(ext);
        }
        return exts;
    }

    public List<CircleExt> getUserCircles(long userId) {
        List<Circle> infos = daoSession.getCircleDao().queryBuilder()
                .where(CircleDao.Properties.PublishUserId.eq(userId))
                .orderDesc(CircleDao.Properties.CircleId)
                .list();
        if (infos == null || infos.size() <= 0) return null;
        List<CircleExt> exts = new ArrayList<>(infos.size());
        CircleExt ext;
        for (int i = 0; i < infos.size(); i++) {
            ext = new CircleExt(CircleExt.CircleIndexItem);
            ext.setInfo(infos.get(i));
            ext.setImages(getImages(infos.get(i).getCircleId()));
            ext.setComments(getComments(infos.get(i).getCircleId()));
            ext.setLikes(getLikes(infos.get(i).getCircleId()));
            exts.add(ext);
        }
        return exts;
    }

    public CircleExt get(long CircleId) {
        if (CircleId <= 0) return null;
        Circle model = daoSession.getCircleDao().load(CircleId);
        if (model == null) return null;
        CircleExt info = new CircleExt(
                model,
                getImages(model.getCircleId()),
                getComments(model.getCircleId()),
                getLikes(model.getCircleId())
        );
        return info;
    }
}