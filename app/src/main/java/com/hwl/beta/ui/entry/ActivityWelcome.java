package com.hwl.beta.ui.entry;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.hwl.beta.R;
import com.hwl.beta.net.user.NetUserInfo;
import com.hwl.beta.sp.UserSP;
import com.hwl.beta.ui.common.UITransfer;

public class ActivityWelcome extends AppCompatActivity {

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
