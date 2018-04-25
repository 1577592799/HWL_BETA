package com.hwl.beta.ui.circle;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Toast;

import com.hwl.beta.R;
import com.hwl.beta.databinding.ActivityCircleUserIndexBinding;
import com.hwl.beta.db.DaoUtils;
import com.hwl.beta.db.ext.CircleExt;
import com.hwl.beta.sp.UserSP;
import com.hwl.beta.ui.circle.action.ICircleUserItemListener;
import com.hwl.beta.ui.circle.adp.CircleUserIndexAdapter;
import com.hwl.beta.ui.common.UITransfer;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

public class ActivityCircleUserIndex extends FragmentActivity {

    Activity activity;
    ActivityCircleUserIndexBinding binding;
    List<CircleExt> circles;
    CircleUserIndexAdapter circleAdapter;
    int pageCount = 15;
    long myUserId;
    long viewUserId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        myUserId = UserSP.getUserId();
        viewUserId = getIntent().getLongExtra("viewuserid", 0);
        if (viewUserId <= 0) {
            Toast.makeText(activity, "用户不存在", Toast.LENGTH_SHORT).show();
            finish();
        }

        circles = this.getCircles();
        circleAdapter = new CircleUserIndexAdapter(activity, circles, new CircleUserItemListener());
        binding = DataBindingUtil.setContentView(activity, R.layout.activity_circle_user_index);

        initView();
    }

    private void initView() {
        binding.tbTitle.setTitle("TA的圈子")
                .setImageRightClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        UITransfer.toCirclePublishActivity(activity);
                    }
                })
                .setImageLeftClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                });

        binding.rvCircleContainer.setAdapter(circleAdapter);
        binding.rvCircleContainer.setLayoutManager(new LinearLayoutManager(activity));
        binding.refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
//                loadCircleFromServer(0);
            }
        });
        binding.refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
//                loadCircleFromServer(circles.get(circles.size() - 1).getInfo().getCircleId());
            }
        });

//        binding.refreshLayout.autoRefresh();
        binding.refreshLayout.setEnableLoadMore(false);
    }

    private List<CircleExt> getCircles() {
        List<CircleExt> infos = DaoUtils.getCircleManagerInstance().getUserCircles(viewUserId);
        if (infos == null) {
            infos = new ArrayList<>();
        }
        infos.add(0, new CircleExt(CircleExt.CircleHeadItem,
                viewUserId,
                getIntent().getStringExtra("viewusername"),
                getIntent().getStringExtra("viewuserimage"),
                getIntent().getStringExtra("viewcirclebackimage"),
                getIntent().getStringExtra("viewuserlifenotes")));
        if (infos.size() == 1) {
            infos.add(new CircleExt(CircleExt.CircleNullItem));
        }
        return infos;
    }

    private void showResult() {
        if (circles.size() == 2 && circles.get(1).getCircleItemType() == CircleExt.CircleNullItem) {
            circleAdapter.notifyDataSetChanged();
            binding.refreshLayout.setEnableLoadMore(false);
        } else {
            binding.refreshLayout.setEnableLoadMore(true);
        }
        binding.refreshLayout.finishRefresh();
        binding.refreshLayout.finishLoadMore();
    }

    private void removeEmptyView() {
        if (circles.size() == 2 && circles.get(1).getCircleItemType() == CircleExt.CircleNullItem) {
            circles.remove(1);
        }
    }

    public class CircleUserItemListener implements ICircleUserItemListener {

    }
}
