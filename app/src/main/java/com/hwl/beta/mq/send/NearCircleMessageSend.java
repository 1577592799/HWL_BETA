package com.hwl.beta.mq.send;

import com.hwl.beta.mq.MQConstant;
import com.hwl.beta.mq.MQManager;
import com.hwl.beta.mq.bean.NearCircleCommentMessageBean;
import com.hwl.beta.mq.bean.NearCircleLikeMessageBean;
import com.hwl.beta.mq.receive.MessageReceive;
import com.hwl.beta.sp.UserSP;
import com.hwl.beta.utils.ByteUtils;
import com.hwl.beta.utils.StringUtils;

import java.util.Date;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class NearCircleMessageSend {

    public static Observable<Boolean> sendAddLikeMessage(long nearCircleId, long toUserId, String content) {
        return sendNearCircleLikeMessage(nearCircleId, toUserId, MQConstant.CIRCLE_MESSAGE_ACTION_ADD, content);
    }

    public static Observable<Boolean> sendDeleteLikeMessage(long nearCircleId, long toUserId) {
        return sendNearCircleLikeMessage(nearCircleId, toUserId, MQConstant.CIRCLE_MESSAGE_ACTION_DELETE, "");
    }

    private static Observable<Boolean> sendNearCircleLikeMessage(long nearCircleId, long toUserId, int actionType, String content) {
        if (nearCircleId <= 0) return Observable.just(false);
        long myUserId = UserSP.getUserId();
        if (myUserId == toUserId) return Observable.just(false);

        NearCircleLikeMessageBean bean = new NearCircleLikeMessageBean();
        bean.setNearCircleId(nearCircleId);
        bean.setFromUserId(myUserId);
        bean.setFromUserName(UserSP.getUserName());
        bean.setFromUserImage(UserSP.getUserHeadImage());
        bean.setToUserId(toUserId);
        bean.setActionType(actionType);
        bean.setContent(StringUtils.cutString(content, 40));
        bean.setActionTime(new Date());

        return Observable.just(bean)
                .map(new Function<NearCircleLikeMessageBean, Boolean>() {
                    @Override
                    public Boolean apply(NearCircleLikeMessageBean messageBean) throws Exception {
                        MQManager.sendMessage(
                                MessageReceive.getMessageQueueName(messageBean.getToUserId()),
                                ByteUtils.mergeToStart(MQConstant.NEAR_CIRCLE_LIKE_MESSAGE, MessageReceive.convertToBytes(messageBean))
                        );
                        return true;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<Boolean> sendAddCommentMessage(long nearCircleId, long toUserId, int commentId, String comment, String content) {
        return sendNearCircleCommentMessage(nearCircleId, toUserId, 0, null, MQConstant.CIRCLE_MESSAGE_ACTION_ADD, commentId, comment, content);
    }

    public static Observable<Boolean> sendAddCommentMessage(long nearCircleId, long toUserId, long replyUserId, String replyUserName, int commentId, String comment, String content) {
        return sendNearCircleCommentMessage(nearCircleId, toUserId, replyUserId, replyUserName, MQConstant.CIRCLE_MESSAGE_ACTION_ADD, commentId, comment, content);
    }

    public static Observable<Boolean> sendDeleteCommentDeleteMessage(long nearCircleId, long toUserId, int commentId, String comment, String content) {
        return sendNearCircleCommentMessage(nearCircleId, toUserId, 0, null, MQConstant.CIRCLE_MESSAGE_ACTION_DELETE, commentId, comment, content);
    }

    private static Observable<Boolean> sendNearCircleCommentMessage(long nearCircleId, long toUserId, long replyUserId, String replyUserName, int actionType, int commentId, String comment, String content) {
        if (nearCircleId <= 0) return Observable.just(false);
        final long myUserId = UserSP.getUserId();
        if (myUserId == toUserId) return Observable.just(false);

        NearCircleCommentMessageBean bean = new NearCircleCommentMessageBean();
        bean.setNearCircleId(nearCircleId);
        bean.setFromUserId(myUserId);
        bean.setFromUserName(UserSP.getUserName());
        bean.setFromUserImage(UserSP.getUserHeadImage());
        bean.setToUserId(toUserId);
        bean.setActionType(actionType);
        bean.setContent(StringUtils.cutString(content, 40));
        bean.setCommentId(commentId);
        bean.setComment(comment);
        bean.setReplyUserId(replyUserId);
        bean.setReplyUserName(replyUserName);
        bean.setActionTime(new Date());

        return Observable.just(bean)
                .map(new Function<NearCircleCommentMessageBean, Boolean>() {
                    @Override
                    public Boolean apply(NearCircleCommentMessageBean messageBean) throws Exception {
                        MQManager.sendMessage(
                                MessageReceive.getMessageQueueName(messageBean.getToUserId()),
                                ByteUtils.mergeToStart(MQConstant.NEAR_CIRCLE_COMMENT_MESSAGE, MessageReceive.convertToBytes(messageBean))
                        );

                        if (messageBean.getReplyUserId() > 0 && messageBean.getReplyUserId() != myUserId) {
                            MQManager.sendMessage(
                                    MessageReceive.getMessageQueueName(messageBean.getReplyUserId()),
                                    ByteUtils.mergeToStart(MQConstant.NEAR_CIRCLE_COMMENT_MESSAGE, MessageReceive.convertToBytes(messageBean))
                            );
                        }

                        return true;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
