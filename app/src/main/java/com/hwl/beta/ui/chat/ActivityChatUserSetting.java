package com.hwl.beta.ui.chat;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.hwl.beta.R;
import com.hwl.beta.databinding.ActivityChatUserSettingBinding;
import com.hwl.beta.db.DaoUtils;
import com.hwl.beta.db.entity.ChatUserSetting;
import com.hwl.beta.ui.common.UITransfer;
import com.hwl.beta.ui.user.bean.ImageViewBean;

public class ActivityChatUserSetting extends FragmentActivity {

    ActivityChatUserSettingBinding binding;
    Activity activity;
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
//        binding.rlClear.setOnClickListener()
    }
}
