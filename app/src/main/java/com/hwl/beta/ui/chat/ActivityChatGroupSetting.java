package com.hwl.beta.ui.chat;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.hwl.beta.R;
import com.hwl.beta.databinding.ActivityChatGroupSettingBinding;
import com.hwl.beta.db.DaoUtils;
import com.hwl.beta.db.entity.GroupInfo;
import com.hwl.beta.db.entity.GroupUserInfo;
import com.hwl.beta.sp.UserSP;
import com.hwl.beta.ui.chat.action.IChatGroupSettingListener;
import com.hwl.beta.ui.common.UITransfer;
import com.hwl.beta.ui.group.adp.GroupUserAdapter;
import com.hwl.beta.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class ActivityChatGroupSetting extends FragmentActivity {

    Activity activity;
    ActivityChatGroupSettingBinding binding;
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
        if (StringUtils.isBlank(group.getMyUserName())) {
            group.setMyUserName(UserSP.getUserName());
        }

        users = DaoUtils.getGroupUserInfoManagerInstance().getUsers(groupGuid);
        if (users == null) {
            users = new ArrayList<>();
        }

        binding = DataBindingUtil.setContentView(activity, R.layout.activity_chat_group_setting);
        binding.setAction(new ChatGroupSettingListener());
        binding.setSetting(group);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==Activity.RESULT_OK){
            switch (requestCode){
                case 1:
                    group.setGroupNote(data.getStringExtra("content"));
                    break;
                case 2:
                    group.setGroupName(data.getStringExtra("content"));
                    break;
                case 3:
                    group.setMyUserName(data.getStringExtra("content"));
                    break;
            }
        }
    }

    public class ChatGroupSettingListener implements IChatGroupSettingListener {

        @Override
        public void onGroupNoteClick() {
            UITransfer.toChatGroupSettingEditActivity(activity, 1, group.getGroupNote());
        }

        @Override
        public void onGroupNameClick() {
            UITransfer.toChatGroupSettingEditActivity(activity, 2, group.getGroupName());
        }

        @Override
        public void onMyNameClick() {
            UITransfer.toChatGroupSettingEditActivity(activity, 3, group.getMyUserName());
        }

        @Override
        public void onShieldCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            group.setIsShield(isChecked);
            DaoUtils.getGroupInfoManagerInstance().add(group);
        }

        @Override
        public void onSearchClick() {

        }

        @Override
        public void onClearClick() {

        }

        @Override
        public void onExitClick() {

        }
    }
}
