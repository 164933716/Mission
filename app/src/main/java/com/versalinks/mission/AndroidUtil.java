package com.versalinks.mission;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.TypedValue;
import android.view.WindowManager;

import androidx.fragment.app.Fragment;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import static android.os.Environment.DIRECTORY_PICTURES;

/**
 * Created by Ksy.
 */

public class AndroidUtil {
    public static final String folder = File.separator + "AA_" + BuildConfig.modeName + File.separator + BuildConfig.BUILD_TYPE;
    public static final String tempFolder = folder + File.separator + "Temp";
    public static final String imageExt = ".jpg";
    public static final String videoExt = ".mp4";
    public static final String txtExt = ".txt";

    //用于在设置界面返回后再次检验
    public static void toAppSetting(Activity activity, int requestCode, String appID) {
        try {
            activity.startActivityForResult(new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).setData(Uri.parse("package:" + appID)),
                    requestCode);
        } catch (Exception ignored) {
        }
    }

    public void toAppSetting(Fragment fragment, int requestCode, String appID) {
        try {
            fragment.startActivityForResult(new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).setData(Uri.parse("package:" + appID)),
                    requestCode);
        } catch (Exception ignored) {
        }
    }

    public static String getTempImageName() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
        String formatDate = dateFormat.format(new Date());
        return formatDate + "_" + getRandom() + imageExt;
    }

    public static String getTempVideoName() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
        String formatDate = dateFormat.format(new Date());
        return formatDate + "_" + getRandom() + videoExt;
    }


    public static File getTempImageFile(Context context) {
        File var1 = context.getExternalFilesDir(DIRECTORY_PICTURES);
        if (var1 == null) {
            var1 = context.getFilesDir();
        } else {
            if (!var1.exists()) {
                var1.mkdirs();
            }
        }
        File dir = new File(var1, tempFolder);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File file = new File(dir, getTempImageName());
        if (file.exists()) {
            file.delete();
        }
        return file;
    }

    public static int getRandom() {
        Random random = new Random();
        int s = random.nextInt(9999) % (9999 - 1000 + 1) + 1000;
        return s;
    }

    public static void deleteAllFiles(File file) {
        if (file == null) {
            return;
        }
        if (file.isFile()) {
            file.delete();
            return;
        }
        if (file.isDirectory()) {
            File[] childFile = file.listFiles();
            if (childFile == null || childFile.length == 0) {
                file.delete();
                return;
            }
            for (File f : childFile) {
                deleteAllFiles(f);
            }
            file.delete();
        }
    }

    private static String FormatFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString;
        String wrongSize = "0KB";
        if (fileS == 0) {
            return wrongSize;
        }
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "KB";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "MB";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "GB";
        }
        return fileSizeString;
    }

    public static boolean getVideoThumb(String videoPath, File file) {
        Bitmap videoThumbnail = ThumbnailUtils.createVideoThumbnail(videoPath, MediaStore.Video.Thumbnails.FULL_SCREEN_KIND);
        return bitmap2File(videoThumbnail, file);
    }

    public static boolean getVideoThumb(Context context, String videoPath, int width, int height, int kind) {
        Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, kind);
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
                ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        return bitmap2File(bitmap, AndroidUtil.getTempImageFile(context));
    }

    public static boolean bitmap2File(Bitmap bitmap, File file) {
        if (bitmap == null) {
            return false;
        }
        FileOutputStream fOut;
        try {
            fOut = new FileOutputStream(file);
            BufferedOutputStream bos = new BufferedOutputStream(fOut);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public static boolean saveText(File file, String text) {
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(text.getBytes());
            fos.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String readText(File file) {
        String data = "";
        try {
            FileInputStream fis = new FileInputStream(file);
            DataInputStream in = new DataInputStream(fis);
            BufferedReader br =
                    new BufferedReader(new InputStreamReader(in));
            String strLine;
            while ((strLine = br.readLine()) != null) {
                data = data + strLine;
            }
            in.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }


    public static int dp2Px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, Resources.getSystem().getDisplayMetrics());
    }

    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (wm == null) {
            return Resources.getSystem().getDisplayMetrics().widthPixels;
        }
        Point point = new Point();
        wm.getDefaultDisplay().getRealSize(point);
        return point.x;
    }

    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (wm == null) {
            return Resources.getSystem().getDisplayMetrics().heightPixels;
        }
        Point point = new Point();
        wm.getDefaultDisplay().getRealSize(point);
        return point.y;
    }

}
