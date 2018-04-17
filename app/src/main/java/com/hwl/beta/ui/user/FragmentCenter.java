package com.hwl.beta.ui.user;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hwl.beta.R;
import com.hwl.beta.databinding.FragmentCenterBinding;
import com.hwl.beta.net.user.NetUserInfo;
import com.hwl.beta.sp.UserPosSP;
import com.hwl.beta.sp.UserSP;
import com.hwl.beta.ui.busbean.EventBusConstant;
import com.hwl.beta.ui.common.UITransfer;
import com.hwl.beta.ui.user.action.ICenterListener;
import com.hwl.beta.ui.user.bean.CenterBean;
import com.hwl.beta.ui.user.bean.ImageViewBean;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by Administrator on 2017/12/27.
 */

public class FragmentCenter extends Fragment {
    FragmentCenterBinding binding;
    Activity activity;
    CenterBean centerBean;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        activity = getActivity();

        centerBean = new CenterBean();
        setCenterBean();
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_center, container, false);
        binding.setUser(centerBean);
        binding.setAction(new CenterListener());
        binding.setImage(new ImageViewBean(centerBean.getHeadImage()));

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        return binding.getRoot();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(Integer ebType) {
        if (ebType == EventBusConstant.EB_TYPE_USER_UPDATE) {
            setCenterBean();
        } else if (ebType == EventBusConstant.EB_TYPE_USER_HEAD_UPDATE) {
            ImageViewBean.loadImage(binding.ivHeader, UserSP.getUserHeadImage());
        }
    }

    private void setCenterBean() {
        NetUserInfo netUser = UserSP.getUserInfo();
        centerBean.setName(netUser.getName() + " - " + netUser.getId());
        centerBean.setHeadImage(netUser.getHeadImage());
        centerBean.setSymbol(netUser.getSymbol());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
//            //Log.d("当前刷新的图片：", UserSP.getUserHeadImage());
//            Glide.with(this).load(UserSP.getUserHeadImage())
//                    .placeholder(R.drawable.empty_photo)
//                    .error(R.drawable.empty_photo)
//                    .into(ivHeader);
//            tvName.setText(UserSP.getUserName());
//            tvAccount.setText(UserSP.getUserSymbol());
        }
    }

    public class CenterListener implements ICenterListener {

        @Override
        public void onHeadImageClick() {
            UITransfer.toUserEditActivity(activity);
        }

        @Override
        public void onCircleClick() {

        }

        @Override
        public void onSettingClick() {

        }

        @Override
        public void onCheckUpdateClick() {

        }

        @Override
        public void onLogoutClick() {
            UserSP.clearUserInfo();
            UserPosSP.clearPosInfo();

            UITransfer.toWelcomeActivity(activity);
            activity.finish();
        }
    }
}
