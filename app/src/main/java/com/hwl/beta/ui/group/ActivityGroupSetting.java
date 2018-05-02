package com.hwl.beta.ui.group;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.hwl.beta.R;
import com.hwl.beta.databinding.ActivityGroupSettingBinding;
import com.hwl.beta.db.DaoUtils;
import com.hwl.beta.db.entity.GroupInfo;
import com.hwl.beta.db.entity.GroupUserInfo;
import com.hwl.beta.ui.common.UITransfer;
import com.hwl.beta.ui.group.adp.GroupUserAdapter;
import com.hwl.beta.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class ActivityGroupSetting extends FragmentActivity {

    Activity activity;
    ActivityGroupSettingBinding binding;
    List<GroupUserInfo> users;
    GroupInfo group;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        String groupGuid = getIntent().getStringExtra("groupguid");
        if (StringUtils.isBlank(groupGuid)) {
            Toast.makeText(activity, "群组参数错误", Toast.LENGTH_SHORT).show();
            finish();
        }
        group = DaoUtils.getGroupInfoManagerInstance().get(groupGuid);
        if (group == null) {
            Toast.makeText(activity, "群组不存在", Toast.LENGTH_SHORT).show();
            finish();
        }

        users = DaoUtils.getGroupUserInfoManagerInstance().getUsers(groupGuid);
        if (users == null) {
            users = new ArrayList<>();
        }

        binding = DataBindingUtil.setContentView(activity, R.layout.activity_group_setting);
        initView();
    }

    private void initView() {
        binding.tbTitle.setTitle("群组设置")
                .setImageRightHide()
                .setImageLeftClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                });
        binding.gvUserContainer.setAdapter(new GroupUserAdapter(activity, users));
        binding.gvUserContainer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GroupUserInfo user = users.get(position);
                if (user != null) {
                    UITransfer.toUserIndexActivity(activity, user.getUserId(), user.getUserName(), user.getUserHeadImage());
                }
            }
        });
    }

}
