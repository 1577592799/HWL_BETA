package com.hwl.beta.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.hwl.beta.R;
import com.hwl.beta.emotion.EmotionDefaultPannel;
import com.hwl.beta.photoview.PhotoView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/1/4.
 */

public class TestActivity extends FragmentActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        ViewPager viewPager = findViewById(R.id.pvp_images);
        viewPager.setAdapter(new SamplePagerAdapter());
    }
    static class SamplePagerAdapter extends PagerAdapter {

        private static final int[] sDrawables = { R.drawable.circle, R.drawable.welcome, R.drawable.empty_photo };

        @Override
        public int getCount() {
            return sDrawables.length;
        }

        @Override
        public View instantiateItem(ViewGroup container, int position) {
            PhotoView photoView = new PhotoView(container.getContext());
            photoView.setImageResource(sDrawables[position]);

            // Now just add PhotoView to ViewPager and return it
            container.addView(photoView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

            return photoView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

    }
}
