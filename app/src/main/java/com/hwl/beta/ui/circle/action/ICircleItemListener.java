package com.hwl.beta.ui.circle.action;

import android.view.View;

import com.hwl.beta.db.entity.CircleComment;
import com.hwl.beta.db.entity.CircleLike;

/**
 * Created by Administrator on 2018/4/14.
 */

public interface ICircleItemListener {

    void onItemViewClick(View view);

    void onUserHeadClick();

    void onLikeUserHeadClick(CircleLike likeInfo);

    void onCommentUserClick(CircleComment comment);

    void onReplyUserClick(CircleComment comment);

    void onCommentContentClick(CircleComment comment);

    void onContentClick();

    void onMoreActionClick(View view, int position);

    void onMoreCommentClick();

    void onDeleteClick();

    void onPublishClick();

}
