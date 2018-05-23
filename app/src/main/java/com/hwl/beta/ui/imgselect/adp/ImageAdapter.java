package com.hwl.beta.ui.imgselect.adp;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hwl.beta.R;
import com.hwl.beta.databinding.CameraItemBinding;
import com.hwl.beta.databinding.ImageItemBinding;
import com.hwl.beta.ui.imgselect.action.IImageSelectItemListener;
import com.hwl.beta.ui.imgselect.bean.ImageBean;
import com.hwl.beta.ui.imgselect.bean.ImageSelectType;
import com.hwl.beta.utils.ScreenUtils;

import java.util.List;

/**
 * Created by Administrator on 2018/4/4.
 */

public class ImageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    List<ImageBean> images;
    LayoutInflater inflater;
    float screenWidth;
    IImageSelectItemListener itemListener;
    int imageSelectType;

    public ImageAdapter(Context context, List<ImageBean> images, int imageSelectType, IImageSelectItemListener itemListener) {
        this.context = context;
        this.images = images;
        this.imageSelectType = imageSelectType;
        this.itemListener = itemListener;
        inflater = LayoutInflater.from(context);
        //获取屏幕宽度
        screenWidth = ScreenUtils.getScreenWidth(context);
    }

    //直接创建ViewHolder即可
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            CameraItemBinding itemBinding = DataBindingUtil.inflate(inflater, R.layout.camera_item, parent, false);
            return new CameraItemViewHolder(itemBinding);
        } else {
            ImageItemBinding itemBinding = DataBindingUtil.inflate(inflater, R.layout.image_item, parent, false);
            return new ImageItemViewHolder(itemBinding);
        }
    }

    @Override
    public int getItemViewType(int position) {
        //如果选择的是多张图片，就排除照相的功能
        if (position == 0 && (imageSelectType == ImageSelectType.USER_HEAD || imageSelectType == ImageSelectType.CIRCLE_BACK_IMAGE)) {
            return 0;
        }
        return 1;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ImageBean img = images.get(position);
        if (getItemViewType(position) == 0) {
            CameraItemBinding itemBinding = ((CameraItemViewHolder) holder).getItemBinding();
            itemBinding.setAction(itemListener);
            setImageRelativeLayout(itemBinding.ivCamera);
        } else {
            ImageItemBinding itemBinding = ((ImageItemViewHolder) holder).getItemBinding();
            itemBinding.setImage(img);
            itemBinding.setAction(itemListener);
            setImageRelativeLayout(itemBinding.ivImage);

            Glide.with(context).load(img.getPath())
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(itemBinding.ivImage);
            if (img.isSelect()) {
                itemBinding.cbSelect.setChecked(true);
            } else {
                itemBinding.cbSelect.setChecked(false);
            }
            if (imageSelectType == ImageSelectType.USER_HEAD || imageSelectType == ImageSelectType.CIRCLE_BACK_IMAGE) {
                itemBinding.cbSelect.setVisibility(View.GONE);
            } else {
                itemBinding.cbSelect.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    private void setImageRelativeLayout(ImageView iv) {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) iv.getLayoutParams();
        params.width = (int) screenWidth / 3 - params.rightMargin - params.leftMargin;
        params.height = (int) screenWidth / 3 - params.topMargin - params.bottomMargin;
        iv.setLayoutParams(params);
    }

    class CameraItemViewHolder extends RecyclerView.ViewHolder {
        private CameraItemBinding itemBinding;

        public CameraItemViewHolder(CameraItemBinding itemBinding) {
            super(itemBinding.getRoot());
            this.itemBinding = itemBinding;
        }

        public CameraItemBinding getItemBinding() {
            return itemBinding;
        }
    }

    class ImageItemViewHolder extends RecyclerView.ViewHolder {
        private ImageItemBinding itemBinding;

        public ImageItemViewHolder(ImageItemBinding itemBinding) {
            super(itemBinding.getRoot());
            this.itemBinding = itemBinding;
        }

        public ImageItemBinding getItemBinding() {
            return itemBinding;
        }
    }
}