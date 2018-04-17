package com.hwl.beta.ui.imgselect.adp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hwl.beta.R;
import com.hwl.beta.ui.imgselect.bean.ImageDirBean;

import java.util.List;

/**
 * Created by Administrator on 2018/1/20.
 */

public class ImageDirAdapter extends RecyclerView.Adapter<ImageDirAdapter.ImageDirViewHolder> {

    final Context context;
    String currDirName;
    List<ImageDirBean> imageDirList;
    IImageDirItemEvent dirClickListener;

    public ImageDirAdapter(Context context, List<ImageDirBean> imageDirList, String currDirName, IImageDirItemEvent dirClickListener) {
        this.context = context;
        this.imageDirList = imageDirList;
        this.dirClickListener = dirClickListener;
        this.currDirName = currDirName;
    }

    @Override
    public ImageDirViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.image_dir_item, parent, false);
        return new ImageDirViewHolder(view, dirClickListener);
    }

    @Override
    public void onBindViewHolder(ImageDirViewHolder holder, int position) {
        ImageDirBean dirBean = imageDirList.get(position);
        Glide.with(context).load(dirBean.getImagePath()).into(holder.ivImageDir);
        holder.tvDirName.setText(dirBean.getImageName());
        holder.tvImageCount.setText(dirBean.getImageCount() + "张");
        if (dirBean.getDir().equals(this.currDirName)) {
            holder.ivImageSelect.setVisibility(View.VISIBLE);
        } else {
            holder.ivImageSelect.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return imageDirList.size();
    }

    class ImageDirViewHolder extends RecyclerView.ViewHolder {

        ImageView ivImageDir, ivImageSelect;
        TextView tvDirName, tvImageCount;
        IImageDirItemEvent dirClickListener;

        public ImageDirViewHolder(View itemView, final IImageDirItemEvent dirClickListener) {
            super(itemView);

            ivImageDir = itemView.findViewById(R.id.iv_image_dir);
            tvDirName = itemView.findViewById(R.id.tv_dir_name);
            tvImageCount = itemView.findViewById(R.id.tv_image_count);
            ivImageSelect = itemView.findViewById(R.id.iv_image_select);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (dirClickListener != null) {
                        dirClickListener.onImageDirItemListener(v, getAdapterPosition());
                    }
                }
            });
        }
    }

    public interface IImageDirItemEvent {
        void onImageDirItemListener(View view, int position);
    }
}
