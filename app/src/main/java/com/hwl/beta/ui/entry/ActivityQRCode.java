package com.hwl.beta.ui.entry;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.hwl.beta.R;
import com.hwl.beta.ui.common.BaseActivity;
import com.hwl.beta.ui.common.ShareTransfer;
import com.hwl.beta.ui.widget.TitleBar;
import com.hwl.beta.utils.DisplayUtils;
import com.hwl.beta.utils.QRCodeUtils;

public class ActivityQRCode extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);

        TitleBar tbTitle = findViewById(R.id.tb_title);
        tbTitle.setTitle("应用分享")
                .setImageRightHide()
                .setImageLeftClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                });

        ImageView ivQRCode = findViewById(R.id.iv_qrcode);
        ivQRCode.setImageBitmap(QRCodeUtils.createQRImage(ShareTransfer.APP_DOWNLOAD_URL, DisplayUtils.dp2px(this, 250), DisplayUtils.dp2px(this, 250)));
    }
}
