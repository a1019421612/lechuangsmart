package com.hbdiye.lechuangsmart.fragment;

import android.app.Activity;
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
import com.hbdiye.lechuangsmart.MainActivity;
import com.hbdiye.lechuangsmart.MyApp;
import com.hbdiye.lechuangsmart.R;
import com.hbdiye.lechuangsmart.activity.AnFangActivity;
import com.hbdiye.lechuangsmart.activity.ChuangLianActivity;
import com.hbdiye.lechuangsmart.activity.ChuanganqiActivity;
import com.hbdiye.lechuangsmart.activity.FangjianActivity;
import com.hbdiye.lechuangsmart.activity.KaiguanActivity;
import com.hbdiye.lechuangsmart.activity.LoginActivity;
import com.hbdiye.lechuangsmart.activity.YaokongqiActivity;
import com.hbdiye.lechuangsmart.activity.ZhaoMingActivity;
import com.hbdiye.lechuangsmart.util.SPUtils;
import com.tencent.bugly.beta.ui.BetaActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.tavendo.autobahn.WebSocketConnection;
import de.tavendo.autobahn.WebSocketException;
import de.tavendo.autobahn.WebSocketHandler;

public class HomeFragment extends Fragment {
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_date)
    TextView tvDate;
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

    private boolean exit=false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this, view);
        mConnection = new WebSocketConnection();
        mobilephone = (String) SPUtils.get(getActivity(), "mobilephone", "");
        password = (String) SPUtils.get(getActivity(), "password", "");
        try {
            mConnection.connect("ws://39.104.105.10:18888/mobilephone=" + mobilephone + "&password=" + password, new MyWebSocketHandler());

        } catch (WebSocketException e) {
            e.printStackTrace();
            SmartToast.show("网络连接错误");
        }
        //启动新的线程
        timeThread = new TimeThread();
        timeThread.start();
        Calendar now = Calendar.getInstance();
        tvDate.setText((now.get(Calendar.MONTH) + 1) + "月" + (now.get(Calendar.DAY_OF_MONTH)) + "日");
        return view;
    }

    class TimeThread extends Thread {
        @Override
        public void run() {
            do {
                try {
                    if (!exit){
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
                    CharSequence sysTimeStr = DateFormat.format("hh:mm:ss", sysTime);
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        exit=true;
    }

    @OnClick({R.id.ll_anfang, R.id.ll_zhaoming, R.id.ll_chuanglian, R.id.ll_chuanganqi, R.id.ll_fangjian, R.id.ll_kaiguan, R.id.ll_yaokong})
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
        }
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
    private List<String> mList= new ArrayList<>();
    class MyWebSocketHandler extends WebSocketHandler {
        @Override
        public void onOpen() {
            Log.e(TAG, "open");
            mConnection.sendTextMessage("{\"pn\":\"UITP\"}");
        }

        @Override
        public void onTextMessage(String payload) {
            Log.e(TAG, "onTextMessage" + payload);
            if (payload.contains("{\"pn\":\"HRQP\"}")) {
                mConnection.sendTextMessage("{\"pn\":\"HRSP\"}");
            }if (payload.contains("{\"pn\":\"PRTP\"}")) {
                for (Activity activity : MyApp.mActivitys) {
                    String packageName = activity.getLocalClassName();
                    Log.e("LLL",packageName);
                    mList.add(packageName);
                }
                if (mList.contains("MainActivity")&&MyApp.mActivitys.size()==1){
                    Log.e("LLL","只有MainActivity");
                    MyApp.finishAllActivity();
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }else if (mList.contains("MainActivity")&&mList.contains("BetaActivity")&&MyApp.mActivitys.size()==2){
                    MyApp.finishAllActivity();
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }else {
                    Log.e("LLL","多个Activity");
                }
            }
        }

        @Override
        public void onClose(int code, String reason) {
            Log.e(TAG, "onClose");
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (hidden) {
            // 隐藏
            Log.e(TAG, "home" + "隐藏");
            mConnection.disconnect();

        } else {
            // 可视
            Log.e(TAG, "home" + "显示");
            if (mConnection != null) {
                try {
                    mConnection.connect("ws://39.104.105.10:18888/mobilephone=" + mobilephone + "&password=" + password, new MyWebSocketHandler());

                } catch (WebSocketException e) {
                    e.printStackTrace();
                    SmartToast.show("网络连接错误");
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mConnection.disconnect();
    }
}
