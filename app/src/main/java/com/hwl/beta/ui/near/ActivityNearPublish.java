package com.hwl.beta.ui.near;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hwl.beta.R;
import com.hwl.beta.databinding.ActivityNearPulishBinding;
import com.hwl.beta.db.entity.NearCircleImage;
import com.hwl.beta.emotion.IDefaultEmotionListener;
import com.hwl.beta.net.ResponseBase;
import com.hwl.beta.net.near.NearCircleService;
import com.hwl.beta.net.near.NetImageInfo;
import com.hwl.beta.net.near.body.AddNearCircleInfoResponse;
import com.hwl.beta.net.resx.ResxType;
import com.hwl.beta.net.resx.UploadService;
import com.hwl.beta.net.resx.body.UpResxResponse;
import com.hwl.beta.sp.UserSP;
import com.hwl.beta.ui.common.BaseActivity;
import com.hwl.beta.ui.common.UITransfer;
import com.hwl.beta.ui.common.rxext.NetDefaultObserver;
import com.hwl.beta.ui.dialog.LoadingDialog;
import com.hwl.beta.ui.imgcompress.CompressChatImage;
import com.hwl.beta.ui.imgselect.bean.ImageBean;
import com.hwl.beta.ui.imgselect.bean.ImageSelectType;
import com.hwl.beta.utils.ScreenUtils;
import com.hwl.beta.utils.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2018/2/16.
 */

public class ActivityNearPublish extends BaseActivity {
    ActivityNearPulishBinding binding;
    Activity activity;
    static final int MAX_IMAGE_COUNT = 9;
    List<String> imagePaths = null;
    List<NearCircleImage> nearCircleImages;
    int screenWidth;
    boolean isRuning = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        imagePaths = new ArrayList<>(MAX_IMAGE_COUNT);
        screenWidth = ScreenUtils.getScreenWidth(activity);

        binding = DataBindingUtil.setContentView(activity, R.layout.activity_near_pulish);
        initView();
    }

    public void initView() {
        binding.tbTitle.setTitle("向附近发布信息")
                .setTitleRightText("发布")
                .setTitleRightBackground(R.drawable.bg_top)
                .setImageRightHide()
                .setTitleRightShow()
                .setImageLeftClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                })
                .setTitleRightClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (isRuning) return;
                        isRuning = true;
                        nearCircleImages = new ArrayList<>();
                        publishImages();
                    }
                });

        binding.ecpEmotion.setDefaultEmotionListener(new IDefaultEmotionListener() {
            @Override
            public void onDefaultItemClick(String name) {
                binding.ecpEmotion.addEmotion(binding.etEmotionText, name);
            }

            @Override
            public void onDefaultItemDeleteClick() {
                binding.ecpEmotion.deleteEmotion(binding.etEmotionText);
            }
        });

        binding.etEmotionText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    binding.ecpEmotion.setVisibility(View.VISIBLE);
                }
                return false;
            }
        });

        View viewImageAdd = View.inflate(activity, R.layout.image_add_item, null);
        ImageView ivAdd = viewImageAdd.findViewById(R.id.iv_add);
        setImageContent(ivAdd);
        ivAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imagePaths.size() >= MAX_IMAGE_COUNT) {
                    Toast.makeText(activity, "只能发布" + MAX_IMAGE_COUNT + "张图片", Toast.LENGTH_SHORT).show();
                    return;
                }
                UITransfer.toImageSelectActivity(activity, ImageSelectType.CHAT_PUBLISH, MAX_IMAGE_COUNT - imagePaths.size(), 1);

            }
        });
        binding.wllImageContainer.addView(viewImageAdd);
    }

    private void addImages(final String localPath) {
        View view = View.inflate(activity, R.layout.publish_image_item, null);
        ImageView ivImage = view.findViewById(R.id.iv_image);
        ImageView ivDelete = view.findViewById(R.id.iv_delete);
        setImageContent(ivImage);
        Glide.with(activity).load(localPath)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(ivImage);
        ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagePaths.remove(localPath);
                binding.wllImageContainer.removeView((View) v.getParent());
            }
        });
        binding.wllImageContainer.addView(view, 0);
        imagePaths.add(localPath);
    }

    private void setImageContent(ImageView ivImage) {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ivImage.getLayoutParams();
        params.width = (int) screenWidth / 4 - params.rightMargin - params.leftMargin;
        params.height = (int) screenWidth / 4 - params.topMargin - params.bottomMargin;
        ivImage.setLayoutParams(params);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case 0:
                    break;
                case 1:
                    ArrayList<ImageBean> list = data.getExtras().getParcelableArrayList("selectimages");
                    for (int i = 0; i < list.size(); i++) {
                        addImages(list.get(i).getPath());
                    }
                    break;
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            binding.ecpEmotion.setSystemKeyboard(false);
        }
        return super.onTouchEvent(event);
    }

    private void publishImages() {
        final String content = binding.etEmotionText.getText() + "";
        if (StringUtils.isBlank(content) && imagePaths.size() <= 0) {
            isRuning = false;
            Toast.makeText(activity, "发布内容不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        LoadingDialog.show(activity, "正在发布,请稍后...");
        Observable.fromIterable(imagePaths)
                .subscribeOn(Schedulers.io())
                .map(new Function<String, File>() {
                    @Override
                    public File apply(String imagePath) throws Exception {
                        File file = CompressChatImage.chatImage(activity, imagePath);
                        if (file == null) {//说明压缩失败
                            file = new File(imagePath);
                        }
                        return file;
                    }
                })
                .flatMap(new Function<File, ObservableSource<ResponseBase<UpResxResponse>>>() {
                    @Override
                    public ObservableSource<ResponseBase<UpResxResponse>> apply(File file) throws Exception {
                        return UploadService.upImage(file, ResxType.NEARCIRCLEPOST);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetDefaultObserver<UpResxResponse>() {
                    @Override
                    protected void onSuccess(UpResxResponse res) {
                        if (res.isSuccess()) {
                            NearCircleImage imageModel = new NearCircleImage();
                            imageModel.setPostUserId(UserSP.getUserId());
                            imageModel.setNearCircleId(0);
                            if (StringUtils.isBlank(res.getPreviewUrl())) {
                                imageModel.setImageUrl(res.getOriginalUrl());
                            } else {
                                imageModel.setImageUrl(res.getPreviewUrl());
                            }
                            imageModel.setImageWidth(res.getWidth());
                            imageModel.setImageHeight(res.getHeight());
                            nearCircleImages.add(imageModel);
                        }
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                        List<NetImageInfo> images = null;
                        if (nearCircleImages != null && nearCircleImages.size() > 0) {
                            images = new ArrayList<>();
                            for (int i = 0; i < nearCircleImages.size(); i++) {
                                images.add(new NetImageInfo(nearCircleImages.get(i).getImageWidth(), nearCircleImages.get(i).getImageHeight(), nearCircleImages.get(i).getImageUrl()));
                            }
                        }
                        //图片上传完成后再发布内容
                        publishContent(content, images);
                    }

                    @Override
                    protected void onError(String resultMessage) {
                        super.onError(resultMessage);
                        isRuning = false;
                        LoadingDialog.hide();
                    }
                });
    }

    private void publishContent(String content, List<NetImageInfo> images) {
        if (StringUtils.isBlank(content) && images.size() <= 0) {
            Toast.makeText(activity, "发布内容不能为空", Toast.LENGTH_SHORT).show();
            isRuning = false;
            LoadingDialog.hide();
            return;
        }

        NearCircleService.addNearCircleInfo(content, images)
                .subscribe(new NetDefaultObserver<AddNearCircleInfoResponse>() {
                    @Override
                    protected void onSuccess(AddNearCircleInfoResponse res) {
                        LoadingDialog.hide();
                        isRuning = false;
                        if (res != null && res.getNearCircleId() > 0) {
                            Toast.makeText(getApplicationContext(), "发布成功", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            onError("发布失败");
                        }
                    }

                    @Override
                    protected void onError(String resultMessage) {
                        super.onError(resultMessage);
                        isRuning = false;
                        LoadingDialog.hide();
                    }
                });
    }
}
