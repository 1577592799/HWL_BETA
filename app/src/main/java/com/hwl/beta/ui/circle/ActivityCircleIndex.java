package com.hwl.beta.ui.circle;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.hwl.beta.R;
import com.hwl.beta.databinding.ActivityCircleIndexBinding;
import com.hwl.beta.ui.common.UITransfer;

public class ActivityCircleIndex extends FragmentActivity {

    Activity activity;
    ActivityCircleIndexBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        binding = DataBindingUtil.setContentView(activity, R.layout.activity_circle_index);

        initView();
    }

    private void initView() {
        binding.tbTitle.setTitle("朋友圈")
                .setImageRightClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        UITransfer.toCirclePublishActivity(activity);
                    }
                })
                .setImageLeftClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                });
    }
}
