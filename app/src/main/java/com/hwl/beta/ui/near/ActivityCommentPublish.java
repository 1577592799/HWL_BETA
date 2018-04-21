package com.hwl.beta.ui.near;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.hwl.beta.R;
import com.hwl.beta.emotion.EmotionDefaultPannel;

public class ActivityCommentPublish extends FragmentActivity {

    Activity activity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_publish);
        activity = this;

        EmotionDefaultPannel edpEmotion = findViewById(R.id.ecp_emotion);
        edpEmotion.setEditTextFocus(false);
        edpEmotion.setDefaultPannelListener(new EmotionDefaultPannel.IDefaultPannelListener() {
            @Override
            public boolean onSendMessageClick(String text) {
                Toast.makeText(activity, text, Toast.LENGTH_SHORT).show();
                finish();
                return true;
            }
        });
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();

        View view = getWindow().getDecorView();
        WindowManager.LayoutParams lp = (WindowManager.LayoutParams) view.getLayoutParams();
        lp.gravity = Gravity.BOTTOM;
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindowManager().updateViewLayout(view, lp);
    }
}
