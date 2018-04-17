package com.hwl.beta.ui.chat;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.hwl.beta.R;
import com.hwl.beta.db.DaoUtils;
import com.hwl.beta.db.entity.ChatRecordMessage;
import com.hwl.beta.db.entity.ChatUserMessage;
import com.hwl.beta.db.entity.Friend;
import com.hwl.beta.emotion.EmotionControlPannel;
import com.hwl.beta.emotion.audio.AudioPlay;
import com.hwl.beta.emotion.audio.MediaManager;
import com.hwl.beta.sp.UserSP;
import com.hwl.beta.ui.chat.action.IChatMessageItemListener;
import com.hwl.beta.ui.chat.adp.ChatUserMessageAdapter;
import com.hwl.beta.ui.chat.bean.ChatImageViewBean;
import com.hwl.beta.ui.chat.imp.ChatUserEmotionPannelListener;
import com.hwl.beta.ui.common.ClipboardAction;
import com.hwl.beta.ui.common.UITransfer;
import com.hwl.beta.ui.imgselect.bean.ImageBean;
import com.hwl.beta.ui.video.ActivityVideoPlay;
import com.hwl.beta.ui.widget.TitleBar;
import com.hwl.beta.utils.StringUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/12/31.
 */

public class ActivityChatUser extends FragmentActivity {

    Activity activity;
    List<ChatUserMessage> messages;
    ChatUserMessageAdapter messageAdapter;
    RecyclerView rvMessageContainer;
    ChatUserEmotionPannelListener emotionPannelListener;
    Friend user;
    boolean isFriend = false;
    long myUserId;
    long currentRecordId = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_user);
        activity = this;

        currentRecordId = getIntent().getLongExtra("recordid", 0);
        long userId = getIntent().getLongExtra("userid", 0);
        String userName = getIntent().getStringExtra("username");
        String userImage = getIntent().getStringExtra("userimage");
        if (userId <= 0) {
            Toast.makeText(activity, "用户不存在", Toast.LENGTH_SHORT).show();
            finish();
        }
        myUserId = UserSP.getUserId();
        user = DaoUtils.getFriendManagerInstance().get(userId);
        if (user == null) {
            user = new Friend();
            user.setId(userId);
            user.setName(userName);
            user.setHeadImage(userImage);
        } else {
            isFriend = true;
        }

        messages = DaoUtils.getChatUserMessageManagerInstance().getFromUserMessages(myUserId, user.getId());
        if (messages == null) {
            messages = new ArrayList<>();
        }

        TitleBar tbTitle = findViewById(R.id.tb_title);
        tbTitle.setTitle(user.getName())
                .setImageRightResource(R.drawable.ic_setting)
                .setImageRightClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(activity, "用户聊天信息设置", Toast.LENGTH_SHORT).show();
                    }
                })
                .setImageLeftClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                });

        emotionPannelListener = new ChatUserEmotionPannelListener(activity, user);
        final EmotionControlPannel ecpEmotion = findViewById(R.id.ecp_emotion);
        ecpEmotion.setEmotionPannelListener(emotionPannelListener);

        messageAdapter = new ChatUserMessageAdapter(activity, messages, new ChatMessageItemListener(),
                new ChatUserMessageAdapter.IAdapterListener() {
                    @Override
                    public void onLoadLastReceivedMessageComplete(ChatUserMessage message) {
                        checkFriendInfo(message);
                    }
                });
        rvMessageContainer = findViewById(R.id.rv_message_container);
        rvMessageContainer.setAdapter(messageAdapter);
        rvMessageContainer.setLayoutManager(new LinearLayoutManager(activity));
        rvMessageContainer.scrollToPosition(messageAdapter.getItemCount() - 1);
        rvMessageContainer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        ecpEmotion.hideEmotionFunction();
                        break;
                }
                return false;
            }
        });

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        MediaManager.release();
        if (currentRecordId > 0) {
            ChatRecordMessage recordMessage = DaoUtils.getChatRecordMessageManagerInstance().clearUnreadCount(currentRecordId);
            if (recordMessage != null) {
                EventBus.getDefault().post(recordMessage);
            }
        }
    }

    private void checkFriendInfo(ChatUserMessage message) {
        if (message == null) return;
        //检测用户的名称和头像j是否已经更改
        if (message.getFromUserId() == user.getId()) {
            if (!message.getFromUserHeadImage().equals(user.getHeadImage()) || !message.getFromUserName().equals(user.getName())) {
                user.setHeadImage(message.getFromUserHeadImage());
                user.setName(message.getFromUserName());
                if (isFriend) {
                    //Log.d("CheckFriendInfo", "更换用户头像成功");
                    DaoUtils.getFriendManagerInstance().save(user);
                }
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateMessage(ChatUserMessage message) {
        if (message == null) return;
        if (message.getFromUserId() != user.getId() && message.getFromUserId() != myUserId) return;
        checkFriendInfo(message);

        boolean isExists = false;
        int position = 0;
        for (int i = 0; i < messages.size(); i++) {
            if (messages.get(i).getMsgId().equals(message.getMsgId())) {
                isExists = true;
                position = i;
                break;
            }
        }

        if (isExists) {
            messages.remove(position);
            messages.add(position, message);
        } else {
            messages.add(message);
        }

        messageAdapter.notifyDataSetChanged();
        rvMessageContainer.scrollToPosition(messageAdapter.getItemCount() - 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case 1:
                    ArrayList<ImageBean> list = data.getExtras().getParcelableArrayList("selectimages");
                    //Toast.makeText(activity, list.size() + " 张图片！", Toast.LENGTH_SHORT).show();
                    for (int i = 0; i < list.size(); i++) {
                        emotionPannelListener.sendChatUserImageMessage(list.get(i).getPath());
                    }
                    break;
                case 2:
                    emotionPannelListener.sendChatUserImageMessage();
                    break;
                case 3:
                    //Toast.makeText(activity, data.getStringExtra("videopath"), Toast.LENGTH_SHORT).show();
                    emotionPannelListener.sendChatUserVideoMessage(data.getStringExtra("videopath"));
                    break;
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public class ChatMessageItemListener implements IChatMessageItemListener {
        AudioPlay audioPlay;

        @Override
        public void onHeadImageClick(int position) {
            ChatUserMessage message = messages.get(position);
            UITransfer.toUserIndexActivity(activity, message.getFromUserId(), message.getFromUserName(), message.getFromUserHeadImage());
        }

        @Override
        public boolean onChatItemLongClick(View view, final int position) {
            final PopupMenu popup = new PopupMenu(activity, view);
            popup.getMenuInflater().inflate(R.menu.popup_message_menu, popup.getMenu());
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                public boolean onMenuItemClick(MenuItem item) {
                    ChatUserMessage message = messages.get(position);
                    switch (item.getItemId()) {
                        case R.id.pop_copy:
                            ClipboardAction.copy(activity, message.getContent());
                            break;
                        case R.id.pop_send_friend:
                            break;
                        case R.id.pop_delete:
                            if (DaoUtils.getChatUserMessageManagerInstance().deleteMessage(message)) {
                                messages.remove(message);
                                messageAdapter.notifyItemRemoved(position);
                                messageAdapter.notifyItemRangeChanged(position, messages.size() - position);
                            }
                            break;
                        case R.id.pop_collection:
                            break;
                    }
                    return true;
                }
            });
            popup.show();
            return true;
        }

        @Override
        public void onImageItemClick(int position) {

        }

        @Override
        public void onVideoItemClick(int position) {
            ChatUserMessage message = messages.get(position);
            String showUrl = ChatImageViewBean.getShowUrl(message.getLocalUrl(), null, message.getOriginalUrl());
            UITransfer.toVideoPlayActivity(activity, ActivityVideoPlay.MODE_VIEW, showUrl);
        }

        @Override
        public void onAudioItemClick(View view, int position) {
            ChatUserMessage message = messages.get(position);
            emotionPannelListener.playAudio((ImageView) view.findViewById(R.id.iv_audio), message);
        }

        @Override
        public void onFaildStatusClick(final View view, final int position) {
            new AlertDialog.Builder(activity)
                    .setMessage("重新发送")
                    .setPositiveButton("发送", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            view.setVisibility(View.GONE);
                            emotionPannelListener.resendMessage(messages.get(position));
                            dialog.dismiss();
                        }
                    })
                    .setNegativeButton("取消", null)
                    .show();
        }
    }
}
