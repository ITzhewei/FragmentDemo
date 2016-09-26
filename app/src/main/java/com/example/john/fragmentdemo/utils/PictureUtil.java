package com.example.john.fragmentdemo.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;

/**
 * Created by ZheWei on 2016/9/26.
 */
public class PictureUtil {
    //得到缩小后的Bitmap对象
    //首先确定文件多大, 然后按照给定的区域合理缩放文件,,重新读取缩放后的文件,并创建Bitmap对象
    public static Bitmap getScaledBitmap(String path, int destWidth, int destHeight) {
        //读取存在文件中的图片
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        int srcWidth = options.outWidth;
        int srcHeight = options.outHeight;
        //确定缩略图像素的大小
        int inSampleSize = 1;
        if (srcHeight > destHeight || srcWidth < destWidth) {
            if (srcWidth > srcHeight) { //依据小的边进行比例缩放
                inSampleSize = Math.round(srcHeight / destHeight);
            } else {
                inSampleSize = Math.round(srcWidth / destWidth);
            }
        }

        //
        options = new BitmapFactory.Options();
        options.inSampleSize = inSampleSize;
        //读取文件并且创建最终的bitmap
        return BitmapFactory.decodeFile(path, options);
    }

    //利用保守的估算值来进行缩放
    public static Bitmap getScaledBitmap(String path, Activity activity) {
        Point size = new Point();
        activity.getWindowManager().getDefaultDisplay().getSize(size);
        return getScaledBitmap(path, size.x, size.y);
    }
}
