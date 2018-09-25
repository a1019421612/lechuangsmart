package com.hbdiye.lechuangsmart.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.coder.zzq.smartshow.toast.SmartToast;
import com.hbdiye.lechuangsmart.Global.InterfaceManager;
import com.hbdiye.lechuangsmart.MyWebSocketHandler;
import com.hbdiye.lechuangsmart.R;
import com.hbdiye.lechuangsmart.SingleWebSocketConnection;
import com.hbdiye.lechuangsmart.SingleWebSocketHandler;
import com.hbdiye.lechuangsmart.SocketSendMessage;
import com.hbdiye.lechuangsmart.activity.AnFangActivity;
import com.hbdiye.lechuangsmart.activity.ChuangLianActivity;
import com.hbdiye.lechuangsmart.activity.ChuanganqiActivity;
import com.hbdiye.lechuangsmart.activity.FangjianActivity;
import com.hbdiye.lechuangsmart.activity.InfraredActivity;
import com.hbdiye.lechuangsmart.activity.KaiguanActivity;
import com.hbdiye.lechuangsmart.activity.LoginActivity;
import com.hbdiye.lechuangsmart.activity.YaokongqiActivity;
import com.hbdiye.lechuangsmart.activity.YiLiaoActivity;
import com.hbdiye.lechuangsmart.activity.ZhaoMingActivity;
import com.hbdiye.lechuangsmart.util.SPUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import javax.security.auth.login.LoginException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.tavendo.autobahn.WebSocketConnection;
import de.tavendo.autobahn.WebSocketException;
import okhttp3.Call;

import static com.hbdiye.lechuangsmart.MyApp.finishAllActivity;

public class HomeFragment extends Fragment {
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_date)
    TextView tvDate;
    @BindView(R.id.ll_yiliao)
    LinearLayout llYiliao;
    @BindView(R.id.ll_hongwai)
    LinearLayout llHongwai;
    private String TAG = HomeFragment.class.getName();
    @BindView(R.id.ll_anfang)
    LinearLayout llAnfang;
    @BindView(R.id.ll_zhaoming)
    LinearLayout llZhaoming;
    @BindView(R.id.ll_chuanglian)
    LinearLayout llChuanglian;
    @BindView(R.id.ll_chuanganqi)
    LinearLayout llChuanganqi;
    @BindView(R.id.ll_fangjian)
    LinearLayout llFangjian;
    @BindView(R.id.ll_kaiguan)
    LinearLayout llKaiguan;
    @BindView(R.id.ll_yaokong)
    LinearLayout llYaokong;

    private Unbinder unbinder;

    private WebSocketConnection mConnection;
    private String mobilephone;
    private String password;
    private TimeThread timeThread;
    public MyWebSocketHandler instance;
//    private HomeReceiver homeReceiver;

    private boolean exit = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        initWebSocket();
        unbinder = ButterKnife.bind(this, view);
//        mConnection= SingleWebSocketConnection.getInstance();
//        mConnection.sendTextMessage("{\"pn\":\"UITP\"}");
//
//        IntentFilter intentFilter=new IntentFilter();
//        intentFilter.addAction("UITP");
//        homeReceiver = new HomeReceiver();
//        getActivity().registerReceiver(homeReceiver,intentFilter);

//        mConnection = new WebSocketConnection();
//        mobilephone = (String) SPUtils.get(getActivity(), "mobilephone", "");
//        password = (String) SPUtils.get(getActivity(), "password", "");
//        try {
//            mConnection.connect("ws://39.104.119.0:18888/mobilephone=" + mobilephone + "&password=" + password, new MyWebSocketHandler());
//
//        } catch (WebSocketException e) {
//            e.printStackTrace();
//            SmartToast.show("网络连接错误");
//        }
//        //启动新的线程
        timeThread = new TimeThread();
        timeThread.start();
        Calendar now = Calendar.getInstance();
        tvDate.setText((now.get(Calendar.MONTH) + 1) + "月" + (now.get(Calendar.DAY_OF_MONTH)) + "日");
        return view;
    }

    //    class HomeReceiver extends BroadcastReceiver {
//
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            String action = intent.getAction();
//            if (action.equals("UITP")){
//                String message = intent.getStringExtra("message");
//                Log.e("bbb",message);
//            }
//        }
//    }
    class TimeThread extends Thread {
        @Override
        public void run() {
            do {
                try {
                    if (!exit) {
                        Thread.sleep(1000);
                        Message msg = new Message();
                        msg.what = 1;  //消息(一个整型值)
                        mHandler.sendMessage(msg);// 每隔1秒发送一个msg给mHandler
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while (true);
        }
    }

    //在主线程里面处理消息并更新UI界面
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    long sysTime = System.currentTimeMillis();
                    CharSequence sysTimeStr = DateFormat.format("HH:mm", sysTime);
                    try {
                        tvTime.setText(sysTimeStr); //更新时间
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    break;

            }
        }
    };

    private void initWebSocket() {
        mobilephone = (String) SPUtils.get(getActivity(), "mobilephone", "");
        password = (String) SPUtils.get(getActivity(), "password", "");
        mConnection = SingleWebSocketConnection.getInstance();
        instance = SingleWebSocketHandler.getInstance(mConnection, "{\"pn\":\"UITP\"}");
        try {
            mConnection.connect("ws://39.104.119.0:18888/mobilephone=" + mobilephone + "&password=" + password, instance);
        } catch (WebSocketException e) {
            Log.e("sss", "异常：" + e.toString());
            e.printStackTrace();
        }
        instance.SetSocketsendMessage(new SocketSendMessage() {
            @Override
            public void websocketSendMessage(String message) {
                if (message.contains("\"pn\":\"PRTP\"")) {
                    Log.e("EEE", message);
                    mConnection.disconnect();
                    finishAllActivity();
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }
                if (message.contains("\"pn\":\"SLTP\"")) {
                    websocketSendBroadcase(message, "SLTP");
                }
                if (message.contains("\"pn\":\"UITP\"")) {

                    websocketSendBroadcase(message, "UITP");
                }
                if (message.contains("\"pn\":\"NSSTP\"")) {
                    //开启场景
                    websocketSendBroadcase(message, "NSSTP");
                }
                if (message.contains("\"pn\":\"SUTP\"")) {
                    //修改场景名称
                    websocketSendBroadcase(message, "SUTP");
                }
                if (message.contains("\"pn\":\"NSDTP\"")) {
                    //删除场景
                    websocketSendBroadcase(message, "NSDTP");
                }
                if (message.contains("\"pn\":\"NSATP\"")) {
                    //添加场景
                    websocketSendBroadcase(message, "NSATP");
                }
                if (message.contains("\"pn\":\"LLTP\"")) {
                    //联动
                    websocketSendBroadcase(message, "LLTP");
                }
                if (message.contains("\"pn\":\"LUTP\"")) {
//                修改联动名称
                    websocketSendBroadcase(message, "LUTP");
                }
                if (message.contains("\"pn\":\"LDTP\"")) {
                    //删除联动
                    websocketSendBroadcase(message, "LDTP");
                }
                if (message.contains("\"pn\":\"LATP\"")) {
                    //LATPSPUtils.put(this,"isTrigger",true);
                    boolean isTrigger = (boolean) SPUtils.get(getActivity(), "isTrigger", false);
                    if (!isTrigger) {
                        websocketSendBroadcase(message, "LATP");
                    } else {
                        websocketSendBroadcase(message, "LATP_T");
                    }
                }
//========================scenesettingActivity======================================
                if (message.contains("\"pn\":\"STLTP\"")) {
                    //STLTP  scenesettingActivity
                    websocketSendBroadcase(message, "STLTP");
                }
                if (message.contains("\"pn\":\"IRLTP\"")) {
                    //IRLTP
                    websocketSendBroadcase(message, "IRLTP");
                }
                if (message.contains("\"pn\":\"SUTP\"")) {
                    //修改场景名称
                    websocketSendBroadcase(message, "SUTP");
                }
                if (message.contains("\"pn\":\"NSTATP\"")) {
                    //添加设备
                    websocketSendBroadcase(message, "NSTATP");
                }
                if (message.contains("\"pn\":\"NSTUTP\"")) {
                    //设置延时
                    websocketSendBroadcase(message, "NSTUTP");
                }
                if (message.contains("\"pn\":\"NSTDTP\"")) {
                    //删除设备
                    websocketSendBroadcase(message, "NSTDTP");
                }
                if (message.contains("\"pn\":\"SDRTP\"")) {
                    //情景面板设备
                    websocketSendBroadcase(message, "SDRTP");
                }
//                ============================FangjianActivity========================
                if (message.contains("\"pn\":\"GSTP\"")) {
//                    GSTP设备 调试 停止入网
                    websocketSendBroadcase(message, "GSTP");
                }
//========================LinkageSettingActivity=====================
                if (message.contains("\"pn\":\"LCTP\"")) {
                    //
                    websocketSendBroadcase(message, "LCTP");
                }
                if (message.contains("\"pn\":\"LDLTP\"")) {
                    //
                    websocketSendBroadcase(message, "LDLTP");
                }
                if (message.contains("\"pn\":\"IRLTP\"")) {
                    //设备列表
                    websocketSendBroadcase(message, "IRLTP");
                }
                if (message.contains("\"pn\":\"LUTP\"")) {
                    //修改联动名称
                    websocketSendBroadcase(message, "LUTP");
                }
                if (message.contains("\"pn\":\"LTDTP\"")) {
                    // 删除联动设备 LTDTP
                    websocketSendBroadcase(message, "LTDTP");
                }
                if (message.contains("\"pn\":\"LTUTP\"")) {
                    //  延时时间修改 LTUTP
                    websocketSendBroadcase(message, "LTUTP");
                }
                if (message.contains("\"pn\":\"LTATP\"")) {
                    //   联动添加设备 LTATP
                    websocketSendBroadcase(message, "LTATP");
                }
// =========================devicetriggeredactivity============
                if (message.contains("\"pn\":\"LDLTP\"")) {
                    //  设备列表
                    websocketSendBroadcase(message, "LDLTP");
                }
                if (message.contains("\"pn\":\"LUTP\"")) {
                    //  修改触发设备
                    websocketSendBroadcase(message, "LUTP");
                }
                if (message.contains("\"pn\":\"LCTP\"")) {
                    //
                    websocketSendBroadcase(message, "LCTP");
                }
//=========================FamilyNameActivity==================
                if (message.contains("\"pn\":\"UITP\"")) {
                    //
                    websocketSendBroadcase(message, "UITP");
                }
                if (message.contains("\"pn\":\"UUITP\"")) {
                    //
                    websocketSendBroadcase(message, "UUITP");
                }
                if (message.contains("\"pn\":\"UJFTP\"")) {
                    //扫描加入家庭
                    websocketSendBroadcase(message, "UJFTP");
                }
//======================editpswactivity=================
                if (message.contains("\"pn\":\"UUITP\"")) {
                    //修改密码
                    websocketSendBroadcase(message, "UUITP");
                }
//======================FamilyManagerActivity===================
                if (message.contains("\"pn\":\"RGLTP\"")) {
                    //房间管理
//                    boolean isRoom= (boolean) SPUtils.get(getActivity(),"RoomRgltp",false);
//                    if (isRoom){
//                        websocketSendBroadcase(message,"RGLTP_R");
//                    }else {
                    websocketSendBroadcase(message, "RGLTP");
//                    }
//                    SPUtils.remove(getActivity(),"RoomRgltp");
                }
                if (message.contains("\"pn\":\"DDUTP\"")) {
//                    DDUTP 将设备放置到对应房间
                    websocketSendBroadcase(message, "DDUTP");
                }
                if (message.contains("\"pn\":\"RUTP\"")) {
                    //修改房间名
                    websocketSendBroadcase(message, "RUTP");
                }
                if (message.contains("\"pn\":\"RATP\"")) {
                    //添加新房间 RATP
                    websocketSendBroadcase(message, "RATP");
                }
                if (message.contains("\"pn\":\"RDTP\"")) {
                    //删除房间
                    websocketSendBroadcase(message, "RDTP");
                }
                if (message.contains("\"pn\":\"RDTP\"")) {
                    //删除房间中的设备
                    websocketSendBroadcase(message, "DDUTP");
                }
//=========================RoomActivity========================
                if (message.contains("\"pn\":\"SDOSTP\"")) {
                    //子设备在线
                    websocketSendBroadcase(message, "SDOSTP");
                }
                if (message.contains("\"pn\":\"DUTP\"")) {
                    //修改设备名称
                    websocketSendBroadcase(message, "DUTP");
                }
                if (message.contains("\"pn\":\"DGLTP\"")) {
                    //列表
                    websocketSendBroadcase(message, "DGLTP");
                }
                if (message.contains("\"pn\":\"SDBTP\"")) {
                    //扫描加入家庭
                    websocketSendBroadcase(message, "SDBTP");
                }
                if (message.contains("\"pn\":\"ATP\"")) {
                    websocketSendBroadcase(message, "ATP");
                }
//==========================YaoKongListActivity=========================
                if (message.contains("\"pn\":\"IRUTP\"")) {
                    //                修改设备名称 IRUTP
                    websocketSendBroadcase(message, "IRUTP");
                }
                if (message.contains("\"pn\":\"IRDTP\"")) {
                    //删除 IRDTP
                    websocketSendBroadcase(message, "IRDTP");
                }
//===========================PicKTYaoKongActivity=====================
                if (message.contains("\"pn\":\"IRTP\"")) {
                    websocketSendBroadcase(message, "IRTP");
                }
                if (message.contains("\"pn\":\"IRATP\"")) {
                    websocketSendBroadcase(message, "IRATP");
                }
//                ==============医疗BTLTP
                if (message.contains("\"pn\":\"BTLTP\"")) {
                    websocketSendBroadcase(message, "BTLTP");
                }
            }
        });
    }

    private void websocketSendBroadcase(String message, String param) {
        Intent intent = new Intent();
        intent.setAction(param);
        intent.putExtra("message", message);
        try {
            getActivity().sendBroadcast(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        exit = true;

//        getActivity().unregisterReceiver(homeReceiver);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //这是一条错误指令目的是为了立刻断开连接 如果用mConnection.disconnect()会等很长时间才会断开连接
//        mConnection.sendTextMessage("{\"pn\":\"DLLL\", \"classify\":\"protype\", \"id\":\"PROTYPE07\"}");
        mConnection.disconnect();
    }

    @OnClick({R.id.ll_anfang, R.id.ll_zhaoming, R.id.ll_chuanglian, R.id.ll_chuanganqi, R.id.ll_fangjian,
            R.id.ll_kaiguan, R.id.ll_yaokong, R.id.ll_yiliao, R.id.ll_hongwai})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_anfang:
                //安防
                startActivity(new Intent(getActivity(), AnFangActivity.class));
                break;
            case R.id.ll_zhaoming:
                //照明
                startActivity(new Intent(getActivity(), ZhaoMingActivity.class));
                break;
            case R.id.ll_chuanglian:
                //窗帘
                startActivity(new Intent(getActivity(), ChuangLianActivity.class));
                break;
            case R.id.ll_chuanganqi:
                //传感器
                startActivity(new Intent(getActivity(), ChuanganqiActivity.class));
                break;
            case R.id.ll_fangjian:
                //房间
                startActivity(new Intent(getActivity(), FangjianActivity.class));
                break;
            case R.id.ll_kaiguan:
                //开关
                startActivity(new Intent(getActivity(), KaiguanActivity.class));
                break;
            case R.id.ll_yaokong:
                //遥控器
                startActivity(new Intent(getActivity(), YaokongqiActivity.class));
                break;
            case R.id.ll_yiliao:
                //医疗
                startActivity(new Intent(getActivity(), YiLiaoActivity.class));
                break;
            case R.id.ll_hongwai:
                //红外
                registerInfrared();
                startActivity(new Intent(getActivity(), InfraredActivity.class));
                break;
        }
    }

    private void registerInfrared() {
        String phone= (String) SPUtils.get(getActivity(),"mobilephone","");
        OkHttpUtils
                .post()
                .url(InterfaceManager.getInstance().getURL(InterfaceManager.HWREGISTER))
                .addParams("app_id",InterfaceManager.APPID)
                .addParams("app_type",InterfaceManager.APPKEY)
                .addParams("phone",phone)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        SmartToast.show("网络连接错误");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            if (success){

                            }else {
                                String info = jsonObject.getString("info");
//                                SmartToast.show(info);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    //    class MyWebSocketHandler extends CWebSocketHandler{
//        @Override
//        public void onOpen() {
//            Log.e(TAG, "open");
//            mConnection.sendTextMessage("{\"pn\":\"UITP\"}");
//        }
//
//        @Override
//        public void onTextMessage(String payload) {
//            super.onTextMessage(payload);
//            if (payload.contains("{\"pn\":\"HRQP\"}")) {
//                mConnection.sendTextMessage("{\"pn\":\"HRSP\"}");
//            }
//        }
//
//        @Override
//        public void onClose(int code, String reason) {
//            Log.e(TAG, "onClose");
//        }
//    }
//    private List<String> mList= new ArrayList<>();
//    class MyWebSocketHandler extends WebSocketHandler {
//        @Override
//        public void onOpen() {
//            Log.e(TAG, "open");
//            mConnection.sendTextMessage("{\"pn\":\"UITP\"}");
//        }
//
//        @Override
//        public void onTextMessage(String payload) {
//            Log.e(TAG, "onTextMessage" + payload);
//            if (payload.contains("{\"pn\":\"HRQP\"}")) {
//                mConnection.sendTextMessage("{\"pn\":\"HRSP\"}");
//            }if (payload.contains("{\"pn\":\"PRTP\"}")) {
//                for (Activity activity : MyApp.mActivitys) {
//                    String packageName = activity.getLocalClassName();
//                    Log.e("LLL",packageName);
//                    mList.add(packageName);
//                }
//                if (mList.contains("MainActivity")&&MyApp.mActivitys.size()==1){
//                    Log.e("LLL","只有MainActivity");
//                    MyApp.finishAllActivity();
//                    Intent intent = new Intent(getActivity(), LoginActivity.class);
//                    startActivity(intent);
//                }else if (mList.contains("MainActivity")&&mList.contains("BetaActivity")&&MyApp.mActivitys.size()==2){
//                    MyApp.finishAllActivity();
//                    Intent intent = new Intent(getActivity(), LoginActivity.class);
//                    startActivity(intent);
//                }else {
//                    Log.e("LLL","多个Activity");
//                }
//            }
//        }
//
//        @Override
//        public void onClose(int code, String reason) {
//            Log.e(TAG, "onClose");
//        }
//    }

//    @Override
//    public void onHiddenChanged(boolean hidden) {
//        if (hidden) {
//            // 隐藏
//            Log.e(TAG, "home" + "隐藏");
//
//            mConnection.disconnect();
//
//        } else {
//            // 可视
//            Log.e(TAG, "home" + "显示");
//            if (mConnection != null) {
//                try {
//                    mConnection.connect("ws://39.104.119.0:18888/mobilephone=" + mobilephone + "&password=" + password, new MyWebSocketHandler());
//
//                } catch (WebSocketException e) {
//                    e.printStackTrace();
//                    SmartToast.show("网络连接错误");
//                }
//            }
//        }
//    }

}
