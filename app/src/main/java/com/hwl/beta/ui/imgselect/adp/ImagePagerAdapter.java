package com.hwl.beta.ui.imgselect.adp;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hwl.beta.photoview.PhotoView;

import java.util.List;

public class ImagePagerAdapter extends PagerAdapter {
    Context context;
    List<String> imageUrls;

    public ImagePagerAdapter(Context context, List<String> imageUrls) {
        this.context = context;
        this.imageUrls = imageUrls;
    }

    @Override
    public int getCount() {
        return imageUrls == null ? 0 : imageUrls.size();
    }

    @Override
    public View instantiateItem(ViewGroup container, int position) {
        PhotoView photoView = new PhotoView(container.getContext());
//        photoView.setImageResource(imageUrls[position]);
        Glide.with(context).
                load(imageUrls.get(position)).
                crossFade(1000).//淡入淡出,注意:如果设置了这个,则必须要去掉asBitmap
                centerCrop().//中心裁剪,缩放填充至整个ImageView
                diskCacheStrategy(DiskCacheStrategy.RESULT).//保存最终图片
                into(photoView);
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
