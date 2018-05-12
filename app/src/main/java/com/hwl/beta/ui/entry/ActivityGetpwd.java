package com.hwl.beta.ui.entry;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import com.hwl.beta.R;
import com.hwl.beta.ui.common.BaseActivity;
import com.hwl.beta.ui.widget.TitleBar;

/**
 * Created by Administrator on 2018/1/13.
 */

public class ActivityGetpwd extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getpwd);

        TitleBar tbTitle=findViewById(R.id.tb_title);
        tbTitle.setTitle("找回密码").setImageRightHide();
    }
}
