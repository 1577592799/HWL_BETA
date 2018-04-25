package com.hwl.beta.ui.common;

import android.app.Activity;
import android.content.Intent;

import com.hwl.beta.ui.chat.ActivityChatGroup;
import com.hwl.beta.ui.chat.ActivityChatUser;
import com.hwl.beta.ui.circle.ActivityCircleCommentPublish;
import com.hwl.beta.ui.circle.ActivityCircleIndex;
import com.hwl.beta.ui.circle.ActivityCirclePublish;
import com.hwl.beta.ui.circle.ActivityCircleUserIndex;
import com.hwl.beta.ui.entry.ActivityGetpwd;
import com.hwl.beta.ui.entry.ActivityLogin;
import com.hwl.beta.ui.entry.ActivityMain;
import com.hwl.beta.ui.entry.ActivityRegister;
import com.hwl.beta.ui.entry.ActivityWelcome;
import com.hwl.beta.ui.imgselect.ActivityImageSelect;
import com.hwl.beta.ui.near.ActivityCommentPublish;
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

    public static void toChatGroupActivity(Activity context, String groupGuid, String groupName) {
        toChatGroupActivity(context, groupGuid, groupName, 0);
    }

    public static void toChatGroupActivity(Activity context, String groupGuid, String groupName, long recordId) {
        Intent intent = new Intent(context, ActivityChatGroup.class);
        intent.putExtra("groupguid", groupGuid);
        intent.putExtra("groupname", groupName);
        intent.putExtra("recordid", recordId);
        context.startActivity(intent);
    }

    public static void toNearPublishActivity(Activity context) {
        Intent intent = new Intent(context, ActivityNearPublish.class);
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

    public static void toCircleUserIndexActivity(Activity context, long viewUserId, String viewUserName, String viewUserImage, String viewCircleBackImage,String viewUserLifeNotes) {
        Intent intent = new Intent(context, ActivityCircleUserIndex.class);
        intent.putExtra("viewuserid", viewUserId);
        intent.putExtra("viewusername", viewUserName);
        intent.putExtra("viewuserimage", viewUserImage);
        intent.putExtra("viewcirclebackimage", viewCircleBackImage);
        intent.putExtra("viewuserlifenotes", viewUserLifeNotes);
        context.startActivity(intent);
    }
}
