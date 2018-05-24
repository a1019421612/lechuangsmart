package com.hbdiye.lechuangsmart.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.coder.zzq.smartshow.toast.SmartToast;
import com.hbdiye.lechuangsmart.R;
import com.hbdiye.lechuangsmart.activity.AnFangActivity;
import com.hbdiye.lechuangsmart.activity.ChuangLianActivity;
import com.hbdiye.lechuangsmart.activity.ChuanganqiActivity;
import com.hbdiye.lechuangsmart.activity.FangjianActivity;
import com.hbdiye.lechuangsmart.activity.KaiguanActivity;
import com.hbdiye.lechuangsmart.activity.YaokongqiActivity;
import com.hbdiye.lechuangsmart.activity.ZhaoMingActivity;
import com.hbdiye.lechuangsmart.util.SPUtils;

import org.java_websocket.client.WebSocketClient;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.tavendo.autobahn.WebSocketConnection;
import de.tavendo.autobahn.WebSocketException;
import de.tavendo.autobahn.WebSocketHandler;
import de.tavendo.autobahn.WebSocketOptions;

public class HomeFragment extends Fragment {
    private static final String TAG = HomeFragment.class.getName();
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
    private WebSocketClient webSocketClient;

    private WebSocketConnection mConnection;
    WebSocketOptions mOptions = new WebSocketOptions();
    private Unbinder unbinder;
    private String mobilephone;
    private String password;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this, view);
        mConnection = new WebSocketConnection();
        mobilephone= (String) SPUtils.get(getActivity(),"mobilephone","");
        password= (String) SPUtils.get(getActivity(),"password","");
        try {
            mConnection.connect("ws://39.104.105.10:18888/mobilephone="+mobilephone+"&password="+password, new MyWebSocketHandler());

        } catch (WebSocketException e) {
            e.printStackTrace();
            SmartToast.show("网络连接错误");
        }
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
       unbinder.unbind();
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

    class MyWebSocketHandler extends WebSocketHandler {
        @Override
        public void onOpen() {
            Log.e("TAG", "open");
            mConnection.sendTextMessage("{\"pn\":\"UITP\"}");
        }

        @Override
        public void onTextMessage(String payload) {
            Log.e("TAG", "onTextMessage" + payload);
            if (payload.contains("{\"pn\":\"HRQP\"}")) {
                mConnection.sendTextMessage("{\"pn\":\"HRSP\"}");
            }
        }

        @Override
        public void onClose(int code, String reason) {
            Log.e("TAG", "onClose");
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (hidden) {
            // 隐藏
            Log.e("TAG", "home" + "隐藏");
            mConnection.disconnect();

        } else {
            // 可视
            Log.e("TAG", "home" + "显示");
            if (mConnection != null) {
                try {
                    mConnection.connect("ws://39.104.105.10:18888/mobilephone=15944444444&password=123", new MyWebSocketHandler());

                } catch (WebSocketException e) {
                    e.printStackTrace();
                    SmartToast.show("网络连接错误");
                }
            }
        }
    }
}
