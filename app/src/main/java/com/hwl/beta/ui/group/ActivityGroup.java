package com.hwl.beta.ui.group;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Toast;

import com.hwl.beta.R;
import com.hwl.beta.databinding.ActivityGroupBinding;
import com.hwl.beta.db.DaoUtils;
import com.hwl.beta.db.entity.GroupInfo;
import com.hwl.beta.ui.common.UITransfer;
import com.hwl.beta.ui.group.adp.GroupAdapter;

import java.util.ArrayList;
import java.util.List;

public class ActivityGroup extends FragmentActivity {

    ActivityGroupBinding binding;
    Activity activity;
    List<GroupInfo> groupInfos;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        groupInfos = this.getGroupInfos();

        binding = DataBindingUtil.setContentView(activity, R.layout.activity_group);

        initView();
    }

    private void initView() {
        binding.tbTitle.setTitle("我的群组")
                .setImageLeftClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                })
                .setImageRightClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Toast.makeText(activity, "发起群聊天", Toast.LENGTH_SHORT).show();
                        UITransfer.toGroupAddActivity(activity);
                    }
                });
        binding.rvGroupContainer.setAdapter(new GroupAdapter(activity, groupInfos));
        binding.rvGroupContainer.setLayoutManager(new LinearLayoutManager(activity));
    }

    public List<GroupInfo> getGroupInfos() {
        groupInfos = DaoUtils.getGroupInfoManagerInstance().getAll();
        if (groupInfos == null) {
            groupInfos = new ArrayList<>();
        }
        groupInfos.add(0, new GroupInfo());

        return groupInfos;
    }
}
