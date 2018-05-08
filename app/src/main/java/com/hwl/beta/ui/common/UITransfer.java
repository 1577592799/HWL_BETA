package com.hwl.beta.ui.common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.hwl.beta.db.ext.CircleExt;
import com.hwl.beta.db.ext.NearCircleExt;
import com.hwl.beta.sp.UserPosSP;
import com.hwl.beta.sp.UserSP;
import com.hwl.beta.ui.chat.ActivityChatGroup;
import com.hwl.beta.ui.chat.ActivityChatGroupSettingEdit;
import com.hwl.beta.ui.chat.ActivityChatUser;
import com.hwl.beta.ui.chat.ActivityChatUserSetting;
import com.hwl.beta.ui.circle.ActivityCircleCommentPublish;
import com.hwl.beta.ui.circle.ActivityCircleDetail;
import com.hwl.beta.ui.circle.ActivityCircleIndex;
import com.hwl.beta.ui.circle.ActivityCirclePublish;
import com.hwl.beta.ui.circle.ActivityCircleUserIndex;
import com.hwl.beta.ui.dialog.ReloginDialogFragment;
import com.hwl.beta.ui.entry.ActivityGetpwd;
import com.hwl.beta.ui.entry.ActivityLogin;
import com.hwl.beta.ui.entry.ActivityMain;
import com.hwl.beta.ui.entry.ActivityRegister;
import com.hwl.beta.ui.entry.ActivityWelcome;
import com.hwl.beta.ui.group.ActivityGroup;
import com.hwl.beta.ui.group.ActivityGroupAdd;
import com.hwl.beta.ui.chat.ActivityChatGroupSetting;
import com.hwl.beta.ui.imgselect.ActivityImageSelect;
import com.hwl.beta.ui.near.ActivityCommentPublish;
import com.hwl.beta.ui.near.ActivityNearDetail;
import com.hwl.beta.ui.near.ActivityNearMessages;
import com.hwl.beta.ui.near.ActivityNearPublish;
import com.hwl.beta.ui.user.ActivityNewFriend;
import com.hwl.beta.ui.user.ActivityUserEdit;
import com.hwl.beta.ui.user.ActivityUserEditItem;
import com.hwl.beta.ui.user.ActivityUserIndex;
import com.hwl.beta.ui.user.ActivityUserSearch;
import com.hwl.beta.ui.video.ActivityVideoPlay;
import com.hwl.beta.ui.video.ActivityVideoSelect;

/**
 * Created by Administrator on 2018/3/27.
 */

public class UITransfer {

    public static void toLoginActivity(Activity context) {
        Intent intent = new Intent(context, ActivityLogin.class);
        context.startActivity(intent);
    }

    public static void toRegisterActivity(Activity context) {
        Intent intent = new Intent(context, ActivityRegister.class);
        context.startActivity(intent);
    }

    public static void toGetpwdActivity(Activity context) {
        Intent intent = new Intent(context, ActivityGetpwd.class);
        context.startActivity(intent);
    }

    public static void toWelcomeActivity(Activity context) {
        Intent intent = new Intent(context, ActivityWelcome.class);
        context.startActivity(intent);
    }

    public static void toMainActivity(Activity context) {
        Intent intent = new Intent(context, ActivityMain.class);
        context.startActivity(intent);
    }

    public static void toUserIndexActivity(Activity context, long userId, String userName, String userImage) {
        Intent intent = new Intent(context, ActivityUserIndex.class);
        intent.putExtra("userid", userId);
        intent.putExtra("username", userName);
        intent.putExtra("userimage", userImage);
        context.startActivity(intent);
    }

    public static void toUserSearchActivity(Activity context) {
        Intent intent = new Intent(context, ActivityUserSearch.class);
        context.startActivity(intent);
    }

    public static void toNewFriendActivity(Activity context) {
        Intent intent = new Intent(context, ActivityNewFriend.class);
        context.startActivity(intent);
    }

    public static void toUserEditActivity(Activity context) {
        Intent intent = new Intent(context, ActivityUserEdit.class);
        context.startActivity(intent);
    }

    public static void toUserEditItemActivity(Activity context, int actoinType, String editContent) {
        toUserEditItemActivity(context, actoinType, editContent, 0);
    }

    public static void toUserEditItemActivity(Activity context, int actoinType, String editContent, long friendId) {
        Intent intent = new Intent(context, ActivityUserEditItem.class);
        intent.putExtra("actiontype", actoinType);
        intent.putExtra("editcontent", editContent);
        if (friendId > 0) {
            intent.putExtra("friendid", friendId);
        }
        context.startActivity(intent);
    }

    public static void toImageSelectActivity(Activity context, int selectType, int requestCode) {
        toImageSelectActivity(context, selectType, 1, requestCode);
    }

    public static void toImageSelectActivity(Activity context, int selectType, int selectCount, int requestCode) {
        Intent intent = new Intent(context, ActivityImageSelect.class);
        intent.putExtra("selecttype", selectType);
        intent.putExtra("selectcount", selectCount);
//        context.startActivity(intent);
        context.startActivityForResult(intent, requestCode);
    }

    public static void toChatUserActivity(Activity context, long userId, String userName, String userImage) {
        toChatUserActivity(context, userId, userName, userImage, 0);
    }

    public static void toChatUserActivity(Activity context, long userId, String userName, String userImage, long recordId) {
        Intent intent = new Intent(context, ActivityChatUser.class);
        intent.putExtra("userid", userId);
        intent.putExtra("username", userName);
        intent.putExtra("userimage", userImage);
        intent.putExtra("recordid", recordId);
        context.startActivity(intent);
    }

    public static void toVideoSelectActivity(Activity context, int requestCode) {
        Intent intent = new Intent(context, ActivityVideoSelect.class);
        context.startActivityForResult(intent, requestCode);
    }

    public static void toVideoPlayActivity(Activity context, int videoMode, String videoPath) {
        toVideoPlayActivity(context, videoMode, videoPath, 0);
    }

    public static void toVideoPlayActivity(Activity context, int videoMode, String videoPath, int requestCode) {
        Intent intent = new Intent(context, ActivityVideoPlay.class);
        intent.putExtra("videopath", videoPath);
        intent.putExtra("videomode", videoMode);
        if (requestCode <= 0) {
            context.startActivity(intent);
        } else {
            context.startActivityForResult(intent, requestCode);
        }
    }

    public static void toChatGroupActivity(Context context, String groupGuid) {
        Intent intent = new Intent(context, ActivityChatGroup.class);
        intent.putExtra("groupguid", groupGuid);
        context.startActivity(intent);
    }

    public static void toNearPublishActivity(Activity context) {
        Intent intent = new Intent(context, ActivityNearPublish.class);
        context.startActivity(intent);
    }

    public static void toNearDetailActivity(Activity context, long circleId){
        toNearDetailActivity(context,circleId,null);
    }

    public static void toNearDetailActivity(Activity context, NearCircleExt info){
        toNearDetailActivity(context,0,info);
    }

    private static void toNearDetailActivity(Activity context, long circleId, NearCircleExt info) {
        Intent intent = new Intent(context, ActivityNearDetail.class);
        if (info != null && info.getInfo() != null) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("nearcircleext", info);
            intent.putExtras(bundle);
        }
        intent.putExtra("nearcircleid", circleId);
        context.startActivity(intent);
    }

    public static void toNearMessagesActivity(Activity context) {
        Intent intent = new Intent(context, ActivityNearMessages.class);
        context.startActivity(intent);
    }

    public static void toNearCommentPublishActivity(Activity context, long nearCircleId) {
        toNearCommentPublishActivity(context, nearCircleId, 0, null);
    }

    public static void toNearCommentPublishActivity(Activity context, long nearCircleId, long replyUserId, String replyUserName) {
        Intent intent = new Intent(context, ActivityCommentPublish.class);
        intent.putExtra("nearcircleid", nearCircleId);
        intent.putExtra("replyuserid", replyUserId);
        intent.putExtra("replyusername", replyUserName);
        context.startActivity(intent);
    }

    public static void toCircleIndexActivity(Activity context) {
        Intent intent = new Intent(context, ActivityCircleIndex.class);
        context.startActivity(intent);
    }

    public static void toCirclePublishActivity(Activity context) {
        Intent intent = new Intent(context, ActivityCirclePublish.class);
        context.startActivity(intent);
    }

    public static void toCircleCommentPublishActivity(Activity context, long circleId) {
        toCircleCommentPublishActivity(context, circleId, 0, null);
    }

    public static void toCircleCommentPublishActivity(Activity context, long circleId, long replyUserId, String replyUserName) {
        Intent intent = new Intent(context, ActivityCircleCommentPublish.class);
        intent.putExtra("circleid", circleId);
        intent.putExtra("replyuserid", replyUserId);
        intent.putExtra("replyusername", replyUserName);
        context.startActivity(intent);
    }

    public static void toCircleUserIndexActivity(Activity context, long viewUserId, String viewUserName, String viewUserImage, String viewCircleBackImage, String viewUserLifeNotes) {
        Intent intent = new Intent(context, ActivityCircleUserIndex.class);
        intent.putExtra("viewuserid", viewUserId);
        intent.putExtra("viewusername", viewUserName);
        intent.putExtra("viewuserimage", viewUserImage);
        intent.putExtra("viewcirclebackimage", viewCircleBackImage);
        intent.putExtra("viewuserlifenotes", viewUserLifeNotes);
        context.startActivity(intent);
    }

    public static void toCircleDetailActivity(Activity context, long circleId) {
        toCircleDetailActivity(context, circleId, null);
    }

    public static void toCircleDetailActivity(Activity context, CircleExt info) {
        toCircleDetailActivity(context, 0, info);
    }

    public static void toCircleDetailActivity(Activity context, long circleId, CircleExt info) {
        Intent intent = new Intent(context, ActivityCircleDetail.class);
        if (info != null && info.getInfo() != null) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("circleext", info);
            intent.putExtras(bundle);
        }
        intent.putExtra("circleid", circleId);
        context.startActivity(intent);
    }

    public static void toGroupActivity(Activity context) {
        Intent intent = new Intent(context, ActivityGroup.class);
        context.startActivity(intent);
    }

    public static void toGroupAddActivity(Activity context) {
        toGroupAddActivity(context, ActivityGroupAdd.TYPE_CREATE, null);
    }

    public static void toGroupAddActivity(Activity context, int actionType, String groupGuid) {
        Intent intent = new Intent(context, ActivityGroupAdd.class);
        intent.putExtra("actiontype", actionType);
        intent.putExtra("groupguid", groupGuid);
        context.startActivity(intent);
    }

    public static void toReloginDialog(final FragmentActivity fragmentActivity) {
        final ReloginDialogFragment reloginFragment = new ReloginDialogFragment();
        reloginFragment.setReloginClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                UserSP.clearUserInfo();
                UserPosSP.clearPosInfo();

                Intent intent = new Intent(fragmentActivity, ActivityWelcome.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                fragmentActivity.startActivity(intent);

                reloginFragment.dismiss();
            }
        });
        reloginFragment.show(fragmentActivity.getSupportFragmentManager(), "ReloginDialogFragment");
    }

    public static void toChatGroupSettingActivity(Activity context, String groupGuid) {
        Intent intent = new Intent(context, ActivityChatGroupSetting.class);
        intent.putExtra("groupguid", groupGuid);
        context.startActivity(intent);
    }

    public static void toChatGroupSettingEditActivity(Activity context, String groupGuid, int editType, String content) {
        Intent intent = new Intent(context, ActivityChatGroupSettingEdit.class);
        intent.putExtra("groupguid", groupGuid);
        intent.putExtra("edittype", editType);
        intent.putExtra("content", content);
        context.startActivityForResult(intent, editType);
    }

    public static void toChatUserSettingActivity(Activity context, long userId, String userName, String userImage) {
        Intent intent = new Intent(context, ActivityChatUserSetting.class);
        intent.putExtra("userid", userId);
        intent.putExtra("username", userName);
        intent.putExtra("userimage", userImage);
        context.startActivity(intent);
    }
}
