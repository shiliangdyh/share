package com.mirstone.sharelib.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by lenovo on 2018/2/5/0005.
 */

public final class BitmapUtil {
    /**
     * bitmap 转 bytes
     *
     * @param bitmap
     * @return
     */
    public static byte[] bitmap2bytes(Bitmap bitmap, boolean needRecycle) {
        if (bitmap == null) return null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        if (needRecycle) {
            bitmap.recycle();
        }
        return baos.toByteArray();
    }

    /**
     * bytes 转 bitmap
     *
     * @param bytes
     * @return
     */
    public static Bitmap bytes2bitmap(byte[] bytes) {
        if (bytes == null) return null;
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    /**
     * 缩放bitmap
     *
     * @param bitmap
     * @param size        bitmap的宽和高
     * @param needRecycle 是否回收原bitmap
     * @return
     */
    public static Bitmap scaledBitmap(Bitmap bitmap, int size, boolean needRecycle) {
        if (bitmap == null) return null;
        if (bitmap.getWidth() > size) {
            Bitmap ret = Bitmap.createScaledBitmap(bitmap, size, size, true);
            if (needRecycle) {
                bitmap.recycle();
            }
            return ret;
        } else {
            return bitmap;
        }
    }

    public static String save(Activity activity, Bitmap bmp) {
        if (bmp != null) {
            try {
                String sdCardPath = activity.getExternalCacheDir().getPath();
                // 图片文件路径
                String imagePath = sdCardPath + File.separator + "screenshot.png";
                File file = new File(imagePath);
                FileOutputStream os = new FileOutputStream(file);
                bmp.compress(Bitmap.CompressFormat.PNG, 100, os);
                os.flush();
                os.close();
                return imagePath;
            } catch (Exception e) {
            }
        }
        return null;
    }

    public static String saveImage(final String imageName, final Bitmap bitmap, final File file , final Bitmap.CompressFormat format) {
        if (bitmap == null) return null;
        if (!file.exists()) {
            return null;
        }
        File f = new File(file, imageName);
        if (f.exists()) {
            f.delete();
        }
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(new File(file, imageName));
            bitmap.compress(format, 80, fos);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }finally {
            bitmap.recycle();
//            Util.close(fos);
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return f.getAbsolutePath();
    }
}
