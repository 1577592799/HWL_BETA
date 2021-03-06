package com.hwl.beta.db.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.hwl.beta.db.entity.FriendRequest;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "FRIEND_REQUEST".
*/
public class FriendRequestDao extends AbstractDao<FriendRequest, Long> {

    public static final String TABLENAME = "FRIEND_REQUEST";

    /**
     * Properties of entity FriendRequest.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property FriendId = new Property(0, long.class, "friendId", true, "_id");
        public final static Property FriendName = new Property(1, String.class, "friendName", false, "FRIEND_NAME");
        public final static Property FriendHeadImage = new Property(2, String.class, "friendHeadImage", false, "FRIEND_HEAD_IMAGE");
        public final static Property Remark = new Property(3, String.class, "remark", false, "REMARK");
        public final static Property Status = new Property(4, int.class, "status", false, "STATUS");
    }


    public FriendRequestDao(DaoConfig config) {
        super(config);
    }
    
    public FriendRequestDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"FRIEND_REQUEST\" (" + //
                "\"_id\" INTEGER PRIMARY KEY NOT NULL ," + // 0: friendId
                "\"FRIEND_NAME\" TEXT," + // 1: friendName
                "\"FRIEND_HEAD_IMAGE\" TEXT," + // 2: friendHeadImage
                "\"REMARK\" TEXT," + // 3: remark
                "\"STATUS\" INTEGER NOT NULL );"); // 4: status
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"FRIEND_REQUEST\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, FriendRequest entity) {
        stmt.clearBindings();
        stmt.bindLong(1, entity.getFriendId());
 
        String friendName = entity.getFriendName();
        if (friendName != null) {
            stmt.bindString(2, friendName);
        }
 
        String friendHeadImage = entity.getFriendHeadImage();
        if (friendHeadImage != null) {
            stmt.bindString(3, friendHeadImage);
        }
 
        String remark = entity.getRemark();
        if (remark != null) {
            stmt.bindString(4, remark);
        }
        stmt.bindLong(5, entity.getStatus());
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, FriendRequest entity) {
        stmt.clearBindings();
        stmt.bindLong(1, entity.getFriendId());
 
        String friendName = entity.getFriendName();
        if (friendName != null) {
            stmt.bindString(2, friendName);
        }
 
        String friendHeadImage = entity.getFriendHeadImage();
        if (friendHeadImage != null) {
            stmt.bindString(3, friendHeadImage);
        }
 
        String remark = entity.getRemark();
        if (remark != null) {
            stmt.bindString(4, remark);
        }
        stmt.bindLong(5, entity.getStatus());
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.getLong(offset + 0);
    }    

    @Override
    public FriendRequest readEntity(Cursor cursor, int offset) {
        FriendRequest entity = new FriendRequest( //
            cursor.getLong(offset + 0), // friendId
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // friendName
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // friendHeadImage
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // remark
            cursor.getInt(offset + 4) // status
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, FriendRequest entity, int offset) {
        entity.setFriendId(cursor.getLong(offset + 0));
        entity.setFriendName(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setFriendHeadImage(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setRemark(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setStatus(cursor.getInt(offset + 4));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(FriendRequest entity, long rowId) {
        entity.setFriendId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(FriendRequest entity) {
        if(entity != null) {
            return entity.getFriendId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(FriendRequest entity) {
        throw new UnsupportedOperationException("Unsupported for entities with a non-null key");
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
