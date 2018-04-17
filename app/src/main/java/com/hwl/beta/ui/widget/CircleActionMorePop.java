package com.hwl.beta.ui.widget;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.hwl.beta.R;
import com.hwl.beta.autolayout.utils.AutoUtils;

/**
 * Created by Administrator on 2018/3/24.
 */

public class CircleActionMorePop extends PopupWindow {
    private int mShowMorePopupWindowWidth;
    private int mShowMorePopupWindowHeight;
    private LinearLayout llShowMoreLike;
    private LinearLayout llShowMoreComment;
    private LinearLayout llShowMoreClose;
    private TextView tvLikeItem;

    private IActionMoreListener actionMoreListener;

    public void setActionMoreListener(IActionMoreListener actionMoreListener) {
        this.actionMoreListener = actionMoreListener;
    }

    public CircleActionMorePop(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View conentView = inflater.inflate(R.layout.circle_action_more_pop, null);
        setContentView(conentView);
        this.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setBackgroundDrawable(new BitmapDrawable());
        setTouchable(true);
        setOutsideTouchable(true);

        conentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        mShowMorePopupWindowWidth = conentView.getMeasuredWidth();
        mShowMorePopupWindowHeight = conentView.getMeasuredHeight();

        View parent = getContentView();
//        tvLikeItem = (TextView) parent.findViewById(R.id.tv_like_item);
//        llShowMoreLike = (LinearLayout) parent.findViewById(R.id.ll_like_container);
//        llShowMoreComment = (LinearLayout) parent.findViewById(R.id.ll_comment_item);
        parent.findViewById(R.id.ll_item_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    public void show(final int position,View moreActionView) {
        if (isShowing()) {
            dismiss();
        } else {
//            //已经点过赞
//            if (circleNewInfo.getIsLike() == 1) {
//                tvShowMoreLike.setText("取消");
//            } else {
//                tvShowMoreLike.setText("点赞");
//            }
//            //点赞按钮
//            llShowMoreLike.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    dgyCircleListener.onLikeDgyCircleNew(position, circleNewInfo);
//                    dismiss();
//                }
//            });
//            //评论按钮
//            llShowMoreReply.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    dgyCircleListener.onCommentDgyCircleNew(position, circleNewInfo, null);
//                    dismiss();
//                }
//            });

            int heightMoreBtnView = moreActionView.getHeight();
            int widthMoreBtnView = moreActionView.getWidth();
            moreActionView.setVisibility(View.INVISIBLE);
            showAsDropDown(moreActionView, AutoUtils.getPercentWidthSize(10) + widthMoreBtnView - mShowMorePopupWindowWidth,
                    -(mShowMorePopupWindowHeight + heightMoreBtnView) / 2);
        }
    }

    public interface IActionMoreListener {

        void onDeleteClick(int position);

        void onCommentClick(int position);

        void onLikeClick(int position);
    }
}
