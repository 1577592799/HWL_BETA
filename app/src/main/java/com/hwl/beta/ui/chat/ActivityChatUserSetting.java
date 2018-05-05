package com.hwl.beta.ui.chat;

import android.app.Activity;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.hwl.beta.R;
import com.hwl.beta.databinding.ActivityChatUserSettingBinding;
import com.hwl.beta.db.DaoUtils;
import com.hwl.beta.db.entity.ChatUserSetting;
import com.hwl.beta.sp.UserSP;
import com.hwl.beta.ui.busbean.EventActionGroup;
import com.hwl.beta.ui.busbean.EventBusConstant;
import com.hwl.beta.ui.busbean.EventClearUserMessages;
import com.hwl.beta.ui.common.UITransfer;
import com.hwl.beta.ui.user.bean.ImageViewBean;

import org.greenrobot.eventbus.EventBus;

public class ActivityChatUserSetting extends FragmentActivity {

    ActivityChatUserSettingBinding binding;
    Activity activity;
    long myUserId;
    long viewUserId;
    String viewUserName;
    String viewUserImage;
    ChatUserSetting userSetting;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;

        viewUserId = getIntent().getLongExtra("userid", 0);
        if (viewUserId <= 0) {
            Toast.makeText(activity, "用户参数错误", Toast.LENGTH_SHORT).show();
            finish();
        }

        myUserId = UserSP.getUserId();
        viewUserName = getIntent().getStringExtra("username");
        viewUserImage = getIntent().getStringExtra("userimage");
        userSetting = DaoUtils.getChatUserMessageManagerInstance().getChatUserSetting(viewUserId);
        if (userSetting == null) {
            userSetting = new ChatUserSetting();
            userSetting.setUserId(viewUserId);
            userSetting.setIsShield(false);
            DaoUtils.getChatUserMessageManagerInstance().setChatUserSetting(userSetting);
        }

        binding = DataBindingUtil.setContentView(activity, R.layout.activity_chat_user_setting);

        initView();
    }

    private void initView() {
        binding.tbTitle.setTitle("聊天设置")
                .setImageRightHide()
                .setImageLeftClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                });

        ImageViewBean.loadImage(binding.ivHeader, viewUserImage);
        binding.tvName.setText(viewUserName);
        binding.switchShield.setChecked(userSetting.getIsShield());
        binding.switchShield.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                userSetting.setIsShield(isChecked);
                DaoUtils.getChatUserMessageManagerInstance().setChatUserSetting(userSetting);
            }
        });
        binding.rlSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(activity, "搜索功能稍后开放...", Toast.LENGTH_SHORT).show();
            }
        });
        binding.rlClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(activity)
                        .setMessage("聊天数据清空后,不能恢复,确认清空 ?")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DaoUtils.getChatUserMessageManagerInstance().deleteUserMessages(myUserId, viewUserId);
                                EventBus.getDefault().post(new EventClearUserMessages(EventBusConstant.EB_TYPE_ACTINO_CLEAR, myUserId, viewUserId));
                                Toast.makeText(activity, "聊天记录已经清空", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
            }
        });
    }
}
