package com.hwl.beta.mq.send;

import com.hwl.beta.mq.MQConstant;
import com.hwl.beta.mq.MQManager;
import com.hwl.beta.mq.bean.FriendDeleteMessageBean;
import com.hwl.beta.mq.bean.FriendRequestBean;
import com.hwl.beta.mq.bean.NearCircleMessageBean;
import com.hwl.beta.mq.receive.MessageReceive;
import com.hwl.beta.sp.UserSP;
import com.hwl.beta.utils.ByteUtils;
import com.hwl.beta.utils.StringUtils;

import java.util.Date;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2018/3/31.
 */

public class UserMessageSend {

    public static Observable<Boolean> sendFriendRequestMessage(long friendId, String remark) {
        FriendRequestBean bean = new FriendRequestBean();
        bean.setUserId(UserSP.getUserId());
        bean.setUserName(UserSP.getUserShowName());
        bean.setHeadImage(UserSP.getUserHeadImage());
        bean.setFriendId(friendId);
        bean.setRemark(remark);

        return Observable.just(bean)
                .map(new Function<FriendRequestBean, Boolean>() {
                    @Override
                    public Boolean apply(FriendRequestBean friendRequestBean) throws Exception {
                        MQManager.sendMessage(
                                MessageReceive.getMessageQueueName(friendRequestBean.getFriendId()),
                                ByteUtils.mergeToStart(MQConstant.FRIEND_REQUEST, MessageReceive.convertToBytes(friendRequestBean))
                        );
                        return true;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<Boolean> sendFriendDeleteMessage(long friendId) {
        FriendDeleteMessageBean bean = new FriendDeleteMessageBean();
        bean.setActionUserId(UserSP.getUserId());
        bean.setFriendUserId(friendId);

        return Observable.just(bean)
                .map(new Function<FriendDeleteMessageBean, Boolean>() {
                    @Override
                    public Boolean apply(FriendDeleteMessageBean friendRequestBean) throws Exception {
                        MQManager.sendMessage(
                                MessageReceive.getMessageQueueName(friendRequestBean.getFriendUserId()),
                                ByteUtils.mergeToStart(MQConstant.FRIEND_DELETE_MESSAGE, MessageReceive.convertToBytes(friendRequestBean))
                        );
                        return true;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<Boolean> sendNearCircleLikeMessage(long toUserId, String content) {
        return sendNearCircleMessage(toUserId, MQConstant.CIRCLE_MESSAGE_LIKE, "", content);
    }

    public static Observable<Boolean> sendNearCircleCommentMessage(long toUserId, String comment, String content) {
        return sendNearCircleMessage(toUserId, MQConstant.CIRCLE_MESSAGE_COMMENT, comment, content);
    }

    private static Observable<Boolean> sendNearCircleMessage(long toUserId, int actionType, String comment, String content) {
        long myUserId = UserSP.getUserId();
        if (myUserId == toUserId) return Observable.just(false);

        NearCircleMessageBean bean = new NearCircleMessageBean();
        bean.setFromUserId(myUserId);
        bean.setFromUserName(UserSP.getUserName());
        bean.setFromUserImage(UserSP.getUserHeadImage());
        bean.setToUserId(toUserId);
        bean.setActionType(actionType);
        bean.setComment(comment);
        bean.setContent(StringUtils.cutString(content,40));
        bean.setActionTime(new Date());

        return Observable.just(bean)
                .map(new Function<NearCircleMessageBean, Boolean>() {
                    @Override
                    public Boolean apply(NearCircleMessageBean messageBean) throws Exception {
                        MQManager.sendMessage(
                                MessageReceive.getMessageQueueName(messageBean.getToUserId()),
                                ByteUtils.mergeToStart(MQConstant.NEAR_CIRCLE_ADD_MESSAGE, MessageReceive.convertToBytes(messageBean))
                        );
                        return true;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
