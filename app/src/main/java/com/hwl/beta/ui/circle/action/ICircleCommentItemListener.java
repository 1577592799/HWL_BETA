package com.hwl.beta.ui.circle.action;

import com.hwl.beta.db.entity.NearCircleComment;

/**
 * Created by Administrator on 2018/4/15.
 */

public interface ICircleCommentItemListener {
    void onCommentUserClick();

    void onReplyUserClick();

    void onContentClick();
}
