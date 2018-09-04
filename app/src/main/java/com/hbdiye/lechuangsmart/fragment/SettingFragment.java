package com.hbdiye.lechuangsmart.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hbdiye.lechuangsmart.R;
import com.hbdiye.lechuangsmart.SingleWebSocketConnection;
import com.hbdiye.lechuangsmart.activity.AboutUsActivity;
import com.hbdiye.lechuangsmart.activity.CameraListActivity;
import com.hbdiye.lechuangsmart.activity.EditPswActivity;
import com.hbdiye.lechuangsmart.activity.FamilyManagerActivity;
import com.hbdiye.lechuangsmart.activity.FamilyNameActivity;
import com.hbdiye.lechuangsmart.activity.LoginActivity;
import com.hbdiye.lechuangsmart.activity.VersionActivity;
import com.hbdiye.lechuangsmart.activity.WiFiActivity;
import com.hbdiye.lechuangsmart.util.SPUtils;
import com.hbdiye.lechuangsmart.views.GetPhotoPopwindow;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.tavendo.autobahn.WebSocketConnection;

public class SettingFragment extends Fragment {
    @BindView(R.id.tv_setting_family_name)
    TextView tvSettingFamilyName;
    @BindView(R.id.tv_setting_editpsw)
    TextView tvSettingEditpsw;
    @BindView(R.id.tv_family_manager)
    TextView tvFamilyManager;
    @BindView(R.id.tv_setting_wifi)
    TextView tvSettingWifi;
    @BindView(R.id.tv_setting_about_us)
    TextView tvSettingAboutUs;
    @BindView(R.id.tv_setting_version)
    TextView tvSettingVersion;
    @BindView(R.id.tv_settting_logout)
    TextView tvSetttingLogout;
    @BindView(R.id.ll_parent)
    LinearLayout parent_ll;
    @BindView(R.id.tv_setting_camera)
    TextView tvSettingCamera;
    private Unbinder unbinder;

    private GetPhotoPopwindow getPhotoPopwindow;

    private WebSocketConnection mConnection;

    private String TAG = SettingFragment.class.getName();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        unbinder = ButterKnife.bind(this, view);
        mConnection = SingleWebSocketConnection.getInstance();
//        mConnection.sendTextMessage("{\"pn\":\"DGLTP\", \"classify\":\"protype\", \"id\":\"PROTYPE07\"}");
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
        tvSettingVersion.setText("版本号V" + getVersionName(getActivity()));
        return view;
    }

    public String getVersionName(Context context) {
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packageInfo;
        String versionName = "";
        try {
            packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            versionName = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.tv_setting_family_name, R.id.tv_setting_editpsw, R.id.tv_family_manager, R.id.tv_setting_wifi,
            R.id.tv_setting_about_us, R.id.tv_setting_version, R.id.tv_settting_logout, R.id.tv_setting_camera})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_setting_family_name:
                startActivity(new Intent(getActivity(), FamilyNameActivity.class));
                break;
            case R.id.tv_setting_editpsw:
                startActivity(new Intent(getActivity(), EditPswActivity.class));
                break;
            case R.id.tv_family_manager:
                startActivity(new Intent(getActivity(), FamilyManagerActivity.class));
                break;
            case R.id.tv_setting_wifi:
                startActivity(new Intent(getActivity(), WiFiActivity.class));
                break;
            case R.id.tv_setting_camera:
                startActivity(new Intent(getActivity(), CameraListActivity.class));
                break;
            case R.id.tv_setting_about_us:
                startActivity(new Intent(getActivity(), AboutUsActivity.class));
                break;
            case R.id.tv_setting_version:
                startActivity(new Intent(getActivity(), VersionActivity.class));
                break;
            case R.id.tv_settting_logout:
                getPhotoPopwindow = new GetPhotoPopwindow(getActivity(), photoclicer);
                getPhotoPopwindow.showPopupWindowBottom(parent_ll);
                break;
        }
    }

    public View.OnClickListener photoclicer = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.item_popupwindows_camera:
                    //注销账号
//                    mConnection.disconnect();
                    getPhotoPopwindow.dismiss();
                    SPUtils.clear(getActivity());
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    getActivity().finish();
                    break;
                case R.id.item_popupwindows_Photo:
                    //退出应用
                    getPhotoPopwindow.dismiss();
                    getActivity().finish();
                    break;
                case R.id.item_popupwindows_cancel:
                    getPhotoPopwindow.dismiss();
                    break;
            }
        }
    };
    private List<String> mList = new ArrayList<>();
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
//            }
//
//            if (payload.contains("{\"pn\":\"PRTP\"}")) {
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
//                }if (mList.contains("WiFiActivity")){
//                    Log.e("LLL","多个Activity");
//                    MyApp.finishAllActivity();
//                    Intent intent = new Intent(getActivity(), LoginActivity.class);
//                    startActivity(intent);
//                }else if (mList.contains("AboutUsActivity")){
//                    MyApp.finishAllActivity();
//                    Intent intent = new Intent(getActivity(), LoginActivity.class);
//                    startActivity(intent);
//                }else {
//                    Log.e("LLL","多个Activity");
//                }
//            }
////            if (payload.contains("{\"pn\":\"PRTP\"}")) {
////                if (MyApp.mActivitys.contains(MainActivity.class)&&MyApp.mActivitys.size()==1){
////                    Log.e("LLL","只有MainActivity");
////                    MyApp.finishAllActivity();
////                    Intent intent = new Intent(getActivity(), LoginActivity.class);
////                    startActivity(intent);
////                }else if (MyApp.mActivitys.contains(WiFiActivity.class)){
////                    Log.e("LLL","多个Activity");
////                    MyApp.finishAllActivity();
////                    Intent intent = new Intent(getActivity(), LoginActivity.class);
////                    startActivity(intent);
////                }else if (MyApp.mActivitys.contains(AboutUsActivity.class)){
////                    MyApp.finishAllActivity();
////                    Intent intent = new Intent(getActivity(), LoginActivity.class);
////                    startActivity(intent);
////                }
////            }
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

//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        mConnection.disconnect();
//    }
}
