package com.hbdiye.lechuangsmart;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import com.coder.zzq.smartshow.toast.SmartToast;
import com.hbdiye.lechuangsmart.activity.LoginActivity;
import com.hbdiye.lechuangsmart.util.SPUtils;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.videogo.openapi.EZOpenSDK;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import de.tavendo.autobahn.WebSocketConnection;
import de.tavendo.autobahn.WebSocketException;

public class MyApp extends Application {
    /**
     * 维护Activity 的list
     */
    public static List<Activity> mActivitys = Collections
            .synchronizedList(new LinkedList<Activity>());

    private static Context context;

    private String mobilephone;
    private String password;
    public WebSocketConnection mConnection;
    public MyWebSocketHandler instance;

    @Override
    public void onCreate() {
        super.onCreate();
        context=getApplicationContext();
        SmartToast.plainToast(this);
        registerActivityListener();
        updateApk();

        //摄像头
        /** * sdk日志开关，正式发布需要去掉 */
        EZOpenSDK.showSDKLog(true);
        /** * 设置是否支持P2P取流,详见api */
        EZOpenSDK.enableP2P(false);
        /** * APP_KEY请替换成自己申请的 */
        EZOpenSDK.initLib(this, "378b43177968438bb78bf72e645f2ddc");
//        initWebSocket();
    }
    private void initWebSocket() {
        mobilephone = (String) SPUtils.get(this, "mobilephone", "");
        password = (String) SPUtils.get(this, "password", "");
        mConnection = SingleWebSocketConnection.getInstance();
        instance = SingleWebSocketHandler.getInstance(mConnection, "{\"pn\":\"UITP\"}");
        try {
            mConnection.connect("ws://39.104.119.0:18888/mobilephone=" + mobilephone + "&password=" + password, instance);
        } catch (WebSocketException e) {
            e.printStackTrace();
        }
        instance.SetSocketsendMessage(new SocketSendMessage() {
            @Override
            public void websocketSendMessage(String message) {
                if (message.contains("\"pn\":\"PRTP\"")) {
                    Log.e("EEE",message);
//                    finishAllActivity();
//                    Intent intent = new Intent(context, LoginActivity.class);
//                    startActivity(intent);
                }if (message.contains("\"pn\":\"SLTP\"")){
                    websocketSendBroadcase(message,"SLTP");
                }if (message.contains("\"pn\":\"UITP\"")){
                    websocketSendBroadcase(message,"UITP");
                }if (message.contains("\"pn\":\"NSSTP\"")) {
                    //开启场景
                    websocketSendBroadcase(message,"NSSTP");
                }if (message.contains("\"pn\":\"SUTP\"")){
                    //修改场景名称
                    websocketSendBroadcase(message,"SUTP");
                }if (message.contains("\"pn\":\"NSDTP\"")){
                    //删除场景
                    websocketSendBroadcase(message,"NSDTP");
                }if (message.contains("\"pn\":\"NSATP\"")){
                    //添加场景
                    websocketSendBroadcase(message,"NSATP");
                }if (message.contains("\"pn\":\"LLTP\"")){
                    //联动
                    websocketSendBroadcase(message,"LLTP");
                }if (message.contains("\"pn\":\"LUTP\"")) {
//                修改联动名称
                    websocketSendBroadcase(message,"LUTP");
                }if (message.contains("\"pn\":\"LDTP\"")) {
                    //删除联动
                    websocketSendBroadcase(message,"LDTP");
                }if (message.contains("\"pn\":\"LATP\"")) {
                    //LATPSPUtils.put(this,"isTrigger",true);
                    boolean isTrigger= (boolean) SPUtils.get(context,"isTrigger",false);
                    if (!isTrigger){
                        websocketSendBroadcase(message,"LATP");
                    }else {
                        websocketSendBroadcase(message,"LATP_T");
                    }
                }
//========================scenesettingActivity======================================
                if (message.contains("\"pn\":\"STLTP\"")) {
                    //STLTP  scenesettingActivity
                    websocketSendBroadcase(message,"STLTP");
                }if (message.contains("\"pn\":\"IRLTP\"")) {
                    //IRLTP
                    websocketSendBroadcase(message,"IRLTP");
                }if (message.contains("\"pn\":\"SUTP\"")) {
                    //修改场景名称
                    websocketSendBroadcase(message,"SUTP");
                }if (message.contains("\"pn\":\"NSTATP\"")) {
                    //添加设备
                    websocketSendBroadcase(message,"NSTATP");
                }if (message.contains("\"pn\":\"NSTUTP\"")) {
                    //设置延时
                    websocketSendBroadcase(message,"NSTUTP");
                }if (message.contains("\"pn\":\"NSTDTP\"")) {
                    //删除设备
                    websocketSendBroadcase(message,"NSTDTP");
                }
//========================LinkageSettingActivity=====================
                if (message.contains("\"pn\":\"LCTP\"")) {
                    //
                    websocketSendBroadcase(message,"LCTP");
                }if (message.contains("\"pn\":\"LDLTP\"")) {
                    //
                    websocketSendBroadcase(message,"LDLTP");
                }  if (message.contains("\"pn\":\"IRLTP\"")) {
                    //设备列表
                    websocketSendBroadcase(message,"IRLTP");
                }if (message.contains("\"pn\":\"LUTP\"")) {
                    //修改联动名称
                    websocketSendBroadcase(message,"LUTP");
                } if (message.contains("\"pn\":\"LTDTP\"")) {
                    // 删除联动设备 LTDTP
                    websocketSendBroadcase(message,"LTDTP");
                }if (message.contains("\"pn\":\"LTUTP\"")) {
                    //  延时时间修改 LTUTP
                    websocketSendBroadcase(message,"LTUTP");
                }  if (message.contains("\"pn\":\"LTATP\"")) {
                    //   联动添加设备 LTATP
                    websocketSendBroadcase(message,"LTATP");
                }
// =========================devicetriggeredactivity============
                if (message.contains("\"pn\":\"LDLTP\"")) {
                    //  设备列表
                    websocketSendBroadcase(message,"LDLTP");
                }if (message.contains("\"pn\":\"LUTP\"")) {
                    //  修改触发设备
                    websocketSendBroadcase(message,"LUTP");
                }if (message.contains("\"pn\":\"LCTP\"")) {
                    //
                    websocketSendBroadcase(message,"LCTP");
                }
//=========================FamilyNameActivity==================
                if (message.contains("\"pn\":\"UITP\"")) {
                    //
                    websocketSendBroadcase(message,"UITP");
                }if (message.contains("\"pn\":\"UUITP\"")) {
                    //
                    websocketSendBroadcase(message,"UUITP");
                }if (message.contains("\"pn\":\"UJFTP\"")) {
                    //扫描加入家庭
                    websocketSendBroadcase(message,"UJFTP");
                }
//======================editpswactivity=================
                if (message.contains("\"pn\":\"UUITP\"")) {
                    //修改密码
                    websocketSendBroadcase(message,"UUITP");
                }
//======================FamilyManagerActivity===================
                if (message.contains("\"pn\":\"RGLTP\"")) {
                    //房间管理
                    websocketSendBroadcase(message,"RGLTP");
                }if (message.contains("\"pn\":\"RUTP\"")) {
                    //修改房间名
                    websocketSendBroadcase(message,"RUTP");
                }if (message.contains("\"pn\":\"RATP\"")) {
                    //添加新房间 RATP
                    websocketSendBroadcase(message,"RATP");
                }if (message.contains("\"pn\":\"RDTP\"")) {
                    //删除房间
                    websocketSendBroadcase(message,"RDTP");
                }
//=========================RoomActivity========================
                if (message.contains("\"pn\":\"SDOSTP\"")) {
                    //子设备在线
                    websocketSendBroadcase(message,"SDOSTP");
                }if (message.contains("\"pn\":\"DUTP\"")) {
                    //修改设备名称
                    websocketSendBroadcase(message,"DUTP");
                }if (message.contains("\"pn\":\"DGLTP\"")) {
                    //列表
                    websocketSendBroadcase(message,"DGLTP");
                }if (message.contains("\"pn\":\"SDBTP\"")) {
                    //扫描加入家庭
                    websocketSendBroadcase(message,"SDBTP");
                }if (message.contains("\"pn\":\"ATP\"")) {
                    websocketSendBroadcase(message,"ATP");
                }
//==========================YaoKongListActivity=========================
                if (message.contains("\"pn\":\"IRUTP\"")) {
                    //                修改设备名称 IRUTP
                    websocketSendBroadcase(message,"IRUTP");
                }if (message.contains("\"pn\":\"IRDTP\"")) {
                    //删除 IRDTP
                    websocketSendBroadcase(message,"IRDTP");
                }
//===========================PicKTYaoKongActivity=====================
                if (message.contains("\"pn\":\"IRTP\"")) {
                    websocketSendBroadcase(message,"IRTP");
                } if (message.contains("\"pn\":\"IRATP\"")) {
                    websocketSendBroadcase(message,"IRATP");
                }
            }
        });
    }

    private void websocketSendBroadcase(String message,String param) {
        Intent intent = new Intent();
        intent.setAction(param);
        intent.putExtra("message",message);
        sendBroadcast(intent);
    }

    private void updateApk() {
        Beta.autoInit = true;

        /**
         * true表示初始化时自动检查升级
         * false表示不会自动检查升级，需要手动调用Beta.checkUpgrade()方法
         */
        Beta.autoCheckUpgrade = true;

        /**
         * 设置升级周期为60s（默认检查周期为0s），60s内SDK不重复向后天请求策略
         */
        Beta.initDelay = 1 * 1000;

        /**
         * 设置通知栏大图标，largeIconId为项目中的图片资源；
         */
        Beta.largeIconId = R.mipmap.logo;

        /**
         * 设置状态栏小图标，smallIconId为项目中的图片资源id;
         */
        Beta.smallIconId = R.mipmap.logo;


        /**
         * 设置更新弹窗默认展示的banner，defaultBannerId为项目中的图片资源Id;
         * 当后台配置的banner拉取失败时显示此banner，默认不设置则展示“loading“;
         */
        Beta.defaultBannerId = R.mipmap.logo;

        /**
         * 设置sd卡的Download为更新资源保存目录;
         * 后续更新资源会保存在此目录，需要在manifest中添加WRITE_EXTERNAL_STORAGE权限;
         */
        Beta.storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

        /**
         * 点击过确认的弹窗在APP下次启动自动检查更新时会再次显示;
         */
        Beta.showInterruptedStrategy = false;

        /**
         * 只允许在MainActivity上显示更新弹窗，其他activity上不显示弹窗;
         * 不设置会默认所有activity都可以显示弹窗;
         */
        Beta.canShowUpgradeActs.add(MainActivity.class);

        Bugly.init(getApplicationContext(), "393e7089d9", true);
    }

    /**
     * @param activity 作用说明 ：添加一个activity到管理里
     */
    public void pushActivity(Activity activity) {
        mActivitys.add(activity);
        Log.i("MYapp==","activityList:size:"+mActivitys.size());
    }

    /**
     * @param activity 作用说明 ：删除一个activity在管理里
     */
    public void popActivity(Activity activity) {
        mActivitys.remove(activity);
        Log.i("MYapp==","activityList:size:"+mActivitys.size());
    }
    /**
     * 结束所有Activity
     */
    public static void finishAllActivity() {
        if (mActivitys == null) {
            return;
        }
        for (Activity activity : mActivitys) {
            activity.finish();
        }
        SPUtils.clear(context);
        mActivitys.clear();
    }

    /**
     * 退出应用程序
     */
    public  static void appExit() {
        try {
            finishAllActivity();
        } catch (Exception e) {
        }
    }

    private void registerActivityListener() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {

            registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
                @Override
                public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                    /**
                     *  监听到 Activity创建事件 将该 Activity 加入list
                     */
                    pushActivity(activity);
                }

                @Override
                public void onActivityStarted(Activity activity) {

                }

                @Override
                public void onActivityResumed(Activity activity) {

                }

                @Override
                public void onActivityPaused(Activity activity) {

                }

                @Override
                public void onActivityStopped(Activity activity) {

                }

                @Override
                public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

                }

                @Override
                public void onActivityDestroyed(Activity activity) {
                    if (null==mActivitys&&mActivitys.isEmpty()){
                        return;
                    }
                    if (mActivitys.contains(activity)){
                        /**
                         *  监听到 Activity销毁事件 将该Activity 从list中移除
                         */
                        popActivity(activity);
                    }
                }
            });
        }
    }
    //返回
    public static Context getContextObject(){
        return context;
    }
}
