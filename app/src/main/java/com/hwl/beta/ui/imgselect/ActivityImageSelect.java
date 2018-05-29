package com.hwl.beta.ui.imgselect;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.Toast;

import com.hwl.beta.R;
import com.hwl.beta.databinding.ActivityImageSelectBinding;
import com.hwl.beta.ui.common.BaseActivity;
import com.hwl.beta.ui.common.UITransfer;
import com.hwl.beta.ui.imgselect.action.IImageSelectItemListener;
import com.hwl.beta.ui.imgselect.action.IImageSelectListener;
import com.hwl.beta.ui.imgselect.adp.ImageAdapter;
import com.hwl.beta.ui.imgselect.adp.ImageDirAdapter;
import com.hwl.beta.ui.imgselect.bean.DirPopupWindow;
import com.hwl.beta.ui.imgselect.bean.ImageBean;
import com.hwl.beta.ui.imgselect.bean.ImageDirBean;
import com.hwl.beta.ui.imgselect.bean.ImageLoadManage;
import com.hwl.beta.ui.imgselect.bean.ImageSelectBean;
import com.hwl.beta.ui.imgselect.bean.ImageSelectType;
import com.hwl.beta.utils.DisplayUtils;
import com.hwl.beta.utils.StorageUtils;
import com.yalantis.ucrop.UCrop;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/1/20.
 */
public class ActivityImageSelect extends BaseActivity {
    private static final String TAG = "ActivityImageSelect";
    private static final int PHOTO_REQUEST_CAMERA = 1;// 拍照
    private static final int PHOTO_REQUEST_CUT = 2;// 结果

    ActivityImageSelectBinding binding;
    Activity activity;
    ImageSelectBean selectBean;
    //    List<ImageBean> images;
//    List<ImageDirBean> imageDirs;
    ArrayList<ImageBean> selectImages;
    int maxImageCount = 1;
    ImageAdapter imageAdapter;
    ImageDirAdapter dirAdapter;
    ImageLoadManage imageLoadManage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        selectImages = new ArrayList<>();

        maxImageCount = getIntent().getIntExtra("selectcount", 1);
        selectBean = new ImageSelectBean(getIntent().getIntExtra("selecttype", 0));
        binding = DataBindingUtil.setContentView(this, R.layout.activity_image_select);
        binding.setBean(selectBean);
        binding.setAction(new ImageSelectListener());

        initView();
    }

    private void initView() {
        binding.tbTitle.setTitleRightShow()
                .setTitle("图片选择器")
                .setImageRightHide()
                .setImageLeftClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                });
        if (selectBean.isShowCamera()) {
            binding.tbTitle.setTitleRightHide();
        } else {
            binding.tbTitle
                    .setTitleRightText(selectImages.size() + "/" + maxImageCount + " 确定")
                    .setTitleRightBackground(R.drawable.bg_top)
                    .setTitleRightClick(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (selectImages.size() > 0) {
                                Intent intent = new Intent();
                                intent.putParcelableArrayListExtra("selectimages", selectImages);
                                setResult(Activity.RESULT_OK, intent);
                                finish();
                            } else {
                                Toast.makeText(activity, "请选择至少一张图片", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }

        imageLoadManage = new ImageLoadManage(activity, new ImageLoadManage.IImageLoadListener() {
            @Override
            public void onLoadComplete() {
                imageAdapter.refreshData(imageLoadManage.getAllImages());
                dirAdapter.refreshData(imageLoadManage.getDirs());
                selectBean.setDirName("所有图片");
                selectBean.setImageCount(imageLoadManage.getAllImages().size() + "张");
            }
        });

        dirAdapter = new ImageDirAdapter(activity, imageLoadManage.getDirs());
        imageAdapter = new ImageAdapter(activity, imageLoadManage.getAllImages(), selectBean
                .isShowCamera(), new
                IImageSelectItemListener() {
                    @Override
                    public void onCameraClick() {
                        UITransfer.toSystemCamera(activity, PHOTO_REQUEST_CAMERA);
                    }

                    @Override
                    public void onImageClick(ImageBean image) {
                        toUCropActivity(Uri.fromFile(new File(image.getPath())), StorageUtils
                                .getUriForTempFile());
                    }

                    @Override
                    public void onCheckBoxClick(CheckBox cb, ImageBean image) {
                        if (image == null) return;
                        if (cb.isChecked()) {
                            if (selectImages.size() >= maxImageCount) {
                                cb.setChecked(false);
                                Toast.makeText(getApplicationContext(), "只能选择" + maxImageCount +
                                                "张图片！",
                                        Toast.LENGTH_SHORT).show();
                                return;
                            } else {
                                image.setSelect(true);
                                if (!selectImages.contains(image)) {
                                    selectImages.add(image);
                                }
                            }
                        } else {
                            image.setSelect(false);
                            if (selectImages.contains(image)) {
                                selectImages.remove(image);
                            }
                        }
                        binding.tbTitle.setTitleRightText(selectImages.size() + "/" +
                                maxImageCount + " " +
                                "确定");
                    }
                });
        binding.rvImageList.setAdapter(imageAdapter);
        binding.rvImageList.setLayoutManager(new GridLayoutManager(this, 3));

        binding.rlDir.setOnClickListener(new View.OnClickListener() {
            DirPopupWindow dirPopupWindow;

            @Override
            public void onClick(View v) {
                if (dirAdapter.getCount() <= 0 || dirPopupWindow != null && dirPopupWindow
                        .isShowing())
                    return;
                dirPopupWindow = new DirPopupWindow(activity, dirAdapter);
                dirPopupWindow.setOnItemClickListener(new DirPopupWindow.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position,
                                            long l) {
                        dirAdapter.setSelectIndex(position);
                        dirPopupWindow.dismiss();
                        ImageDirBean dirBean = (ImageDirBean) adapterView.getAdapter().getItem
                                (position);
                        if (dirBean != null) {
                            imageAdapter.refreshData(dirBean.getImages());
                            selectBean.setDirName(dirBean.getName());
                            selectBean.setImageCount(dirBean.getImages().size() + "张");
                        }
                    }
                });
                dirPopupWindow.setMargin(binding.llImageDirs.getHeight());
                dirPopupWindow.showAtLocation(binding.llImageDirs, Gravity.NO_GRAVITY, 0, 0);
            }
        });
        imageLoadManage.init();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PHOTO_REQUEST_CAMERA:
//                    clipPhoto(Uri.fromFile(tempFile), PHOTO_REQUEST_CAMERA);//开始裁减图片
                    Uri source = Uri.fromFile(StorageUtils.getTempImageFile());
                    toUCropActivity(source, source);
                    break;
                case PHOTO_REQUEST_CUT:
                    Bitmap bitmap = data.getParcelableExtra("data");
                    Intent intent = new Intent();
                    ByteArrayOutputStream bs = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, bs);
                    byte[] bitmapByte = bs.toByteArray();
                    intent.putExtra("bitmap", bitmapByte);
                    setResult(RESULT_OK, intent);
                    finish();
                    break;
                case UCrop.REQUEST_CROP:
                    Uri resultUri = UCrop.getOutput(data);
                    Intent intent2 = new Intent();
                    intent2.putExtra("localpath", resultUri.getPath());
                    setResult(RESULT_OK, intent2);
                    finish();
                    break;
            }
        } else if (resultCode == UCrop.RESULT_ERROR) {
            handleCropError(data);
        }
    }

    private void handleCropError(@NonNull Intent result) {
        final Throwable cropError = UCrop.getError(result);
        if (cropError != null) {
            Log.e(TAG, "handleCropError: ", cropError);
            Toast.makeText(activity, cropError.getMessage(), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(activity, "Unexpected error", Toast.LENGTH_SHORT).show();
        }
    }

//    private void clipPhoto(Uri uri, int type) {
//        Intent intent = new Intent("com.android.camera.action.CROP");
//        intent.setAction("com.android.camera.action.CROP");
//        intent.setDataAndType(uri, "image/*");// mUri是已经选择的图片Uri
//        intent.putExtra("crop", "true");
//        intent.putExtra("aspectX", 1);// 裁剪框比例
//        intent.putExtra("aspectY", 1);
//        intent.putExtra("outputX", 150);// 输出图片大小
//        intent.putExtra("outputY", 150);
//        intent.putExtra("scale", true);//黑边
//        intent.putExtra("circleCrop ", false);
//        intent.putExtra("scaleUpIfNeeded", true);//黑边
//        intent.putExtra("return-data", true);
//
//        if (type == PHOTO_REQUEST_CAMERA) {
//            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
//        }
//        startActivityForResult(intent, PHOTO_REQUEST_CUT);
//    }

    static class ImageFilter implements FilenameFilter {
        public ImageFilter() {
        }

        public boolean accept(File dir, String name) {
            if (name.equals(null) || name == "") return false;
            return name.endsWith(".jpg") || name.endsWith(".png") || name.endsWith(".jpeg");
        }
    }

    public class ImageSelectListener implements IImageSelectListener {

//        String[] exts = new String[]{"image/png", "image/jpeg", "image/jpg"};
//        HashSet<String> dirPaths = new HashSet<>();//临时的辅助类，用于防止同一个文件夹的多次扫描
//
//        private Cursor getImageCursor() {
//            //获取内存卡路径
//            Uri imageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
//            //通过内容解析器解析出png,jpeg,jpg格式的图片
//            ContentResolver contentResolver = activity.getContentResolver();
//            Cursor cursor = contentResolver.query(imageUri, null,
//                    MediaStore.Images.Media.MIME_TYPE + "=? or "
//                            + MediaStore.Images.Media.MIME_TYPE + "=? or "
//                            + MediaStore.Images.Media.MIME_TYPE + "=?", exts,
//                    MediaStore.Images.Media.DATE_MODIFIED);
//            return cursor;
//        }
//
//        @Override
//        public void onInit() {
//            Cursor cursor = getImageCursor();
//            if (cursor == null || cursor.getCount() <= 0) {
//                setCamera();
//                return;
//            }
//            String firstImage = null;
//            while (cursor.moveToNext()) {
//                String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media
//                        .DATA));// 获取图片的路径
////                if (path.endsWith(TEMP_FILE_NAME)) continue;
//                images.add(new ImageBean(path));
//                if (firstImage == null) firstImage = path;// 拿到第一张图片的路径
//
//                File parentFile = new File(path).getParentFile();// 获取该图片的文件名
//                if (parentFile == null || !parentFile.exists())
//                    continue;
//
//                String dirPath = parentFile.getAbsolutePath();
//                ImageDirBean imageDirBean = null;
//                if (dirPaths.contains(dirPath)) {
//                    continue;
//                } else {
//                    dirPaths.add(dirPath);
//                    imageDirBean = new ImageDirBean();
//                    imageDirBean.setDir(dirPath);
//                    imageDirBean.setImagePath(path);
//                }
//
//                File[] files = parentFile.listFiles(imageFilter);//获取当前目录下面图片的数量
//                if (files == null) continue;
//
//                imageTotalCount += files.length;
//                imageDirBean.setImageCount(files.length);
//                imageDirs.add(imageDirBean);
//            }
//            cursor.close();
//            dirPaths.clear();
//        }

        private void setCamera() {
//            if (selectBean.getSelectType() == ImageSelectType.USER_HEAD || selectBean
//                    .getSelectType() == ImageSelectType.CIRCLE_BACK_IMAGE) {
//                images.add(0, new ImageBean());
//            }
        }

        @Override
        public void onDirNameClick(String dirFullName) {

        }

    }

    private void toUCropActivity(Uri sourceUri, Uri saveUri) {
        Log.d(TAG, sourceUri.getPath() + "   " + saveUri.getPath());
        UCrop uCrop = UCrop.of(sourceUri, saveUri);
        UCrop.Options options = new UCrop.Options();
        options.setToolbarColor(activity.getResources().getColor(R.color.main));
        options.setHideBottomControls(true);
        options.setShowCropGrid(false);
//        options.setFreeStyleCropEnabled(true);
//        uCrop.withAspectRatio(0.1f,0.1f);

        switch (selectBean.getSelectType()) {
            case ImageSelectType.USER_HEAD:
                options.setCompressionQuality(60);
                options.withAspectRatio(1, 1);
                options.withMaxResultSize(300, 300);
                uCrop.withOptions(options);
                uCrop.start(activity);
                break;
            case ImageSelectType.CIRCLE_BACK_IMAGE:
                options.setCompressionQuality(90);
                options.withAspectRatio(4, 3);
                options.withMaxResultSize(800, 600);
                uCrop.withOptions(options);
                uCrop.start(activity);
                break;
        }
    }

    public class ImageSelectItemListener implements IImageSelectItemListener {

        @Override
        public void onCameraClick() {
//            tempFile = new File(getTempFileName());
//            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, StorageUtils.getUriForTempFile());
//            startActivityForResult(cameraIntent, PHOTO_REQUEST_CAMERA);// CAMERA_OK是用作判断返回结果的标识
            UITransfer.toSystemCamera(activity, PHOTO_REQUEST_CAMERA);
        }

        @Override
        public void onImageClick(ImageBean image) {
//            Uri source = Uri.fromFile(new File(image.getPath()));
//            Uri temp = Uri.fromFile(new File(getTempFileName()));
            toUCropActivity(Uri.fromFile(new File(image.getPath())), StorageUtils
                    .getUriForTempFile());
//            Toast.makeText(activity, image.getPath(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCheckBoxClick(CheckBox cb, ImageBean image) {
//            if (image == null) return;
//            if (cb.isChecked()) {
//                if (selectImages.size() >= maxImageCount) {
//                    cb.setChecked(false);
//                    Toast.makeText(getApplicationContext(), "只能选择" + maxImageCount + "张图片！",
//                            Toast.LENGTH_SHORT).show();
//                    return;
//                } else {
//                    image.setSelect(true);
//                    if (!selectImages.contains(image)) {
//                        selectImages.add(image);
//                    }
//                }
//            } else {
//                image.setSelect(false);
//                if (selectImages.contains(image)) {
//                    selectImages.remove(image);
//                }
//            }
//            binding.tbTitle.setTitleRightText(selectImages.size() + "/" + maxImageCount + " 确定");
        }
    }
}