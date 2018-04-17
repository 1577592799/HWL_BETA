package com.hwl.beta.ui.chat;

import android.app.Activity;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import com.hwl.beta.R;
import com.hwl.beta.databinding.FragmentRecordBinding;
import com.hwl.beta.db.DaoUtils;
import com.hwl.beta.db.entity.ChatRecordMessage;
import com.hwl.beta.mq.MQConstant;
import com.hwl.beta.sp.MessageCountSP;
import com.hwl.beta.sp.UserSP;
import com.hwl.beta.ui.busbean.EventBusConstant;
import com.hwl.beta.ui.chat.adp.RecordAdapter;
import com.hwl.beta.ui.common.BaseFragment;
import com.hwl.beta.ui.common.ChatRecordMessageComparator;
import com.hwl.beta.ui.common.UITransfer;
import com.hwl.beta.utils.NetworkUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Administrator on 2017/12/27.
 */

public class FragmentRecord extends BaseFragment {
    FragmentRecordBinding binding;
    List<ChatRecordMessage> records;
    RecordAdapter recordAdapter;
    Activity activity;
    ChatRecordMessageComparator dateComparator;
    int currMessageTotalCount = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        activity = getActivity();
        dateComparator = new ChatRecordMessageComparator();

        records = DaoUtils.getChatRecordMessageManagerInstance().getAll();
        if (records == null) {
            records = new ArrayList<>();
        }

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_record, container, false);
        initView();

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        return binding.getRoot();
    }

    private void initView() {
        Collections.sort(records, dateComparator);
        recordAdapter = new RecordAdapter(activity, records, new RecordAdapter.IAdapterListener() {
            @Override
            public void onLoadComplete(int messageTotalCount) {
                if (currMessageTotalCount == messageTotalCount) return;
                currMessageTotalCount = messageTotalCount;
                MessageCountSP.setChatMessageCount(messageTotalCount);
                EventBus.getDefault().post(EventBusConstant.EB_TYPE_CHAT_MESSAGE_UPDATE);
            }

            @Override
            public void onItemClick(int position) {
                ChatRecordMessage record = records.get(position);
                if (record.getRecordType() == MQConstant.CHAT_RECORD_TYPE_USER) {
                    if (record.getFromUserId() == UserSP.getUserId()) {
                        UITransfer.toChatUserActivity(activity, record.getToUserId(), record.getToUserName(), record.getToUserHeadImage(), record.getRecordId());
                    } else {
                        UITransfer.toChatUserActivity(activity, record.getFromUserId(), record.getFromUserName(), record.getFromUserHeadImage(), record.getRecordId());
                    }
                } else if (record.getRecordType() == MQConstant.CHAT_RECORD_TYPE_GROUP) {
                    UITransfer.toChatGroupActivity(activity, record.getGruopGuid(), record.getGroupName(), record.getRecordId());
                }
            }

            @Override
            public void onItemLongClick(View view, final int position) {
                PopupMenu popup = new PopupMenu(activity, view);
                popup.getMenuInflater().inflate(R.menu.popup_record_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        final ChatRecordMessage record = records.get(position);
                        switch (item.getItemId()) {
                            case R.id.pop_set_top:
                                break;
                            case R.id.pop_delete_record:
                                if (DaoUtils.getChatRecordMessageManagerInstance().deleteRecord(record)) {
                                    records.remove(record);
                                    recordAdapter.notifyDataSetChanged();
                                }
                                break;
                            case R.id.pop_delete_messages:
                                new AlertDialog.Builder(activity)
                                        .setMessage("聊天数据清空后,不能恢复,确认清空 ?")
                                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (DaoUtils.getChatRecordMessageManagerInstance().deleteRecord(record)) {
                                                    boolean succ = false;
                                                    if (record.getRecordType() == MQConstant.CHAT_RECORD_TYPE_GROUP) {
                                                        succ = DaoUtils.getChatGroupMessageManagerInstance().deleteMessages(record.getGruopGuid());
                                                    } else if (record.getRecordType() == MQConstant.CHAT_RECORD_TYPE_USER) {
                                                        succ = DaoUtils.getChatUserMessageManagerInstance().deleteUserMessages(record.getFromUserId(), record.getToUserId());
                                                    }
                                                    if (succ) {
                                                        records.remove(record);
                                                        recordAdapter.notifyDataSetChanged();
                                                    }
                                                } else {
                                                    records.remove(record);
                                                    recordAdapter.notifyDataSetChanged();
                                                }
                                                dialog.dismiss();
                                            }
                                        })
                                        .setNegativeButton("取消", null)
                                        .show();
                                break;
                        }
                        return true;
                    }
                });
                popup.show();
            }
        });
        binding.rvRecordContainer.setAdapter(recordAdapter);
        binding.rvRecordContainer.setLayoutManager(new LinearLayoutManager(activity));

        binding.tvCheckNetwork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NetworkUtils.openWirelessSettings();
            }
        });
        if (NetworkUtils.isConnected()) {
            binding.llNetworkNone.setVisibility(View.GONE);
        } else {
            binding.llNetworkNone.setVisibility(View.VISIBLE);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateRecord(ChatRecordMessage record) {
        if (record == null) return;
        for (int i = 0; i < records.size(); i++) {
            if (record.getRecordId() == records.get(i).getRecordId()) {
                records.remove(records.get(i));
                break;
            }
        }
        records.add(record);
        Collections.sort(records, dateComparator);
        recordAdapter.notifyDataSetChanged();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateNetwork(Integer ebType) {
        switch (ebType) {
            case EventBusConstant.EB_TYPE_NETWORK_CONNECT_UPDATE:
                binding.llNetworkNone.setVisibility(View.GONE);
                break;
            case EventBusConstant.EB_TYPE_NETWORK_BREAK_UPDATE:
                binding.llNetworkNone.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        Log.d("FragmentRecord", "onDestroy");
        EventBus.getDefault().unregister(this);
    }
}
