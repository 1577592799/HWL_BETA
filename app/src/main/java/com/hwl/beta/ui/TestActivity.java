package com.hwl.beta.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.hwl.beta.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/1/4.
 */

public class TestActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        Button button= findViewById(R.id.btn_rxbus);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

//        CustomImageView civImage=findViewById(R.id.civ_image);
//        civImage.setImageUrl(getImageUrls().get(0));
//        NineImageGridView nigvImages=findViewById(R.id.nigv_images);
//        nigvImages.setImagesData(getImageUrls());
//        MultiImageView ivOne2 = findViewById(R.id.miv_images);
//        ivOne2.setImagesData(getImageUrls());
    }

//    private List<MultiImageView.ImageBean> getImageUrls() {
//        List<MultiImageView.ImageBean> urls = new ArrayList<>();
////        urls.add(new MultiImageView.ImageBean(439,300,"http://img2.imgtn.bdimg.com/it/u=114139281,1353799337&fm=27&gp=0.jpg"));
//        urls.add(new MultiImageView.ImageBean(590,300,"http://image.uisdc.com/wp-content/uploads/2014/12/20141212164404274-590x300.jpg"));
//        urls.add(new MultiImageView.ImageBean(1160,653,"http://pic.pptbz.com/201506/2015070581208537.JPG"));
//        return urls;
//    }
}
