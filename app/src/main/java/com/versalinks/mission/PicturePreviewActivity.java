//package com.versalinks.mission;
//
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//
//import androidx.annotation.Nullable;
//
//import com.bumptech.glide.Glide;
//import com.github.chrisbanes.photoview.PhotoView;
//import com.ksy.common.activity.CommonBaseActivity;
//
//import java.io.File;
//import java.io.Serializable;
//
//
//public class PicturePreviewActivity extends BaseActivity {
//
//
//    private PhotoView imageView;
//    private File file;
//    private boolean isChoose;
//
//    public static void preview(CommonBaseActivity activity, File File, int code) {
//        Intent intent = new Intent(activity, PicturePreviewActivity.class);
//        intent.putExtra("file", File);
//        intent.putExtra("isChoose", true);
//        activity.jump2Activity(intent, code);
//    }
//
//    public static void preview(Context activity, File File) {
//        Intent intent = new Intent(activity, PicturePreviewActivity.class);
//        intent.putExtra("file", File);
//        activity.startActivity(intent);
//    }
//
//
//    @Override
//    protected boolean isShowHeadBar() {
//        return true;
//    }
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_picture_preview);
//        getHeadBar().setTitle(getString(R.string.title_preview_image));
//        imageView = findViewById(R.id.image);
//        Intent intent = getIntent();
//        Serializable fileSer = intent.getSerializableExtra("file");
//        isChoose = intent.getBooleanExtra("isChoose", false);
//        if (fileSer instanceof File) {
//            file = (File) fileSer;
//        }
//        if (file == null || !file.exists() || file.length() <= 0) {
//            destroyActivity();
//            return;
//        }
//        if (isChoose) {
//            getHeadBar().setRightText(getString(R.string.text_enter), new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent();
//                    intent.putExtra("targetFile", file);
//                    setResult(RESULT_OK, intent);
//                    destroyActivity();
//                }
//            });
//        }
//        Glide.with(context).load(file).into(imageView);
//    }
//
//}
