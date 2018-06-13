package com.hbdiye.lechuangsmart.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class ImageUtil {
    public static Bitmap drawable2Bitmap(Context context,int drawable) {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), drawable);
        return bitmap;
    }
}
