package com.hwl.beta.ui.near;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Toast;

import com.hwl.beta.R;
import com.hwl.beta.databinding.ActivityNearMessagesBinding;
import com.hwl.beta.db.DaoUtils;
import com.hwl.beta.db.entity.NearCircleMessage;
import com.hwl.beta.ui.near.adp.NearMessageAdapter;

import java.util.ArrayList;
import java.util.List;

public class ActivityNearMessages extends FragmentActivity {

    Activity activity;
    ActivityNearMessagesBinding binding;
    List<NearCircleMessage> messages;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        messages = DaoUtils.getNearCircleMessageManagerInstance().getAll();
        if (messages == null) {
            messages = new ArrayList<>();
        }
        binding = DataBindingUtil.setContentView(activity, R.layout.activity_near_messages);

        initView();
    }

    private void initView() {
        binding.tbTitle.setTitle("附近的消息")
                .setTitleRightText("清空")
                .setTitleRightBackground(R.drawable.bg_top)
                .setImageRightHide()
                .setTitleRightShow()
                .setImageLeftClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                })
                .setTitleRightClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(activity, "清空消息功能稍后开放...", Toast.LENGTH_SHORT).show();
                    }
                });
        binding.rvMessageContainer.setAdapter(new NearMessageAdapter(activity, messages));
        binding.rvMessageContainer.setLayoutManager(new LinearLayoutManager(activity));
    }
}
