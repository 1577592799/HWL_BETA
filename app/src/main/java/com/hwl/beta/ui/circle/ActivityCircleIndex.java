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
import com.hwl.beta.databinding.ActivityCircleIndexBinding;
import com.hwl.beta.db.DaoUtils;
import com.hwl.beta.db.entity.CircleComment;
import com.hwl.beta.db.ext.CircleExt;
import com.hwl.beta.net.circle.CircleService;
import com.hwl.beta.net.circle.NetCircleInfo;
import com.hwl.beta.net.circle.body.GetCircleInfosResponse;
import com.hwl.beta.sp.UserSP;
import com.hwl.beta.ui.circle.action.ICircleItemListener;
import com.hwl.beta.ui.circle.adp.CircleIndexAdapter;
import com.hwl.beta.ui.common.UITransfer;
import com.hwl.beta.ui.common.rxext.NetDefaultFunction;
import com.hwl.beta.ui.convert.DBCircleAction;
import com.hwl.beta.utils.NetworkUtils;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;

public class ActivityCircleIndex extends FragmentActivity {

    Activity activity;
    ActivityCircleIndexBinding binding;
    List<CircleExt> circles;
    CircleIndexAdapter circleAdapter;
    int pageCount = 15;
    long myUserId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        myUserId = UserSP.getUserId();
        circles = this.getCircles();
        circleAdapter = new CircleIndexAdapter(activity, circles, new CircleItemListener());

        binding = DataBindingUtil.setContentView(activity, R.layout.activity_circle_index);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        binding.refreshLayout.autoRefresh();
    }

    private void initView() {
        binding.tbTitle.setTitle("我的圈子")
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
                loadCircleFromServer(0);
            }
        });
        binding.refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                loadCircleFromServer(circles.get(circles.size() - 1).getInfo().getCircleId());
            }
        });

        binding.refreshLayout.setEnableLoadMore(false);
    }

    private List<CircleExt> getCircles() {
        List<CircleExt> infos = DaoUtils.getCircleManagerInstance().getAll();
        if (infos == null) {
            infos = new ArrayList<>();
        }
        infos.add(0, new CircleExt(CircleExt.CircleHeadItem));
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
//        binding.pbLoading.setVisibility(View.GONE);
    }

    private void removeEmptyView() {
        if (circles.size() == 2 && circles.get(1).getCircleItemType() == CircleExt.CircleNullItem) {
            circles.remove(1);
        }
    }

    private void loadCircleFromServer(final long minCircleId) {
        if (!NetworkUtils.isConnected()) {
            showResult();
            return;
        }
        CircleService.getCircleInfos(minCircleId, pageCount)
                .flatMap(new NetDefaultFunction<GetCircleInfosResponse, NetCircleInfo>() {
                    @Override
                    protected ObservableSource<NetCircleInfo> onSuccess(GetCircleInfosResponse response) {
                        if (response.getCircleInfos() != null && response.getCircleInfos().size() > 0) {
                            removeEmptyView();
                            return Observable.fromIterable(response.getCircleInfos());
                        }
                        return Observable.fromIterable(new ArrayList<NetCircleInfo>());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<NetCircleInfo, CircleExt>() {
                    @Override
                    public CircleExt apply(NetCircleInfo info) throws Exception {
                        CircleExt circleBean = new CircleExt(CircleExt.CircleIndexItem);
                        if (info != null && info.getCircleId() > 0) {
                            circleBean.setInfo(DBCircleAction.convertToCircleInfo(info));
                            circleBean.setImages(DBCircleAction.convertToCircleImageInfos(info.getCircleId(), info.getPublishUserId(), info.getImages()));
                            circleBean.setComments(DBCircleAction.convertToCircleCommentInfos(info.getCommentInfos()));
                            circleBean.setLikes(DBCircleAction.convertToCircleLikeInfos(info.getLikeInfos()));
                            return circleBean;
                        }
                        return circleBean;
                    }
                })
                .subscribe(new Observer<CircleExt>() {

                    int updateCount = 0;
                    int insertCount = 1;

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(CircleExt circleExt) {
                        if (circleExt != null && circleExt.getInfo() != null) {
                            boolean isExists = false;
                            for (int i = 0; i < circles.size(); i++) {
                                if (circles.get(i).getCircleItemType() == CircleExt.CircleIndexItem &&
                                        circles.get(i).getInfo().getCircleId() == circleExt.getInfo().getCircleId()) {
                                    circles.remove(i);
                                    circles.add(i, circleExt);
                                    updateCount++;
                                    isExists = true;
                                    break;
                                }
                            }
                            if (!isExists) {
                                if (minCircleId > 0) {
                                    circles.add(circleExt);
                                } else {
                                    circles.add(insertCount, circleExt);
                                }
                                updateCount++;
                                insertCount++;
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(activity, e.getMessage(), Toast.LENGTH_SHORT).show();
                        showResult();
                    }

                    @Override
                    public void onComplete() {
                        showResult();
                        if (updateCount > 0) {
                            circleAdapter.notifyItemRangeChanged(0, circles.size());
                        }
                    }
                });
    }

    private class CircleItemListener implements ICircleItemListener {

        @Override
        public void onItemViewClick(View view) {

        }

        @Override
        public void onUserHeadClick() {

        }

        @Override
        public void onLikeUserHeadClick() {

        }

        @Override
        public void onCommentUserClick() {

        }

        @Override
        public void onReplyUserClick(CircleComment comment) {

        }

        @Override
        public void onCommentContentClick() {

        }

        @Override
        public void onContentClick() {

        }

        @Override
        public void onMoreActionClick(View view, int position) {

        }

        @Override
        public void onMoreCommentClick() {

        }

        @Override
        public void onDeleteClick() {

        }

        @Override
        public void onPublishClick() {
            UITransfer.toCirclePublishActivity(activity);
        }
    }
}
