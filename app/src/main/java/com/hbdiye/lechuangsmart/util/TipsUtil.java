package com.hbdiye.lechuangsmart.util;

import android.content.Context;
import android.widget.Toast;

public class TipsUtil
{
    
    public static void toast(Context context, String msg) {
        toast(context, msg, Toast.LENGTH_SHORT);
    }
    
    public static void toast(Context context, String msg, int time) {
        Toast sToast = Toast.makeText(context, msg, time);
        sToast.show();
    }
    
}
