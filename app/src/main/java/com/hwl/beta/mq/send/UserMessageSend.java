package com.hwl.beta.mq.send;

import com.hwl.beta.mq.MQConstant;
import com.hwl.beta.mq.MQManager;
import com.hwl.beta.mq.bean.FriendRequestBean;
import com.hwl.beta.mq.receive.MessageReceive;
import com.hwl.beta.sp.UserSP;
import com.hwl.beta.utils.ByteUtils;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2018/3/31.
 */

public class UserMessageSend {

    public static Observable<Boolean> sendFriendRequestMessage(long friendId, String remark) {
        final FriendRequestBean bean = new FriendRequestBean();
        bean.setUserId(UserSP.getUserId());
        bean.setUserName(UserSP.getUserShowName());
        bean.setHeadImage(UserSP.getUserHeadImage());
        bean.setFriendId(friendId);
        bean.setRemark(remark);

        return Observable.just(bean)
                .map(new Function<FriendRequestBean, Boolean>() {
                    @Override
                    public Boolean apply(FriendRequestBean friendRequestBean) throws Exception {
                        MQManager.sendMessage(MessageReceive.getMessageQueueName(friendRequestBean.getFriendId()),
                                ByteUtils.mergeToStart(MQConstant.FRIEND_REQUEST,
                                        MessageReceive.convertToBytes(friendRequestBean)));
                        return true;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
