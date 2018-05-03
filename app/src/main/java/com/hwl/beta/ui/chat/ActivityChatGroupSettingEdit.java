package com.hwl.beta.ui.chat;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.hwl.beta.R;
import com.hwl.beta.databinding.ActivityChatGroupSettingEditBinding;
import com.hwl.beta.ui.dialog.LoadingDialog;

public class ActivityChatGroupSettingEdit extends FragmentActivity {
    Activity activity;
    ActivityChatGroupSettingEditBinding binding;
    int editType;
    String content;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;

        binding = DataBindingUtil.setContentView(activity, R.layout.activity_chat_group_setting_edit);

        initView();
    }

    private void initView() {
        binding.tbTitle
                .setTitleRightShow()
                .setTitleRightText("提交")
                .setImageRightHide()
                .setTitleRightBackground(R.drawable.bg_top)
                .setTitleRightClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                })
                .setImageLeftClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                });

        editType = getIntent().getIntExtra("edittype", 0);
        content = getIntent().getStringExtra("content");

        switch (editType) {
            case 1:
                binding.tbTitle.setTitle("设置群组公告");
                binding.etGroupNote.setText(content);
                binding.etGroupNote.setVisibility(View.VISIBLE);
                binding.etContent.setVisibility(View.GONE);
                break;
            case 2:
                binding.tbTitle.setTitle("设置群组名称");
                binding.etContent.setText(content);
                binding.etGroupNote.setVisibility(View.GONE);
                binding.etContent.setVisibility(View.VISIBLE);
                break;
            case 3:
                binding.tbTitle.setTitle("设置我在群的昵称");
                binding.etContent.setText(content);
                binding.etGroupNote.setVisibility(View.GONE);
                binding.etContent.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void saveGroupNote() {
        if (content == null) {
            content = "";
        }
        if (content.equals(binding.etGroupNote.getText())) {
            return;
        }
        LoadingDialog.show(activity);
        //调用server api
        //成功后保存到本地
        //发送MQ通知群组人
    }
}
