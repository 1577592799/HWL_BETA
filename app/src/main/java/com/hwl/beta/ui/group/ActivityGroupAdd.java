package com.hwl.beta.ui.group;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.Toast;

import com.hwl.beta.R;
import com.hwl.beta.databinding.ActivityGroupAddBinding;
import com.hwl.beta.db.DaoUtils;
import com.hwl.beta.db.entity.Friend;
import com.hwl.beta.ui.common.FriendComparator;
import com.hwl.beta.ui.group.adp.GroupAddAdapter;
import com.hwl.beta.ui.widget.SideBar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ActivityGroupAdd extends FragmentActivity {

    Activity activity;
    ActivityGroupAddBinding binding;
    FriendComparator pinyinComparator;
    List<Friend> users;
    GroupAddAdapter addAdapter;
    List<Friend> selectUsers;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        pinyinComparator = new FriendComparator();
        users = DaoUtils.getFriendManagerInstance().getAll();
        if (users == null) {
            users = new ArrayList<>();
        } else {
            Collections.sort(users, pinyinComparator);
        }
        addAdapter = new GroupAddAdapter(activity, users);

        binding = DataBindingUtil.setContentView(activity, R.layout.activity_group_add);
        binding.setFriendAdapter(addAdapter);

        initView();
    }

    private void initView() {
        binding.tbTitle.setTitle("发起群聊")
                .setTitleRightShow()
                .setTitleRightText("创建")
                .setImageRightHide()
                .setTitleRightBackground(R.drawable.bg_top)
                .setImageLeftClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                })
                .setTitleRightClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(activity, selectUsers.size() + "发起群聊天", Toast.LENGTH_SHORT).show();

                    }
                });
        binding.lvFriends.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Friend friend = users.get(position);
                if(addAdapter.setCheckBox(view))
                    selectUsers.add(friend);
                else{
                    selectUsers.remove(friend);
                }
            }
        });
        binding.sidrbarLetter.setTextView(binding.tvLetter);
        binding.sidrbarLetter.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String letter) {
                // 该字母首次出现的位置
                int position = addAdapter.getPositionForSection(letter.charAt(0));
                if (position != -1) {
                    binding.lvFriends.setSelection(position);
                }
            }
        });

        selectUsers = new ArrayList<>();
    }
}
