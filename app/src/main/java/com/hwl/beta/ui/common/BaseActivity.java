package com.hwl.beta.ui.common;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

/**
 * Created by Administrator on 2018/2/4.
 */

public abstract class BaseActivity extends FragmentActivity {
    protected Activity activity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        activity = this;

        initView();
    }

    public void initView() {
    }

    public abstract String getName();

    public abstract int getLayoutId();
}
