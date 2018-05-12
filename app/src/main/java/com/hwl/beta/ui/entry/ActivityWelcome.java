package com.hwl.beta.ui.entry;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.widget.TextView;

import com.hwl.beta.R;
import com.hwl.beta.net.user.NetUserInfo;
import com.hwl.beta.sp.UserSP;
import com.hwl.beta.ui.common.OpenInstallManage;
import com.hwl.beta.ui.common.UITransfer;

public class ActivityWelcome extends FragmentActivity {

    private TextView tvCountdown;
    private int tmrCount = 3;
    final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        tvCountdown = (TextView) findViewById(R.id.tv_countdown);
        tvCountdown.setText(tmrCount + " s");
        handler.postDelayed(runnable, 1000);

        OpenInstallManage.getWakeUp(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        /**
         * 此处要调用，否则App在后台运行时，会无法截获
         */
        OpenInstallManage.getWakeUp(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void check() {
        NetUserInfo user = UserSP.getUserInfo();
        if (user != null && user.getId() > 0) {
            UITransfer.toMainActivity(this);
        } else {
            UITransfer.toLoginActivity(this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        OpenInstallManage.clearWakeUp();
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.finish();
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (tmrCount <= 1) {
                check();
            } else {
                tmrCount--;
                tvCountdown.setText(tmrCount + " s");
                handler.postDelayed(this, 1000);
            }
        }
    };
}
