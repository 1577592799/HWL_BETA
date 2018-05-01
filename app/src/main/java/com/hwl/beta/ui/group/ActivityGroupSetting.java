package com.hwl.beta.ui.group;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import com.hwl.beta.R;
import com.hwl.beta.databinding.ActivityGroupSettingBinding;

public class ActivityGroupSetting extends FragmentActivity {

    Activity activity;
    ActivityGroupSettingBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        binding = DataBindingUtil.setContentView(activity, R.layout.activity_group_setting);
    }
}
