package com.hwl.beta.ui.circle.action;

import android.view.View;

import com.hwl.beta.db.entity.NearCircle;
import com.hwl.beta.db.entity.NearCircleComment;
import com.hwl.beta.db.entity.NearCircleLike;

/**
 * Created by Administrator on 2018/4/14.
 */

public interface ICircleItemListener {

    void onItemViewClick(View view);

    void onUserHeadClick();

    void onLikeUserHeadClick();

    void onCommentUserClick();

    void onReplyUserClick(NearCircleComment comment);

    void onCommentContentClick();

    void onContentClick();

    void onMoreActionClick(View view, int position);

    void onMoreCommentClick();

    void onDeleteClick();

    void onPublishClick();

}
