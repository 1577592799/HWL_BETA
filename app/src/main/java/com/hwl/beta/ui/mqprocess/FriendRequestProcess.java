package com.hwl.beta.ui.mqprocess;

import com.hwl.beta.db.DaoUtils;
import com.hwl.beta.db.entity.FriendRequest;
import com.hwl.beta.mq.bean.FriendRequestBean;
import com.hwl.beta.mq.receive.IMessageProcess;
import com.hwl.beta.sp.MessageCountSP;
import com.hwl.beta.ui.busbean.EventBusConstant;
import com.hwl.beta.ui.common.MessageNotifyManager;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Administrator on 2018/2/5.
 */

public class FriendRequestProcess implements IMessageProcess<FriendRequestBean> {

    private final static String TAG = "FriendRequestProcess";

    @Override
    public void execute(byte messageType, FriendRequestBean model) {
        //存储消息到本地数据库中
        if (DaoUtils.getFriendRequestManagerInstance().isExists(model.getUserId())) {
            addFriendRequest(model);
            return;
        } else {
            addFriendRequest(model);

            //将数量存储到sp中
            MessageCountSP.setFriendRequestCount(MessageCountSP.getFriendRequestCount() + 1);

            EventBus.getDefault().post(EventBusConstant.EB_TYPE_FRIEND_REQUEST_UPDATE);
            MessageNotifyManager.play();
        }
    }

    private void addFriendRequest(FriendRequestBean model) {
        FriendRequest request = new FriendRequest();
        request.setRemark(model.getRemark());
        request.setFriendHeadImage(model.getUserHeadImage());
        request.setFriendName(model.getUserName());
        request.setFriendId(model.getUserId());
        request.setStatus(0);
        DaoUtils.getFriendRequestManagerInstance().save(request);
    }
}
