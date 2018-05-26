package com.hwl.beta.mq.send;

import com.hwl.beta.mq.MQConstant;
import com.hwl.beta.mq.MQManager;
import com.hwl.beta.mq.bean.CircleCommentMessageBean;
import com.hwl.beta.mq.bean.CircleLikeMessageBean;
import com.hwl.beta.mq.receive.MessageReceive;
import com.hwl.beta.sp.UserSP;
import com.hwl.beta.utils.ByteUtils;
import com.hwl.beta.utils.StringUtils;

import java.util.Date;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class CircleMessageSend {

    public static Observable<Boolean> sendAddLikeMessage(long circleId, long toUserId, String content) {
        return sendCircleLikeMessage(circleId, toUserId, MQConstant.CIRCLE_MESSAGE_ACTION_ADD, content);
    }

    public static Observable<Boolean> sendDeleteLikeMessage(long circleId, long toUserId) {
        return sendCircleLikeMessage(circleId, toUserId, MQConstant.CIRCLE_MESSAGE_ACTION_DELETE, "");
    }

    private static Observable<Boolean> sendCircleLikeMessage(long circleId, long toUserId, int actionType, String content) {
        if (circleId <= 0) return Observable.just(false);
        long myUserId = UserSP.getUserId();
        if (myUserId == toUserId) return Observable.just(false);

        CircleLikeMessageBean bean = new CircleLikeMessageBean();
        bean.setCircleId(circleId);
        bean.setFromUserId(myUserId);
        bean.setFromUserName(UserSP.getUserName());
        bean.setFromUserImage(UserSP.getUserHeadImage());
        bean.setToUserId(toUserId);
        bean.setActionType(actionType);
        bean.setContent(StringUtils.cutString(content, 40));
        bean.setActionTime(new Date());

        return Observable.just(bean)
                .map(new Function<CircleLikeMessageBean, Boolean>() {
                    @Override
                    public Boolean apply(CircleLikeMessageBean messageBean) throws Exception {
                        MQManager.sendMessage(
                                MessageReceive.getMessageQueueName(messageBean.getToUserId()),
                                ByteUtils.mergeToStart(MQConstant.CIRCLE_LIKE_MESSAGE, MessageReceive.convertToBytes(messageBean))
                        );
                        return true;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    public static Observable<Boolean> sendAddCommentMessage(long circleId, long toUserId, int commentId, String comment, String content) {
        return sendCircleCommentMessage(circleId, toUserId, 0, null, MQConstant.CIRCLE_MESSAGE_ACTION_ADD, commentId, comment, content);
    }

    public static Observable<Boolean> sendAddCommentMessage(long nearCircleId, long toUserId, long replyUserId, String replyUserName, int commentId, String comment, String content) {
        return sendCircleCommentMessage(nearCircleId, toUserId, replyUserId, replyUserName, MQConstant.CIRCLE_MESSAGE_ACTION_ADD, commentId, comment, content);
    }

    public static Observable<Boolean> sendDeleteCommentDeleteMessage(long circleId, long toUserId, int commentId, String comment, String content) {
        return sendCircleCommentMessage(circleId, toUserId, 0, null, MQConstant.CIRCLE_MESSAGE_ACTION_DELETE, commentId, comment, content);
    }

    private static Observable<Boolean> sendCircleCommentMessage(long circleId, long toUserId, long replyUserId, String replyUserName, int actionType, int commentId, String comment, String content) {
        if (circleId <= 0) return Observable.just(false);
        final long myUserId = UserSP.getUserId();
        if (myUserId == toUserId) return Observable.just(false);

        CircleCommentMessageBean bean = new CircleCommentMessageBean();
        bean.setCircleId(circleId);
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
                .map(new Function<CircleCommentMessageBean, Boolean>() {
                    @Override
                    public Boolean apply(CircleCommentMessageBean messageBean) throws Exception {
                        MQManager.sendMessage(
                                MessageReceive.getMessageQueueName(messageBean.getToUserId()),
                                ByteUtils.mergeToStart(MQConstant.CIRCLE_COMMENT_MESSAGE, MessageReceive.convertToBytes(messageBean))
                        );
                        if (messageBean.getReplyUserId() > 0 && messageBean.getReplyUserId() != myUserId) {
                            MQManager.sendMessage(
                                    MessageReceive.getMessageQueueName(messageBean.getReplyUserId()),
                                    ByteUtils.mergeToStart(MQConstant.CIRCLE_COMMENT_MESSAGE, MessageReceive.convertToBytes(messageBean))
                            );
                        }
                        return true;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
