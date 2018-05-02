package com.hwl.beta.ui.chat;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.hwl.beta.R;
import com.hwl.beta.databinding.ActivityChatUserSettingBinding;
import com.hwl.beta.ui.common.UITransfer;

public class ActivityChatUserSetting extends FragmentActivity {

    ActivityChatUserSettingBinding binding;
    Activity activity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;

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
    }
}
