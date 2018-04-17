package com.hwl.beta.swiperefresh;

/**
 * Created by Administrator on 2018/4/14.
 */

public interface SwipeTrigger {
    void onPrepare();

    void onMove(int y, boolean isComplete, boolean automatic);

    void onRelease();

    void onComplete();

    void onReset();
}
