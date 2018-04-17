package com.hwl.beta.swiperefresh;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Created by Administrator on 2018/4/14.
 */

public class DefaultLoadFooterView extends SwipeLoadMoreFooterLayout {
    private TextView tvLoadMore;
    private ImageView ivSuccess;
    private ProgressBar progressBar;

    private int mFooterHeight;

    public DefaultLoadFooterView(Context context) {
        this(context, null);
    }

    public DefaultLoadFooterView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DefaultLoadFooterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mFooterHeight = getResources().getDimensionPixelOffset(R.dimen.default_load_more_footer_height);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        tvLoadMore = findViewById(R.id.tvLoadMore);
        ivSuccess = findViewById(R.id.ivSuccess);
        progressBar = findViewById(R.id.progressbar);
    }

    @Override
    public void onPrepare() {
        ivSuccess.setVisibility(GONE);
    }

    @Override
    public void onMove(int y, boolean isComplete, boolean automatic) {
        if (!isComplete) {
            ivSuccess.setVisibility(GONE);
            progressBar.setVisibility(GONE);
            if (-y >= mFooterHeight) {
                tvLoadMore.setText("松开加载更多");
            } else {
                tvLoadMore.setText("松开加载更多");
            }
        }
    }

    @Override
    public void onLoadMore() {
        tvLoadMore.setText("数据加载中...");
        progressBar.setVisibility(VISIBLE);
    }

    @Override
    public void onRelease() {

    }

    @Override
    public void onComplete() {
        progressBar.setVisibility(GONE);
        ivSuccess.setVisibility(VISIBLE);
    }

    @Override
    public void onReset() {
        ivSuccess.setVisibility(GONE);
    }
}
