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
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import com.hwl.beta.HWLApp;
import com.hwl.beta.R;
import com.hwl.beta.databinding.ActivityImageSelectBinding;
import com.hwl.beta.ui.imgselect.action.IImageSelectItemListener;
import com.hwl.beta.ui.imgselect.action.IImageSelectListener;
import com.hwl.beta.ui.imgselect.adp.ImageAdapter;
import com.hwl.beta.ui.imgselect.bean.ImageBean;
import com.hwl.beta.ui.imgselect.bean.ImageDirBean;
import com.hwl.beta.ui.imgselect.bean.ImageSelectBean;
import com.hwl.beta.ui.imgselect.bean.ImageSelectType;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by Administrator on 2018/1/20.
 */
public class ActivityImageSelect extends FragmentActivity {
    private static final int PHOTO_REQUEST_CAMERA = 1;// 拍照
    private static final int PHOTO_REQUEST_CUT = 2;// 结果
    private static final String TEMP_FILE_NAME = "temp.jpg";

    ActivityImageSelectBinding binding;
    Activity activity;
    ImageSelectBean selectBean;
    ImageSelectListener selectListener;
    List<ImageBean> images;
    List<ImageDirBean> imageDirs;
    ArrayList<ImageBean> selectImages;
    int maxImageCount = 1;
    int imageTotalCount = 0;
    ImageFilter imageFilter;
    ImageAdapter imageAdapter;
    File tempFile;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        images = new ArrayList<>();
        imageDirs = new ArrayList<>();
        imageFilter = new ImageFilter();
        selectImages = new ArrayList<>();

        maxImageCount = getIntent().getIntExtra("selectcount", 1);
        selectBean = new ImageSelectBean(getIntent().getIntExtra("selecttype", 0));
        selectListener = new ImageSelectListener();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_image_select);
        binding.setBean(selectBean);
        binding.setAction(selectListener);

        selectListener.onInit();
        selectBean.setImageCount(imageTotalCount + "张");

        binding.tbTitle.setTitleRightShow()
                .setTitle("图片选择器")
                .setImageRightHide()
                .setImageLeftClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                });
        if (selectBean.getSelectType() == ImageSelectType.USER_HEAD) {
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


        imageAdapter = new ImageAdapter(activity, images, selectBean.getSelectType(), new ImageSelectItemListener());
        binding.rvImageList.setAdapter(imageAdapter);
        binding.rvImageList.setLayoutManager(new GridLayoutManager(this, 3));
    }

    public static String getTempFileName() {
        return Environment.getExternalStorageDirectory() + File.separator + TEMP_FILE_NAME;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK)
            switch (requestCode) {
                case PHOTO_REQUEST_CAMERA:
                    clipPhoto(Uri.fromFile(tempFile), PHOTO_REQUEST_CAMERA);//开始裁减图片
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
            }
    }

    private void clipPhoto(Uri uri, int type) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setAction("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");// mUri是已经选择的图片Uri
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);// 裁剪框比例
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 150);// 输出图片大小
        intent.putExtra("outputY", 150);
        intent.putExtra("scale", true);//黑边
        intent.putExtra("circleCrop ", false);
        intent.putExtra("scaleUpIfNeeded", true);//黑边
        intent.putExtra("return-data", true);

        if (type == PHOTO_REQUEST_CAMERA) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        }
        startActivityForResult(intent, PHOTO_REQUEST_CUT);
    }

    static class ImageFilter implements FilenameFilter {
        public ImageFilter() {
        }

        public boolean accept(File dir, String name) {
            if (name.equals(null) || name == "") return false;
            return name.endsWith(".jpg") || name.endsWith(".png") || name.endsWith(".jpeg");
        }
    }

    public class ImageSelectListener implements IImageSelectListener {

        String[] exts = new String[]{"image/png", "image/jpeg", "image/jpg"};
        HashSet<String> dirPaths = new HashSet<>();//临时的辅助类，用于防止同一个文件夹的多次扫描

        private Cursor getImageCursor() {
            //获取内存卡路径
            Uri imageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            //通过内容解析器解析出png,jpeg,jpg格式的图片
            ContentResolver contentResolver = activity.getContentResolver();
            Cursor cursor = contentResolver.query(imageUri, null,
                    MediaStore.Images.Media.MIME_TYPE + "=? or "
                            + MediaStore.Images.Media.MIME_TYPE + "=? or "
                            + MediaStore.Images.Media.MIME_TYPE + "=?", exts,
                    MediaStore.Images.Media.DATE_MODIFIED);
            return cursor;
        }

        @Override
        public void onInit() {
            Cursor cursor = getImageCursor();
            if (cursor == null || cursor.getCount() <= 0) {
                setCamera();
                return;
            }
            String firstImage = null;
            while (cursor.moveToNext()) {
                String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));// 获取图片的路径
                if (path.endsWith(TEMP_FILE_NAME)) continue;
                images.add(new ImageBean(path));
                if (firstImage == null) firstImage = path;// 拿到第一张图片的路径

                File parentFile = new File(path).getParentFile();// 获取该图片的文件名
                if (parentFile == null || !parentFile.exists())
                    continue;

                String dirPath = parentFile.getAbsolutePath();
                ImageDirBean imageDirBean = null;
                if (dirPaths.contains(dirPath)) {
                    continue;
                } else {
                    dirPaths.add(dirPath);
                    imageDirBean = new ImageDirBean();
                    imageDirBean.setDir(dirPath);
                    imageDirBean.setImagePath(path);
                }

                File[] files = parentFile.listFiles(imageFilter);//获取当前目录下面图片的数量
                if (files == null) continue;

                imageTotalCount += files.length;
                imageDirBean.setImageCount(files.length);
                imageDirs.add(imageDirBean);
            }
            cursor.close();
            dirPaths.clear();
        }

        private void setCamera() {
            if (selectBean.getSelectType() == ImageSelectType.USER_HEAD) {
                images.add(0, new ImageBean());
            }
        }

        @Override
        public void onDirNameClick(String dirFullName) {

        }

    }

    public class ImageSelectItemListener implements IImageSelectItemListener {

        @Override
        public void onCameraClick() {
            tempFile = new File(getTempFileName());
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            // 下面这句指定调用相机拍照后的照片存储的路径
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
            startActivityForResult(cameraIntent, PHOTO_REQUEST_CAMERA);// CAMERA_OK是用作判断返回结果的标识
        }

        @Override
        public void onImageClick(ImageBean image) {
            if (selectBean.getSelectType() == ImageSelectType.USER_HEAD) {
                clipPhoto(Uri.fromFile(new File(image.getPath())), PHOTO_REQUEST_CUT);//开始裁减图片
            } else {
                Toast.makeText(activity, image.getPath(), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onCheckBoxClick(CheckBox cb, ImageBean image) {
            if (image == null) return;
            if (cb.isChecked()) {
                if (selectImages.size() >= maxImageCount) {
                    cb.setChecked(false);
                    Toast.makeText(getApplicationContext(), "只能选择" + maxImageCount + "张图片！", Toast.LENGTH_SHORT).show();
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
            binding.tbTitle.setTitleRightText(selectImages.size() + "/" + maxImageCount + " 确定");
        }
    }
}