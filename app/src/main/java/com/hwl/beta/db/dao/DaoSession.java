package com.hwl.beta.db.dao;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.hwl.beta.db.entity.ChatGroupMessage;
import com.hwl.beta.db.entity.ChatRecordMessage;
import com.hwl.beta.db.entity.ChatUserMessage;
import com.hwl.beta.db.entity.ChatUserSetting;
import com.hwl.beta.db.entity.Circle;
import com.hwl.beta.db.entity.CircleComment;
import com.hwl.beta.db.entity.CircleImage;
import com.hwl.beta.db.entity.CircleLike;
import com.hwl.beta.db.entity.CircleMessage;
import com.hwl.beta.db.entity.Friend;
import com.hwl.beta.db.entity.FriendRequest;
import com.hwl.beta.db.entity.GroupInfo;
import com.hwl.beta.db.entity.GroupUserInfo;
import com.hwl.beta.db.entity.NearCircle;
import com.hwl.beta.db.entity.NearCircleComment;
import com.hwl.beta.db.entity.NearCircleImage;
import com.hwl.beta.db.entity.NearCircleLike;
import com.hwl.beta.db.entity.NearCircleMessage;

import com.hwl.beta.db.dao.ChatGroupMessageDao;
import com.hwl.beta.db.dao.ChatRecordMessageDao;
import com.hwl.beta.db.dao.ChatUserMessageDao;
import com.hwl.beta.db.dao.ChatUserSettingDao;
import com.hwl.beta.db.dao.CircleDao;
import com.hwl.beta.db.dao.CircleCommentDao;
import com.hwl.beta.db.dao.CircleImageDao;
import com.hwl.beta.db.dao.CircleLikeDao;
import com.hwl.beta.db.dao.CircleMessageDao;
import com.hwl.beta.db.dao.FriendDao;
import com.hwl.beta.db.dao.FriendRequestDao;
import com.hwl.beta.db.dao.GroupInfoDao;
import com.hwl.beta.db.dao.GroupUserInfoDao;
import com.hwl.beta.db.dao.NearCircleDao;
import com.hwl.beta.db.dao.NearCircleCommentDao;
import com.hwl.beta.db.dao.NearCircleImageDao;
import com.hwl.beta.db.dao.NearCircleLikeDao;
import com.hwl.beta.db.dao.NearCircleMessageDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig chatGroupMessageDaoConfig;
    private final DaoConfig chatRecordMessageDaoConfig;
    private final DaoConfig chatUserMessageDaoConfig;
    private final DaoConfig chatUserSettingDaoConfig;
    private final DaoConfig circleDaoConfig;
    private final DaoConfig circleCommentDaoConfig;
    private final DaoConfig circleImageDaoConfig;
    private final DaoConfig circleLikeDaoConfig;
    private final DaoConfig circleMessageDaoConfig;
    private final DaoConfig friendDaoConfig;
    private final DaoConfig friendRequestDaoConfig;
    private final DaoConfig groupInfoDaoConfig;
    private final DaoConfig groupUserInfoDaoConfig;
    private final DaoConfig nearCircleDaoConfig;
    private final DaoConfig nearCircleCommentDaoConfig;
    private final DaoConfig nearCircleImageDaoConfig;
    private final DaoConfig nearCircleLikeDaoConfig;
    private final DaoConfig nearCircleMessageDaoConfig;

    private final ChatGroupMessageDao chatGroupMessageDao;
    private final ChatRecordMessageDao chatRecordMessageDao;
    private final ChatUserMessageDao chatUserMessageDao;
    private final ChatUserSettingDao chatUserSettingDao;
    private final CircleDao circleDao;
    private final CircleCommentDao circleCommentDao;
    private final CircleImageDao circleImageDao;
    private final CircleLikeDao circleLikeDao;
    private final CircleMessageDao circleMessageDao;
    private final FriendDao friendDao;
    private final FriendRequestDao friendRequestDao;
    private final GroupInfoDao groupInfoDao;
    private final GroupUserInfoDao groupUserInfoDao;
    private final NearCircleDao nearCircleDao;
    private final NearCircleCommentDao nearCircleCommentDao;
    private final NearCircleImageDao nearCircleImageDao;
    private final NearCircleLikeDao nearCircleLikeDao;
    private final NearCircleMessageDao nearCircleMessageDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        chatGroupMessageDaoConfig = daoConfigMap.get(ChatGroupMessageDao.class).clone();
        chatGroupMessageDaoConfig.initIdentityScope(type);

        chatRecordMessageDaoConfig = daoConfigMap.get(ChatRecordMessageDao.class).clone();
        chatRecordMessageDaoConfig.initIdentityScope(type);

        chatUserMessageDaoConfig = daoConfigMap.get(ChatUserMessageDao.class).clone();
        chatUserMessageDaoConfig.initIdentityScope(type);

        chatUserSettingDaoConfig = daoConfigMap.get(ChatUserSettingDao.class).clone();
        chatUserSettingDaoConfig.initIdentityScope(type);

        circleDaoConfig = daoConfigMap.get(CircleDao.class).clone();
        circleDaoConfig.initIdentityScope(type);

        circleCommentDaoConfig = daoConfigMap.get(CircleCommentDao.class).clone();
        circleCommentDaoConfig.initIdentityScope(type);

        circleImageDaoConfig = daoConfigMap.get(CircleImageDao.class).clone();
        circleImageDaoConfig.initIdentityScope(type);

        circleLikeDaoConfig = daoConfigMap.get(CircleLikeDao.class).clone();
        circleLikeDaoConfig.initIdentityScope(type);

        circleMessageDaoConfig = daoConfigMap.get(CircleMessageDao.class).clone();
        circleMessageDaoConfig.initIdentityScope(type);

        friendDaoConfig = daoConfigMap.get(FriendDao.class).clone();
        friendDaoConfig.initIdentityScope(type);

        friendRequestDaoConfig = daoConfigMap.get(FriendRequestDao.class).clone();
        friendRequestDaoConfig.initIdentityScope(type);

        groupInfoDaoConfig = daoConfigMap.get(GroupInfoDao.class).clone();
        groupInfoDaoConfig.initIdentityScope(type);

        groupUserInfoDaoConfig = daoConfigMap.get(GroupUserInfoDao.class).clone();
        groupUserInfoDaoConfig.initIdentityScope(type);

        nearCircleDaoConfig = daoConfigMap.get(NearCircleDao.class).clone();
        nearCircleDaoConfig.initIdentityScope(type);

        nearCircleCommentDaoConfig = daoConfigMap.get(NearCircleCommentDao.class).clone();
        nearCircleCommentDaoConfig.initIdentityScope(type);

        nearCircleImageDaoConfig = daoConfigMap.get(NearCircleImageDao.class).clone();
        nearCircleImageDaoConfig.initIdentityScope(type);

        nearCircleLikeDaoConfig = daoConfigMap.get(NearCircleLikeDao.class).clone();
        nearCircleLikeDaoConfig.initIdentityScope(type);

        nearCircleMessageDaoConfig = daoConfigMap.get(NearCircleMessageDao.class).clone();
        nearCircleMessageDaoConfig.initIdentityScope(type);

        chatGroupMessageDao = new ChatGroupMessageDao(chatGroupMessageDaoConfig, this);
        chatRecordMessageDao = new ChatRecordMessageDao(chatRecordMessageDaoConfig, this);
        chatUserMessageDao = new ChatUserMessageDao(chatUserMessageDaoConfig, this);
        chatUserSettingDao = new ChatUserSettingDao(chatUserSettingDaoConfig, this);
        circleDao = new CircleDao(circleDaoConfig, this);
        circleCommentDao = new CircleCommentDao(circleCommentDaoConfig, this);
        circleImageDao = new CircleImageDao(circleImageDaoConfig, this);
        circleLikeDao = new CircleLikeDao(circleLikeDaoConfig, this);
        circleMessageDao = new CircleMessageDao(circleMessageDaoConfig, this);
        friendDao = new FriendDao(friendDaoConfig, this);
        friendRequestDao = new FriendRequestDao(friendRequestDaoConfig, this);
        groupInfoDao = new GroupInfoDao(groupInfoDaoConfig, this);
        groupUserInfoDao = new GroupUserInfoDao(groupUserInfoDaoConfig, this);
        nearCircleDao = new NearCircleDao(nearCircleDaoConfig, this);
        nearCircleCommentDao = new NearCircleCommentDao(nearCircleCommentDaoConfig, this);
        nearCircleImageDao = new NearCircleImageDao(nearCircleImageDaoConfig, this);
        nearCircleLikeDao = new NearCircleLikeDao(nearCircleLikeDaoConfig, this);
        nearCircleMessageDao = new NearCircleMessageDao(nearCircleMessageDaoConfig, this);

        registerDao(ChatGroupMessage.class, chatGroupMessageDao);
        registerDao(ChatRecordMessage.class, chatRecordMessageDao);
        registerDao(ChatUserMessage.class, chatUserMessageDao);
        registerDao(ChatUserSetting.class, chatUserSettingDao);
        registerDao(Circle.class, circleDao);
        registerDao(CircleComment.class, circleCommentDao);
        registerDao(CircleImage.class, circleImageDao);
        registerDao(CircleLike.class, circleLikeDao);
        registerDao(CircleMessage.class, circleMessageDao);
        registerDao(Friend.class, friendDao);
        registerDao(FriendRequest.class, friendRequestDao);
        registerDao(GroupInfo.class, groupInfoDao);
        registerDao(GroupUserInfo.class, groupUserInfoDao);
        registerDao(NearCircle.class, nearCircleDao);
        registerDao(NearCircleComment.class, nearCircleCommentDao);
        registerDao(NearCircleImage.class, nearCircleImageDao);
        registerDao(NearCircleLike.class, nearCircleLikeDao);
        registerDao(NearCircleMessage.class, nearCircleMessageDao);
    }
    
    public void clear() {
        chatGroupMessageDaoConfig.clearIdentityScope();
        chatRecordMessageDaoConfig.clearIdentityScope();
        chatUserMessageDaoConfig.clearIdentityScope();
        chatUserSettingDaoConfig.clearIdentityScope();
        circleDaoConfig.clearIdentityScope();
        circleCommentDaoConfig.clearIdentityScope();
        circleImageDaoConfig.clearIdentityScope();
        circleLikeDaoConfig.clearIdentityScope();
        circleMessageDaoConfig.clearIdentityScope();
        friendDaoConfig.clearIdentityScope();
        friendRequestDaoConfig.clearIdentityScope();
        groupInfoDaoConfig.clearIdentityScope();
        groupUserInfoDaoConfig.clearIdentityScope();
        nearCircleDaoConfig.clearIdentityScope();
        nearCircleCommentDaoConfig.clearIdentityScope();
        nearCircleImageDaoConfig.clearIdentityScope();
        nearCircleLikeDaoConfig.clearIdentityScope();
        nearCircleMessageDaoConfig.clearIdentityScope();
    }

    public ChatGroupMessageDao getChatGroupMessageDao() {
        return chatGroupMessageDao;
    }

    public ChatRecordMessageDao getChatRecordMessageDao() {
        return chatRecordMessageDao;
    }

    public ChatUserMessageDao getChatUserMessageDao() {
        return chatUserMessageDao;
    }

    public ChatUserSettingDao getChatUserSettingDao() {
        return chatUserSettingDao;
    }

    public CircleDao getCircleDao() {
        return circleDao;
    }

    public CircleCommentDao getCircleCommentDao() {
        return circleCommentDao;
    }

    public CircleImageDao getCircleImageDao() {
        return circleImageDao;
    }

    public CircleLikeDao getCircleLikeDao() {
        return circleLikeDao;
    }

    public CircleMessageDao getCircleMessageDao() {
        return circleMessageDao;
    }

    public FriendDao getFriendDao() {
        return friendDao;
    }

    public FriendRequestDao getFriendRequestDao() {
        return friendRequestDao;
    }

    public GroupInfoDao getGroupInfoDao() {
        return groupInfoDao;
    }

    public GroupUserInfoDao getGroupUserInfoDao() {
        return groupUserInfoDao;
    }

    public NearCircleDao getNearCircleDao() {
        return nearCircleDao;
    }

    public NearCircleCommentDao getNearCircleCommentDao() {
        return nearCircleCommentDao;
    }

    public NearCircleImageDao getNearCircleImageDao() {
        return nearCircleImageDao;
    }

    public NearCircleLikeDao getNearCircleLikeDao() {
        return nearCircleLikeDao;
    }

    public NearCircleMessageDao getNearCircleMessageDao() {
        return nearCircleMessageDao;
    }

}
