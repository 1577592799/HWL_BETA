package com.hwl.beta.ui.user;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.hwl.beta.R;
import com.hwl.beta.databinding.ActivityUserMessageSettingBinding;
import com.hwl.beta.ui.common.BaseActivity;

public class ActivityUserMessageSetting extends BaseActivity {

    Activity activity;
    ActivityUserMessageSettingBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        binding = DataBindingUtil.setContentView(activity, R.layout.activity_user_message_setting);

        initView();
    }

    private void initView() {
        binding.tbTitle.setTitle("消息提示设置")
                .setImageLeftClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                })
                .setImageRightHide();
    }
}
