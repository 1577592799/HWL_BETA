package com.hwl.beta.ui.user;

import android.app.Activity;
import android.app.Dialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.hwl.beta.R;
import com.hwl.beta.databinding.ActivityUserIndexBinding;
import com.hwl.beta.db.DaoUtils;
import com.hwl.beta.db.entity.Friend;
import com.hwl.beta.net.NetConstant;
import com.hwl.beta.net.user.NetUserInfo;
import com.hwl.beta.net.user.UserService;
import com.hwl.beta.net.user.body.DeleteFriendResponse;
import com.hwl.beta.sp.UserSP;
import com.hwl.beta.ui.busbean.EventBusConstant;
import com.hwl.beta.ui.busbean.EventDeleteFriend;
import com.hwl.beta.ui.common.UITransfer;
import com.hwl.beta.ui.common.rxext.NetDefaultObserver;
import com.hwl.beta.ui.dialog.LoadingDialog;
import com.hwl.beta.ui.user.action.IUserIndexListener;
import com.hwl.beta.ui.user.bean.ImageViewBean;
import com.hwl.beta.ui.user.bean.UserIndexBean;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Administrator on 2018/1/9.
 */
public class ActivityUserIndex extends FragmentActivity {

    ActivityUserIndexBinding binding;
    Activity activity;
    UserIndexBean userBean;
    UserIndexListener indexListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        userBean = new UserIndexBean(
                getIntent().getLongExtra("userid", 0),
                getIntent().getStringExtra("username"),
                getIntent().getStringExtra("userimage"));

        if (userBean.getUserId() <= 0) {
            Toast.makeText(activity, "用户不存在", Toast.LENGTH_SHORT).show();
            finish();
        }

        Friend friend = DaoUtils.getFriendManagerInstance().get(userBean.getUserId());
        if (friend != null) {
            userBean.setFriend(true);
            userBean.setRegisterAddress(friend.getCountry());
            userBean.setRemark(friend.getRemark());
            userBean.setSymbol(friend.getSymbol());
        } else if (userBean.getUserId() == UserSP.getUserId()) {
            NetUserInfo netUserInfo = UserSP.getUserInfo();
            userBean.setSymbol(netUserInfo.getSymbol());
            userBean.setShowName(netUserInfo.getShowName());
//            userBean.setRemark("");
        } else {
            userBean.setFriend(false);
        }
        indexListener = new UserIndexListener();
        binding = DataBindingUtil.setContentView(activity, R.layout.activity_user_index);
        binding.setAction(indexListener);
        binding.setImage(new ImageViewBean(userBean.getUserImage()));
        binding.setUser(userBean);

        binding.tbTitle.setTitle("用户详情")
                .setImageRightResource(R.drawable.v_more)
                .setImageLeftClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                })
                .setImageRightClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showUserActionDialog();
                    }
                });
    }

    private void showUserActionDialog() {
        final Dialog actionDialog = new Dialog(activity, R.style.BottomDialog);
        LinearLayout root = (LinearLayout) LayoutInflater.from(activity).inflate(
                R.layout.user_action_dialog, null);
        //初始化视图
        root.findViewById(R.id.btn_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!userBean.isFriend()) {
                    Toast.makeText(activity, "对方还不是你的好友", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (userBean.getUserId() == UserSP.getUserId()) {
                    Toast.makeText(activity, "不能删除自己", Toast.LENGTH_SHORT).show();
                    return;
                }
                LoadingDialog.show(activity, "删除中,请稍后...");
                actionDialog.dismiss();
                indexListener.onDeleteClick();
            }
        });
        root.findViewById(R.id.btn_add_black).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userBean.getUserId() == UserSP.getUserId()) {
                    Toast.makeText(activity, "不能加自己到黑名单", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(activity, "添加黑名单功能稍后开放...", Toast.LENGTH_SHORT).show();
            }
        });
        root.findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionDialog.dismiss();
            }
        });
        actionDialog.setContentView(root);
        Window dialogWindow = actionDialog.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
//        dialogWindow.setWindowAnimations(R.style.dialogstyle); // 添加动画
        WindowManager.LayoutParams lp = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        lp.x = 0; // 新位置X坐标
        lp.y = 0; // 新位置Y坐标
        lp.width = (int) getResources().getDisplayMetrics().widthPixels; // 宽度
        root.measure(0, 0);
        lp.height = root.getMeasuredHeight();

        lp.alpha = 9f; // 透明度
        dialogWindow.setAttributes(lp);
        actionDialog.show();
    }

    public class UserIndexListener implements IUserIndexListener {

        @Override
        public void onRemarkClick() {

        }

        @Override
        public void onCircleClick() {

        }

        @Override
        public void onAddUserClick(View view) {
            Toast.makeText(activity, "添加好友功能稍后开放...", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onSendMessageClick() {
            UITransfer.toChatUserActivity(activity, userBean.getUserId(), userBean.getShowName(), userBean.getUserImage());
        }

        @Override
        public void onDeleteClick() {
            UserService.deleteFriend(userBean.getUserId())
                    .subscribe(new NetDefaultObserver<DeleteFriendResponse>() {
                        @Override
                        protected void onSuccess(DeleteFriendResponse response) {
                            LoadingDialog.hide();
                            if (response.getStatus() == NetConstant.RESULT_SUCCESS) {
                                Toast.makeText(activity, "成功删除好友", Toast.LENGTH_SHORT).show();
                                DaoUtils.getFriendManagerInstance().deleteFriend(userBean.getUserId());
                                UserSP.deleteOneFriendCount();
                                EventBus.getDefault().post(new EventDeleteFriend(userBean.getUserId()));
                                //删除好友聊天数据
                                if (DaoUtils.getChatRecordMessageManagerInstance().deleteUserRecords(UserSP.getUserId(), userBean.getUserId())) {
                                    DaoUtils.getChatUserMessageManagerInstance().deleteUserMessages(UserSP.getUserId(), userBean.getUserId());
                                }
                                activity.finish();
                            } else {
                                onError("删除失败");
                            }
                        }

                        @Override
                        protected void onError(String resultMessage) {
                            LoadingDialog.hide();
                            super.onError(resultMessage);
                        }
                    });
        }

        @Override
        public void onBlackClick() {

        }
    }
}
