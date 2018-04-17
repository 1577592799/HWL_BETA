package com.hwl.beta.ui.user;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Toast;

import com.hwl.beta.R;
import com.hwl.beta.databinding.ActivityUserIndexBinding;
import com.hwl.beta.db.DaoUtils;
import com.hwl.beta.db.entity.Friend;
import com.hwl.beta.net.user.NetUserInfo;
import com.hwl.beta.sp.UserSP;
import com.hwl.beta.ui.common.UITransfer;
import com.hwl.beta.ui.user.action.IUserIndexListener;
import com.hwl.beta.ui.user.bean.ImageViewBean;
import com.hwl.beta.ui.user.bean.UserIndexBean;

/**
 * Created by Administrator on 2018/1/9.
 */
public class ActivityUserIndex extends FragmentActivity {

    ActivityUserIndexBinding binding;
    Activity activity;
    UserIndexBean userBean;

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
            NetUserInfo netUserInfo=UserSP.getUserInfo();
            userBean.setSymbol(netUserInfo.getSymbol());
            userBean.setShowName(netUserInfo.getShowName());
//            userBean.setRemark("");
        } else {
            userBean.setFriend(false);
        }
        binding = DataBindingUtil.setContentView(activity, R.layout.activity_user_index);
        binding.setAction(new UserIndexListener());
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
                    }
                });
    }

    public class UserIndexListener implements IUserIndexListener {

        @Override
        public void onRemarkClick() {

        }

        @Override
        public void onCircleClick() {

        }

        @Override
        public void onSendMessageClick() {
            UITransfer.toChatUserActivity(activity, userBean.getUserId(), userBean.getShowName(), userBean.getUserImage());
        }

        @Override
        public void onDeleteClick() {

        }

        @Override
        public void onBlackClick() {

        }
    }
}
