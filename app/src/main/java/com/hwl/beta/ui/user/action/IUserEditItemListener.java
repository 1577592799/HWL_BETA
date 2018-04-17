package com.hwl.beta.ui.user.action;

import android.widget.RadioButton;

/**
 * Created by Administrator on 2018/4/4.
 */

public interface IUserEditItemListener {
    void init();

    void onManClick(String sexName);

    void onWomanClick(String sexName);
}
